package com.srecko.reddit.controller;

import com.srecko.reddit.dto.VoteDto;
import com.srecko.reddit.entity.Vote;
import com.srecko.reddit.exception.DtoValidationException;
import com.srecko.reddit.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<Vote> save(@Valid @RequestBody VoteDto voteDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new DtoValidationException(bindingResult.getAllErrors());
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/votes").toUriString());
        return ResponseEntity.created(uri).body(voteService.save(voteDto));
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<Vote> delete(@PathVariable("voteId") Long voteId) {
        return ResponseEntity.ok(voteService.delete(voteId));
    }
}