package com.example.backend_vuln.config;

import com.example.backend_vuln.model.Usuario;
import com.example.backend_vuln.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioService usuarioService) {
        return args -> {
            // Cria o usuário de teste para o k6
            Usuario user = new Usuario();
            user.setNome("Tester K6");
            user.setEmail("teste@email.com"); 
            user.setSenha("123");             
            user.setTipo_usuario("ANALISTA_SEGURANCA");

            usuarioService.criarUsuario(user);
            System.out.println("✅ Usuário de teste criado com sucesso!");
        };
    }
}
