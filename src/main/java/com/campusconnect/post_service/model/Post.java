package com.campusconnect.post_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    private String authorEmail;

    private Long authorId;

    private String authorName;

    private String name;

    private String role;

    private String collegeCode;

    private String status;

    private Integer reactionCount = 0;

    private LocalDateTime createdAt;

    @Transient
    private long likesCount;

    @Transient
    private long commentsCount;

    @Transient
    private boolean likedByUser;
}
