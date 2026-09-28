package com.campusconnect.post_service.service;

import com.campusconnect.post_service.model.Post;
import com.campusconnect.post_service.model.PostComment;
import com.campusconnect.post_service.model.PostLike;
import com.campusconnect.post_service.repository.PostCommentRepository;
import com.campusconnect.post_service.repository.PostLikeRepository;
import com.campusconnect.post_service.repository.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LikeAndCommentService {

    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public LikeAndCommentService(PostLikeRepository postLikeRepository,
                                 PostCommentRepository postCommentRepository,
                                 PostRepository postRepository,
                                 RestTemplate restTemplate) {
        this.postLikeRepository = postLikeRepository;
        this.postCommentRepository = postCommentRepository;
        this.postRepository = postRepository;
        this.restTemplate = restTemplate;
    }

    // add reaction to a post
    @Transactional
    public void addReaction(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!postLikeRepository.existsByPostIdAndUserEmail(postId, email)) {
            PostLike postLike = PostLike.builder()
                    .postId(postId)
                    .userEmail(email)
                    .createdAt(LocalDateTime.now())
                    .build();
            postLikeRepository.save(postLike);

            int count = post.getReactionCount() != null ? post.getReactionCount() : 0;
            post.setReactionCount(count + 1);
            postRepository.save(post);
        }
    }

    // remove reaction from a post
    @Transactional
    public void removeReaction(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Optional<PostLike> postLikeOpt = postLikeRepository.findByPostIdAndUserEmail(postId, email);
        if (postLikeOpt.isPresent()) {
            postLikeRepository.delete(postLikeOpt.get());

            int count = post.getReactionCount() != null ? post.getReactionCount() : 0;
            post.setReactionCount(Math.max(0, count - 1));
            postRepository.save(post);
        }
    }

    @Transactional
    public void likePost(Long postId, String email){
        if(postLikeRepository.existsByPostIdAndUserEmail(postId, email)){
            removeReaction(postId, email);
        } else {
            addReaction(postId, email);
        }
    }

    // add comment to a post
    public PostComment addComment(Long postId, String email, String content){
        Long userId = null;
        String userName = email != null ? email.split("@")[0] : "unknown";

        try {
            String url = userServiceUrl + "/user/by-email?email=" + email;
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null) {
                if (response.get("id") != null) {
                    userId = Long.valueOf(response.get("id").toString());
                }
                if (response.get("fullName") != null && !response.get("fullName").toString().trim().isEmpty()) {
                    userName = response.get("fullName").toString();
                }
            }
        } catch (Exception e) {
            // log fallback if User Service is unreachable
        }

        PostComment postComment = PostComment.builder()
                .postId(postId)
                .userEmail(email)
                .userId(userId)
                .userName(userName)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();

        return postCommentRepository.save(postComment);
    }

    // get all comments
    public List<PostComment> getComments(Long postId){
        return postCommentRepository.findByPostId(postId);
    }

    @Transactional
    public void deleteComment(Long commentId, String email) {
        PostComment comment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (comment.getUserEmail() == null || !comment.getUserEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You can only delete your own comment");
        }

        postCommentRepository.delete(comment);
    }
}
