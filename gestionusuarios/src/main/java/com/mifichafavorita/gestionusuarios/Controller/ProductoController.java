package com.mifichafavorita.gestionusuarios.Controller;

import java.util.List;

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

/**
 * Productos del micromarket. Lista, alta y borrado exigen JWT valido con rol admin o cajero (ver {@link JwtService#esCajero(String)}).
 */
@RestController
@RequestMapping("/api/micromarket/productos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService service;
    private final JwtService jwtService;

    /** Lista productos; 403 si el token no autoriza como admin o cajero. */
    @GetMapping("/lista")
    public ResponseEntity<List<Producto>> getAll(
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!jwtService.esCajero(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(service.listarTodo());
    }

    /** Guarda producto; 403 si no hay permiso de admin o cajero. */
    @PostMapping("/guardar")
    public ResponseEntity<Producto> save(@RequestBody Producto p,
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!jwtService.esCajero(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(service.registrarProducto(p));
    }

    /** Borra por id; 403 si no hay permiso de admin o cajero. */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!jwtService.esCajero(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        service.borrar(id);
        return ResponseEntity.noContent().build();
    }
}