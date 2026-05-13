package com.mifichafavorita.gestionusuarios.dto;

import lombok.Data;

/**
 * Vista de usuario para listados (por ejemplo {@code GET /users/list-users}). No incluye contraseña.
 */
@Data
public class UserResponseDTO {
    /**
     * Id del usuario
     */
    private Long id;

    /**
     * Nombre del usuario
     */
    private String name;

    /**
     * Email del usuario
     */
    private String email;

    /**
     * Edad del usuario
     */
    private Long age;

    /**
     * Rol del usuario
     */
    private Long rol;
}
