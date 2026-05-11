package com.mifichafavorita.gestionusuarios.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mifichafavorita.gestionusuarios.entity.Producto;
import com.mifichafavorita.gestionusuarios.service.JwtService;
import com.mifichafavorita.gestionusuarios.service.ProductoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/micromarket/productos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService service;
    private final JwtService jwtService;

    @GetMapping("/lista")
    public ResponseEntity<?> getAll(
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!jwtService.esCajero(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "No tiene permisos para esta operación"));
        }
        return ResponseEntity.ok(service.listarTodo());
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> save(@RequestBody Producto p,
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!jwtService.esCajero(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "No tiene permisos para esta operación"));
        }
        return ResponseEntity.ok(service.registrarProducto(p));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!jwtService.esCajero(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "No tiene permisos para esta operación"));
        }
        service.borrar(id);
        return ResponseEntity.noContent().build();
    }
}