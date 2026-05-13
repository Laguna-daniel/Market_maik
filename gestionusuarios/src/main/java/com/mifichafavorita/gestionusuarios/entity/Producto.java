package com.mifichafavorita.gestionusuarios.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Articulo del catalogo del micromarket. Se persiste en la tabla {@code productos}.
 * Las rutas REST de alta, listado y borrado estan en {@link com.mifichafavorita.gestionusuarios.Controller.ProductoController}
 * y exigen JWT con rol administrador o cajero.
 */
@Entity
@Table(name = "productos")
@Data
public class Producto {

    /** Identificador unico generado por la base de datos. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre comercial o descriptivo del producto (obligatorio, hasta 100 caracteres). */
    @Column(nullable = false, length = 100)
    private String nombre;

    /** Marca del fabricante o proveedor; opcional. */
    private String marca;

    /** Precio unitario de venta; obligatorio. */
    @Column(nullable = false)
    private Double precio;

    /**
     * Unidades disponibles en inventario. Si no se envia al crear, {@link com.mifichafavorita.gestionusuarios.service.ProductoService}
     * asigna {@code 0} por defecto.
     */
    private Integer stock;

    /** Clasificacion opcional (por ejemplo bebidas, lacteos). */
    private String categoria;

    /** Unidad de medida para la venta (por ejemplo kg, unidad, litro); opcional. */
    private String unidadMedida;
}