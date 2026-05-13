package com.mifichafavorita.gestionusuarios.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Usuario de la aplicacion. La columna {@code rol_id} debe coincidir con los ids de {@link com.mifichafavorita.gestionusuarios.enums.RolEnum}
 * en la base de datos (admin, cliente, cajero).
 */
@Entity
@Data
@Table(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "age", nullable = false)
    private Long age;

    @Column(name = "rol_id", nullable = false)
    private Long rolId;
}