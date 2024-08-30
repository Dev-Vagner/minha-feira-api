package br.com.vbruno.minhafeira.mapper.user;

import br.com.vbruno.minhafeira.DTO.request.user.CreateUserRequest;
import br.com.vbruno.minhafeira.domain.User;
import br.com.vbruno.minhafeira.domain.UserRole;

public class CreateUserMapper {

    public static User toEntity(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .role(UserRole.USER)
                .build();
    }
}
