package com.srecko.reddit.users.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.srecko.reddit.users.entity.User;
import com.srecko.reddit.users.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
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
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

//  private final String jwt = JwtTestUtils.getJwt();
  private final String jwt = "2342okn1o24n.124oini4o1.1jk12n4k1nj4k12";

  private User user;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    user = new User("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB", true);
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
  }

  @Test
  void getUsers_ReturnsUsers() throws Exception {
    User user2 = new User("John", "Doe", "john.doe@example.org", "johndoe", "iloveyou", "GB", true);
    userRepository.saveAll(List.of(user, user2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].userDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.userDtoList[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$._embedded.userDtoList[1].username", is(user2.getUsername())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void getUser_ReturnsUser_WhenUserIsFound() throws Exception {
    userRepository.save(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/user/{userId}", user.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.username", is(user.getUsername())));
  }

  @Test
  void getUser_ThrowsUserNotFoundException_WhenUserNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/user/{userId}", 0L)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("User with id 0 is not found.")));
  }

  @Test
  void getUserByUsername_ReturnsUser_WhenUserIsFound() throws Exception {
    userRepository.save(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", user.getUsername())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.username", is(user.getUsername())));
  }

  @Test
  void getUserByUsername_ThrowsUserNotFoundException_WhenUserNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", "janinedoe")
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("User with username janinedoe is not found.")));
  }

  @Test
  void delete_ReturnsDeletedUser_WhenSuccessfullyDeleted() throws Exception {
    userRepository.save(user);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{username}", user.getUsername())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.username", is("janedoe")));
  }

  @Test
  void delete_ThrowsUserNotFoundException_WhenUserNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{username}", "janinedoe")
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("User with username janinedoe is not found.")));
  }

  @Test
  void getUserIdByUsername_ReturnsUserId() throws Exception {
    userRepository.save(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/getUserIdByUsername")
        .header("AUTHORIZATION", "Bearer " + jwt)
        .contentType(APPLICATION_JSON)
        .content(user.getUsername()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$", is(user.getId().intValue())));
  }

  @Test
  void getUserIdByUsername_ThrowsUserNotFoundException_WhenUserIsNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/getUserIdByUsername")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(APPLICATION_JSON)
            .content("bob"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("User with username bob is not found.")));
  }

  @Test
  void search_ReturnsUsers_WhenTheyMatchQuery() throws Exception {
    User user1 = new User("Jane", "Smith", "jane.smith@example.org", "janesmith", "iloveyou", "GB", true);
    userRepository.saveAll(List.of(user, user1));
    String query = "jane";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users/search")
        .contentType(APPLICATION_JSON)
        .content(query))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].userDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.userDtoList[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$._embedded.userDtoList[1].username", is(user1.getUsername())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void search_ReturnsEmptyPage_WhenNoUserMatchesQuery() throws Exception {
    User user1 = new User("Jane", "Smith", "jane.smith@example.org", "janesmith", "iloveyou", "GB", true);
    userRepository.saveAll(List.of(user, user1));
    String query = "john";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users/search")
            .contentType(APPLICATION_JSON)
            .content(query))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].userDtoList").doesNotExist())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements", is(0)))
        .andExpect(jsonPath("$.page.totalPages", is(0)));
  }
}