package com.mycompany.oficina.gui.tela;

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.gui.LoginGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClienteManagerGUI extends Stage {

    private final AtendenteController controller;
    private TableView<Cliente> tableView;

    public ClienteManagerGUI() {
        this.controller = new AtendenteController();
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Gerenciador de Clientes");

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        tableView = new TableView<>();
        setupTableColumns();
        borderPane.setCenter(tableView);

        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(10, 0, 0, 0));
        Button btnAdicionar = new Button("Adicionar");
        Button btnEditar = new Button("Editar");
        Button btnRemover = new Button("Remover");

        btnAdicionar.setOnAction(e -> handleAdicionar());
        btnEditar.setOnAction(e -> handleEditar());
        btnRemover.setOnAction(e -> handleRemover());

        hbox.getChildren().addAll(btnAdicionar, btnEditar, btnRemover);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 700, 450);
        setScene(scene);

        carregarDados();
    }

    private void setupTableColumns() {
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("ID", "idCliente", 80));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Nome", "nome", 200));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("CPF", "cpf", 120));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Telefone", "telefone", 120));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Email", "email", 200));
    }

    private void carregarDados() {
        ObservableList<Cliente> observableList = FXCollections.observableArrayList(controller.listarClientes());
        tableView.setItems(observableList);
    }

    private void handleAdicionar() {
        new ClienteDialog(null).showAndWait().ifPresent(cliente -> {
            if (controller.cadastrarCliente(cliente.getNome(), cliente.getCpf(), cliente.getTelefone(), cliente.getEndereco(), cliente.getEmail())) {
                Alerts.showAlert("Sucesso", "Cliente cadastrado!", Alert.AlertType.INFORMATION);
                carregarDados();
            } else {
                Alerts.showAlert("Erro", "CPF já existe ou dados inválidos.", Alert.AlertType.ERROR);
            }
        });
    }

    private void handleEditar() {
        Cliente sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alerts.showAlert("Seleção Necessária", "Selecione um cliente para editar.", Alert.AlertType.WARNING);
            return;
        }

        new ClienteDialog(sel).showAndWait().ifPresent(editado -> {
            if (controller.editarCliente(sel.getCpf(), editado.getNome(), editado.getCpf(), editado.getTelefone(), editado.getEndereco(), editado.getEmail())) {
                Alerts.showAlert("Sucesso", "Cliente atualizado!", Alert.AlertType.INFORMATION);
                carregarDados();
            } else {
                Alerts.showAlert("Erro", "Não foi possível atualizar o cliente.", Alert.AlertType.ERROR);
            }
        });
    }

    private void handleRemover() {
        Cliente sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alerts.showAlert("Seleção Necessária", "Selecione um cliente para remover.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Remover " + sel.getNome() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (controller.removerCliente(sel.getCpf())) {
                    Alerts.showAlert("Sucesso", "Cliente removido.", Alert.AlertType.INFORMATION);
                    carregarDados();
                } else {
                    Alerts.showAlert("Erro", "Não foi possível remover. Verifique se o cliente possui veículos.", Alert.AlertType.ERROR);
                }
            }
        });
    }
}