package br.com.fiap.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.bo.ConsultaBO;
import br.com.fiap.model.AreaDireito;
import br.com.fiap.model.Bairro;
import br.com.fiap.model.Lei;
import br.com.fiap.model.PontoAtendimento;

/**
 * Consultas de apoio (somente leitura) usadas pelas telas do app.
 */
@RestController
public class ConsultaController {

    private final ConsultaBO consultaBO;

    public ConsultaController(ConsultaBO consultaBO) {
        this.consultaBO = consultaBO;
    }

    @GetMapping("/bairros")
    public ResponseEntity<List<Bairro>> bairros() {
        return ResponseEntity.ok(consultaBO.listarBairros());
    }

    @GetMapping("/areas")
    public ResponseEntity<List<AreaDireito>> areas() {
        return ResponseEntity.ok(consultaBO.listarAreas());
    }

    @GetMapping("/areas/{id}")
    public ResponseEntity<AreaDireito> area(@PathVariable("id") int id) {
        return ResponseEntity.ok(consultaBO.buscarArea(id));
    }

    @GetMapping("/areas/{id}/leis")
    public ResponseEntity<List<Lei>> leisDaArea(@PathVariable("id") int id) {
        return ResponseEntity.ok(consultaBO.listarLeisDaArea(id));
    }

    @GetMapping("/leis")
    public ResponseEntity<List<Lei>> leis() {
        return ResponseEntity.ok(consultaBO.listarLeis());
    }

    @GetMapping("/leis/{id}")
    public ResponseEntity<Lei> lei(@PathVariable("id") int id) {
        return ResponseEntity.ok(consultaBO.buscarLei(id));
    }

    /** Filtros opcionais: ?tipo=PROCON&acessivel=true */
    @GetMapping("/pontos")
    public ResponseEntity<List<PontoAtendimento>> pontos(@RequestParam(name = "tipo", required = false) String tipo,
                                                         @RequestParam(name = "acessivel", defaultValue = "false") boolean acessivel) {
        return ResponseEntity.ok(consultaBO.listarPontos(tipo, acessivel));
    }

    @GetMapping("/pontos/{id}")
    public ResponseEntity<PontoAtendimento> ponto(@PathVariable("id") int id) {
        return ResponseEntity.ok(consultaBO.buscarPonto(id));
    }
}
