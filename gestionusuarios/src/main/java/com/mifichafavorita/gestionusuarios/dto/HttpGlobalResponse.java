package com.mifichafavorita.gestionusuarios.dto;

import lombok.Data;

/**
 * Respuesta generica para envolver un mensaje y un cuerpo tipado (por ejemplo el JWT en el login).
 *
 * @param <T> tipo del campo {@code data}
 */
@Data
public class HttpGlobalResponse<T> {
    /** Carga util de la respuesta (ej. {@link JwtDTO} en login exitoso). */
    private T data;

    /** Texto para el cliente (error, confirmacion, etc.). */
    private String message;
}
