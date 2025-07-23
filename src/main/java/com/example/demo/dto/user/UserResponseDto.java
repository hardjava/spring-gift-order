package com.example.demo.dto.user;

public record UserResponseDto(
    String token,
    UserDataInfo user
) {
}
