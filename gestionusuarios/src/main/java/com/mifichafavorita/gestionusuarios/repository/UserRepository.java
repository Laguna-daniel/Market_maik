package com.mifichafavorita.gestionusuarios.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mifichafavorita.gestionusuarios.entity.Users;

/**
 * Persistencia de {@link Users}. Incluye busqueda por email para login y registro.
 */
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    /**
     * Localiza un usuario por su email unico (usado en {@link com.mifichafavorita.gestionusuarios.service.AuthService}).
     *
     * @param email direccion exacta a buscar
     * @return presente si existe fila con ese email
     */
    Optional<Users> findByEmail(String email);
}