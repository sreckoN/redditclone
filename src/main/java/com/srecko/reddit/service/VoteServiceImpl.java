package com.srecko.reddit.service;

import com.srecko.reddit.dto.VoteDto;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.entity.Vote;
import com.srecko.reddit.entity.VoteType;
import com.srecko.reddit.exception.PostNotFoundException;
import com.srecko.reddit.exception.UserNotFoundException;
import com.srecko.reddit.exception.VoteNotFoundException;
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

    @Autowired
    public VoteServiceImpl(VoteRepository voteRepository, UserRepository userRepository, PostRepository postRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Vote save(VoteDto voteDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findUserByUsername((String) principal);
        if (userOptional.isPresent()) {
            Optional<Post> postOptional = postRepository.findById(voteDto.getPostId());
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                post.setVotes(post.getVotes() + voteDto.getType().getVal());
                Vote vote = new Vote(userOptional.get(), postOptional.get(), voteDto.getType());
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
    public Vote delete(Long id) {
        Optional<Vote> voteOptional = voteRepository.findById(id);
        if (voteOptional.isPresent()) {
            Vote vote = voteOptional.get();
            Optional<Post> postOptional = postRepository.findById(vote.getPost().getId() );
            if (postOptional.isPresent()) {
                Post post = postOptional.get();
                int change = (vote.getType().equals(VoteType.UPVOTE)) ? -1 : 1;
                post.setVotes(post.getVotes() + change);
                voteRepository.delete(vote);
            } else {
                throw new PostNotFoundException(vote.getPost().getId());
            }
        } else {
            throw new VoteNotFoundException(id);
        }
        return null;
    }
}
