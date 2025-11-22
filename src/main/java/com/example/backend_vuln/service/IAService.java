package com.example.backend_vuln.service;

import com.example.backend_vuln.controller.LlmAgentClient;
import com.example.backend_vuln.model.Vulnerabilidade;
import org.springframework.stereotype.Service;

@Service
public class IAService{
    public String gerarRecomendacao(Vulnerabilidade vul){
        String textoParaIA = String.format(
            "Título: %s. Sistema: %s. Descrição Técnica: %s. CVSS Score: %s.",
            vul.getTitulo(),
            vul.getSistema_impactado(),
            vul.getDescricao(),
            vul.getPontuacao_cvss()
        );

        try{
            LlmAgentClient clienteHugo = new LlmAgentClient();
            
            return clienteHugo.getSuggestion(textoParaIA);
        }
        catch(Exception e){
            return "Falha no serviço de IA: " + e.getMessage();
        }
    }
}
