package com.srecko.reddit.votes.repository;

import com.srecko.reddit.votes.entity.Vote;
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