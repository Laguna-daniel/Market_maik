package com.mifichafavorita.gestionusuarios.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {
    /**
     * Nombre del usuario
     */
    private String name;

    /**
     * Email del usuario
     */
    private String email;

    /**
     * Contraseña; el backend la guarda de forma que no se puede reconstruir el texto original (BCrypt).
     */
    private String password;

    /**
     * Edad del usuario
     */
    private Long age;

    /**
     * Rol del usuario
     */
    private Long rol;
}
