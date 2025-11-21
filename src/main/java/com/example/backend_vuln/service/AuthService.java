package com.example.backend_vuln.service;

import com.example.backend_vuln.model.Usuario;
import com.example.backend_vuln.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Nossa ferramenta de criptografia

    public Usuario autenticar(String email, String senhaPura) {
        // 1. Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou e-mail incorreto."));

        // 2. Verifica se a senha bate 
        if (passwordEncoder.matches(senhaPura, usuario.getSenha())) {
            return usuario; 
        } else {
            throw new RuntimeException("Senha incorreta.");
        }
    }
}
