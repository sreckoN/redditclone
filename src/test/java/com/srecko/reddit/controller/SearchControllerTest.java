package com.srecko.reddit.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import java.util.Date;
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
class SearchControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private SubredditRepository subredditRepository;

  @Autowired
  private UserRepository userRepository;

  private User user;

  @BeforeEach
  void setUp() {
    postRepository.deleteAll();
    commentRepository.deleteAll();
    subredditRepository.deleteAll();
    user = new User("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB", true);
    userRepository.save(user);
  }

  @AfterEach
  void tearDown() {
    userRepository.deleteAll();
    commentRepository.deleteAll();
    postRepository.deleteAll();
    subredditRepository.deleteAll();
  }

  @Test
  void searchSubreddits_ReturnsSubredditsPage_WithDefaultSort() throws Exception {
    Subreddit subreddit1 = new Subreddit("Serbia", "Serbia's official subreddit", user);
    Subreddit subreddit2 = new Subreddit("Programming Serbia", "Serbia's programming community", user);
    subredditRepository.saveAll(List.of(subreddit1, subreddit2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/subreddits")
            .servletPath("/api/search/subreddits")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].subredditList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.subredditList[0].name", is(subreddit2.getName())))
        .andExpect(jsonPath("$._embedded.subredditList[1].name", is(subreddit1.getName())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchSubreddits_ReturnsSubredditsPage_IsCaseInsensitive() throws Exception {
    Subreddit subreddit1 = new Subreddit("Serbia", "Serbia's official subreddit", user);
    Subreddit subreddit2 = new Subreddit("Programming Serbia", "Serbia's programming community", user);
    subredditRepository.saveAll(List.of(subreddit1, subreddit2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/subreddits")
            .servletPath("/api/search/subreddits")
            .param("q", "serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].subredditList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.subredditList[0].name", is(subreddit2.getName())))
        .andExpect(jsonPath("$._embedded.subredditList[1].name", is(subreddit1.getName())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchSubreddits_ReturnsSubredditsPageSortedByName_WithWrongSortProvided() throws Exception {
    Subreddit subreddit1 = new Subreddit("Serbia", "Serbia's official subreddit", user);
    Subreddit subreddit2 = new Subreddit("Programming Serbia", "Serbia's programming community", user);
    subredditRepository.saveAll(List.of(subreddit1, subreddit2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/subreddits")
            .servletPath("/api/search/subreddits")
            .param("q", "Serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].subredditList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.subredditList[0].name", is(subreddit2.getName())))
        .andExpect(jsonPath("$._embedded.subredditList[1].name", is(subreddit1.getName())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchSubreddits_ReturnsEmptyList_WhenSubredditsDontMatchQuery() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/subreddits")
            .servletPath("/api/search/subreddits")
            .param("q", "Serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded").doesNotExist())
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(0)))
        .andExpect(jsonPath("$.page.totalPages", is(0)));
  }

  @Test
  void searchPosts_ReturnsPostsPage_WithDefaultSort() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    subredditRepository.save(subreddit);
    Post post1 = new Post(user, "How is the weather in Serbia?", "Is it warm?", subreddit);
    Post post2 = new Post(user, "Serbia's player just won", "Congrats", subreddit);
    postRepository.saveAll(List.of(post1, post2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/posts")
            .servletPath("/api/search/posts")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postList[0].title", is(post1.getTitle())))
        .andExpect(jsonPath("$._embedded.postList[1].title", is(post2.getTitle())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchPosts_ReturnsPostsPage_IsCaseInsensitive() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    subredditRepository.save(subreddit);
    Post post1 = new Post(user, "How is the weather in Serbia?", "Is it warm?", subreddit);
    Post post2 = new Post(user, "Serbia's player just won", "Congrats", subreddit);
    postRepository.saveAll(List.of(post1, post2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/posts")
            .servletPath("/api/search/posts")
            .param("q", "serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postList[0].title", is(post1.getTitle())))
        .andExpect(jsonPath("$._embedded.postList[1].title", is(post2.getTitle())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchPosts_ReturnsPostsPage_WithWrongSortProvided() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    subredditRepository.save(subreddit);
    Post post1 = new Post(user, "How is the weather in Serbia?", "Is it warm?", subreddit);
    post1.setDateOfCreation(new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000));
    Post post2 = new Post(user, "Serbia's player just won", "Congrats", subreddit);
    postRepository.saveAll(List.of(post1, post2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/posts")
            .servletPath("/api/search/posts")
            .param("q", "serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postList[0].title", is(post1.getTitle())))
        .andExpect(jsonPath("$._embedded.postList[1].title", is(post2.getTitle())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchPosts_ReturnsEmptyPage_WhenNoPostsMatchQuery() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/posts")
            .servletPath("/api/search/posts")
            .param("q", "serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded").doesNotExist())
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(0)))
        .andExpect(jsonPath("$.page.totalPages", is(0)));
  }

  @Test
  void searchComment_ReturnsPostsPage_WithDefaultSort() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    subredditRepository.save(subreddit);
    Post post = new Post(user, "How is the weather in Serbia?", "Is it warm?", subreddit);
    postRepository.save(post);
    Comment comment1 = new Comment(user, "Serbia's weather is nice", post);
    Comment comment2 = new Comment(user, "I love weather in Serbia", post);
    commentRepository.saveAll(List.of(comment1, comment2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/comments")
            .servletPath("/api/search/comments")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentList[0].text", is(comment2.getText())))
        .andExpect(jsonPath("$._embedded.commentList[1].text", is(comment1.getText())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchComment_ReturnsPostsPage_IsCaseInsensitive() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    subredditRepository.save(subreddit);
    Post post = new Post(user, "How is the weather in Serbia?", "Is it warm?", subreddit);
    postRepository.save(post);
    Comment comment1 = new Comment(user, "Serbia's weather is nice", post);
    Comment comment2 = new Comment(user, "I love weather in Serbia", post);
    commentRepository.saveAll(List.of(comment1, comment2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/comments")
            .servletPath("/api/search/comments")
            .param("q", "serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentList[0].text", is(comment2.getText())))
        .andExpect(jsonPath("$._embedded.commentList[1].text", is(comment1.getText())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchComment_ReturnsPostsPage_WithWrongSortProvided() throws Exception {
    Subreddit subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    subredditRepository.save(subreddit);
    Post post = new Post(user, "How is the weather in Serbia?", "Is it warm?", subreddit);
    postRepository.save(post);
    Comment comment1 = new Comment(user, "Serbia's weather is nice", post);
    Comment comment2 = new Comment(user, "I love weather in Serbia", post);
    commentRepository.saveAll(List.of(comment1, comment2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/comments")
            .servletPath("/api/search/comments")
            .param("q", "Serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentList[0].text", is(comment2.getText())))
        .andExpect(jsonPath("$._embedded.commentList[1].text", is(comment1.getText())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchComment_ReturnsEmptyPage_WhenNoPostsMatchQuery() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/comments")
            .servletPath("/api/search/comments")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded").doesNotExist())
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(0)))
        .andExpect(jsonPath("$.page.totalPages", is(0)));
  }

  @Test
  void searchUsers() {
  }

  @Test
  void searchUsers_ReturnsPostsPage_WithDefaultSort() throws Exception {
    User user1 = new User("Jane", "Doe", "jane.doe@example.org", "serbiadoe", "iloveyou", "GB", true);
    User user2 = new User("Jane", "Doe", "jane.doe@example.org", "janeSerbia", "iloveyou", "GB", true);
    userRepository.saveAll(List.of(user1, user2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/users")
            .servletPath("/api/search/users")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].userList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.userList[0].username", is(user2.getUsername())))
        .andExpect(jsonPath("$._embedded.userList[1].username", is(user1.getUsername())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchUsers_ReturnsPostsPage_IsCaseInsensitive() throws Exception {
    User user1 = new User("Jane", "Doe", "jane.doe@example.org", "serbiadoe", "iloveyou", "GB", true);
    User user2 = new User("Jane", "Doe", "jane.doe@example.org", "janeSerbia", "iloveyou", "GB", true);
    userRepository.saveAll(List.of(user1, user2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/users")
            .servletPath("/api/search/users")
            .param("q", "serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].userList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.userList[0].username", is(user2.getUsername())))
        .andExpect(jsonPath("$._embedded.userList[1].username", is(user1.getUsername())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchUsers_ReturnsPostsPage_WithWrongSortProvided() throws Exception {
    User user1 = new User("Jane", "Doe", "jane.doe@example.org", "serbiadoe", "iloveyou", "GB", true);
    User user2 = new User("Jane", "Doe", "jane.doe@example.org", "janeSerbia", "iloveyou", "GB", true);
    userRepository.saveAll(List.of(user1, user2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/users")
            .servletPath("/api/search/users")
            .param("q", "Serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].userList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.userList[0].username", is(user2.getUsername())))
        .andExpect(jsonPath("$._embedded.userList[1].username", is(user1.getUsername())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchUsers_ReturnsEmptyPage_WhenNoPostsMatchQuery() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/users")
            .servletPath("/api/search/users")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded").doesNotExist())
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(0)))
        .andExpect(jsonPath("$.page.totalPages", is(0)));
  }
}