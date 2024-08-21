package br.com.vbruno.minhafeira.mapper.user;

import br.com.vbruno.minhafeira.DTO.request.CreateUserRequest;
import br.com.vbruno.minhafeira.domain.User;

public class CreateUserMapper {

    public static User toEntity(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}
