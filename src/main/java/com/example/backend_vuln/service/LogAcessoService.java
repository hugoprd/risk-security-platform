package com.example.backend_vuln.service;

import com.example.backend_vuln.model.LogAcesso;
import com.example.backend_vuln.repository.LogAcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogAcessoService{
    @Autowired
    private LogAcessoRepository repository; 

    public void registrarLog(String usuario, String acao){ 
        LogAcesso log = new LogAcesso(usuario, acao);
        repository.save(log);
    }
}