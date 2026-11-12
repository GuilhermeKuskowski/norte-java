package br.com.fiap.controller;

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
import br.com.fiap.model.AcaoPlano;

@RestController
@RequestMapping("/acoes")
public class AcaoPlanoController {

    private final AcaoPlanoBO acaoPlanoBO;

    public AcaoPlanoController(AcaoPlanoBO acaoPlanoBO) {
        this.acaoPlanoBO = acaoPlanoBO;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AcaoPlano> buscar(@PathVariable("id") int id) {
        return ResponseEntity.ok(acaoPlanoBO.buscar(id));
    }

    @PostMapping
    public ResponseEntity<AcaoPlano> adicionar(@RequestBody AcaoPlano acao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(acaoPlanoBO.adicionar(acao));
    }

    /** Ex.: marcar o passo como concluido (concluida = true). O semaforo e recalculado. */
    @PutMapping("/{id}")
    public ResponseEntity<AcaoPlano> atualizar(@PathVariable("id") int id, @RequestBody AcaoPlano acao) {
        return ResponseEntity.ok(acaoPlanoBO.atualizar(id, acao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") int id) {
        acaoPlanoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
