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
