package com.mycompany.oficina.gui.tela;

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.entidades.Carro;
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

import java.util.List;
import java.util.Optional;

public class VeiculoManagerGUI extends Stage {

    private final AtendenteController controller;
    private TableView<Carro> tableView;

    public VeiculoManagerGUI() {
        this.controller = new AtendenteController();
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Gerenciador de Veículos");

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

        Scene scene = new Scene(borderPane, 750, 500);
        setScene(scene);

        carregarDados();
    }

    private void setupTableColumns() {
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Chassi", "chassi", 150));
        tableView.getColumns().add(TableViewFactory.createColumn("Fabricante", "fabricante", 120));
        tableView.getColumns().add(TableViewFactory.createColumn("Modelo", "modelo", 120));
        tableView.getColumns().add(TableViewFactory.createColumn("Placa", "placa", 100));
        tableView.getColumns().add(TableViewFactory.createColumn("Proprietário", "nomeDono", 200));
    }

    private void carregarDados() {
        ObservableList<Carro> observableList = FXCollections.observableArrayList(controller.listarVeiculos());
        tableView.setItems(observableList);
    }

    private void handleAdicionar() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Buscar Cliente");
        dialog.setHeaderText("Para adicionar um veículo, primeiro informe o CPF do proprietário.");
        dialog.setContentText("CPF do Cliente:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(cpf -> {
            Cliente proprietario = controller.buscarCliente(cpf);
            if (proprietario == null) {
                Alerts.showAlert("Erro", "Cliente não encontrado. Cadastre o cliente primeiro.", Alert.AlertType.ERROR);
                return;
            }

            new VeiculoDialog(null).showAndWait().ifPresent(carro -> {
                if (controller.cadastrarVeiculo(proprietario.getCpf(), carro.getFabricante(), carro.getModelo(), carro.getPlaca(), carro.getChassi())) {
                    Alerts.showAlert("Sucesso", "Veículo cadastrado!", Alert.AlertType.INFORMATION);
                    carregarDados();
                } else {
                    Alerts.showAlert("Erro", "Chassi já existe ou dados inválidos.", Alert.AlertType.ERROR);
                }
            });
        });
    }

    private void handleEditar() {
        Carro sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alerts.showAlert("Seleção Necessária", "Selecione um veículo para editar.", Alert.AlertType.WARNING);
            return;
        }

        new VeiculoDialog(sel).showAndWait().ifPresent(editado -> {
            if (controller.editarVeiculo(sel.getChassi(), editado.getFabricante(), editado.getModelo(), editado.getPlaca())) {
                Alerts.showAlert("Sucesso", "Veículo atualizado!", Alert.AlertType.INFORMATION);
                carregarDados();
            } else {
                Alerts.showAlert("Erro", "Não foi possível atualizar o veículo.", Alert.AlertType.ERROR);
            }
        });
    }

    private void handleRemover() {
        // A lógica de remoção de veículos deve ser feita com cuidado,
        // verificando se existem agendamentos ou OS ativas.
        // Como a GUI não tem acesso direto a essa lógica, a remoção é omitida
        // para evitar inconsistências, conforme sugerido pela estrutura do seu projeto.
        Alerts.showAlert("Aviso", "Funcionalidade de remoção deve ser implementada com validações no backend.", Alert.AlertType.INFORMATION);
    }
}