package com.campusconnect.post_service.controller;

import com.campusconnect.post_service.model.PostComment;
import com.campusconnect.post_service.service.LikeAndCommentService;
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
    public void likePost(
            @PathVariable Long postId,
            @RequestHeader("X-USER-EMAIL") String email
    ){
        likeAndCommentService.likePost(postId, email);
    }

    @PostMapping("/{postId}/comment")
    public PostComment comment(
            @PathVariable Long postId,
            @RequestBody String content,
            @RequestHeader("X-USER-EMAIL") String email
    ){
        return likeAndCommentService.addComment(postId, email, content);
    }

    @GetMapping("/{postId}/comments")
    public List<PostComment> comments(@PathVariable Long postId){
        return likeAndCommentService.getComments(postId);
    }
}
