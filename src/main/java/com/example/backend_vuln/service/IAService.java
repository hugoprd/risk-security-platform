package com.example.backend_vuln.service;

import com.example.backend_vuln.controller.LlmAgentClient;
import com.example.backend_vuln.model.Vulnerabilidade;
import org.springframework.stereotype.Service;

@Service
public class IAService{
    public String gerarRecomendacao(Vulnerabilidade vul){
        String textoParaIA = "Analise a vulnerabilidade: " + vul.getTitulo() + 
                             ". Descrição: " + vul.getDescricao() + 
                             ". CVSS: " + vul.getPontuacao_cvss();

        try{
            // Chama o código do Hugo
            LlmAgentClient clienteHugo = new LlmAgentClient();

            return clienteHugo.getSuggestion(textoParaIA);
        }
        catch(Exception e){
            return "Erro na IA: " + e.getMessage();
        }
    }
}
