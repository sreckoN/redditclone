package com.srecko.reddit.controller;

import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/subreddits")
public class SubredditController {

    private final SubredditService subredditService;

    @Autowired
    public SubredditController(SubredditService subredditService) {
        this.subredditService = subredditService;
    }

    @GetMapping
    public ResponseEntity<List<Subreddit>> getAll() {
        return ResponseEntity.ok(subredditService.getAll());
    }

    @GetMapping("/{subredditId}")
    public ResponseEntity<Subreddit> getSubreddit(@PathVariable("subredditId") Long id) {
        return ResponseEntity.ok(subredditService.getSubredditById(id));
    }

    @PostMapping
    public ResponseEntity<Subreddit> save(@RequestBody SubredditDto subredditDto) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/subreddits/").toUriString());
        return ResponseEntity.created(uri).body(subredditService.save(subredditDto));
    }

    @DeleteMapping("/{subredditId}")
    public ResponseEntity<Subreddit> delete(@PathVariable("subredditId") Long id) {
        return ResponseEntity.ok(subredditService.delete(id));
    }

    @PutMapping
    public ResponseEntity<Subreddit> update(@RequestBody SubredditDto subredditDto) {
        return ResponseEntity.ok(subredditService.update(subredditDto));
    }
}
