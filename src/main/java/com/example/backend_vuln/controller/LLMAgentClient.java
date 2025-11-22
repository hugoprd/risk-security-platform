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
    private static final String AGENT_URL = "https://hugoprd-security-llm-agent.hf.space/generate-suggestion";
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(30)) // serve apenas como um Timeout para uma possível 
            .build();                               // reconexão (30seg)

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
        String userInput = ""; // aqui tem que colocar o input do usuário, o que ele quer analisar e etc
        // TODO: fazer esse link: colocar o input do usuário para ca
        
        System.out.println("Enviando pedido para a IA...");
        String jsonResponse = agent.getSuggestion(userInput);
        
        System.out.println("Resposta da IA:");
        System.out.println(jsonResponse);
    }
}