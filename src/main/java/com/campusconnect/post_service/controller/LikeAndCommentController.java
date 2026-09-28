package com.campusconnect.post_service.controller;

import com.campusconnect.post_service.dto.CommentRequest;
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

    // add like to post
    @PostMapping("/{postId}/reaction")
    public ResponseEntity<?> addReaction(
            @PathVariable Long postId,
            @RequestHeader("X-USER-EMAIL") String email
    ){
        likeAndCommentService.addReaction(postId, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // remove like from a post
    @DeleteMapping("/{postId}/reaction")
    public ResponseEntity<?> removeReaction(
            @PathVariable Long postId,
            @RequestHeader("X-USER-EMAIL") String email
    ){
        likeAndCommentService.removeReaction(postId, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(
            @PathVariable Long postId,
            @RequestHeader("X-USER-EMAIL") String email
    ){
        likeAndCommentService.likePost(postId, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // add comment to a post
    @PostMapping("/{postId}/comment")
    public ResponseEntity<PostComment> comment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request,
            @RequestHeader("X-USER-EMAIL") String email
    ){
        PostComment postComment = likeAndCommentService.addComment(postId, email, request.getContent());
        return new ResponseEntity<>(postComment, HttpStatus.CREATED);
    }

    // get all comments of a post
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<PostComment>> comments(@PathVariable Long postId){
        List<PostComment> postComments = likeAndCommentService.getComments(postId);
        return new ResponseEntity<>(postComments, HttpStatus.OK);
    }

    // delete comment
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("X-USER-EMAIL") String email
    ){
        likeAndCommentService.deleteComment(commentId, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
