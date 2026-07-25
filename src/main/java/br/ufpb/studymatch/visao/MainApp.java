package br.ufpb.studymatch.visao;

import br.ufpb.studymatch.entidades.SessaoEstudo;
import br.ufpb.studymatch.excecoes.SessaoJaExisteException;
import br.ufpb.studymatch.negocio.AgendaDeMonitorias;
import br.ufpb.studymatch.negocio.ISistemaMonitoria;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainApp extends Application{

    private ISistemaMonitoria sistema;

    @Override
    public void start(Stage palcoPrincipal) {
        this.sistema = new AgendaDeMonitorias();

        try {
            this.sistema.recuperarDados();
        } catch (Exception e) {
            System.out.println("Primeira execução ou arquivo não encontrado. Iniciando agenda vazia.");
        }

        palcoPrincipal.setTitle("StudyMatch - Gestão de Monitorias");

        MenuBar barraDeMenu = new MenuBar();
        Menu menuArquivo = new Menu("Arquivo");
        MenuItem itemSalvar = new MenuItem("Salvar Dados");
        MenuItem itemSair = new MenuItem("Sair");
        menuArquivo.getItems().addAll(itemSalvar, itemSair);
        barraDeMenu.getMenus().add(menuArquivo);

        itemSalvar.setOnAction(evento -> {
            try {
                sistema.salvarDados();

                Alert alertaSalvar = new Alert(Alert.AlertType.INFORMATION);
                alertaSalvar.setTitle("Sucesso");
                alertaSalvar.setHeaderText(null);
                alertaSalvar.setContentText("Todas as monitorias foram salvas.");
                alertaSalvar.showAndWait();

            } catch (Exception e) {
                Alert alertaErro = new Alert(Alert.AlertType.ERROR);
                alertaErro.setTitle("Erro de Gravação");
                alertaErro.setHeaderText(null);
                alertaErro.setContentText("Falha ao salvar os dados: " + e.getMessage());
                alertaErro.showAndWait();
            }
        });

        itemSair.setOnAction(evento -> Platform.exit());

        GridPane formulario = new GridPane();
        formulario.setPadding(new Insets(20));
        formulario.setHgap(10);
        formulario.setVgap(10);

        Label lblId = new Label("ID da Sessão:");
        TextField txtId = new TextField();

        Label lblDisciplina = new Label("Disciplina:");
        TextField txtDisciplina = new TextField();

        Label lblMonitor = new Label("Nome do Monitor:");
        TextField txtMonitor = new TextField();

        Button btnCadastrar = new Button("Cadastrar Sessão");

        formulario.add(lblId, 0, 0);
        formulario.add(txtId, 1, 0);

        formulario.add(lblDisciplina, 0, 1);
        formulario.add(txtDisciplina, 1, 1);

        formulario.add(lblMonitor, 0, 2);
        formulario.add(txtMonitor, 1, 2);

        formulario.add(btnCadastrar, 1, 3);

        btnCadastrar.setOnAction(evento -> {
            try {
                String id = txtId.getText();
                String disciplina = txtDisciplina.getText();
                String monitor = txtMonitor.getText();

                SessaoEstudo novaSessao = new SessaoEstudo(id, disciplina, monitor);
                sistema.cadastrarSessao(novaSessao);

                txtId.clear();
                txtDisciplina.clear();
                txtMonitor.clear();

                Alert alertaSucesso = new Alert(Alert.AlertType.INFORMATION);
                alertaSucesso.setTitle("Sucesso");
                alertaSucesso.setHeaderText(null);
                alertaSucesso.setContentText("Monitoria cadastrada com sucesso!");
                alertaSucesso.showAndWait();

            } catch (SessaoJaExisteException e) {
                Alert alertaErro = new Alert(Alert.AlertType.ERROR);
                alertaErro.setTitle("Erro no Cadastro");
                alertaErro.setHeaderText(null);
                alertaErro.setContentText(e.getMessage());
                alertaErro.showAndWait();
            }
        });

        BorderPane painelPrincipal = new BorderPane();
        painelPrincipal.setTop(barraDeMenu);
        painelPrincipal.setCenter(formulario);

        Scene cena = new Scene(painelPrincipal, 800, 600);
        palcoPrincipal.setScene(cena);
        palcoPrincipal.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
