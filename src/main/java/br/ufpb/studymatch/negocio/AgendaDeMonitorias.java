package br.ufpb.studymatch.negocio;

import br.ufpb.studymatch.entidades.Aluno;
import br.ufpb.studymatch.entidades.SessaoEstudo;
import br.ufpb.studymatch.excecoes.SessaoJaExisteException;
import br.ufpb.studymatch.excecoes.SessaoNaoEncontradaException;
import br.ufpb.studymatch.persistencia.GravadorDeDados;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AgendaDeMonitorias implements ISistemaMonitoria{

    private Map<String, SessaoEstudo> sessoes;
    private GravadorDeDados gravador;

    public AgendaDeMonitorias() {
        this.sessoes = new HashMap<>();
        this.gravador = new GravadorDeDados();
    }

    @Override
    public void cadastrarSessao(SessaoEstudo sessao) throws SessaoJaExisteException {
        if(this.sessoes.containsKey(sessao.getId())) {
            throw new SessaoJaExisteException("Já existe uma sessão cadastrada com o ID: " + sessao.getId());
        }
        this.sessoes.put(sessao.getId(), sessao);
    }

    @Override
    public void removerSessao(String id) throws SessaoNaoEncontradaException {
        if(!this.sessoes.containsKey(id)) {
            throw new SessaoNaoEncontradaException("Não foi possível remover. Sessão não encontrada para o ID: " + id);
        }
        this.sessoes.remove(id);
    }

    @Override
    public SessaoEstudo pesquisarSessaoPorId(String id) throws SessaoNaoEncontradaException {
        if(!this.sessoes.containsKey(id)) {
            throw new SessaoNaoEncontradaException("Sessão não encontrada com o ID: " + id);
        }
        return this.sessoes.get(id);
    }

    @Override
    public void atualizarSessao(String id, SessaoEstudo novaSessao) throws SessaoNaoEncontradaException {
        if(!this.sessoes.containsKey(id)) {
            throw new SessaoNaoEncontradaException("Sessão não encontrada com o ID: " + id);
        }
        this.sessoes.put(id, novaSessao);
    }

    @Override
    public List<SessaoEstudo> listarTodasSessoes() {
        return new ArrayList<>(this.sessoes.values());
    }

    @Override
    public List<SessaoEstudo> listarSessoesPorDisciplina(String disciplina) {
        return this.sessoes.values().stream()
                .filter(sessao -> sessao.getDisciplina().equalsIgnoreCase(disciplina))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> obterNomesDosMonitores() {
        return this.sessoes.values().stream()
                .map(SessaoEstudo::getNomeMonitor)
                .distinct().collect(Collectors.toList());
    }

    @Override
    public void cadastrarAlunoEmSessao(String idSessao, Aluno aluno) throws SessaoJaExisteException, SessaoNaoEncontradaException {
        SessaoEstudo sessao = pesquisarSessaoPorId(idSessao);
        sessao.adicionarAluno(aluno);
    }

    @Override
    public void salvarDados() throws IOException {
        this.gravador.salvarDados(this.sessoes);
    }

    @Override
    public void recuperarDados() throws IOException {
        this.sessoes = this.gravador.recuperarDados();
    }
}
