package com.example.backend_vuln.service;

import com.example.backend_vuln.model.MetricasCVSS;
import com.example.backend_vuln.model.Vulnerabilidade;
import org.springframework.stereotype.Service;
import us.springett.cvss.Cvss;
import us.springett.cvss.Score;

@Service
public class CvssService {

    public void calcularEPreencherCvss(Vulnerabilidade vulnerabilidade) {
        MetricasCVSS metricas = vulnerabilidade.getMetricasCVSS();

        if (metricas == null || metricas.getVectorString() == null || metricas.getVectorString().isEmpty()) {
            return;
        }

        try {
            String vetor = metricas.getVectorString();

            Cvss cvss = Cvss.fromVector(vetor);
            Score score = cvss.calculateScore();

            // 1. Pega a pontuação (o número)
            double pontuacao = score.getBaseScore();

            // 2. Preenche a pontuação no objeto
            vulnerabilidade.setPontuacao_cvss(pontuacao);

            // 3.  para definir a criticidade
            vulnerabilidade.setCriticidade(mapearCriticidade(pontuacao));

        } catch (Exception e) {
            System.err.println("Erro ao calcular CVSS: " + e.getMessage());
            vulnerabilidade.setPontuacao_cvss(0.0);
            vulnerabilidade.setCriticidade("INVALIDO");
        }
    }

    /**
     * implementa a Regra de Negócio RN03 .
     *
     */
    private String mapearCriticidade(double score) {
        if (score == 0.0) {
            return "NENHUMA"; // [cite: 60]
        } else if (score >= 0.1 && score <= 3.9) {
            return "BAIXA"; // [cite: 61]
        } else if (score >= 4.0 && score <= 6.9) {
            return "MEDIA"; // [cite: 62]
        } else if (score >= 7.0 && score <= 8.9) {
            return "ALTA"; // [cite: 63]
        } else if (score >= 9.0 && score <= 10.0) {
            return "CRITICA"; // [cite: 64]
        } else {
            return "INVALIDO"; // Um fallback caso algo dê errado
        }
    }
}