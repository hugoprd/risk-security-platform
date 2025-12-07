package com.example.backend_vuln.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class LlmAgentClient{
    // comentar qual das maneiras nao vai querer usar
    // private static final String AGENT_URL = "https://hugoprd-security-llm-agent.hf.space/generate-suggestion"; // <- pro hugging face
    private static final String AGENT_URL = "http://127.0.0.1:8000/generate-suggestion"; // <- pro local
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1) 
            .connectTimeout(Duration.ofSeconds(30))
            .build();
            // .version(HttpClient.Version.HTTP_2)
            // .connectTimeout(Duration.ofSeconds(30)) // serve apenas como um Timeout para uma possível 
            // .build();                               // reconexão (30seg)

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public String getSuggestion(String userInput){
        try{
            Map<String, String> payloadMap = new HashMap<>();
            payloadMap.put("description", userInput);

            String jsonPayload = objectMapper.writeValueAsString(payloadMap);

            System.out.println("--- ENVIANDO PARA IA ---");
            System.out.println("Payload: " + jsonPayload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(AGENT_URL))
                    .timeout(Duration.ofMinutes(5))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status IA: " + response.statusCode());
            
            System.out.println("Status IA: " + response.statusCode());
            System.out.println("CORPO DA RESPOSTA: " + response.body());

            if(response.statusCode() != 200){
                // se retornar erro 503 ou 500 é pq o hugging face space crashou
                return "Erro na IA (Status " + response.statusCode() + "): " + response.body();
            }

            return response.body();
        }
        catch(Exception e){
            e.printStackTrace();
            
            return "Erro interno ao chamar IA: " + e.getMessage();
        }
    }

    public static void main(String[] args){
        LlmAgentClient agent = new LlmAgentClient();
        String userInput = "";
        
        System.out.println("Enviando pedido para a IA...");
        String jsonResponse = agent.getSuggestion(userInput);
        
        System.out.println("Resposta da IA:");
        System.out.println(jsonResponse);
    }
}