package com.prajwalmh.AI_Enhanced.LMS.backend.dto.response;

import com.prajwalmh.AI_Enhanced.LMS.backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private Long userId;
    private String fullName;
    private String email;
    private Role role;
}