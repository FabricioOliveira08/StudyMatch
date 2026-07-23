package br.ufpb.studymatch.negocio;

import br.ufpb.studymatch.entidades.Aluno;
import br.ufpb.studymatch.entidades.SessaoEstudo;
import br.ufpb.studymatch.excecoes.SessaoNaoEncontradaException;
import br.ufpb.studymatch.excecoes.SessaoJaExisteException;

import java.io.IOException;
import java.util.List;

public interface ISistemaMonitoria {
    void cadastrarSessao(SessaoEstudo sessao) throws SessaoJaExisteException;
    void removerSessao(String id) throws SessaoNaoEncontradaException;
    SessaoEstudo pesquisarSessaoPorId(String id) throws SessaoNaoEncontradaException;
    void atualizarSessao(String id, SessaoEstudo novaSessao) throws SessaoNaoEncontradaException;
    List<SessaoEstudo> listarTodasSessoes();
    List<SessaoEstudo> listarSessoesPorDisciplina(String disciplina);
    List<String> obterNomesDosMonitores();
    void cadastrarAlunoEmSessao(String idSessao, Aluno aluno) throws SessaoJaExisteException, SessaoNaoEncontradaException;
    void salvarDados() throws IOException;
    void recuperarDados() throws IOException;
}
