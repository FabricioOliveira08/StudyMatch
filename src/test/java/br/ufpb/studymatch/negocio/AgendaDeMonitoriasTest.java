package br.ufpb.studymatch.negocio;

import br.ufpb.studymatch.entidades.SessaoEstudo;
import br.ufpb.studymatch.excecoes.SessaoNaoEncontradaException;
import br.ufpb.studymatch.excecoes.SessaoJaExisteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AgendaDeMonitoriasTest {

    private ISistemaMonitoria sistema;

    @BeforeEach
    public void setUp() {
        sistema = new AgendaDeMonitorias();
    }

    @Test
    public void testCadastrarEPesquisarSessaoComSucesso() throws SessaoJaExisteException, SessaoNaoEncontradaException {
        SessaoEstudo sessao = new SessaoEstudo("1", "Programação Orientada a Objetos", "Fabricio");

        sistema.cadastrarSessao(sessao);
        SessaoEstudo sessaoRetornada = sistema.pesquisarSessaoPorId("1");

        assertEquals("Programação Orientada a Objetos", sessaoRetornada.getDisciplina());
        assertEquals("Fabricio", sessaoRetornada.getNomeMonitor());
    }

    @Test
    public void testCadastrarSessaoDuplicadaGeraExceção() throws SessaoJaExisteException {
        SessaoEstudo sessao1 = new SessaoEstudo("1", "Cálculo", "Geovana");
        SessaoEstudo sessao2 = new SessaoEstudo("1", "Vetorial", "Angélica");
        sistema.cadastrarSessao(sessao1);

        assertThrows(SessaoJaExisteException.class, () -> {
            sistema.cadastrarSessao(sessao2);
        });
    }

    @Test
    public void testPesquisarSessaoInexistenteGeraExcecao() {
        assertThrows(SessaoNaoEncontradaException.class, () -> {
            sistema.pesquisarSessaoPorId("999");
        });
    }

    @Test
    public void testListarSessoesPorDisciplina() throws SessaoJaExisteException {
        SessaoEstudo sessao1 = new SessaoEstudo("1", "Matemática Discreta", "João");
        SessaoEstudo sessao2 = new SessaoEstudo("2", "Matemática Discreta", "Maria");
        SessaoEstudo sessao3 = new SessaoEstudo("3", "Estrutura de Dados", "José");

        sistema.cadastrarSessao(sessao1);
        sistema.cadastrarSessao(sessao2);
        sistema.cadastrarSessao(sessao3);

        List<SessaoEstudo> resultado = sistema.listarSessoesPorDisciplina("Matemática Discreta");

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(sessao1));
        assertTrue(resultado.contains(sessao2));
        assertFalse(resultado.contains(sessao3));
    }
}
