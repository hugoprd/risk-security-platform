package com.example.backend_vuln.repository;

import com.example.backend_vuln.model.LogAcesso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAcessoRepository extends MongoRepository<LogAcesso, String> {}
