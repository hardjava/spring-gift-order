package com.example.demo.service.refreshtoken;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

  public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
      UserRepository userRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public void saveRefreshToken(User user, String refreshToken) {
    RefreshToken token = new RefreshToken(user, refreshToken);
    refreshTokenRepository.save(token);
  }
}
