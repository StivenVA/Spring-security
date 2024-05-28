package org.example.springsecuritydemo.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.springsecuritydemo.entity.Rol;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class UserEntity {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();

    @Id
    private Long id;

    private String name;
    private String password;
}

