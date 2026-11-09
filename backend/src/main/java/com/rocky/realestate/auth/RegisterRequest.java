package com.rocky.realestate.auth;

import com.rocky.realestate.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank @Email String email,
    String mobile,
    @NotBlank @Size(min = 8, max = 100) String password,
    @NotNull Role role) {}
