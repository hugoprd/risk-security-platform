package com.example.backend_vuln.controller;

import com.example.backend_vuln.model.Vulnerabilidade;
import com.example.backend_vuln.repository.VulnerabilidadeRepository;
import com.example.backend_vuln.service.CvssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.backend_vuln.model.Usuario;
import com.example.backend_vuln.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vulnerabilidades")
@CrossOrigin(origins = "*")
public class VulnerabilidadeController {

    @Autowired
    private VulnerabilidadeRepository repository;

    @Autowired
    private CvssService cvssService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // --- Injeção do serviço de IA ---
    @Autowired
    private com.example.backend_vuln.service.IAService iaService;
   

    // UC01: Cadastrar Nova Vulnerabilidade
    @PostMapping("/usuario/{idUsuario}")
    public Vulnerabilidade cadastrarVulnerabilidade(@PathVariable Long idUsuario,
                                                    @RequestBody Vulnerabilidade novaVulnerabilidade) {

        // 1. Busca o usuário no banco
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        // 2. Seta o usuario na vulnerabilidade
        novaVulnerabilidade.setUsuario(usuario);

        // 3. Calcula e preenche o CVSS
        cvssService.calcularEPreencherCvss(novaVulnerabilidade);

        // 4. Define a data de registro
        novaVulnerabilidade.setData_registro(LocalDateTime.now());

        // 5. Salva no banco
        return repository.save(novaVulnerabilidade);
    }


    // UC02: Listar Todas as vulnerabilidades
    @GetMapping
    public Page<Vulnerabilidade> listarVulnerabilidades(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        // Cria a "fatia" de dados (Página X, Tamanho 20)
        Pageable paginacao = PageRequest.of(page, size);

        return repository.findAll(paginacao);
    }


    //UC05: Visualizar detalhes de uma vlnerabilidade
    @GetMapping("/{id}")
    public Vulnerabilidade buscarPorId(@PathVariable Long id) {
        // Usa o repository para buscar pelo ID
        // Se não encontrar, lança uma exceção
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vulnerabilidade não encontrada com id: " + id));
    }

    // UC04: Editar Vulnerabilidade eatualizar Status
    @PutMapping("/{id}")
    public Vulnerabilidade atualizarVulnerabilidade(@PathVariable Long id, @RequestBody Vulnerabilidade dadosAtualizados) {

        // 1. busca a vulnerabilidade existente no banco
        Vulnerabilidade vulnerabilidadeExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vulnerabilidade não encontrada com id: " + id));

        // 2. Atualiza os campos da vulnerabilidade existente com os novos dados
        vulnerabilidadeExistente.setTitulo(dadosAtualizados.getTitulo());
        vulnerabilidadeExistente.setDescricao(dadosAtualizados.getDescricao());
        vulnerabilidadeExistente.setSistema_impactado(dadosAtualizados.getSistema_impactado());
        vulnerabilidadeExistente.setCve(dadosAtualizados.getCve());
        vulnerabilidadeExistente.setStatus(dadosAtualizados.getStatus()); // Muito usado para mudar de "ABERTA" para "CORRIGIDA"

        // 3. Atualiza as métricas 
        if (dadosAtualizados.getMetricasCVSS() != null) {
            // Pega o novo vetor
            String novoVectorString = dadosAtualizados.getMetricasCVSS().getVectorString();
            // Atualiza o vetor na entidade existente
            vulnerabilidadeExistente.getMetricasCVSS().setVectorString(novoVectorString);
        }

        // 4. RECALCULA o CVSS
        // caso o vetor tenha sido alterado
        cvssService.calcularEPreencherCvss(vulnerabilidadeExistente);

        // 5. Salva a entidade ATUALIZADA de volta no banco
        return repository.save(vulnerabilidadeExistente);
    }

    //UC (Extra): Deletar uma Vulnerabilidade
    @DeleteMapping("/{id}")
    public String deletarVulnerabilidade(@PathVariable Long id) {

        // 1. Verifica se a vulnerabilidade existe antes de tentar deletar
        if (!repository.existsById(id)) {
            throw new RuntimeException("Vulnerabilidade não encontrada com id: " + id);
        }

        // 2. Deleta do banco de dados
        // (Graças ao CascadeType.ALL também deletará as MetricasCVSS associadas)
        repository.deleteById(id);

        // 3. Retorna uma mensagem de sucesso
        return "Vulnerabilidade com id " + id + " foi deletada com sucesso.";
    }

    // intregacao com a IA
    @PostMapping("/{id}/recomendacao")
    public String gerarRecomendacao(@PathVariable Long id) {
        // Busca a vulnerabilidade
        Vulnerabilidade vul = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vulnerabilidade não encontrada"));

        // Chama o servico Wrapper
        return iaService.gerarRecomendacao(vul);
    }
    

}
