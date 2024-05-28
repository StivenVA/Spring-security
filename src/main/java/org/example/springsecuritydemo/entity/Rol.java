package org.example.springsecuritydemo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Rol {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TipoRol nombre;
    @Id
    private Long id;
}
