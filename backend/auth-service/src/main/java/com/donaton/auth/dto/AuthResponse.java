package com.donaton.auth.dto;

public record AuthResponse(UserResponse user, String token) {
}
