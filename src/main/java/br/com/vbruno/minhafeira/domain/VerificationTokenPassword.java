package br.com.vbruno.minhafeira.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "tb_verification_token")
public class VerificationTokenPassword {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private UUID token;

    private LocalDateTime dataExpiracao;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
