package com.srecko.reddit.service;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.entity.Comment;
import java.util.List;

public interface CommentService {

  List<Comment> getAllCommentsForPost(Long postId);

  List<Comment> getAllCommentsForUsername(String username);

  Comment save(CommentDto commentDto);

  Comment delete(Long commentId);

  Comment getComment(Long commentId);

  List<Comment> getAllComments();
}
