package com.mifichafavorita.gestionusuarios.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mifichafavorita.gestionusuarios.entity.Producto;
import com.mifichafavorita.gestionusuarios.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    public List<Producto> listarTodo() {
        return repository.findAll();
    }

    public Producto registrarProducto(Producto producto) {
        
        if (producto.getStock() == null) {
            producto.setStock(0);
        }
        return repository.save(producto);
    }

    public void borrar(Long id) {
        repository.deleteById(id);
    }
}