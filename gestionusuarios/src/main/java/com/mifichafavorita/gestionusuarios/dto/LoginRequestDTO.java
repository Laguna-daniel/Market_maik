package com.mifichafavorita.gestionusuarios.dto;

import lombok.Data;

/** Credenciales enviadas al endpoint {@code POST /auth/login}. */
@Data
public class LoginRequestDTO {
    /**
     * Email del usuario
     */
    private String email;

    /**
     * Contraseña en texto plano (solo viaja por HTTPS en produccion).
     */
    private String password;
}
