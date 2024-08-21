package br.com.vbruno.minhafeira.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
@Entity
@Table(name = "tb_market")
public class Market {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private LocalDate dateMarket;

    private BigDecimal totalValue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
