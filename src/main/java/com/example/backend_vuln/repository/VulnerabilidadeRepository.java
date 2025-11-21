package com.example.backend_vuln.repository;

import com.example.backend_vuln.model.Vulnerabilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VulnerabilidadeRepository extends JpaRepository<Vulnerabilidade, Long> {

}