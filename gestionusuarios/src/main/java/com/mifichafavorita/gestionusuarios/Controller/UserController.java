package com.mifichafavorita.gestionusuarios.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mifichafavorita.gestionusuarios.dto.UserResponseDTO;
import com.mifichafavorita.gestionusuarios.service.JwtService;
import com.mifichafavorita.gestionusuarios.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * API de usuarios. Rutas sensibles piden header Authorization y {@link JwtService#esCajero(String)}.
 * CORS: configuracion global en {@link com.mifichafavorita.gestionusuarios.config.AppConfig}.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    /**
     * Lista usuarios si el JWT es valido y el rol es admin o cajero; si no, HTTP 403.
     */
    @GetMapping("/list-users")
    public ResponseEntity<List<UserResponseDTO>> listUsers(
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!jwtService.esCajero(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(userService.listUsers());
    }
}
