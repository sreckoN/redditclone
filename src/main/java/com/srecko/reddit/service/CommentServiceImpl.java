package com.srecko.reddit.service;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Comment> getAllCommentsForPost(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        // custom exception
        return postOptional.map(commentRepository::findAllByPost).orElse(null);
    }

    @Override
    public List<Comment> getAllCommentsForUsername(String username) {
        Optional<User> optionalUser = userRepository.findUserByUsername(username);
        // custom exception
        return optionalUser.map(commentRepository::findAllByUser).orElse(null);
    }

    @Override
    public Comment save(CommentDto commentDto) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findUserByUsername(principal.getUsername());
        if (user.isPresent()) {
            Comment comment = new Comment(user.get(), commentDto.getText(), commentDto.getPost());
            return commentRepository.save(comment);
        } else {
            // custom exception
            return null;
        }
    }

    @Override
    public Comment delete(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            commentRepository.delete(commentOptional.get());
            return commentOptional.get();
        } else {
            // custom exception
            return null;
        }
    }
}
