package com.srecko.reddit;

import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ConfigurationPropertiesScan
@EntityScan(basePackages = {"com.srecko.reddit"})
public class RedditcloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedditcloneApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner initData(UserRepository userRepository, SubredditRepository subredditRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			User user = new User("John", "Wane", "john@wane.com", "john", passwordEncoder.encode("password"), "Belize", true);

			Subreddit subreddit = new Subreddit("H3H3", "H3's official subreddit", user);

			Post post = new Post(user, "I love the last episode.", "It was nice.", subreddit);
			subreddit.getPosts().add(post);
			user.getPosts().add(post);

			subredditRepository.save(subreddit);
			userRepository.save(user);
		};
	}*/
}
