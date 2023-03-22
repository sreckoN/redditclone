package com.srecko.reddit.service;

import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {

    Page<User> searchUsers(String query, Pageable pageable);
    Page<Subreddit> searchSubreddits(String query, Pageable pageable);
    Page<Post> searchPosts(String query, Pageable pageable);
    Page<Comment> searchComments(String query, Pageable pageable);
}