package com.campusconnect.post_service.controller;

import com.campusconnect.post_service.model.Post;
import com.campusconnect.post_service.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Post>> getAllPendingPosts(
            @RequestHeader("X-COLLEGE-CODE") String collegeCode
    ){
        List<Post> posts = postService.getPendingPosts(collegeCode);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PutMapping("/{postId}/approve")
    public ResponseEntity<?> approvePost(@PathVariable Long postId){
        postService.approvePost(postId);
        return new ResponseEntity<>("Post Approved Successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return new ResponseEntity<>("Post Deleted Successfully", HttpStatus.OK);
    }
}
