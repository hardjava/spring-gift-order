package com.example.demo.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

  @Id
  @Column(name = "user_id")
  private Long userId;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "refresh_token", nullable = false, columnDefinition = "TEXT")
  private String refreshToken;

  protected RefreshToken() {}

  public RefreshToken(User user, String refreshToken) {
    this.user = user;
    this.userId = user.getId();
    this.refreshToken = refreshToken;
  }

  Long getUserId() {
    return userId;
  }

  User getUser(){
    return user;
  }

  String getRefreshToken(){
    return refreshToken;
  }

}
