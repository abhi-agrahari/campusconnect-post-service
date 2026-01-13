package com.campusconnect.post_service.controller;

import com.campusconnect.post_service.dto.PostDto;
import com.campusconnect.post_service.model.Post;
import com.campusconnect.post_service.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @PostMapping
    public Post createPost(
            @RequestBody String content,
            @RequestHeader("X-USER-EMAIL") String email,
            @RequestHeader("X-USER-ROLE") String role,
            @RequestHeader("X-COLLEGE-CODE") String collegeCode
    ){
        return postService.createPost(content, email, role, collegeCode);
    }

    @GetMapping
    public List<Post> getAllPost(@RequestHeader("X-COLLEGE-CODE") String collegeCode){
        return postService.getAllPost(collegeCode);
    }
}
