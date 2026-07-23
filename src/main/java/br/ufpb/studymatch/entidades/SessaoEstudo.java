package br.ufpb.studymatch.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SessaoEstudo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String disciplina;
    private String nomeMonitor;
    private List<Aluno> alunosInscritos;

    public SessaoEstudo(String id, String disciplina, String nomeMonitor) {
        this.id = id;
        this.disciplina = disciplina;
        this.nomeMonitor = nomeMonitor;
        this.alunosInscritos = new ArrayList<>();
    }

    public void adicionarAluno(Aluno aluno) {
        this.alunosInscritos.add(aluno);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDisciplina() {
        return disciplina;
    }
    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getNomeMonitor() {
        return nomeMonitor;
    }
    public void setNomeMonitor(String nomeMonitor) {
        this.nomeMonitor = nomeMonitor;
    }

    public List<Aluno> getAlunosInscritos() { return alunosInscritos; }
}
