package com.campusconnect.post_service.controller;

import com.campusconnect.post_service.model.PostComment;
import com.campusconnect.post_service.service.LikeAndCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class LikeAndCommentController {

    private final LikeAndCommentService likeAndCommentService;

    public LikeAndCommentController(LikeAndCommentService postLikeService) {
        this.likeAndCommentService = postLikeService;
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(
            @PathVariable Long postId,
            @RequestHeader("X-USER-EMAIL") String email
    ){
        likeAndCommentService.likePost(postId, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<PostComment> comment(
            @PathVariable Long postId,
            @RequestBody String content,
            @RequestHeader("X-USER-EMAIL") String email
    ){
        PostComment postComment = likeAndCommentService.addComment(postId, email, content);
        return new ResponseEntity<>(postComment, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<PostComment>> comments(@PathVariable Long postId){
        List<PostComment> postComments = likeAndCommentService.getComments(postId);
        return new ResponseEntity<>(postComments, HttpStatus.OK);
    }
}
