package com.example.backend_vuln.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "log_acesso")
public class LogAcesso {
    @Id
    private String id;

    private LocalDateTime dataHora;
    private String usuario;
    private String acao;
    private Long fkIdUsuario;
    private String recurso;
    private String ipOrigem;
    private boolean sucesso;
    private LocalDateTime dataEvento;

    public LogAcesso() {}

    public String getId(){
        return id;
    }

    public void setId(String id){
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

    public Long getFkIdUsuario(){
        return fkIdUsuario;
    }

    public void setFkIdUsuario(Long fkIdUsuario){
        this.fkIdUsuario = fkIdUsuario;
    }

    public String getRecurso(){
        return recurso;
    }

    public void setRecurso(String recurso){
        this.recurso = recurso;
    }

    public String getIpOrigem(){
        return ipOrigem;
    }

    public void setIpOrigem(String ipOrigem){
        this.ipOrigem = ipOrigem;
    }

    public boolean isSucesso(){
        return sucesso;
    }

    public void setSucesso(boolean sucesso){
        this.sucesso = sucesso;
    }

    public LocalDateTime getDataEvento(){
        return dataEvento;
    }

    public void setDataEvento(LocalDateTime dataEvento){
        this.dataEvento = dataEvento;
    }
}
