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

import br.com.fiap.bo.EncaminhamentoBO;
import br.com.fiap.model.Encaminhamento;

@RestController
@RequestMapping("/encaminhamentos")
public class EncaminhamentoController {

    private final EncaminhamentoBO encaminhamentoBO;

    public EncaminhamentoController(EncaminhamentoBO encaminhamentoBO) {
        this.encaminhamentoBO = encaminhamentoBO;
    }

    @GetMapping
    public ResponseEntity<List<Encaminhamento>> listar() {
        return ResponseEntity.ok(encaminhamentoBO.listar(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Encaminhamento> buscar(@PathVariable("id") int id) {
        return ResponseEntity.ok(encaminhamentoBO.buscar(id));
    }

    @PostMapping
    public ResponseEntity<Encaminhamento> encaminhar(@RequestBody Encaminhamento encaminhamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(encaminhamentoBO.encaminhar(encaminhamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Encaminhamento> atualizar(@PathVariable("id") int id, @RequestBody Encaminhamento encaminhamento) {
        return ResponseEntity.ok(encaminhamentoBO.atualizar(id, encaminhamento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") int id) {
        encaminhamentoBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
