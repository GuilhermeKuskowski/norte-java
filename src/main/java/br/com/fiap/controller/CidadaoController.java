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
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.bo.CidadaoBO;
import br.com.fiap.bo.SolicitacaoBO;
import br.com.fiap.model.Cidadao;
import br.com.fiap.model.Solicitacao;

@RestController
@RequestMapping("/cidadaos")
public class CidadaoController {

    private final CidadaoBO cidadaoBO;
    private final SolicitacaoBO solicitacaoBO;

    public CidadaoController(CidadaoBO cidadaoBO, SolicitacaoBO solicitacaoBO) {
        this.cidadaoBO = cidadaoBO;
        this.solicitacaoBO = solicitacaoBO;
    }

    @GetMapping
    public ResponseEntity<List<Cidadao>> listar() {
        return ResponseEntity.ok(cidadaoBO.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cidadao> buscar(@PathVariable("id") int id) {
        return ResponseEntity.ok(cidadaoBO.buscar(id));
    }

    @GetMapping("/{id}/solicitacoes")
    public ResponseEntity<List<Solicitacao>> solicitacoesDoCidadao(@PathVariable("id") int id) {
        cidadaoBO.buscar(id); // 404 se o cidadao nao existir
        return ResponseEntity.ok(solicitacaoBO.listar(id, null, null));
    }

    @PostMapping
    public ResponseEntity<Cidadao> cadastrar(@RequestBody Cidadao cidadao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cidadaoBO.cadastrar(cidadao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cidadao> atualizar(@PathVariable("id") int id, @RequestBody Cidadao cidadao) {
        return ResponseEntity.ok(cidadaoBO.atualizar(id, cidadao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") int id) {
        cidadaoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
