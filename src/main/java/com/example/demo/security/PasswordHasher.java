package com.example.demo.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
  public static String hash(String password) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] encoded = digest.digest(password.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(encoded);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("SHA-256 알고리즘 사용불가", e);
    }
  }
  private static String bytesToHex(byte[] bytes){
    StringBuilder stringBuilder= new StringBuilder();
    for(byte b : bytes) {
      stringBuilder.append(String.format("%02x", b));
    }
    return stringBuilder.toString();
  }
}

