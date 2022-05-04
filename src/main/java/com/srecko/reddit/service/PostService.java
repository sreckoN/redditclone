package com.srecko.reddit.service;

import com.srecko.reddit.dto.PostDto;
import com.srecko.reddit.entity.Post;

import java.util.List;

public interface PostService {

    Post save(PostDto postRequestost);
    List<Post> getAllPosts();
    Post getPost(Long postId);
    List<Post> getAllPostsForSubreddit(Long subredditId);
    List<Post> getAllPostsForUser(String username);
    Post delete(Long postId);
}
