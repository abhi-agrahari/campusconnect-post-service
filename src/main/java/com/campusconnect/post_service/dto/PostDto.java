package com.campusconnect.post_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {

    private String content;

    private String authorEmail;

    private String role;

    private String collegeCode;

}

@Getter
@Setter
class UserProfileDto {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private String collegeCode;
}
