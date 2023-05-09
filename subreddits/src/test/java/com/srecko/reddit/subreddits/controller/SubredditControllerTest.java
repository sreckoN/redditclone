package com.srecko.reddit.subreddits.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.subreddits.dto.SubredditRequest;
import com.srecko.reddit.subreddits.entity.Subreddit;
import com.srecko.reddit.subreddits.repository.SubredditRepository;
import com.srecko.reddit.subreddits.service.client.UsersFeignClient;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
/*@WithMockUser(username = "janedoe", password = "iloveyou")
@WithUserDetails("janedoe")*/
@Transactional
class SubredditControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private SubredditRepository subredditRepository;

  @MockBean
  private UsersFeignClient usersFeignClient;

  // private final String jwt = JwtTestUtils.getJwt();
  private final String jwt = "ff42lk12mk1.lfkfm12o3limf2po3.1lkfm3olk1mf3";

  private Long userId;

  @BeforeEach
  void setUp() {
    subredditRepository.deleteAll();
    userId = 123L;
  }

  @AfterEach
  void tearDown() {
    subredditRepository.deleteAll();
  }

  @Test
  void getAll_ReturnsSubreddits() throws Exception {
    Subreddit subreddit1 = new Subreddit("Serbia", "Serbia's official subreddit", userId);
    Subreddit subreddit2 = new Subreddit("United Kingdom", "UK's official subreddit", userId);
    subredditRepository.saveAll(List.of(subreddit1, subreddit2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/subreddits")
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].subredditDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.subredditDtoList[0].name", is(subreddit1.getName())))
        .andExpect(jsonPath("$._embedded.subredditDtoList[1].name", is(subreddit2.getName())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void getSubreddit_ReturnsSubreddit() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", userId);
    subredditRepository.save(subreddit);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/subreddits/{subredditId}", subreddit.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(subreddit.getId().intValue())))
        .andExpect(jsonPath("$.name", is(subreddit.getName())))
        .andExpect(jsonPath("$.description", is(subreddit.getDescription())))
        .andExpect(jsonPath("$.creatorId", is(userId.intValue())));
  }

  @Test
  void getSubreddit_ThrowsSubredditNotFoundException_WhenSubredditDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/subreddits/{subredditId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Subreddit with id 0 is not found.")));
  }

  @Test
  void save_ReturnsSubreddit_WhenSuccessfullySaved() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", userId);
    SubredditRequest subredditRequest = new SubredditRequest(0L, subreddit.getName(), subreddit.getDescription());
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(subredditRequest);

    given(usersFeignClient.getUserId(anyString())).willReturn(userId);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/subreddits")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(APPLICATION_JSON)
            .content(valueAsString))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.name", is(subreddit.getName())))
        .andExpect(jsonPath("$.description", is(subreddit.getDescription())));
  }

  @Test
  void save_ThrowsDtoValidationException_WhenInvalidDto() throws Exception {
    SubredditRequest subredditRequest = new SubredditRequest();
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(subredditRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/subreddits")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(APPLICATION_JSON)
            .content(valueAsString))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value(containsString("name")))
        .andExpect(jsonPath("$.message").value(containsString("description")))
        .andExpect(jsonPath("$.message").value(containsString("subredditId")));
  }

  @Test
  void delete_ReturnsDeletedSubreddit_WhenSuccessfullyDeleted() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", userId);
    subredditRepository.save(subreddit);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/subreddits/{subredditId}", subreddit.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(subreddit.getId().intValue())))
        .andExpect(jsonPath("$.name", is(subreddit.getName())))
        .andExpect(jsonPath("$.description", is(subreddit.getDescription())))
        .andExpect(jsonPath("$.creatorId", is(userId.intValue())));
  }

  @Test
  void delete_ThrowsSubredditNotFoundException_WhenSubredditIsNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/subreddits/{subredditId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Subreddit with id 0 is not found.")));
  }

  @Test
  void update_ReturnsUpdatedSubreddit_WhenSuccessfullyUpdated() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", userId);
    subredditRepository.save(subreddit);
    SubredditRequest subredditRequest = new SubredditRequest(subreddit.getId(), subreddit.getName(), "Official subreddit");
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(subredditRequest);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/subreddits")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(APPLICATION_JSON)
            .content(valueAsString))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(subreddit.getId().intValue())))
        .andExpect(jsonPath("$.name", is(subreddit.getName())))
        .andExpect(jsonPath("$.description", is("Official subreddit")));
  }

  @Test
  void update_ThrowsSubredditNotFoundException_WhenSubredditNotFound() throws Exception {
    SubredditRequest subredditRequest = new SubredditRequest(0L, "Serbias subreddit", "Welcome to Yugoslavia");
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(subredditRequest);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/subreddits")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(APPLICATION_JSON)
            .content(valueAsString))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Subreddit with id 0 is not found.")));
  }

  @Test
  void update_ThrowsDtoValidationException_WhenProvidedInvalidDto() throws Exception {
    SubredditRequest subredditRequest = new SubredditRequest(null, "Serbias subreddit",
        "Welcome to Yugoslavia");
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(subredditRequest);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/subreddits")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(APPLICATION_JSON)
            .content(valueAsString))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message",
            is("DTO validation failed for the following fields: subredditId.")));
  }

  @Test
  void checkIfSubredditExists_NothingHappens_WhenSubredditExists() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", userId);
    subredditRepository.save(subreddit);

    mockMvc.perform(MockMvcRequestBuilders.head("/api/subreddits/checkIfSubredditExists")
        .servletPath("/api/subreddits/checkIfSubredditExists")
        .contentType(APPLICATION_JSON)
        .content(subreddit.getId().toString()))
        .andExpect(status().isOk());
  }

  @Test
  void checkIfSubredditExists_ThrowsSubredditNotFoundException_WhenSubredditDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.head("/api/subreddits/checkIfSubredditExists")
            .servletPath("/api/subreddits/checkIfSubredditExists")
            .contentType(APPLICATION_JSON)
            .content("0"))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void searchSubreddits_ReturnsPagedModelOfSubreddits_WhenTheyMatchQuery() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", userId);
    subredditRepository.save(subreddit);
    String query = "serbia";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/subreddits/search")
            .contentType(MediaType.APPLICATION_JSON)
            .content(query))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded.subredditDtoList").exists())
        .andExpect(jsonPath("$._embedded.subredditDtoList[0].name", is(subreddit.getName())))
        .andExpect(jsonPath("$._embedded.subredditDtoList[0].creatorId", is(subreddit.getCreatorId().intValue())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalElements", is(1)))
        .andExpect(jsonPath("$.page.totalPages").exists())
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchSubreddits_ReturnsEmptyPagedModel_WhenNoSubredditsMatchQuery() throws Exception {
    String query = "serbia";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/subreddits/search")
            .contentType(MediaType.APPLICATION_JSON)
            .content(query))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].userDtoList").doesNotExist())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements", is(0)))
        .andExpect(jsonPath("$.page.totalPages", is(0)));
  }
}