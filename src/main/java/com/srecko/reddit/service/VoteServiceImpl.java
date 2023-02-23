package com.srecko.reddit.service;

import com.srecko.reddit.dto.VoteCommentDto;
import com.srecko.reddit.dto.VotePostDto;
import com.srecko.reddit.entity.*;
import com.srecko.reddit.exception.CommentNotFoundException;
import com.srecko.reddit.exception.PostNotFoundException;
import com.srecko.reddit.exception.UserNotFoundException;
import com.srecko.reddit.exception.VoteNotFoundException;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.UserRepository;
import com.srecko.reddit.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = {PostNotFoundException.class, UserNotFoundException.class, VoteNotFoundException.class})
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository, UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Vote savePostVote(VotePostDto voteDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findUserByUsername((String) principal);
        if (userOptional.isPresent()) {
            Optional<Post> postOptional = postRepository.findById(voteDto.getPostId());
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                post.setVotes(post.getVotes() + voteDto.getType().getVal());
                Vote vote = new VotePost(userOptional.get(), voteDto.getType(), postOptional.get());
                voteRepository.save(vote);
                return vote;
            } else {
                throw new PostNotFoundException(voteDto.getPostId());
            }
        } else {
            throw new UserNotFoundException((String) principal);
        }
    }

    @Override
    public Vote saveCommentVote(VoteCommentDto voteDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findUserByUsername((String) principal);
        if (userOptional.isPresent()) {
            Optional<Comment> commentOptional = commentRepository.findById(voteDto.getCommentId());
            if (commentOptional.isPresent()) {
                Comment comment = commentOptional.get();
                comment.setVotes(comment.getVotes() + voteDto.getType().getVal());
                Vote vote = new VoteComment(userOptional.get(), voteDto.getType(), commentOptional.get());
                voteRepository.save(vote);
                return vote;
            } else {
                throw new CommentNotFoundException(voteDto.getCommentId());
            }
        } else {
            throw new UserNotFoundException((String) principal);
        }
    }

    @Override
    public Vote deletePostVote(Long id) {
        Optional<Vote> voteOptional = voteRepository.findById(id);
        if (voteOptional.isPresent()) {
            VotePost vote = (VotePost) voteOptional.get();
            Optional<Post> postOptional = postRepository.findById(vote.getPost().getId() );
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                int change = (vote.getType().equals(VoteType.UPVOTE)) ? -1 : 1;
                post.setVotes(post.getVotes() + change);
                voteRepository.delete(vote);
                return vote;
            } else {
                throw new PostNotFoundException(vote.getPost().getId());
            }
        } else {
            throw new VoteNotFoundException(id);
        }
    }

    @Override
    public Vote deleteCommentVote(Long id) {
        Optional<Vote> voteOptional = voteRepository.findById(id);
        if (voteOptional.isPresent()) {
            VoteComment vote = (VoteComment) voteOptional.get();
            Optional<Comment> commentOptional = commentRepository.findById(vote.getComment().getId() );
            if (commentOptional.isPresent()) {
                Comment comment = commentOptional.get();
                int change = (vote.getType().equals(VoteType.UPVOTE)) ? -1 : 1;
                comment.setVotes(comment.getVotes() + change);
                voteRepository.delete(vote);
                return vote;
            } else {
                throw new PostNotFoundException(vote.getComment().getId());
            }
        } else {
            throw new VoteNotFoundException(id);
        }
    }
}
