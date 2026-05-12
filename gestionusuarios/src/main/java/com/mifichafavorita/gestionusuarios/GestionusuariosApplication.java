package com.mifichafavorita.gestionusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada. {@code @SpringBootApplication} arranca el servidor embebido,
 * escanea componentes bajo este paquete y carga propiedades desde {@code application.yaml}.
 */
@SpringBootApplication
public class GestionusuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionusuariosApplication.class, args);
	}

}
