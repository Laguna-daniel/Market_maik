package com.mifichafavorita.gestionusuarios.service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mifichafavorita.gestionusuarios.enums.RolEnum;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Construye y valida JWT con jjwt. Lee {@code secret-key} y {@code token-expiration}
 * desde {@code application.yaml}. Los claims custom son {@code userId} y {@code rolId};
 * el subject es el email del usuario.
 */
@Service
public class JwtService {
    /** Clave Base64 inyectada desde el yaml; sirve para firmar y verificar el token. */
    @Value("${security.jwt.secret-key}")
    String secretKey;

    /** Duracion del token en milisegundos (inyectada desde el yaml). */
    @Value("${security.jwt.token-expiration}")
    Long tokenExpiration;

    /**
     * Convierte la cadena Base64 del yaml en {@link SecretKey} para HMAC.
     *
     * @return clave simetrica usable por {@code Jwts}
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Arma el JWT despues de un login correcto.
     *
     * @param userId id persistido del usuario
     * @param rolId   valor de rol en BD (debe alinearse con {@link RolEnum})
     * @param username email; queda como {@code subject} del token
     * @return cadena JWT firmada
     */
    public String generateToken(Long userId, Long rolId, String username) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("rolId", rolId)
                .subject(username) // subject = dueño logico del token (aqui el email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSignKey())
                .compact();
    }

    /**
     * Comprueba firma y forma del token; devuelve false ante cualquier error de jjwt.
     *
     * @param token JWT sin prefijo Bearer
     * @return true si el parseo con la clave secreta es valido
     */
    public Boolean isTokenValid(String token) {
        try {
            // Verifica firma HMAC con la misma clave que se uso al generar
            Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lee el payload y aplica una funcion para devolver un campo tipado.
     *
     * @param <T>      tipo de retorno
     * @param token    JWT valido
     * @param resolver funcion que recibe {@link Claims}
     * @return valor extraido
     */
    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }

    /**
     * Extraer el nombre de usuario del token
     * 
     * @param token
     * @return nombre de usuario
     */
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    /**
     * Extrae el id del usuario
     * 
     * @param token
     * @return id del usuario
     */
    public Long extractUserId(String token) {
        return extractClaims(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Extrae el rol del usuario
     * 
     * @param token
     * @return rol del usuario
     */
    public Long extractRolId(String token) {
        return extractClaims(token, claims -> claims.get("rolId", Long.class));
    }

    /**
     * Emite un JWT nuevo con los mismos datos si el anterior sigue siendo parseable
     * (no expirado segun la excepcion que maneja jjwt aqui).
     *
     * @param token JWT actual
     * @return nuevo JWT con nueva fecha de expiracion
     * @throws Exception token expirado o invalido
     */
    public String refreshToken(String token) throws Exception {
        Claims claims;

        try {
            claims = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new Exception("Token is expired" + e.getMessage());
        } catch (JwtException e) {
            throw new Exception("Token is invalid" + e.getMessage());
        }

        return generateToken(claims.get("userId", Long.class), claims.get("rolId", Long.class), claims.getSubject());
    }

    /**
     * Uso manual en controllers: token valido y {@code rolId} igual a {@link RolEnum#CAJERO}.
     * Acepta header completo ({@code Bearer ...}) o solo el JWT.
     *
     * @param token valor del header Authorization o el token solo
     * @return true si el rol en el token es cajero ({@link RolEnum#CAJERO})
     */
    public boolean esCajero(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        String jwt = token.startsWith("Bearer ") ? token.replaceFirst("Bearer\\s+", "") : token;
        if (!isTokenValid(jwt)) {
            return false;
        }

        Long rolId = extractRolId(jwt);
        return RolEnum.CAJERO.getId().equals(rolId);
    }
}