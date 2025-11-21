package com.example.backend_vuln.repository;

import com.example.backend_vuln.model.LogAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAcessoRepository extends JpaRepository<LogAcesso, Long>{

}