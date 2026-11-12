package br.com.fiap.bo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Testes da regra do semaforo (nao precisam de banco de dados).
 */
class AcaoPlanoBOTest {

    private final LocalDate hoje = LocalDate.of(2026, 11, 12);

    @Test
    void prazoVencidoFicaVermelho() {
        assertEquals("VERMELHO", AcaoPlanoBO.calcularSemaforo(hoje.minusDays(1), false, hoje));
    }

    @Test
    void prazoHojeOuEmAteTresDiasFicaAmarelo() {
        assertEquals("AMARELO", AcaoPlanoBO.calcularSemaforo(hoje, false, hoje));
        assertEquals("AMARELO", AcaoPlanoBO.calcularSemaforo(hoje.plusDays(3), false, hoje));
    }

    @Test
    void prazoDistanteFicaVerde() {
        assertEquals("VERDE", AcaoPlanoBO.calcularSemaforo(hoje.plusDays(4), false, hoje));
    }

    @Test
    void passoConcluidoOuSemPrazoFicaVerde() {
        assertEquals("VERDE", AcaoPlanoBO.calcularSemaforo(hoje.minusDays(10), true, hoje));
        assertEquals("VERDE", AcaoPlanoBO.calcularSemaforo(null, false, hoje));
    }
}
