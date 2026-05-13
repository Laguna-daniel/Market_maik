package com.mifichafavorita.gestionusuarios.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mifichafavorita.gestionusuarios.entity.Producto;
import com.mifichafavorita.gestionusuarios.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> listarTodo() {
        return productoRepository.findAll();
    }

    public Producto registrarProducto(Producto producto) {

        if (producto.getStock() == null) {
            producto.setStock(0);
        }
        return productoRepository.save(producto);
    }

    public void borrar(Long id) {
        productoRepository.deleteById(id);
    }
}