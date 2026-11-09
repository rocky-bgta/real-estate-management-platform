package com.rocky.realestate.auth;

import com.rocky.realestate.security.JwtService;
import com.rocky.realestate.user.AppUser;
import com.rocky.realestate.user.AppUserRepository;
import com.rocky.realestate.user.Role;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
  private final AppUserRepository users;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(
      AppUserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.users = users;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @Transactional
  public AuthResponse register(RegisterRequest request) {
    if (users.existsByEmailIgnoreCase(request.email())) {
      throw new IllegalArgumentException("Email is already registered");
    }
    Role role = request.role() == Role.ADMIN ? Role.USER : request.role();
    AppUser user =
        users.save(
            new AppUser(
                request.email(), request.mobile(), passwordEncoder.encode(request.password()), role));
    return response(user);
  }

  @Transactional(readOnly = true)
  public AuthResponse login(LoginRequest request) {
    AppUser user =
        users.findByEmailIgnoreCase(request.email()).orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
    if (!user.isEnabled() || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new BadCredentialsException("Invalid credentials");
    }
    return response(user);
  }

  private AuthResponse response(AppUser user) {
    return new AuthResponse(
        jwtService.createAccessToken(user), user.getId(), user.getEmail(), user.getRole());
  }
}
