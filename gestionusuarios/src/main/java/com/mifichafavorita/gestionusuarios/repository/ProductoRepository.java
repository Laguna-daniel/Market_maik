package com.mifichafavorita.gestionusuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mifichafavorita.gestionusuarios.entity.Producto;

/**
 * Acceso a datos de {@link Producto}. Hereda operaciones CRUD estandar de Spring Data JPA
 * ({@code save}, {@code findAll}, {@code deleteById}, etc.).
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
