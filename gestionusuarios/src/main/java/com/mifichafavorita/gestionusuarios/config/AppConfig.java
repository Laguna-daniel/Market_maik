package com.mifichafavorita.gestionusuarios.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Ajustes compartidos: Spring crea aqui el codificador de contraseñas para toda la app.
 * Al registrar se guarda la contraseña en formato irreversible con BCrypt;
 * al iniciar sesion se comprueba la clave tecleada contra ese valor guardado.
 * Solo se usa la libreria de Spring para codificar contraseñas (nombre del artefacto: spring-security-crypto); quien valida el JWT son los controllers.
 * CORS global equivale a {@code @CrossOrigin(origins = "*")} en cada controller (mismo criterio que micromarket / cajero).
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOriginPatterns("*")
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
				.allowedHeaders("*");
	}
}
