package com.srecko.reddit.controller;

import com.srecko.reddit.assembler.CommentModelAssembler;
import com.srecko.reddit.assembler.PostModelAssembler;
import com.srecko.reddit.assembler.SubredditModelAssembler;
import com.srecko.reddit.assembler.UserModelAssembler;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;
    private final PostModelAssembler postModelAssembler;
    private final CommentModelAssembler commentModelAssembler;
    private final UserModelAssembler userModelAssembler;
    private final SubredditModelAssembler subredditModelAssembler;

    @Autowired
    public SearchController(SearchService searchService, PostModelAssembler postModelAssembler, CommentModelAssembler commentModelAssembler, UserModelAssembler userModelAssembler, SubredditModelAssembler subredditModelAssembler) {
        this.searchService = searchService;
        this.postModelAssembler = postModelAssembler;
        this.commentModelAssembler = commentModelAssembler;
        this.userModelAssembler = userModelAssembler;
        this.subredditModelAssembler = subredditModelAssembler;
    }

    @GetMapping("/subreddits")
    public ResponseEntity<PagedModel<EntityModel<Subreddit>>> searchSubreddits(@RequestParam(name = "q") String query,
                                                                          @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                                                                          PagedResourcesAssembler<Subreddit> assembler) {
        Page<Subreddit> page = searchService.searchSubreddits(query, pageable);
        PagedModel<EntityModel<Subreddit>> pagedModel = assembler.toModel(page, subredditModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/posts")
    public ResponseEntity<PagedModel<EntityModel<Post>>> searchPosts(@RequestParam(name = "q") String query,
                                                                    @PageableDefault(sort = "dateOfCreation", direction = Sort.Direction.ASC) Pageable pageable,
                                                                    PagedResourcesAssembler<Post> assembler) {
        Page<Post> page = searchService.searchPosts(query, pageable);
        PagedModel<EntityModel<Post>> pagedModel = assembler.toModel(page, postModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/comments")
    public ResponseEntity<PagedModel<EntityModel<Comment>>> searchComment(@RequestParam(name = "q") String query,
                                                                        @PageableDefault(sort = "text", direction = Sort.Direction.ASC) Pageable pageable,
                                                                        PagedResourcesAssembler<Comment> assembler) {
        Page<Comment> page = searchService.searchComments(query, pageable);
        PagedModel<EntityModel<Comment>> pagedModel = assembler.toModel(page, commentModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/users")
    public ResponseEntity<PagedModel<EntityModel<User>>> searchUsers(@RequestParam(name = "q") String query,
                                                                     @PageableDefault(sort = "username", direction = Sort.Direction.ASC) Pageable pageable,
                                                                     PagedResourcesAssembler<User> assembler) {
        Page<User> page = searchService.searchUsers(query, pageable);
        PagedModel<EntityModel<User>> pagedModel = assembler.toModel(page, userModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    /*
    // Unable to type cast
    @GetMapping("/")
    public ResponseEntity<PagedModel<EntityModel<?>>> search(@RequestParam(name = "q", required = true) String query,
                                               @RequestParam(name = "type", defaultValue = "posts") String type,
                                                 @PageableDefault(page = 0, size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable,
                                                 PagedResourcesAssembler<?> assembler) {
        switch (type) {
            case "posts":
                Page<Post> postPage = searchService.searchPosts(query, pageable);
                PagedModel<EntityModel<Post>> pagedPostModel = assembler.toModel(postPage, postModelAssembler);
                return ResponseEntity.ok(pagedPostModel);
            case "comments":
                Page<Comment> commentPage = searchService.searchComments(query, pageable);
                PagedModel<EntityModel<Comment>> pagedCommentModel = assembler.toModel(commentPage, commentModelAssembler);
                return ResponseEntity.ok(pagedCommentModel);
            case "users":
                Page<User> userPage = searchService.searchUsers(query, pageable);
                PagedModel<EntityModel<User>> pagedUserModel = assembler.toModel(userPage, userModelAssembler);
                return ResponseEntity.ok(pagedUserModel);
            case "subreddits":
                Page<Subreddit> subredditPage = searchService.searchSubreddits(query, pageable);
                PagedModel<EntityModel<Subreddit>> pagedSubredditModel = assembler.toModel(subredditPage, subredditModelAssembler);
                return ResponseEntity.ok(pagedSubredditModel);
            default:
                throw new IllegalArgumentException("Invalid type parameter: " + type);
        }
        return ResponseEntity.ok(searchService.search(query, type, pageable));
    }*/
}