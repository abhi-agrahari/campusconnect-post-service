package com.campusconnect.post_service.service;

import com.campusconnect.post_service.dto.PostDto;
import com.campusconnect.post_service.model.Post;
import com.campusconnect.post_service.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public Post createPost(PostDto postDto){
        Post post = Post.builder()
                .content(postDto.getContent())
                .authorEmail(postDto.getAuthorEmail())
                .role(postDto.getRole())
                .collegeCode(postDto.getCollegeCode())
                .createdAt(LocalDateTime.now())
                .build();

        return postRepository.save(post);
    }

    public List<Post> getAllPost(String collegeCode){
        return postRepository.findAllByCollegeCodeOrderByCreatedAtDesc(collegeCode);
    }
}
