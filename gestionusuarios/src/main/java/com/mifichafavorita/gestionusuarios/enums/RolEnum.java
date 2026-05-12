package com.mifichafavorita.gestionusuarios.enums;

/**
 * Valores de {@code rolId} alineados con la tabla de roles en BD. No cambiar ids sin actualizar datos.
 */
public enum RolEnum {
    /** Administracion global del sistema. */
    ADMIN(1L),
    /** Cliente que usa la tienda. */
    CLIENTE(2L),
    /** Cajero de tienda; mismo chequeo manual que admin en rutas que usan {@code esCajero}. */
    CAJERO(3L);

    private final Long id;

    RolEnum(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}