package com.srecko.reddit.repository;

import com.srecko.reddit.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Vote repository.
 *
 * @author Srecko Nikolic
 */
@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
