package br.com.fiap.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Pagina inicial da API: mostra que ela esta no ar e aponta para a documentacao.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> inicio() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("api", "Norte - orientacao sobre direitos do dia a dia");
        info.put("equipe", "Acesstech - GS 2026.2 CitizenTech - FIAP 2ESPI");
        info.put("status", "no ar");
        info.put("documentacao", "/swagger-ui.html");
        info.put("recursos", new String[] {"/cidadaos", "/solicitacoes", "/planos", "/acoes", "/encaminhamentos",
                "/areas", "/leis", "/pontos", "/bairros"});
        return ResponseEntity.ok(info);
    }
}
