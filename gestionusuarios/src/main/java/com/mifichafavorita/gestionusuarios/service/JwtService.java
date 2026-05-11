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

@Service
public class JwtService {
    /**
     * Inyectamos la clave secreta en el service que viene del yaml
     */
    @Value("${security.jwt.secret-key}")
    String secretKey;

    /**
     * Inyectamos la clave secreta en el service que viene del yaml
     */
    @Value("${security.jwt.token-expiration}")
    Long tokenExpiration;

    /**
     * Transforma la clave secreta de String (BASE64) a un obejto SecretKey
     * utilizable por la libreria
     * 
     * @return firma secreta
     */
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generar el token de seguridad al iniciar sesion
     * 
     * @param userId
     * @param rolId
     * @param username
     * @return jwt
     */
    public String generateToken(Long userId, Long rolId, String username) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("rolId", rolId)
                .subject(username) // claim por defecto (a quien pertenece este token)
                .issuedAt(new Date()) // fecha de creacion
                .expiration(new Date(System.currentTimeMillis() + tokenExpiration)) // fecha de expiracion
                .signWith(getSignKey()) // Con que firmamos el token
                .compact(); // construye el String final
    }

    /**
     * Verifica si el token es válido
     * 
     * @param token
     * @return boleano
     */
    public Boolean isTokenValid(String token) {
        try {
            // El parser intenta descifrar la firma del token y los compara
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
     * Extraer todos los claims del token
     * 
     * @param <T>
     * @param token
     * @param resolver
     * @return
     */
    public <T> T extractClaims(String token, Function<Claims, T> resolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return resolver.apply(claims);
    }

    private static Long toLong(Object claim) {
        if (claim == null) {
            return null;
        }
        if (claim instanceof Long l) {
            return l;
        }
        if (claim instanceof Integer i) {
            return i.longValue();
        }
        if (claim instanceof Number n) {
            return n.longValue();
        }
        return null;
    }

    private static Long toLongClaim(Claims claims, String name) {
        return toLong(claims.get(name));
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
        return extractClaims(token, claims -> toLongClaim(claims, "userId"));
    }

    /**
     * Extrae el rol del usuario
     * 
     * @param token
     * @return rol del usuario
     */
    public Long extractRolId(String token) {
        return extractClaims(token, claims -> toLongClaim(claims, "rolId"));
    }

    /**
     * Refresco del token si no está expirado
     * 
     * @param token
     * @return token
     * @throws Exception
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

        // Generamos nuevo token con nueva expiracion
        return generateToken(toLongClaim(claims, "userId"), toLongClaim(claims, "rolId"), claims.getSubject());
    }

    /**
     * Validación manual de rol autorizado para endpoints protegidos.
     *
     * @param token Header Authorization completo o token limpio
     * @return true si tiene rol permitido
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
        return RolEnum.ADMIN.getId().equals(rolId);
    }
}