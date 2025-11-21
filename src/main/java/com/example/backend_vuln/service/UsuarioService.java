package com.example.backend_vuln.service;

import com.example.backend_vuln.model.LogAcesso;
import com.example.backend_vuln.model.Usuario;
import com.example.backend_vuln.repository.LogAcessoRepository;
import com.example.backend_vuln.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LogAcessoRepository logAcessoRepository;

    // Injeção da ferramenta de criptografia
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarUsuarios() { return usuarioRepository.findAll(); }

    public Optional<Usuario> buscarPorId(Long id) { return usuarioRepository.findById(id); }

    public Usuario criarUsuario(Usuario usuario) {
        System.out.println("INICIO DO PROCESSO DE CRIACAO");

        // 1. Criptografa a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        // 2. Salva no Postgres
        Usuario novoUsuario = usuarioRepository.save(usuario);
        System.out.println("Usuario salvo no Postgres. ID: " + novoUsuario.getId_usuario());

        // 3. Tenta salvar o log no Mongo
        registrarLog(novoUsuario.getId_usuario(), "CRIACAO_USUARIO", "Email: " + novoUsuario.getEmail());

        return novoUsuario;
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setEmail(usuarioAtualizado.getEmail());
            // Se a senha mudar, teria que criptografar aqui também, mas vamos manter simples por enquanto
            usuario.setSenha(usuarioAtualizado.getSenha()); 
            usuario.setTipo_usuario(usuarioAtualizado.getTipo_usuario());
            usuario.setAtivo(usuarioAtualizado.isAtivo());

            Usuario salvo = usuarioRepository.save(usuario);
            registrarLog(salvo.getId_usuario(), "ATUALIZACAO_USUARIO", "Dados atualizados");
            return salvo;
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void deletarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            registrarLog(id, "EXCLUSAO_USUARIO", "Removido");
        }
    }

    private void registrarLog(Long idUsuario, String acao, String detalhe) {
        try {
            LogAcesso log = new LogAcesso();
            log.setFkIdUsuario(idUsuario);
            log.setAcao(acao);
            log.setRecurso(detalhe);
            log.setIpOrigem("localhost");
            log.setSucesso(true);
            log.setDataEvento(LocalDateTime.now());

            logAcessoRepository.save(log);
        } catch (Exception e) {
            System.err.println("Erro ao salvar log no MongoDB: " + e.getMessage());
        }
    }
}
