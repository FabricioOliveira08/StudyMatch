package br.ufpb.studymatch.excecoes;

public class SessaoJaExisteException extends Exception{
    public SessaoJaExisteException(String mensagem) {
        super(mensagem);
    }
}
