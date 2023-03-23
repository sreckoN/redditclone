package com.srecko.reddit.repository;

import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  Optional<Post> findById(Long id);

  List<Post> findAllBySubreddit(Subreddit subreddit);

  List<Post> findAllByUser(User user);

  void deleteById(Long id);

  Page<Post> findByTitleContaining(String query, Pageable pageable);
}
