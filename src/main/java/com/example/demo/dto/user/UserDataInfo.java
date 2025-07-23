package com.example.demo.dto.user;

import com.example.demo.entity.User;

public record UserDataInfo(
    Long id,
    String email,
    String role
){
    public UserDataInfo(User user){
      this(user.getId(), user.getEmail(), user.getRole());
    }
}

