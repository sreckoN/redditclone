package com.srecko.reddit.subreddits;

import com.srecko.reddit.subreddits.entity.Subreddit;
import com.srecko.reddit.subreddits.repository.SubredditRepository;
import java.util.Date;
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

  private final SubredditRepository subredditRepository;

  /**
   * Instantiates a new Data loader.
   *
   * @param subredditRepository the subreddit repository
   */
  public DataLoader(SubredditRepository subredditRepository) {
    this.subredditRepository = subredditRepository;
  }

  @Override
  @Transactional
  public void run(String... args) {
    Subreddit subreddit = new Subreddit("Serbia", "Official subreddit", 1L);
    subreddit.setCreatedDate(new Date());
    subredditRepository.save(subreddit);
  }
}
