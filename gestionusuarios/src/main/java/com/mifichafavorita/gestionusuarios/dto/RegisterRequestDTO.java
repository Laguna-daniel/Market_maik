package com.mifichafavorita.gestionusuarios.dto;

import lombok.Data;

/** Datos del formulario de alta enviados a {@code POST /auth/register}. El campo {@code rol} se persiste como {@code rol_id}. */
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
