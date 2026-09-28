package com.campusconnect.post_service.controller;

import com.campusconnect.post_service.dto.PostDto;
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

    // create a post
    @PostMapping
    public ResponseEntity<Post> createPost(
            @RequestBody PostDto postDto,
            @RequestHeader("X-USER-EMAIL") String email,
            @RequestHeader("X-USER-ROLE") String role,
            @RequestHeader("X-COLLEGE-CODE") String collegeCode
    ){
        Post post = postService.createPost(postDto.getContent(), email, role, collegeCode);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    // get all posts of a user
    @GetMapping
    public ResponseEntity<List<Post>> getAllPost(@RequestHeader("X-COLLEGE-CODE") String collegeCode, @RequestHeader(value = "X-USER-EMAIL", required = false) String email){
        List<Post> posts = postService.getApprovedPosts(collegeCode, email);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // delete post
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deleteOwnPost(
            @PathVariable Long postId,
            @RequestHeader("X-USER-EMAIL") String email
    ){
        postService.deleteOwnPost(postId, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
