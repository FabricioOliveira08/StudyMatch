package br.ufpb.studymatch.persistencia;

import br.ufpb.studymatch.entidades.SessaoEstudo;

import java.io.*;
import java.util.Map;

public class GravadorDeDados {

    private static final String ARQUIVO_DADOS = "dados.dat";

    public void salvarDados(Map<String, SessaoEstudo> sessoes) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(ARQUIVO_DADOS);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(sessoes);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, SessaoEstudo> recuperarDados() throws IOException {
        try(FileInputStream fis = new FileInputStream(ARQUIVO_DADOS);
            ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object objetoRecuperado = ois.readObject();
            return (Map<String, SessaoEstudo>) objetoRecuperado;
        } catch (ClassNotFoundException e) {
            throw new IOException("Erro crítico: A estrutura da classe não foi encontrada ao ler o arquivo.", e);
        }
    }
}
