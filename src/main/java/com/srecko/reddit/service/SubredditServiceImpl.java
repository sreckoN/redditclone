package com.srecko.reddit.service;

import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubredditServiceImpl implements SubredditService {

    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubredditServiceImpl(SubredditRepository subredditRepository, UserRepository userRepository) {
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Subreddit> getAll() {
        return (List<Subreddit>) subredditRepository.findAll();
    }

    @Override
    public Subreddit getSubredditById(Long id) {
        // custom exception
        return subredditRepository.findById(id).orElse(null);
    }

    @Override
    public Subreddit save(SubredditDto subredditDto) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findUserByUsername(principal.getUsername());
        if (user.isPresent()) {
            Subreddit subreddit = new Subreddit(subredditDto.getName(), subredditDto.getDescription(), user.get());
            return subredditRepository.save(subreddit);
        } else {
            // custom exception
            return null;
        }
    }

    @Override
    public Subreddit delete(Long id) {
        Optional<Subreddit> subredditOptional = subredditRepository.findById(id);
        if (subredditOptional.isPresent()) {
            subredditRepository.delete(subredditOptional.get());
            return subredditOptional.get();
        } else {
            // custom exception
            return null;
        }
    }

    @Override
    public Subreddit update(SubredditDto subredditDto) {
        Optional<Subreddit> subredditOptional = subredditRepository.findById(subredditDto.getId());
        if (subredditOptional.isPresent()) {
            Subreddit subreddit = subredditOptional.get();
            subreddit.setName(subredditDto.getName());
            subreddit.setDescription(subredditDto.getDescription());
            return subredditRepository.save(subreddit);
        } else {
            // custom exception
            return null;
        }
    }
}
