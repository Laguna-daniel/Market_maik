package com.mifichafavorita.gestionusuarios.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data 
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    private String marca;

    @Column(nullable = false)
    private Double precio;

    private Integer stock;

    private String categoria; 

    private String unidadMedida; 
}