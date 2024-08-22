package br.com.vbruno.minhafeira.DTO.response.user;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DetailsUserResponse {
    private Long id;
    private String name;
    private String email;
}
