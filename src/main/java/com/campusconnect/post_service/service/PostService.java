package com.campusconnect.post_service.service;

import com.campusconnect.post_service.model.Post;
import com.campusconnect.post_service.repository.PostCommentRepository;
import com.campusconnect.post_service.repository.PostLikeRepository;
import com.campusconnect.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;
    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    // create a post
    public Post createPost(String content, String email, String role, String collegeCode){
        Long authorId = null;
        String authorName = email != null ? email.split("@")[0] : "unknown";

        try {
            String url = userServiceUrl + "/user/by-email?email=" + email;
            Map response = restTemplate.getForObject(url, Map.class);
            if (response != null) {
                if (response.get("id") != null) {
                    authorId = Long.valueOf(response.get("id").toString());
                }
                if (response.get("fullName") != null && !response.get("fullName").toString().trim().isEmpty()) {
                    authorName = response.get("fullName").toString();
                }
            }
        } catch (Exception e) {
            // log fallback if User Service is unreachable
        }

        Post post = Post.builder()
                .content(content)
                .authorEmail(email)
                .authorId(authorId)
                .authorName(authorName)
                .name(authorName)
                .role(role)
                .collegeCode(collegeCode)
                .status("APPROVED")
                .reactionCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        Post savedPost = postRepository.save(post);
        return populatePostMetadata(savedPost, email);
    }

    // get all approved posts
    public List<Post> getApprovedPosts(String collegeCode, String currentUserEmail){
        List<Post> posts = postRepository.findByCollegeCodeAndStatus(collegeCode, "APPROVED");
        return populatePostListMetadata(posts, currentUserEmail);
    }

    // get all pending posts
    public List<Post> getPendingPosts(String collegeCode, String currentUserEmail){
        List<Post> posts = postRepository.findByCollegeCodeAndStatus(collegeCode, "PENDING");
        return populatePostListMetadata(posts, currentUserEmail);
    }

    private List<Post> populatePostListMetadata(List<Post> posts, String currentUserEmail) {
        if (posts == null || posts.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> postIds = posts.stream().map(Post::getId).collect(Collectors.toList());

        List<Object[]> commentCounts = postCommentRepository.countCommentsByPostIds(postIds);
        Map<Long, Long> commentCountMap = commentCounts.stream()
                .collect(Collectors.toMap(row -> (Long) row[0], row -> (Long) row[1]));

        Set<Long> likedPostIds = (currentUserEmail != null && !currentUserEmail.trim().isEmpty())
                ? new HashSet<>(postLikeRepository.findLikedPostIdsByUser(postIds, currentUserEmail))
                : Collections.emptySet();

        for (Post post : posts) {
            if (post.getAuthorName() == null || post.getAuthorName().trim().isEmpty()) {
                post.setAuthorName(post.getAuthorEmail() != null ? post.getAuthorEmail().split("@")[0] : "Anonymous");
            }
            if (post.getReactionCount() == null) {
                post.setReactionCount(0);
            }
            post.setLikesCount(post.getReactionCount());
            post.setCommentsCount(commentCountMap.getOrDefault(post.getId(), 0L));
            post.setLikedByUser(likedPostIds.contains(post.getId()));
        }

        return posts;
    }

    private Post populatePostMetadata(Post post, String currentUserEmail){
        if (post.getAuthorName() == null || post.getAuthorName().trim().isEmpty()) {
            post.setAuthorName(post.getAuthorEmail() != null ? post.getAuthorEmail().split("@")[0] : "Anonymous");
        }
        if (post.getReactionCount() == null) {
            post.setReactionCount(0);
        }
        post.setLikesCount(post.getReactionCount());
        post.setCommentsCount(postCommentRepository.countByPostId(post.getId()));
        if(currentUserEmail != null){
            post.setLikedByUser(postLikeRepository.existsByPostIdAndUserEmail(post.getId(), currentUserEmail));
        }
        return post;
    }

    public void approvePost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setStatus("APPROVED");
        postRepository.save(post);
    }

    public void deletePost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        postLikeRepository.deleteByPostId(postId);
        postCommentRepository.deleteByPostId(postId);
        postRepository.delete(post);
    }

    // delete own post
    @Transactional
    public void deleteOwnPost(Long postId, String userEmail){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getAuthorEmail() == null || !post.getAuthorEmail().equalsIgnoreCase(userEmail)) {
            throw new RuntimeException("You can only delete your own post");
        }

        postLikeRepository.deleteByPostId(postId);
        postCommentRepository.deleteByPostId(postId);
        postRepository.delete(post);
    }
}
