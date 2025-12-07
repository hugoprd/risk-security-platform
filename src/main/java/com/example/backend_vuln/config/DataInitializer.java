package com.example.backend_vuln.config;

import com.example.backend_vuln.model.Usuario;
import com.example.backend_vuln.service.UsuarioService;
import com.example.backend_vuln.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioService usuarioService, UsuarioRepository usuarioRepository){
        return args -> {
            
            String emailTeste = "teste@email.com";

            if(usuarioRepository.findByEmail(emailTeste).isEmpty()){
                
                System.out.println("--- Criando usuário de teste inicial... ---");
                
                Usuario user = new Usuario();
                user.setNome("Tester K6");
                user.setEmail(emailTeste); 
                user.setSenha("123");             
                user.setTipo_usuario("ANALISTA_SEGURANCA");

                usuarioService.criarUsuario(user);
                System.out.println("Usuário de teste criado com sucesso!");
                
            }
            else{
                System.out.println("Usuário de teste já existe no banco Neon. Criação pulada.");
            }
        };
    }
}
