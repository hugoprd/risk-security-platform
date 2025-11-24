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
    public ResponseEntity<?> login(@RequestBody Usuario dadosLogin) {
        System.out.println("--- TENTATIVA DE LOGIN ---");
        System.out.println("Email recebido: " + dadosLogin.getEmail());
        System.out.println("Senha recebida (crua): " + dadosLogin.getSenha());

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(dadosLogin.getEmail());

        if (usuarioOpt.isPresent()) {
            Usuario usuarioBanco = usuarioOpt.get();
            System.out.println("Usuário encontrado no banco: " + usuarioBanco.getEmail());
            System.out.println("Senha no banco (hash): " + usuarioBanco.getSenha());

            boolean senhaBate = passwordEncoder.matches(dadosLogin.getSenha(), usuarioBanco.getSenha());
            System.out.println("As senhas batem? " + senhaBate);

            if(senhaBate){
                return ResponseEntity.ok(usuarioBanco);
            }
        }
        else{
            System.out.println("Usuário NÃO encontrado no banco com este email.");
        }

        return ResponseEntity.status(401).body("Email ou senha inválidos.");
    }
}
