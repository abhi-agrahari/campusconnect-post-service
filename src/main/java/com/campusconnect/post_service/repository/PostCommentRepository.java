package com.campusconnect.post_service.repository;

import com.campusconnect.post_service.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostId(Long postId);
    long countByPostId(Long postId);

    @Query("SELECT c.postId, COUNT(c) FROM PostComment c WHERE c.postId IN :postIds GROUP BY c.postId")
    List<Object[]> countCommentsByPostIds(@Param("postIds") List<Long> postIds);

    @Transactional
    @Modifying
    void deleteByPostId(Long postId);
}
