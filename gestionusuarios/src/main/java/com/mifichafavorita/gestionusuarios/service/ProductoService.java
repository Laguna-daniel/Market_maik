package com.mifichafavorita.gestionusuarios.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mifichafavorita.gestionusuarios.entity.Producto;
import com.mifichafavorita.gestionusuarios.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

/**
 * Logica de negocio del catalogo de productos. No comprueba permisos: la autorizacion
 * (JWT admin o cajero) la aplican los controladores antes de llamar aqui.
 */
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    /**
     * Devuelve todos los productos ordenados por la estrategia por defecto del repositorio
     * (normalmente por {@code id} ascendente).
     *
     * @return lista; puede estar vacia si no hay filas en la tabla
     */
    public List<Producto> listarTodo() {
        return productoRepository.findAll();
    }

    /**
     * Inserta un producto nuevo o actualiza uno existente si {@code producto.id} ya esta en la base de datos.
     * Si {@code stock} es {@code null}, se guarda como {@code 0} para evitar nulos en inventario.
     *
     * @param producto entidad recibida del cuerpo JSON del cliente
     * @return entidad persistida (con {@code id} asignado si era nuevo)
     */
    public Producto registrarProducto(Producto producto) {

        if (producto.getStock() == null) {
            producto.setStock(0);
        }
        return productoRepository.save(producto);
    }

    /**
     * Elimina la fila cuyo {@code id} coincide. Si el id no existe, el comportamiento depende de JPA
     * (puede no lanzar error y simplemente no borrar nada).
     *
     * @param id clave primaria del producto a eliminar
     */
    public void borrar(Long id) {
        productoRepository.deleteById(id);
    }
}