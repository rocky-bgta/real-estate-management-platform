package com.rocky.realestate.auth;

import com.rocky.realestate.user.Role;
import java.util.UUID;

public record AuthResponse(String accessToken, UUID userId, String email, Role role) {}
