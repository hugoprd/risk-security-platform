package com.example.backend_vuln.controller;

import com.example.backend_vuln.model.Usuario;
import com.example.backend_vuln.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint de Login
    @PostMapping("/login")
    public Usuario login(@RequestBody Map<String, String> credenciais) {
        String email = credenciais.get("email");
        String senha = credenciais.get("senha");

        return authService.autenticar(email, senha);
    }
}
