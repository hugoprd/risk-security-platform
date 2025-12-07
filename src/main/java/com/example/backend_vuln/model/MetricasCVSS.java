package com.example.backend_vuln.model;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "metricas_cvss")
public class MetricasCVSS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_metricas;

    private String vectorString;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_vulnerabilidade", nullable = false)
    @JsonIgnore
    // @com.fasterxml.jackson.annotation.JsonBackReference
    private Vulnerabilidade vulnerabilidade;


    public MetricasCVSS() {}

    public Long getId_metricas() {
        return id_metricas;
    }

    public void setId_metricas(Long id_metricas) {
        this.id_metricas = id_metricas;
    }

    public String getVectorString() {
        return vectorString;
    }

    public void setVectorString(String vectorString) {
        this.vectorString = vectorString;
    }

    public Vulnerabilidade getVulnerabilidade() {
        return vulnerabilidade;
    }

    public void setVulnerabilidade(Vulnerabilidade vulnerabilidade) {
        this.vulnerabilidade = vulnerabilidade;
    }
}