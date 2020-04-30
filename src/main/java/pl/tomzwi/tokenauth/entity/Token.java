package pl.tomzwi.tokenauth.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table( name = "tokens" )
@Data
public class Token {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false )
    private String token;

    @Column( nullable = false )
    private LocalDateTime expires;

    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "userid" )
    @ToString.Exclude
    private User user;

}
