package com.example.demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequestDto {

  @Email(message = "이메일 형식이 올바르지 않습니다.")
  @NotBlank(message = "이메일은 공백일 수 없습니다.")
  private String email;

  @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 설정해야 합니다.")
  @Pattern(
      regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-]).*$",
      message = "비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다."
  )
  private String password;
  private String role;

  public UserRequestDto() {}

  public UserRequestDto(String email, String password, String role){
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public String getEmail(){
    return email;
  }

  public String getPassword(){
    return password;
  }

  public String getRole(){
    return role;
  }

  public void setEmail(String email){
    this.email = email;
  }

  public void setRole(String role){
    this.role = role;
  }

  public void setPassword(String password){
    this.password = password;
  }
}
