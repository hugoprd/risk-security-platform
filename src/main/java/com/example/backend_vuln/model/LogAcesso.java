package com.example.backend_vuln.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_acesso")
public class LogAcesso{
    @Id
    private Long id;
    private LocalDateTime dataHora;
    private String usuario;
    private String acao;
    private Long fkIdUsuario;
    private String recurso;
    private String ipOrigem;
    private boolean sucesso;
    private LocalDateTime dataEvento;
    
    public LogAcesso(){}

    public LogAcesso(String usuario, String acao){
        this.dataHora = LocalDateTime.now();
        this.usuario = usuario;
        this.acao = acao;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setFkIdUsuario(Long fkIdUsuario){
        this.fkIdUsuario = fkIdUsuario;
    }

    public void setRecurso(String recurso){
        this.recurso = recurso;
    }

    public void setIpOrigem(String ipOrigem){
        this.ipOrigem = ipOrigem;
    }

    public void setSucesso(boolean sucesso){
        this.sucesso = sucesso;
    }

    public void setDataEvento(LocalDateTime dataEvento){
        this.dataEvento = dataEvento;
    }
    
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public LocalDateTime getDataHora(){
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora){
        this.dataHora = dataHora;
    }

    public String getUsuario(){
        return usuario;
    }

    public void setUsuario(String usuario){
        this.usuario = usuario;
    }

    public String getAcao(){
        return acao;
    }

    public void setAcao(String acao){
        this.acao = acao;
    }
}