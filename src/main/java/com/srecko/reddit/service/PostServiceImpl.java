package com.srecko.reddit.service;

import com.srecko.reddit.dto.CreatePostDto;
import com.srecko.reddit.dto.UpdatePostDto;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.PostNotFoundException;
import com.srecko.reddit.exception.SubredditNotFoundException;
import com.srecko.reddit.exception.UserNotFoundException;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = {UserNotFoundException.class, SubredditNotFoundException.class, PostNotFoundException.class})
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
    public Post save(CreatePostDto createPostDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findUserByUsername((String) principal);
        if (user.isPresent()) {
            Optional<Subreddit> subreddit = subredditRepository.findById(createPostDto.getSubredditId());
            if (subreddit.isPresent()) {
                Post post = new Post(user.get(), createPostDto.getTitle(), createPostDto.getText(), subreddit.get());
                user.get().getPosts().add(post);
                subreddit.get().getPosts().add(post);
                return post;
            } else {
                throw new SubredditNotFoundException(createPostDto.getSubredditId());
            }
        } else {
            throw new UserNotFoundException((String) principal);
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
        else throw new PostNotFoundException(postId);
    }

    @Override
    public List<Post> getAllPostsForSubreddit(Long subredditId) {
        Optional<Subreddit> subredditOptional = subredditRepository.findById(subredditId);
        if (subredditOptional.isPresent()) {
            return postRepository.findAllBySubreddit(subredditOptional.get());
        } else {
            throw new SubredditNotFoundException(subredditId);
        }
    }

    @Override
    public List<Post> getAllPostsForUser(String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if (userOptional.isPresent()) {
            return postRepository.findAllByUser(userOptional.get());
        } else {
            throw new UserNotFoundException(username);
        }
    }

    @Override
    public Post delete(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            postRepository.deleteById(postId);
            return postOptional.get();
        } else {
            throw new PostNotFoundException(postId);
        }
    }

    @Override
    public Post update(UpdatePostDto postDto) {
        Optional<Post> postOptional = postRepository.findById(postDto.getPostId());
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setTitle(postDto.getTitle());
            post.setText(postDto.getText());
            post.setId(postDto.getPostId());
            return postRepository.save(post);
        } else {
            throw new PostNotFoundException(postDto.getPostId());
        }
    }
}
