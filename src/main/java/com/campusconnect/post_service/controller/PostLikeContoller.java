package com.campusconnect.post_service.controller;

import com.campusconnect.post_service.service.PostLikeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostLikeContoller {

    private final PostLikeService postLikeService;

    public PostLikeContoller(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping("/{postId}/like")
    public void likePost(
            @PathVariable Long postId,
            @RequestHeader("X-USER-EMAIL") String email
    ){
        postLikeService.likePost(postId, email);
    }
}
