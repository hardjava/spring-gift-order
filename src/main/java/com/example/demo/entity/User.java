package com.example.demo.entity;

import com.example.demo.security.PasswordHasher;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String role;

  protected User() {}

  public User(Long id, String email, String hashedPassword, String role) {
    this.id = id;
    this.email = email;
    this.password = hashedPassword;
    this.role = role;
  }

  public User(String email, String hashedPassword, String role) {
    this.email = email;
    this.password = hashedPassword;
    this.role = role;
  }

  public boolean isPasswordMatch(String rawInput) {
    return this.password.equals(PasswordHasher.hash(rawInput));
  }

  public Long getId() {
    return id;
  }
  public String getEmail() {
    return email;
  }
  public String getRole() {
    return role;
  }
  public void setId(Long id) {
    this.id = id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void changePassword(String password){
    this.password = password;
  }

  public void assignRole(String role){
    this.role = role;
  }
}
