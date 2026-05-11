package com.mifichafavorita.gestionusuarios.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mifichafavorita.gestionusuarios.dto.UserResponseDTO;
import com.mifichafavorita.gestionusuarios.service.JwtService;
import com.mifichafavorita.gestionusuarios.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    /**
     * Servicio de usuarios
     */
    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/list-users")
    public ResponseEntity<?> listUsers(
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!jwtService.esCajero(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "No tiene permisos para esta operación"));
        }

        try {
            List<UserResponseDTO> response = userService.listUsers();
            return ResponseEntity.status(HttpStatus.FOUND).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
