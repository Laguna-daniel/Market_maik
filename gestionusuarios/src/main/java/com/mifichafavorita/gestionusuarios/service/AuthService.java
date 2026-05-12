package com.mifichafavorita.gestionusuarios.service;

import com.mifichafavorita.gestionusuarios.repository.UserRepository;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mifichafavorita.gestionusuarios.dto.HttpGlobalResponse;
import com.mifichafavorita.gestionusuarios.dto.JwtDTO;
import com.mifichafavorita.gestionusuarios.dto.LoginRequestDTO;
import com.mifichafavorita.gestionusuarios.dto.RegisterRequestDTO;
import com.mifichafavorita.gestionusuarios.dto.RegisterResponseDTO;
import com.mifichafavorita.gestionusuarios.entity.Users;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Reglas de autenticacion: registro con email unico, login comprobando contraseña con BCrypt y emision de JWT.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    /**
     * Crea usuario si el email no existe; guarda la contraseña ya pasada por BCrypt (no se puede leer tal cual en la BD).
     *
     * @param request datos del formulario de registro
     * @return mensaje descriptivo (exito o email duplicado)
     */
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        RegisterResponseDTO response = new RegisterResponseDTO();

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            response.setMessage("El correo ya esta en uso");
            return response;
        }

        Users user = new Users();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAge(request.getAge());
        user.setRolId(request.getRol());
        userRepository.save(user);

        response.setMessage("Se ha registrado correctamente");
        return response;
    }

    /**
     * Busca por email, comprueba la contraseña escrita contra la guardada (BCrypt) y genera JWT.
     *
     * @param request credenciales
     * @return {@code message} siempre; {@code data} solo si el login es valido
     */
    public HttpGlobalResponse<JwtDTO> login(LoginRequestDTO request) {
        HttpGlobalResponse<JwtDTO> response = new HttpGlobalResponse<>();
        Optional<Users> userFound = userRepository.findByEmail(request.getEmail());

        if (userFound.isEmpty()) {
            response.setMessage("Este usuario no se encuentra registrado");
            return response;
        }

        Users user = userFound.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            response.setMessage("Correo o contraseña son incorrectos");
            return response;
        }

        JwtDTO jwtDTO = new JwtDTO();
        String jwt = jwtService.generateToken(user.getId(), user.getRolId(), user.getEmail());
        jwtDTO.setJwt(jwt);
        response.setMessage("Inicio de sesion exitoso");
        response.setData(jwtDTO);
        return response;
    }

    /**
     * Delega en {@link JwtService#refreshToken(String)} y envuelve el string en {@link JwtDTO}.
     *
     * @param token JWT sin prefijo Bearer
     * @return dto con el nuevo token
     * @throws Exception si el token no se puede refrescar
     */
    public JwtDTO refreshToken(String token) throws Exception{
        JwtDTO response = new JwtDTO();
        String jwt = jwtService.refreshToken(token);
        response.setJwt(jwt);
        return response;
    }
}
