package com.mycompany.oficina.gui.tela;
import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.gui.LoginGUI;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
public class ManagerAgendamentoGUI extends Stage {
    private final AtendenteController controller;
    private TableView<Agendamento> tableView;

    public ManagerAgendamentoGUI(AtendenteController controller) {
        this.controller = new AtendenteController();
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Gerenciador de Agendamentos");

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        tableView = new TableView<>();
        setupTableColumns();
        borderPane.setCenter(tableView);

        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(10, 0, 0, 0));
        Button btnNovo = new Button("Novo Agendamento");
        Button btnCancelar = new Button("Cancelar Agendamento");
        Button btnAtualizar = new Button("Atualizar Lista");

        btnNovo.setOnAction(e -> handleNovoAgendamento());
        btnCancelar.setOnAction(e -> handleCancelar());
        btnAtualizar.setOnAction(e -> carregarDados());

        hbox.getChildren().addAll(btnNovo, btnCancelar, btnAtualizar);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 800, 500);
        setScene(scene);

        carregarDados();
    }

    private void setupTableColumns() {
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Data/Hora", "dataHora", 150));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Cliente", "cliente", 200));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Veículo", "carro", 150));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Mecânico", "mecanico", 150));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Serviço", "tipoServico", 120));
    }

    private void carregarDados() {
        // Agora busca os dados e atualiza a tabela.
        // É necessário que o método listarTodosAgendamentos() exista no seu controller.
        tableView.setItems(FXCollections.observableArrayList(controller.listarTodosAgendamentos()));
    }

    private void handleNovoAgendamento() {
        new AgendamentoDialog(controller).showAndWait().ifPresent(agendamento -> {
            // Verifica se todos os campos necessários não são nulos antes de prosseguir
            if (agendamento != null && agendamento.getCliente() != null && agendamento.getCarro() != null && agendamento.getMecanico() != null && agendamento.getTipoServico() != null) {
                if (controller.criarAgendamento(
                        agendamento.getCliente(),
                        agendamento.getCarro(),
                        agendamento.getMecanico(),
                        agendamento.getTipoServico(),
                        agendamento.getDataHora())) {

                    Alerts.showAlert("Sucesso", "Agendamento criado!", Alert.AlertType.INFORMATION);
                    carregarDados(); // Atualiza a tabela com o novo agendamento
                } else {
                    Alerts.showAlert("Erro", "Não foi possível criar o agendamento. Verifique os dados e a disponibilidade.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void handleCancelar() {
        Agendamento sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alerts.showAlert("Seleção Necessária", "Selecione um agendamento para cancelar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Cancelar agendamento para " + sel.getCliente().getNome() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (controller.cancelarAgendamento(sel)) {
                    Alerts.showAlert("Sucesso", "Agendamento cancelado.", Alert.AlertType.INFORMATION);
                    carregarDados(); // Atualiza a tabela para remover o agendamento
                } else {
                    Alerts.showAlert("Erro", "Não foi possível remover. Taxa de cancelamento pode ter sido aplicada.", Alert.AlertType.ERROR);
                }
            }
        });
    }


}
