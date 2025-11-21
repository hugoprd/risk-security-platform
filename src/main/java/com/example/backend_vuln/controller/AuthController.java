package com.example.backend_vuln.controller;

import com.example.backend_vuln.model.Usuario;
import com.example.backend_vuln.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController{
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario dadosLogin){
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(dadosLogin.getEmail());

        if(usuarioOpt.isPresent()){
            Usuario usuarioBanco = usuarioOpt.get();

            if (passwordEncoder.matches(dadosLogin.getSenha(), usuarioBanco.getSenha())){
                return ResponseEntity.ok(usuarioBanco);
            }
        }

        return ResponseEntity.status(401).body("Email ou senha inválidos.");
    }
}
