package com.example.backend_vuln.repository;

import com.example.backend_vuln.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // O Spring Data JPA ele automaticamente cria a query
    // "SELECT * FROM usuario WHERE email = ?"
    Optional<Usuario> findByEmail(String email);

}