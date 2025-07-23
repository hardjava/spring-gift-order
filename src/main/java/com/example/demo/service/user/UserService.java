package com.example.demo.service.user;

import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.entity.User;

public interface UserService {

  User login(UserRequestDto dto);
  User findByEmail(String email);
  void deleteByEmail(String email);
  void signup(UserRequestDto dto);
}
