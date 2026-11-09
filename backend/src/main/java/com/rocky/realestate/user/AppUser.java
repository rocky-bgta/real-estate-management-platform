package com.rocky.realestate.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class AppUser {
  @Id private UUID id;

  @Column(nullable = false, unique = true, length = 255)
  private String email;

  @Column(name = "mobile", unique = true, length = 30)
  private String mobile;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private Role role;

  @Column(nullable = false)
  private boolean enabled;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  protected AppUser() {}

  public AppUser(String email, String mobile, String passwordHash, Role role) {
    this.id = UUID.randomUUID();
    this.email = email.toLowerCase();
    this.mobile = mobile;
    this.passwordHash = passwordHash;
    this.role = role;
    this.enabled = true;
    this.createdAt = OffsetDateTime.now();
  }

  public UUID getId() { return id; }
  public String getEmail() { return email; }
  public String getMobile() { return mobile; }
  public String getPasswordHash() { return passwordHash; }
  public Role getRole() { return role; }
  public boolean isEnabled() { return enabled; }
}
