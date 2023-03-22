package com.srecko.reddit;

import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SubredditRepository subredditRepository;
    private final PostRepository postRepository;

    public DataLoader(PasswordEncoder passwordEncoder, UserRepository userRepository, SubredditRepository subredditRepository, PostRepository postRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.subredditRepository = subredditRepository;
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        User user = new User("Srecko", "Nikolic", "srecko.nikolic@protonmail.com", "sreckon", passwordEncoder.encode("password"), "Serbia", true);
        user.setRegistrationDate(new Date());
        userRepository.save(user);

        Subreddit subreddit = new Subreddit("Serbia", "Official subreddit", user);
        subreddit.setCreatedDate(new Date());
        subredditRepository.save(subreddit);

        Post post1 = new Post(user, "Cool places in Serbia", "...", subreddit);
        Post post2 = new Post(user, "Today's news in Serbia", "...", subreddit);
        postRepository.saveAll(List.of(post1, post2));
    }
}
