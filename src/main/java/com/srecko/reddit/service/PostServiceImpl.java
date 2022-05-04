package com.srecko.reddit.service;

import com.srecko.reddit.dto.PostDto;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SubredditRepository subredditRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, SubredditRepository subredditRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.subredditRepository = subredditRepository;
    }

    @Override
    public Post save(PostDto postRequest) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findUserByUsername(principal.getUsername());
        if (user.isPresent()) {
            Optional<Subreddit> subreddit = subredditRepository.findByName(postRequest.getSubredditName());
            if (subreddit.isPresent()) {
                Post post = new Post(user.get(), postRequest.getTitle(), postRequest.getText(), subreddit.get());
                post.setId(postRequest.getId());
                return postRepository.save(post);
            } else {
                // custom exception
                return null;
            }
        } else {
            // custom exception
            return null;
        }
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPost(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) return postOptional.get();
        else throw new RuntimeException("Post not found"); // custom exception
    }

    @Override
    public List<Post> getAllPostsForSubreddit(Long subredditId) {
        Optional<Subreddit> subredditOptional = subredditRepository.findById(subredditId);
        // custom exception
        return subredditOptional.map(postRepository::findAllBySubreddit).orElse(null);
    }

    @Override
    public List<Post> getAllPostsForUser(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        // custom exception
        return userOptional.map(postRepository::findAllByUser).orElse(null);
    }

    @Override
    public Post delete(Long postId) {
        Post post = postRepository.getById(postId);
        postRepository.deleteById(postId);
        return post;
    }
}
