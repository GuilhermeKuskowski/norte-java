package br.com.fiap.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.bo.EncaminhamentoBO;
import br.com.fiap.bo.PlanoAcaoBO;
import br.com.fiap.bo.SolicitacaoBO;
import br.com.fiap.model.Encaminhamento;
import br.com.fiap.model.PlanoAcao;
import br.com.fiap.model.Solicitacao;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoBO solicitacaoBO;
    private final PlanoAcaoBO planoAcaoBO;
    private final EncaminhamentoBO encaminhamentoBO;

    public SolicitacaoController(SolicitacaoBO solicitacaoBO, PlanoAcaoBO planoAcaoBO, EncaminhamentoBO encaminhamentoBO) {
        this.solicitacaoBO = solicitacaoBO;
        this.planoAcaoBO = planoAcaoBO;
        this.encaminhamentoBO = encaminhamentoBO;
    }

    /** Filtros opcionais: ?cidadao=1&area=3&status=ABERTA */
    @GetMapping
    public ResponseEntity<List<Solicitacao>> listar(@RequestParam(name = "cidadao", required = false) Integer cidadao,
                                                    @RequestParam(name = "area", required = false) Integer area,
                                                    @RequestParam(name = "status", required = false) String status) {
        return ResponseEntity.ok(solicitacaoBO.listar(cidadao, area, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitacao> buscar(@PathVariable("id") int id) {
        return ResponseEntity.ok(solicitacaoBO.buscar(id));
    }

    @GetMapping("/{id}/plano")
    public ResponseEntity<PlanoAcao> planoDaSolicitacao(@PathVariable("id") int id) {
        solicitacaoBO.buscar(id);
        return ResponseEntity.ok(planoAcaoBO.buscarPorSolicitacao(id));
    }

    @GetMapping("/{id}/encaminhamentos")
    public ResponseEntity<List<Encaminhamento>> encaminhamentosDaSolicitacao(@PathVariable("id") int id) {
        solicitacaoBO.buscar(id);
        return ResponseEntity.ok(encaminhamentoBO.listar(id));
    }

    @PostMapping
    public ResponseEntity<Solicitacao> abrir(@RequestBody Solicitacao solicitacao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacaoBO.abrir(solicitacao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Solicitacao> atualizar(@PathVariable("id") int id, @RequestBody Solicitacao solicitacao) {
        return ResponseEntity.ok(solicitacaoBO.atualizar(id, solicitacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") int id) {
        solicitacaoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
