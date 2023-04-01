package com.srecko.reddit.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.srecko.reddit.controller.utils.JwtTestUtils;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
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
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  private final String jwt = JwtTestUtils.getJwt();

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
        .andExpect(jsonPath("$.['_embedded'].userList", hasSize(3)))
        .andExpect(jsonPath("$._embedded.userList[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$._embedded.userList[1].username", is(user2.getUsername())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(3)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void getUser_ReturnsUser_WhenUserIsFound() throws Exception {
    userRepository.save(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{username}", user.getUsername())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.username", is(user.getUsername())));
  }

  @Test
  void getUser_ThrowsUserNotFoundException_WhenUserNotFound() throws Exception {
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
        .andExpect(content().contentType(APPLICATION_JSON))
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
}