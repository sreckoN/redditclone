package com.srecko.reddit.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.controller.utils.JwtTestUtils;
import com.srecko.reddit.controller.utils.WithMockCustomUser;
import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "janedoe", password = "iloveyou")
@WithUserDetails("janedoe")
@Transactional
class SubredditControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SubredditRepository subredditRepository;

  private final String jwt = JwtTestUtils.getJwt();

  private User user;

  @BeforeEach
  void setUp() {
    subredditRepository.deleteAll();
    userRepository.deleteAll();
    user = new User("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB", true);
    userRepository.save(user);
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    subredditRepository.deleteAll();
  }

  @Test
  void getAll_ReturnsSubreddits() throws Exception {
    Subreddit subreddit1 = new Subreddit("Serbia", "Serbia's official subreddit", user);
    Subreddit subreddit2 = new Subreddit("United Kingdom", "UK's official subreddit", user);
    subredditRepository.saveAll(List.of(subreddit1, subreddit2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/subreddits")
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].subredditList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.subredditList[0].name", is(subreddit1.getName())))
        .andExpect(jsonPath("$._embedded.subredditList[1].name", is(subreddit2.getName())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void getSubreddit_ReturnsSubreddit() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    subredditRepository.save(subreddit);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/subreddits/{subredditId}", subreddit.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(subreddit.getId().intValue())))
        .andExpect(jsonPath("$.name", is(subreddit.getName())))
        .andExpect(jsonPath("$.description", is(subreddit.getDescription())))
        .andExpect(jsonPath("$.creator", is(user.getId().intValue())));
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
  @WithMockCustomUser
  void save_ReturnsSubreddit_WhenSuccessfullySaved() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    SubredditDto subredditDto = new SubredditDto(0L, subreddit.getName(), subreddit.getDescription());
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(subredditDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/subreddits")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(APPLICATION_JSON)
            .content(valueAsString))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is(subreddit.getName())))
        .andExpect(jsonPath("$.description", is(subreddit.getDescription())));
  }

  @Test
  @WithMockCustomUser
  void save_ThrowsDtoValidationException_WhenInvalidDto() throws Exception {
    SubredditDto subredditDto = new SubredditDto();
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(subredditDto);

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
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    subredditRepository.save(subreddit);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/subreddits/{subredditId}", subreddit.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(subreddit.getId().intValue())))
        .andExpect(jsonPath("$.name", is(subreddit.getName())))
        .andExpect(jsonPath("$.description", is(subreddit.getDescription())))
        .andExpect(jsonPath("$.creator", is(user.getId().intValue())));
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
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    subredditRepository.save(subreddit);
    SubredditDto subredditDto = new SubredditDto(subreddit.getId(), subreddit.getName(), "Official subreddit");
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(subredditDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/subreddits")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(APPLICATION_JSON)
            .content(valueAsString))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(subreddit.getId().intValue())))
        .andExpect(jsonPath("$.name", is(subreddit.getName())))
        .andExpect(jsonPath("$.description", is("Official subreddit")));
  }

  @Test
  void update_ThrowsSubredditNotFoundException_WhenSubredditNotFound() throws Exception {
    SubredditDto subredditDto = new SubredditDto(0L, "Serbias subreddit", "Welcome to Yugoslavia");
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(subredditDto);

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
    SubredditDto subredditDto = new SubredditDto(null, "Serbias subreddit",
        "Welcome to Yugoslavia");
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(subredditDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/subreddits")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(APPLICATION_JSON)
            .content(valueAsString))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message",
            is("DTO validation failed for the following fields: subredditId.")));
  }
}