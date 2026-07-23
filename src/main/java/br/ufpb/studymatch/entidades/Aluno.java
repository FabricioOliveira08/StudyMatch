package br.ufpb.studymatch.entidades;

import java.io.Serializable;

public class Aluno implements Serializable {
    private static final long serialVersionUID = 1L;

    private String matricula;
    private String nome;
    private String curso;

    public Aluno(String matricula, String nome, String curso) {
        this.matricula = matricula;
        this.nome = nome;
        this.curso = curso;
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    @Override
    public String toString() {
        return nome + " (" + matricula + ") - " + curso;
    }
}
