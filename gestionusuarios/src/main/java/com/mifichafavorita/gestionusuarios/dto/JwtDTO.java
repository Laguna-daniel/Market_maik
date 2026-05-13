package com.mifichafavorita.gestionusuarios.dto;

import lombok.Data;

/** Contenedor del token JWT devuelto en login, refresh y respuestas similares. */
@Data
public class JwtDTO {
    /**
     * JWT del usuario logueado
     */
    private String jwt;
}
