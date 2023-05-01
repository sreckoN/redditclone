package com.srecko.reddit.posts;

import com.srecko.reddit.posts.entity.Post;
import com.srecko.reddit.posts.repository.PostRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Data loader.
 *
 * @author Srecko Nikolic
 */
@Configuration
@Profile("dev")
public class DataLoader implements CommandLineRunner {

  private final PostRepository postRepository;

  /**
   * Instantiates a new Data loader.
   *
   * @param postRepository the post repository
   */
  public DataLoader(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  @Transactional
  public void run(String... args) {
    Post post1 = new Post(1L, "Cool places in Serbia", "...", 1L);
    Post post2 = new Post(2L, "Today's news in Serbia", "...", 1L);
    postRepository.saveAll(List.of(post1, post2));
  }
}
