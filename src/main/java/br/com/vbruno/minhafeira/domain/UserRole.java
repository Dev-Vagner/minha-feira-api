package br.com.vbruno.minhafeira.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("user");

    private final String role;
}
