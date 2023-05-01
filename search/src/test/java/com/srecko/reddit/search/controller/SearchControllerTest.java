package com.srecko.reddit.search.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.srecko.reddit.search.dto.CommentDto;
import com.srecko.reddit.search.dto.PostDto;
import com.srecko.reddit.search.dto.SubredditDto;
import com.srecko.reddit.search.dto.UserDto;
import com.srecko.reddit.search.service.client.CommentsFeignClient;
import com.srecko.reddit.search.service.client.PostsFeignClient;
import com.srecko.reddit.search.service.client.SubredditsFeignClient;
import com.srecko.reddit.search.service.client.UsersFeignClient;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

// @TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
/*@WithMockUser(username = "janedoe", password = "iloveyou")
@WithUserDetails("janedoe")*/
//@Transactional
class SearchControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UsersFeignClient usersFeignClient;

  @MockBean
  private SubredditsFeignClient subredditsFeignClient;

  @MockBean
  private PostsFeignClient postsFeignClient;

  @MockBean
  private CommentsFeignClient commentsFeignClient;

  @Test
  void searchSubreddits_ReturnsSubredditsPage_WithDefaultSort() throws Exception {
    SubredditDto subredditDto = new SubredditDto();
    subredditDto.setName("Serbia");

    SubredditDto subredditDto1 = new SubredditDto();
    subredditDto1.setName("Programming Serbia");

    given(subredditsFeignClient.searchSubreddits(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(subredditDto), EntityModel.of(subredditDto1)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/subreddits")
            .servletPath("/api/search/subreddits")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].subredditDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.subredditDtoList[0].name", is(subredditDto.getName())))
        .andExpect(jsonPath("$._embedded.subredditDtoList[1].name", is(subredditDto1.getName())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchSubreddits_ReturnsSubredditsPage_IsCaseInsensitive() throws Exception {
    SubredditDto subredditDto = new SubredditDto();
    subredditDto.setName("Serbia");

    SubredditDto subredditDto1 = new SubredditDto();
    subredditDto1.setName("Programming Serbia");

    given(subredditsFeignClient.searchSubreddits(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(subredditDto), EntityModel.of(subredditDto1)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/subreddits")
            .servletPath("/api/search/subreddits")
            .param("q", "serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].subredditDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.subredditDtoList[0].name", is(subredditDto.getName())))
        .andExpect(jsonPath("$._embedded.subredditDtoList[1].name", is(subredditDto1.getName())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchSubreddits_ReturnsSubredditsPageSortedByName_WithWrongSortProvided() throws Exception {
    SubredditDto subredditDto = new SubredditDto();
    subredditDto.setName("Serbia");

    SubredditDto subredditDto1 = new SubredditDto();
    subredditDto1.setName("Programming Serbia");

    given(subredditsFeignClient.searchSubreddits(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(subredditDto), EntityModel.of(subredditDto1)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/subreddits")
            .servletPath("/api/search/subreddits")
            .param("q", "Serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].subredditDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.subredditDtoList[0].name", is(subredditDto.getName())))
        .andExpect(jsonPath("$._embedded.subredditDtoList[1].name", is(subredditDto1.getName())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchSubreddits_ReturnsEmptyList_WhenSubredditsDontMatchQuery() throws Exception {
    given(subredditsFeignClient.searchSubreddits(any(), any())).willReturn(
        PagedModel.empty(new PageMetadata(0, 0, 0))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/subreddits")
            .servletPath("/api/search/subreddits")
            .param("q", "Serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded").doesNotExist())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(0)))
        .andExpect(jsonPath("$.page.totalElements", is(0)))
        .andExpect(jsonPath("$.page.totalPages", is(0)));
  }

  @Test
  void searchPosts_ReturnsPostsPage_WithDefaultSort() throws Exception {
    PostDto postDto1 = new PostDto();
    postDto1.setId(123L);
    postDto1.setTitle("How is the weather in Serbia?");
    postDto1.setText("Is it warm?");

    PostDto postDto2 = new PostDto();
    postDto2.setId(124L);
    postDto2.setTitle("Serbia's player just won");
    postDto2.setText("Congrats");

    given(postsFeignClient.searchPosts(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(postDto1), EntityModel.of(postDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/posts")
            .servletPath("/api/search/posts")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postDtoList[0].title", is(postDto1.getTitle())))
        .andExpect(jsonPath("$._embedded.postDtoList[1].title", is(postDto2.getTitle())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchPosts_ReturnsPostsPage_IsCaseInsensitive() throws Exception {
    PostDto postDto1 = new PostDto();
    postDto1.setId(123L);
    postDto1.setTitle("How is the weather in Serbia?");
    postDto1.setText("Is it warm?");

    PostDto postDto2 = new PostDto();
    postDto2.setId(124L);
    postDto2.setTitle("Serbia's player just won");
    postDto2.setText("Congrats");

    given(postsFeignClient.searchPosts(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(postDto1), EntityModel.of(postDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/posts")
            .servletPath("/api/search/posts")
            .param("q", "serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postDtoList[0].title", is(postDto1.getTitle())))
        .andExpect(jsonPath("$._embedded.postDtoList[1].title", is(postDto2.getTitle())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchPosts_ReturnsPostsPage_WithWrongSortProvided() throws Exception {
    PostDto postDto1 = new PostDto();
    postDto1.setId(123L);
    postDto1.setTitle("How is the weather in Serbia?");
    postDto1.setText("Is it warm?");

    PostDto postDto2 = new PostDto();
    postDto2.setId(124L);
    postDto2.setTitle("Serbia's player just won");
    postDto2.setText("Congrats");

    given(postsFeignClient.searchPosts(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(postDto1), EntityModel.of(postDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/posts")
            .servletPath("/api/search/posts")
            .param("q", "serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postDtoList[0].title", is(postDto1.getTitle())))
        .andExpect(jsonPath("$._embedded.postDtoList[1].title", is(postDto2.getTitle())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchPosts_ReturnsEmptyPage_WhenNoPostsMatchQuery() throws Exception {
    given(postsFeignClient.searchPosts(any(), any())).willReturn(
        PagedModel.empty(new PageMetadata(0, 0, 0))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/posts")
            .servletPath("/api/search/posts")
            .param("q", "serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded").doesNotExist())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(0)))
        .andExpect(jsonPath("$.page.totalElements", is(0)))
        .andExpect(jsonPath("$.page.totalPages", is(0)));
  }

  @Test
  void searchComment_ReturnsPostsPage_WithDefaultSort() throws Exception {
    CommentDto commentDto1 = new CommentDto();
    commentDto1.setId(123L);
    commentDto1.setText("Serbia's weather is nice");

    CommentDto commentDto2 = new CommentDto();
    commentDto2.setId(124L);
    commentDto2.setText("I love weather in Serbia");

    given(commentsFeignClient.searchComments(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(commentDto1), EntityModel.of(commentDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/comments")
            .servletPath("/api/search/comments")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentDtoList[0].text", is(commentDto1.getText())))
        .andExpect(jsonPath("$._embedded.commentDtoList[1].text", is(commentDto2.getText())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchComment_ReturnsPostsPage_IsCaseInsensitive() throws Exception {
    CommentDto commentDto1 = new CommentDto();
    commentDto1.setId(123L);
    commentDto1.setText("Serbia's weather is nice");

    CommentDto commentDto2 = new CommentDto();
    commentDto2.setId(124L);
    commentDto2.setText("I love weather in Serbia");

    given(commentsFeignClient.searchComments(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(commentDto1), EntityModel.of(commentDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/comments")
            .servletPath("/api/search/comments")
            .param("q", "serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentDtoList[0].text", is(commentDto1.getText())))
        .andExpect(jsonPath("$._embedded.commentDtoList[1].text", is(commentDto2.getText())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchComment_ReturnsPostsPage_WithWrongSortProvided() throws Exception {
    CommentDto commentDto1 = new CommentDto();
    commentDto1.setId(123L);
    commentDto1.setText("Serbia's weather is nice");

    CommentDto commentDto2 = new CommentDto();
    commentDto2.setId(124L);
    commentDto2.setText("I love weather in Serbia");

    given(commentsFeignClient.searchComments(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(commentDto1), EntityModel.of(commentDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/comments")
            .servletPath("/api/search/comments")
            .param("q", "Serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentDtoList[0].text", is(commentDto1.getText())))
        .andExpect(jsonPath("$._embedded.commentDtoList[1].text", is(commentDto2.getText())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchComment_ReturnsEmptyPage_WhenNoPostsMatchQuery() throws Exception {
    given(commentsFeignClient.searchComments(any(), any())).willReturn(
        PagedModel.empty(new PageMetadata(0, 0, 0))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/comments")
            .servletPath("/api/search/comments")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded").doesNotExist())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(0)))
        .andExpect(jsonPath("$.page.totalElements", is(0)))
        .andExpect(jsonPath("$.page.totalPages", is(0)));
  }

  @Test
  void searchUsers_ReturnsPostsPage_WithDefaultSort() throws Exception {
    UserDto userDto1 = new UserDto();
    userDto1.setUsername("serbiajane");

    UserDto userDto2 = new UserDto();
    userDto2.setUsername("janeserbia");

    given(usersFeignClient.searchUsers(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(userDto1), EntityModel.of(userDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/users")
            .servletPath("/api/search/users")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].userDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.userDtoList[0].username", is(userDto1.getUsername())))
        .andExpect(jsonPath("$._embedded.userDtoList[1].username", is(userDto2.getUsername())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchUsers_ReturnsPostsPage_IsCaseInsensitive() throws Exception {
    UserDto userDto1 = new UserDto();
    userDto1.setUsername("serbiajane");

    UserDto userDto2 = new UserDto();
    userDto2.setUsername("janeserbia");

    given(usersFeignClient.searchUsers(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(userDto1), EntityModel.of(userDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/users")
            .servletPath("/api/search/users")
            .param("q", "serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].userDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.userDtoList[0].username", is(userDto1.getUsername())))
        .andExpect(jsonPath("$._embedded.userDtoList[1].username", is(userDto2.getUsername())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchUsers_ReturnsPostsPage_WithWrongSortProvided() throws Exception {
    UserDto userDto1 = new UserDto();
    userDto1.setUsername("serbiajane");

    UserDto userDto2 = new UserDto();
    userDto2.setUsername("janeserbia");

    given(usersFeignClient.searchUsers(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(userDto1), EntityModel.of(userDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/users")
            .servletPath("/api/search/users")
            .param("q", "Serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].userDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.userDtoList[0].username", is(userDto1.getUsername())))
        .andExpect(jsonPath("$._embedded.userDtoList[1].username", is(userDto2.getUsername())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchUsers_ReturnsEmptyPage_WhenNoPostsMatchQuery() throws Exception {
    given(usersFeignClient.searchUsers(any(), any())).willReturn(
        PagedModel.empty(new PageMetadata(0, 0, 0))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/users")
            .servletPath("/api/search/users")
            .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded").doesNotExist())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(0)))
        .andExpect(jsonPath("$.page.totalElements", is(0)))
        .andExpect(jsonPath("$.page.totalPages", is(0)));
  }

  @Test
  void searchPostsInSubreddit_ReturnsPostsForSubreddit() throws Exception {
    Long subredditId = 1L;

    PostDto postDto1 = new PostDto();
    postDto1.setId(123L);
    postDto1.setTitle("How is the weather in Serbia?");
    postDto1.setText("Is it warm?");
    postDto1.setSubredditId(subredditId);

    PostDto postDto2 = new PostDto();
    postDto2.setId(124L);
    postDto2.setTitle("Serbia's player just won");
    postDto2.setText("Congrats");
    postDto2.setSubredditId(subredditId);

    given(postsFeignClient.searchPostsInSubreddit(any(), any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(postDto1), EntityModel.of(postDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/posts/subreddit/{subredditId}", subredditId)
        .servletPath("/api/search/posts/subreddit/" + subredditId)
        .param("q", "Serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postDtoList[0].title", is(postDto1.getTitle())))
        .andExpect(jsonPath("$._embedded.postDtoList[1].title", is(postDto2.getTitle())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchPostsInSubreddit_ReturnsPostsForSubreddit_IsCaseInsensitive() throws Exception {
    Long subredditId = 1L;

    PostDto postDto1 = new PostDto();
    postDto1.setId(123L);
    postDto1.setTitle("How is the weather in Serbia?");
    postDto1.setText("Is it warm?");
    postDto1.setSubredditId(subredditId);

    PostDto postDto2 = new PostDto();
    postDto2.setId(124L);
    postDto2.setTitle("Serbia's player just won");
    postDto2.setText("Congrats");
    postDto2.setSubredditId(subredditId);

    given(postsFeignClient.searchPostsInSubreddit(any(), any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(postDto1), EntityModel.of(postDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/posts/subreddit/{subredditId}", subredditId)
            .servletPath("/api/search/posts/subreddit/" + subredditId)
            .param("q", "serbia"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postDtoList[0].title", is(postDto1.getTitle())))
        .andExpect(jsonPath("$._embedded.postDtoList[1].title", is(postDto2.getTitle())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void searchPostsInSubreddit_ReturnsPostsPage_WithWrongSortProvided() throws Exception {
    Long subredditId = 1L;

    PostDto postDto1 = new PostDto();
    postDto1.setId(123L);
    postDto1.setTitle("How is the weather in Serbia?");
    postDto1.setText("Is it warm?");
    postDto1.setSubredditId(subredditId);

    PostDto postDto2 = new PostDto();
    postDto2.setId(124L);
    postDto2.setTitle("Serbia's player just won");
    postDto2.setText("Congrats");
    postDto2.setSubredditId(subredditId);

    given(postsFeignClient.searchPostsInSubreddit(any(), any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(postDto1), EntityModel.of(postDto2)), new PageMetadata(2, 0, 2))
    );

    mockMvc.perform(MockMvcRequestBuilders.get("/api/search/posts/subreddit/{subredditId}", subredditId)
            .servletPath("/api/search/posts/subreddit/" + subredditId)
            .param("q", "serbia")
            .param("sort", "id"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postDtoList[0].title", is(postDto1.getTitle())))
        .andExpect(jsonPath("$._embedded.postDtoList[1].title", is(postDto2.getTitle())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(2)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }
}