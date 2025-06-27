package com.mycompany.oficina.gui.tela;

import com.mycompany.oficina.controller.GerenteController;
import com.mycompany.oficina.entidades.Funcionario;
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

public class FuncionarioManagerGUI extends Stage {
    private final GerenteController controller;
    private TableView<Funcionario> tableView;

    public FuncionarioManagerGUI() {
        this.controller = new GerenteController();
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Gerenciador de Funcionários");

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

        Scene scene = new Scene(borderPane, 600, 400);
        setScene(scene);

        carregarDados();
    }

    private void setupTableColumns() {
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("ID", "idUsuario", 80));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Nome", "nome", 200));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("CPF", "cpf", 120));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Cargo", "cargo", 100));
    }

    private void carregarDados() {
        ObservableList<Funcionario> observableList = FXCollections.observableArrayList(controller.listarFuncionarios());
        tableView.setItems(observableList);
    }

    private void handleAdicionar() {
        new FuncionarioDialog(null).showAndWait().ifPresent(f -> {
            if (controller.cadastrarFuncionario(f.getSenha(), f.getCargo(), f.getNome(), f.getCpf(), f.getTelefone(), f.getEndereco(), f.getEmail())) {
                Alerts.showAlert("Sucesso", "Funcionário cadastrado!", Alert.AlertType.INFORMATION);
                carregarDados();
            } else {
                Alerts.showAlert("Erro", "Não foi possível cadastrar. Verifique se o CPF já existe.", Alert.AlertType.ERROR);
            }
        });
    }

    private void handleEditar() {
        Funcionario sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alerts.showAlert("Seleção Necessária", "Selecione um funcionário para editar.", Alert.AlertType.WARNING);
            return;
        }

        new FuncionarioDialog(sel).showAndWait().ifPresent(editado -> {
            if (controller.editarFuncionario(sel.getCpf(), editado.getCpf(), editado.getSenha(), editado.getCargo(), editado.getNome(), editado.getTelefone(), editado.getEndereco(), editado.getEmail())) {
                Alerts.showAlert("Sucesso", "Funcionário atualizado!", Alert.AlertType.INFORMATION);
                carregarDados();
            } else {
                Alerts.showAlert("Erro", "Não foi possível atualizar o funcionário.", Alert.AlertType.ERROR);
            }
        });
    }

    private void handleRemover() {
        Funcionario sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alerts.showAlert("Seleção Necessária", "Selecione um funcionário para remover.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Remover " + sel.getNome() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (controller.removerFuncionario(sel.getCpf())) {
                    Alerts.showAlert("Sucesso", "Funcionário removido.", Alert.AlertType.INFORMATION);
                    carregarDados();
                } else {
                    Alerts.showAlert("Erro", "Não foi possível remover o funcionário.", Alert.AlertType.ERROR);
                }
            }
        });
    }
}