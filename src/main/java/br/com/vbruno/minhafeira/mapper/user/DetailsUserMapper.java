package br.com.vbruno.minhafeira.mapper.user;

import br.com.vbruno.minhafeira.DTO.response.DetailsUserResponse;
import br.com.vbruno.minhafeira.domain.User;

public class DetailsUserMapper {

    public static DetailsUserResponse toResponse(User user) {
        return DetailsUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
