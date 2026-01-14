package com.campusconnect.post_service.service;

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

    public Post createPost(String content, String email, String role, String collegeCode){
        Post post = Post.builder()
                .content(content)
                .authorEmail(email)
                .role(role)
                .collegeCode(collegeCode)
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        return postRepository.save(post);
    }

    public List<Post> getApprovedPosts(String collegeCode){
        return postRepository.findByCollegeCodeAndStatus(collegeCode, "APPROVED");
    }

    public List<Post> getPendingPosts(String collegeCode){
        return postRepository.findByCollegeCodeAndStatus(collegeCode, "PENDING");
    }

    public void approvePost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setStatus("APPROVED");
        postRepository.save(post);
    }

    public void deletePost(Long postId){
        postRepository.deleteById(postId);
    }

    public List<Post> getAllPost(String collegeCode){
        return postRepository.findAllByCollegeCodeOrderByCreatedAtDesc(collegeCode);
    }
}
