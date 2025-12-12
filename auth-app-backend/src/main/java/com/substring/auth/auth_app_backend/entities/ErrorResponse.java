package com.substring.auth.auth_app_backend.entities;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, HttpStatus status, int statusCode) {
}
