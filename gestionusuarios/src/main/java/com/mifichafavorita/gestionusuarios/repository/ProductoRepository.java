package com.mifichafavorita.gestionusuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mifichafavorita.gestionusuarios.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
