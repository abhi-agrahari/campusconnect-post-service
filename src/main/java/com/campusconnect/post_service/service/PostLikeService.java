package com.campusconnect.post_service.service;

import com.campusconnect.post_service.model.PostLike;
import com.campusconnect.post_service.repository.PostLikeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    public PostLikeService(PostLikeRepository postLikeRepository) {
        this.postLikeRepository = postLikeRepository;
    }

    public void likePost(Long postId, String email){
        if(!postLikeRepository.existsByPostIdAndUserEmail(postId, email)){
            PostLike postLike = PostLike.builder()
                    .postId(postId)
                    .userEmail(email)
                    .createdAt(LocalDateTime.now())
                    .build();
            postLikeRepository.save(postLike);
        }
    }
}
