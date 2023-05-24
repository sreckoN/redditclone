package com.srecko.reddit.comments;

import com.srecko.reddit.comments.entity.Comment;
import com.srecko.reddit.comments.entity.CommentParentType;
import com.srecko.reddit.comments.repository.CommentRepository;
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

  private final CommentRepository commentRepository;

  /**
   * Instantiates a new Data loader.
   *
   * @param commentRepository the comment repository
   */
  public DataLoader(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  @Override
  @Transactional
  public void run(String... args) {
    Comment comment1 = new Comment(1L, "Novi Sad", CommentParentType.POST, 1L);
    Comment comment2 = new Comment(1L, "Zlatibor", CommentParentType.POST, 1L);
    commentRepository.saveAll(List.of(comment1, comment2));
  }
}
