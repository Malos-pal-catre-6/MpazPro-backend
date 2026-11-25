package com.mpaz.backend.service;

import com.mpaz.backend.dto.LoginRequest;
import com.mpaz.backend.dto.LoginResponse;
import com.mpaz.backend.dto.RegistroRequest;
import com.mpaz.backend.model.Rol;
import com.mpaz.backend.model.Usuario;
import com.mpaz.backend.repository.UsuarioRepository;
import com.mpaz.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private String limpiar(String usuario) {
        return usuario.trim().toLowerCase();
    }

    public void registrar(RegistroRequest req) {
        String usuario = limpiar(req.usuario());

        if (usuarioRepository.existsByUsuario(usuario)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }

        Rol rol;
        try {
            rol = Rol.valueOf(req.rol().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol invalido");
        }

        Usuario nuevo = Usuario.builder()
                .nombre(req.nombre().trim())
                .usuario(usuario)
                .passwordHash(passwordEncoder.encode(req.password()))
                .rol(rol)
                .build();

        usuarioRepository.save(nuevo);
    }

    public LoginResponse login(LoginRequest req) {
        Usuario u = usuarioRepository.findByUsuario(limpiar(req.usuario()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas"));

        if (!passwordEncoder.matches(req.password(), u.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
        }

        String token = jwtUtil.generarToken(u.getId(), u.getUsuario(), u.getRol().name());
        return new LoginResponse(token, u.getId(), u.getNombre(), u.getUsuario(), u.getRol().name());
    }
}
