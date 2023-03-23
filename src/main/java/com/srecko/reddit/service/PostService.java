package com.srecko.reddit.service;

import com.srecko.reddit.dto.CreatePostDto;
import com.srecko.reddit.dto.UpdatePostDto;
import com.srecko.reddit.entity.Post;
import java.util.List;

public interface PostService {

  Post save(CreatePostDto postDto);

  List<Post> getAllPosts();

  Post getPost(Long postId);

  List<Post> getAllPostsForSubreddit(Long subredditId);

  List<Post> getAllPostsForUser(String username);

  Post delete(Long postId);

  Post update(UpdatePostDto postDto);
}
