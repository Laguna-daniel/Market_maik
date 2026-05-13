package com.mifichafavorita.gestionusuarios.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mifichafavorita.gestionusuarios.dto.HttpGlobalResponse;
import com.mifichafavorita.gestionusuarios.dto.JwtDTO;
import com.mifichafavorita.gestionusuarios.dto.LoginRequestDTO;
import com.mifichafavorita.gestionusuarios.dto.RegisterRequestDTO;
import com.mifichafavorita.gestionusuarios.dto.RegisterResponseDTO;
import com.mifichafavorita.gestionusuarios.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Endpoints de autenticacion: registro, login y refresh del JWT.
 * Las respuestas usan {@link org.springframework.http.ResponseEntity} con codigos HTTP estandar.
 * CORS: configuracion global en {@link com.mifichafavorita.gestionusuarios.config.AppConfig}.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Alta de usuario; la contraseña se convierte a formato seguro en {@link com.mifichafavorita.gestionusuarios.service.AuthService}.
     *
     * @param request cuerpo JSON mapeado a {@link RegisterRequestDTO}
     * @return mensaje de resultado; HTTP 200 si termina bien; 400 si salta excepcion no controlada
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        try {
            RegisterResponseDTO response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Valida credenciales y devuelve JWT dentro de {@link HttpGlobalResponse}.
     *
     * @param request email y contraseña en JSON
     * @return payload con {@code data.jwt} si el login es correcto
     */
    @PostMapping("/login")
    public ResponseEntity<HttpGlobalResponse<JwtDTO>> login(@RequestBody LoginRequestDTO request) {
        try {
            HttpGlobalResponse<JwtDTO> response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Renueva el token; el header debe ser {@code Authorization} con valor {@code Bearer} y el JWT separado por un espacio.
     *
     * @param request peticion HTTP (solo se usa el header Authorization)
     * @return nuevo {@link JwtDTO} o 401 si falta Bearer o el refresh falla
     */
    @GetMapping("/refresh")
    public ResponseEntity<JwtDTO> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = authHeader.replaceFirst("Bearer ", "");

        JwtDTO response = new JwtDTO();

        try {
            response = authService.refreshToken(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
