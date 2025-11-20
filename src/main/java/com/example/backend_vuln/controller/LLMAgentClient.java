package com.example.backend_vuln.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class LlmAgentClient{
    private static final String AGENT_URL = "https://hugoprd-security-llm-agent.hf.space/generate-suggestion";
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(30)) // serve apenas como um Timeout para uma possível 
            .build();                               // reconexão (30seg)

    public String getSuggestion(String userInput){
        String safeInput = userInput.replace("\"", "\\\"");
        String jsonPayload = "{\"description\": \"" + safeInput + "\"}";

        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(AGENT_URL))
                    .timeout(Duration.ofMinutes(5)) // timeout longo, pois a IA pode demorar (5min)
                    .header("Content-Type", "application/json") // funciona como 0 "-H" do curl
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload)) // funciona como o "-X POST" e o "-d" do curl
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        }
        catch(Exception e){
            e.printStackTrace();
            
            return "{\"error\": \"Erro ao contatar o agente LLM: " + e.getMessage() + "\"}";
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