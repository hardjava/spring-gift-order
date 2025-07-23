package com.example.demo.service.refreshtoken;

import com.example.demo.entity.User;

public interface RefreshTokenService {

  void saveRefreshToken(User user, String refreshToken);
}
