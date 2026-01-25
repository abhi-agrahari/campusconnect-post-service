package com.campusconnect.post_service.controller;

import com.campusconnect.post_service.model.Post;
import com.campusconnect.post_service.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody String content,
            @RequestHeader("X-USER-EMAIL") String email,
            @RequestHeader("X-USER-ROLE") String role,
            @RequestHeader("X-COLLEGE-CODE") String collegeCode
    ){
        Post post = postService.createPost(content, email, role, collegeCode);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPost(@RequestHeader("X-COLLEGE-CODE") String collegeCode){
        List<Post> posts = postService.getApprovedPosts(collegeCode);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
