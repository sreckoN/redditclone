package com.srecko.reddit;

import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import java.util.Date;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Data loader.
 *
 * @author Srecko Nikolic
 */
@Configuration
public class DataLoader implements CommandLineRunner {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final SubredditRepository subredditRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;

  /**
   * Instantiates a new Data loader.
   *
   * @param passwordEncoder     the password encoder
   * @param userRepository      the user repository
   * @param subredditRepository the subreddit repository
   * @param postRepository      the post repository
   * @param commentRepository   the comment repository
   */
  public DataLoader(PasswordEncoder passwordEncoder, UserRepository userRepository,
      SubredditRepository subredditRepository, PostRepository postRepository,
      CommentRepository commentRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.subredditRepository = subredditRepository;
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
  }

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    User user = new User("Srecko", "Nikolic", "srecko.nikolic@protonmail.com", "sreckon",
        passwordEncoder.encode("password"), "Serbia", true);
    user.setRegistrationDate(new Date());
    userRepository.save(user);

    Subreddit subreddit = new Subreddit("Serbia", "Official subreddit", user);
    subreddit.setCreatedDate(new Date());
    subredditRepository.save(subreddit);

    Post post1 = new Post(user, "Cool places in Serbia", "...", subreddit);
    Post post2 = new Post(user, "Today's news in Serbia", "...", subreddit);
    postRepository.saveAll(List.of(post1, post2));

    Comment comment1 = new Comment(user, "Novi Sad", post1);
    Comment comment2 = new Comment(user, "Zlatibor", post1);
    commentRepository.saveAll(List.of(comment1, comment2));
  }
}
