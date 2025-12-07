package com.example.backend_vuln.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vulnerabilidade")
public class Vulnerabilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_vulnerabilidade;

    @Column(nullable = false)
    private String titulo;
    @Column(length = 1024)
    private String descricao;
    private String sistema_impactado;
    private String cve;
    private LocalDateTime data_registro;
    private String status;
    private Double pontuacao_cvss;
    private String criticidade;
    private String statusRec;

    @Column(columnDefinition = "TEXT")
    private String recomendacao;


    // Relação 1:1 com MetricasCVSS ou nao
    // CascadeType.ALL significa: "Quando eu salvar/apagar uma Vulnerabilidade,
    // salve/apague as MetricasCVSS junto."
    @OneToOne(mappedBy = "vulnerabilidade", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // @com.fasterxml.jackson.annotation.JsonManagedReference
    private MetricasCVSS metricasCVSS;

    // Relação N:1 com Usuario (Muitas vulnerabilidades para 1 usuário)
    @ManyToOne
    @JoinColumn(name = "fk_id_usuario")
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vulnerabilidade() {}

    public MetricasCVSS getMetricasCVSS() {
        return metricasCVSS;
    }

    public Long getId_vulnerabilidade() {
        return id_vulnerabilidade;
    }

    public void setId_vulnerabilidade(Long id_vulnerabilidade) {
        this.id_vulnerabilidade = id_vulnerabilidade;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSistema_impactado() {
        return sistema_impactado;
    }

    public void setSistema_impactado(String sistema_impactado) {
        this.sistema_impactado = sistema_impactado;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public String getStatusRec() {
        return statusRec;
    }

    public void setStatusRec(String statusRec) {
        this.statusRec = statusRec;
    }

    public LocalDateTime getData_registro() {
        return data_registro;
    }

    public void setData_registro(LocalDateTime data_registro) {
        this.data_registro = data_registro;
    }

    public Double getPontuacao_cvss() {
        return pontuacao_cvss;
    }

    public void setPontuacao_cvss(Double pontuacao_cvss) {
        this.pontuacao_cvss = pontuacao_cvss;
    }

    public String getCriticidade() {
        return criticidade;
    }

    public void setCriticidade(String criticidade) {
        this.criticidade = criticidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMetricasCVSS(MetricasCVSS metricasCVSS) {
        this.metricasCVSS = metricasCVSS;
        if (metricasCVSS != null) {
            metricasCVSS.setVulnerabilidade(this); // Mantém a ligação dos dois lados
        }
    }

    public String getRecomendacao(){
        return recomendacao;
    }

    public void setRecomendacao(String recomendacao){
        this.recomendacao = recomendacao;
    }
}