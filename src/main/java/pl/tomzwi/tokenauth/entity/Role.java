package pl.tomzwi.tokenauth.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "roles" )
@Data
public class Role {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Column( nullable = false )
    private String name;

    @ManyToMany( mappedBy = "roles" )
    @ToString.Exclude
    private List<User> users;

}
