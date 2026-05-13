package com.mifichafavorita.gestionusuarios.dto;

import lombok.Data;

/** Resultado textual del endpoint {@code POST /auth/register} (exito o correo duplicado). */
@Data
public class RegisterResponseDTO {
    /**
     * Mensaje de respuesta del registro
     */
    private String message;
}
