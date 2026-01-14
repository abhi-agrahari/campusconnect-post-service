package com.campusconnect.post_service.controller;

import com.campusconnect.post_service.model.Post;
import com.campusconnect.post_service.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/posts")
public class AdminPostController {

    private final PostService postService;

    public AdminPostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/pending")
    public List<Post> getAllPendingPosts(
            @RequestHeader("X-COLLEGE-CODE") String collegeCode
    ){
        return postService.getPendingPosts(collegeCode);
    }

    @PutMapping("/{postId}/approve")
    public void approvePost(@PathVariable Long postId){
        postService.approvePost(postId);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
    }
}
