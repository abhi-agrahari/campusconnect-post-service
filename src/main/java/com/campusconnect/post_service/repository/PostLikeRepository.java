package com.campusconnect.post_service.repository;

import com.campusconnect.post_service.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndUserEmail(Long postId, String email);
}
