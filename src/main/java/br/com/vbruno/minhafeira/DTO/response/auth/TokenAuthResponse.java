package br.com.vbruno.minhafeira.DTO.response.auth;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenAuthResponse {
    private String token;
}
