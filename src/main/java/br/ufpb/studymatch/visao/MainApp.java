    package br.ufpb.studymatch.visao;

    import br.ufpb.studymatch.entidades.SessaoEstudo;
    import br.ufpb.studymatch.entidades.Aluno;
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
    import javafx.scene.layout.VBox;
    import javafx.stage.Stage;

    import java.util.List;

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

            // Novos componentes para mostrar a lista de alunos
            Label lblAlunosTitulo = new Label("Alunos Matriculados:");
            ListView<String> listaAlunosResultado = new ListView<>();
            listaAlunosResultado.setPrefHeight(150); // Altura menor para caber bem na tela

            formPesquisa.add(lblIdBusca, 0, 0);
            formPesquisa.add(txtIdBusca, 1, 0);
            formPesquisa.add(btnBuscar, 2, 0);

            formPesquisa.add(new Separator(), 0, 1, 3, 1); // Linha divisória

            formPesquisa.add(new Label("Resultados da Pesquisa:"), 0, 2);
            formPesquisa.add(lblResultadoDisciplina, 0, 3, 3, 1);
            formPesquisa.add(lblResultadoMonitor, 0, 4, 3, 1);

            // Encaixando a lista de alunos abaixo das informações da sessão
            formPesquisa.add(lblAlunosTitulo, 0, 5, 3, 1);
            formPesquisa.add(listaAlunosResultado, 0, 6, 3, 1);

            btnBuscar.setOnAction(evento -> {
                try {
                    SessaoEstudo sessaoEncontrada = sistema.pesquisarSessaoPorId(txtIdBusca.getText());
                    lblResultadoDisciplina.setText("Disciplina: " + sessaoEncontrada.getDisciplina());
                    lblResultadoMonitor.setText("Monitor: " + sessaoEncontrada.getNomeMonitor());

                    // Limpa a visualização anterior
                    listaAlunosResultado.getItems().clear();

                    // Puxa a lista de alunos da entidade
                    List<Aluno> alunos = sessaoEncontrada.getAlunosInscritos();

                    // Verifica se há alunos e preenche o ListView
                    if (alunos == null || alunos.isEmpty()) {
                        listaAlunosResultado.getItems().add("Nenhum aluno matriculado nesta sessão.");
                    } else {
                        for (Aluno aluno : alunos) {
                            // Formata a exibição: "Matrícula - Nome"
                            listaAlunosResultado.getItems().add(aluno.getMatricula() + " - " + aluno.getNome());
                        }
                    }

                } catch (SessaoNaoEncontradaException e) {
                    lblResultadoDisciplina.setText("Disciplina: -----");
                    lblResultadoMonitor.setText("Monitor: -----");
                    listaAlunosResultado.getItems().clear(); // Limpa a lista se der erro

                    Alert alertaErro = new Alert(Alert.AlertType.WARNING);
                    alertaErro.setTitle("Aviso");
                    alertaErro.setHeaderText(null);
                    alertaErro.setContentText(e.getMessage());
                    alertaErro.showAndWait();
                }
            });

            VBox formListagem = new VBox(10);
            formListagem.setPadding(new Insets(20));

            Label lblTituloLista = new Label("Todas as Monitorias Cadastradas:");

            ListView<String> listaDeSessoes = new ListView<>();
            listaDeSessoes.setPrefHeight(400);

            Button btnAtualizarLista = new Button("Atualizar Lista");

            btnAtualizarLista.setOnAction(evento -> {
                listaDeSessoes.getItems().clear();

                List<SessaoEstudo> todasSessoes = sistema.listarTodasSessoes();

                if (todasSessoes.isEmpty()) {
                    listaDeSessoes.getItems().add("Nenhuma monitoria cadastrada no sistema.");
                } else {
                    for (SessaoEstudo sessao : todasSessoes) {
                        String item = String.format("ID: %s | Disciplina: %s | Monitor: %s",
                                sessao.getId(),
                                sessao.getDisciplina(),
                                sessao.getNomeMonitor());
                        listaDeSessoes.getItems().add(item);
                    }
                }
            });

            formListagem.getChildren().addAll(lblTituloLista, listaDeSessoes, btnAtualizarLista);

            GridPane formGerenciamento = new GridPane();
            formGerenciamento.setPadding(new Insets(20));
            formGerenciamento.setHgap(10);
            formGerenciamento.setVgap(10);

            Label lblIdGerencia = new Label("ID da Sessão:");
            TextField txtIdGerencia = new TextField();
            Button btnBuscarGerencia = new Button("Buscar para Edição");

            Label lblEditaDisciplina = new Label("Disciplina:");
            TextField txtEditaDisciplina = new TextField();
            txtEditaDisciplina.setDisable(true);

            Label lblEditaMonitor = new Label("Nome do Monitor:");
            TextField txtEditaMonitor = new TextField();
            txtEditaMonitor.setDisable(true);

            Button btnAtualizar = new Button("Atualizar Sessão");
            btnAtualizar.setDisable(true);

            Button btnRemover = new Button("Remover Sessão");
            btnRemover.setStyle("-fx-text-fill: red;");
            btnRemover.setDisable(true);

            formGerenciamento.add(lblIdGerencia, 0, 0);
            formGerenciamento.add(txtIdGerencia, 1, 0);
            formGerenciamento.add(btnBuscarGerencia, 2, 0);

            formGerenciamento.add(new Separator(), 0, 1, 3, 1);

            formGerenciamento.add(lblEditaDisciplina, 0, 2);
            formGerenciamento.add(txtEditaDisciplina, 1, 2, 2, 1);

            formGerenciamento.add(lblEditaMonitor, 0, 3);
            formGerenciamento.add(txtEditaMonitor, 1, 3, 2, 1);

            formGerenciamento.add(btnAtualizar, 1, 4);
            formGerenciamento.add(btnRemover, 2, 4);

            btnBuscarGerencia.setOnAction(evento -> {
                try {
                    SessaoEstudo sessaoEncontrada = sistema.pesquisarSessaoPorId(txtIdGerencia.getText());

                    txtEditaDisciplina.setText(sessaoEncontrada.getDisciplina());
                    txtEditaMonitor.setText(sessaoEncontrada.getNomeMonitor());

                    txtEditaDisciplina.setDisable(false);
                    txtEditaMonitor.setDisable(false);
                    btnAtualizar.setDisable(false);
                    btnRemover.setDisable(false);
                    txtIdGerencia.setDisable(true);

                } catch (SessaoNaoEncontradaException e) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING, e.getMessage());
                    alerta.showAndWait();
                }
            });

            btnAtualizar.setOnAction(evento -> {
                try {
                    String id = txtIdGerencia.getText();
                    SessaoEstudo sessaoAtualizada = new SessaoEstudo(id, txtEditaDisciplina.getText(), txtEditaMonitor.getText());

                    sistema.atualizarSessao(id, sessaoAtualizada);

                    Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Sessão atualizada com sucesso!");
                    alerta.showAndWait();

                    txtIdGerencia.setDisable(false);
                    txtIdGerencia.clear();
                    txtEditaDisciplina.clear();
                    txtEditaDisciplina.setDisable(true);
                    txtEditaMonitor.clear();
                    txtEditaMonitor.setDisable(true);
                    btnAtualizar.setDisable(true);
                    btnRemover.setDisable(true);

                } catch (Exception e) {
                    Alert alerta = new Alert(Alert.AlertType.ERROR, "Erro ao atualizar: " + e.getMessage());
                    alerta.showAndWait();
                }
            });

            btnRemover.setOnAction(evento -> {
                try {
                    sistema.removerSessao(txtIdGerencia.getText());

                    Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Sessão removida do sistema!");
                    alerta.showAndWait();

                    txtIdGerencia.setDisable(false);
                    txtIdGerencia.clear();
                    txtEditaDisciplina.clear();
                    txtEditaDisciplina.setDisable(true);
                    txtEditaMonitor.clear();
                    txtEditaMonitor.setDisable(true);
                    btnAtualizar.setDisable(true);
                    btnRemover.setDisable(true);

                } catch (Exception e) {
                    Alert alerta = new Alert(Alert.AlertType.ERROR, "Erro ao remover: " + e.getMessage());
                    alerta.showAndWait();
                }
            });

            GridPane formMatricula = new GridPane();
            formMatricula.setPadding(new Insets(20));
            formMatricula.setHgap(10);
            formMatricula.setVgap(10);

            Label lblIdSessaoMatricula = new Label("ID da Sessão:");
            TextField txtIdSessaoMatricula = new TextField();

            Label lblMatriculaAluno = new Label("Matrícula do Aluno:");
            TextField txtMatriculaAluno = new TextField();

            Label lblNomeAluno = new Label("Nome do Aluno:");
            TextField txtNomeAluno = new TextField();

            Label lblCursoAluno = new Label("Curso:");
            TextField txtCursoAluno = new TextField();

            Button btnMatricular = new Button("Matricular Aluno");

            formMatricula.add(lblIdSessaoMatricula, 0, 0);
            formMatricula.add(txtIdSessaoMatricula, 1, 0);
            formMatricula.add(lblMatriculaAluno, 0, 1);
            formMatricula.add(txtMatriculaAluno, 1, 1);
            formMatricula.add(lblNomeAluno, 0, 2);
            formMatricula.add(txtNomeAluno, 1, 2);
            formMatricula.add(lblCursoAluno, 0, 3);
            formMatricula.add(txtCursoAluno, 1, 3);
            formMatricula.add(btnMatricular, 1, 4);

            btnMatricular.setOnAction(evento -> {
                try {
                    String idSessao = txtIdSessaoMatricula.getText();

                    // Instancia o aluno recebendo matrícula, nome e curso do formulário
                    Aluno novoAluno = new Aluno(
                            txtMatriculaAluno.getText(),
                            txtNomeAluno.getText(),
                            txtCursoAluno.getText()
                    );

                    // Envia para a regra de negócio
                    sistema.cadastrarAlunoEmSessao(idSessao, novoAluno);

                    Alert alertaSucesso = new Alert(Alert.AlertType.INFORMATION);
                    alertaSucesso.setTitle("Sucesso");
                    alertaSucesso.setHeaderText(null);
                    alertaSucesso.setContentText("Aluno matriculado com sucesso na sessão " + idSessao + "!");
                    alertaSucesso.showAndWait();

                    // Limpa os campos após o sucesso
                    txtIdSessaoMatricula.clear();
                    txtMatriculaAluno.clear();
                    txtNomeAluno.clear();
                    txtCursoAluno.clear();

                } catch (SessaoNaoEncontradaException e) {
                    Alert alertaErro = new Alert(Alert.AlertType.ERROR);
                    alertaErro.setTitle("Erro na Matrícula");
                    alertaErro.setHeaderText(null);
                    alertaErro.setContentText("Não foi possível matricular: " + e.getMessage());
                    alertaErro.showAndWait();
                } catch (SessaoJaExisteException e) {
                    throw new RuntimeException(e);
                }
            });

            TabPane painelDeAbas = new TabPane();

            Tab abaCadastro = new Tab("Cadastro", formCadastro);
            abaCadastro.setClosable(false);

            Tab abaPesquisa = new Tab("Pesquisa", formPesquisa);
            abaPesquisa.setClosable(false);

            Tab abaListagem = new Tab("Relatório", formListagem);
            abaListagem.setClosable(false);

            Tab abaGerenciamento = new Tab("Gerenciar", formGerenciamento);
            abaGerenciamento.setClosable(false);

            Tab abaMatricula = new Tab("Matrícula", formMatricula);
            abaMatricula.setClosable(false);

            painelDeAbas.getTabs().addAll(abaCadastro, abaPesquisa, abaListagem, abaGerenciamento, abaMatricula);


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
