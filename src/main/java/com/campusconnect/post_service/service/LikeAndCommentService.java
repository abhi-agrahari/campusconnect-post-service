package com.campusconnect.post_service.service;

import com.campusconnect.post_service.model.PostComment;
import com.campusconnect.post_service.model.PostLike;
import com.campusconnect.post_service.repository.PostCommentRepository;
import com.campusconnect.post_service.repository.PostLikeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeAndCommentService {

    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;

    public LikeAndCommentService(PostLikeRepository postLikeRepository, PostCommentRepository postCommentRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postCommentRepository = postCommentRepository;
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

    public PostComment addComment(Long postId, String email, String content){
        PostComment postComment = PostComment.builder()
                .postId(postId)
                .userEmail(email)
                .content(content)
                .createAt(LocalDateTime.now())
                .build();

        return postCommentRepository.save(postComment);
    }

    public List<PostComment> getComments(Long postId){
        return postCommentRepository.findByPostId(postId);
    }
}
