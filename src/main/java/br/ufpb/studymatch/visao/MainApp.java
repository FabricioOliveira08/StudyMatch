    package br.ufpb.studymatch.visao;

    import br.ufpb.studymatch.entidades.SessaoEstudo;
    import br.ufpb.studymatch.excecoes.SessaoJaExisteException;
    import br.ufpb.studymatch.excecoes.SessaoNaoEncontradaException;
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
                System.out.println("Iniciando agenda vazia.");
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

            GridPane formCadastro = new GridPane();
            formCadastro.setPadding(new Insets(20));
            formCadastro.setHgap(10);
            formCadastro.setVgap(10);

            Label lblId = new Label("ID da Sessão:");
            TextField txtId = new TextField();

            Label lblDisciplina = new Label("Disciplina:");
            TextField txtDisciplina = new TextField();

            Label lblMonitor = new Label("Nome do Monitor:");
            TextField txtMonitor = new TextField();

            Button btnCadastrar = new Button("Cadastrar Sessão");

            formCadastro.add(lblId, 0, 0);
            formCadastro.add(txtId, 1, 0);

            formCadastro.add(lblDisciplina, 0, 1);
            formCadastro.add(txtDisciplina, 1, 1);

            formCadastro.add(lblMonitor, 0, 2);
            formCadastro.add(txtMonitor, 1, 2);

            formCadastro.add(btnCadastrar, 1, 3);

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

            GridPane formPesquisa = new GridPane();
            formPesquisa.setPadding(new Insets(20));
            formPesquisa.setHgap(10);
            formPesquisa.setVgap(10);

            Label lblIdBusca = new Label("ID para Busca:");
            TextField txtIdBusca = new TextField();
            Button btnBuscar = new Button("Buscar Sessão");

            Label lblResultadoDisciplina = new Label("Disciplina: -----");
            Label lblResultadoMonitor = new Label("Monitor: -----");

            formPesquisa.add(lblIdBusca, 0, 0);
            formPesquisa.add(txtIdBusca, 1, 0);
            formPesquisa.add(btnBuscar, 2, 0);

            formPesquisa.add(new Label("Resultados da Pesquisa:"), 0, 2);
            formPesquisa.add(lblResultadoDisciplina, 0, 3, 2, 1);
            formPesquisa.add(lblResultadoMonitor, 0, 4, 2, 1);

            btnBuscar.setOnAction(evento -> {
                try {
                    SessaoEstudo sessaoEncontrada = sistema.pesquisarSessaoPorId(txtIdBusca.getText());
                    lblResultadoDisciplina.setText("Disciplina: " + sessaoEncontrada.getDisciplina());
                    lblResultadoMonitor.setText("Monitor: " + sessaoEncontrada.getNomeMonitor());
                } catch (SessaoNaoEncontradaException e) {
                    lblResultadoDisciplina.setText("Disciplina: -----");
                    lblResultadoMonitor.setText("Monitor: -----");

                    Alert alertaErro = new Alert(Alert.AlertType.WARNING);
                    alertaErro.setTitle("Aviso");
                    alertaErro.setHeaderText(null);
                    alertaErro.setContentText(e.getMessage());
                    alertaErro.showAndWait();
                }
            });

            TabPane painelDeAbas = new TabPane();

            Tab abaCadastro = new Tab("Cadastro", formCadastro);
            abaCadastro.setClosable(false);

            Tab abaPesquisa = new Tab("Pesquisa", formPesquisa);
            abaPesquisa.setClosable(false);

            painelDeAbas.getTabs().addAll(abaCadastro, abaPesquisa);


            BorderPane painelPrincipal = new BorderPane();
            painelPrincipal.setTop(barraDeMenu);
            painelPrincipal.setCenter(painelDeAbas);

            Scene cena = new Scene(painelPrincipal, 800, 600);
            palcoPrincipal.setScene(cena);
            palcoPrincipal.show();

        }

        public static void main(String[] args) {
            launch(args);
        }
    }
