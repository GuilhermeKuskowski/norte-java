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

import br.com.fiap.bo.AcaoPlanoBO;
import br.com.fiap.bo.PlanoAcaoBO;
import br.com.fiap.model.AcaoPlano;
import br.com.fiap.model.PlanoAcao;

@RestController
@RequestMapping("/planos")
public class PlanoAcaoController {

    private final PlanoAcaoBO planoAcaoBO;
    private final AcaoPlanoBO acaoPlanoBO;

    public PlanoAcaoController(PlanoAcaoBO planoAcaoBO, AcaoPlanoBO acaoPlanoBO) {
        this.planoAcaoBO = planoAcaoBO;
        this.acaoPlanoBO = acaoPlanoBO;
    }

    @GetMapping
    public ResponseEntity<List<PlanoAcao>> listar() {
        return ResponseEntity.ok(planoAcaoBO.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoAcao> buscar(@PathVariable("id") int id) {
        return ResponseEntity.ok(planoAcaoBO.buscar(id));
    }

    @GetMapping("/{id}/acoes")
    public ResponseEntity<List<AcaoPlano>> acoesDoPlano(@PathVariable("id") int id) {
        return ResponseEntity.ok(acaoPlanoBO.listarPorPlano(id));
    }

    /** Cria o plano com os passos (acoes) e o checklist em uma unica transacao. */
    @PostMapping
    public ResponseEntity<PlanoAcao> criar(@RequestBody PlanoAcao plano) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planoAcaoBO.criar(plano));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanoAcao> atualizar(@PathVariable("id") int id, @RequestBody PlanoAcao plano) {
        return ResponseEntity.ok(planoAcaoBO.atualizar(id, plano));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") int id) {
        planoAcaoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
