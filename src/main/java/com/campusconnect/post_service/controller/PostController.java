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
    public Post createPost(@RequestBody PostDto postDto){
        return postService.createPost(postDto);
    }

    @GetMapping
    public List<Post> getAllPost(String collegeCode){
        return postService.getAllPost(collegeCode);
    }
}
