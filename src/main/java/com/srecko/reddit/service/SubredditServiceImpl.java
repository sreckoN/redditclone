package com.srecko.reddit.service;

import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.SubredditNotFoundException;
import com.srecko.reddit.exception.UserNotFoundException;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = {UserNotFoundException.class, SubredditNotFoundException.class})
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
        Optional<Subreddit> subredditOptional = subredditRepository.findById(id);
        if (subredditOptional.isPresent()) {
            return subredditOptional.get();
        } else {
            throw new SubredditNotFoundException(id);
        }
    }

    @Override
    public Subreddit save(SubredditDto subredditDto) {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findUserByUsername((String) o);
        if (user.isPresent()) {
            return new Subreddit(subredditDto.getName(), subredditDto.getDescription(), user.get());
        } else {
            throw new UserNotFoundException((String) o);
        }
    }

    @Override
    public Subreddit delete(Long id) {
        Optional<Subreddit> subredditOptional = subredditRepository.findById(id);
        if (subredditOptional.isPresent()) {
            subredditRepository.delete(subredditOptional.get());
            return subredditOptional.get();
        } else {
            throw new SubredditNotFoundException(id);
        }
    }

    @Override
    public Subreddit update(SubredditDto subredditDto) {
        Optional<Subreddit> subredditOptional = subredditRepository.findById(subredditDto.getSubredditId());
        if (subredditOptional.isPresent()) {
            Subreddit subreddit = subredditOptional.get();
            subreddit.setName(subredditDto.getName());
            subreddit.setDescription(subredditDto.getDescription());
            return subredditRepository.save(subreddit);
        } else {
            throw new SubredditNotFoundException(subredditDto.getSubredditId());
        }
    }
}
