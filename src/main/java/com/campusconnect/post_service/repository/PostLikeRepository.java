package com.campusconnect.post_service.repository;

import com.campusconnect.post_service.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndUserEmail(Long postId, String email);
    Optional<PostLike> findByPostIdAndUserEmail(Long postId, String email);
    long countByPostId(Long postId);

    @Query("SELECT l.postId FROM PostLike l WHERE l.postId IN :postIds AND l.userEmail = :email")
    List<Long> findLikedPostIdsByUser(@Param("postIds") List<Long> postIds, @Param("email") String email);

    @Transactional
    @Modifying
    void deleteByPostId(Long postId);
}
