package com.mycompany.oficina.gui.tela;

import com.mycompany.oficina.gui.LoginGUI;
import javafx.stage.Stage;
import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.controller.MecanicoController;
import com.mycompany.oficina.sistemaponto.RegistroPonto;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;

public class ManagerPontoGUI extends Stage {
    private final Object controller;
    private TableView<RegistroPonto> tableView;

    public ManagerPontoGUI(Object controller) {
        this.controller = controller;
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Registro de Ponto");

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        tableView = new TableView<>();
        setupTableColumns();
        borderPane.setCenter(tableView);

        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(10, 0, 0, 0));
        Button btnEntrada = new Button("Bater Ponto de Entrada");
        Button btnSaida = new Button("Bater Ponto de Saída");
        Button btnAtualizar = new Button("Atualizar");

        btnEntrada.setOnAction(e -> handleEntrada());
        btnSaida.setOnAction(e -> handleSaida());
        btnAtualizar.setOnAction(e -> carregarDados());


        hbox.getChildren().addAll(btnEntrada, btnSaida, btnAtualizar);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 600, 400);
        setScene(scene);

        carregarDados();
    }

    private void setupTableColumns() {
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Funcionário", "funcionario", 200));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Entrada", "dataHoraEntrada", 180));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Saída", "dataHoraSaida", 180));
    }

    private void carregarDados() {
        List<RegistroPonto> registros = Collections.emptyList();
        if (controller instanceof AtendenteController) {
            registros = ((AtendenteController) controller).verRegistrosDeHoje();
        } else if (controller instanceof MecanicoController) {
            registros = ((MecanicoController) controller).verRegistrosDeHoje();
        }
        tableView.setItems(FXCollections.observableArrayList(registros));
    }

    private void handleEntrada() {
        RegistroPonto registro = null;
        if (controller instanceof AtendenteController) {
            registro = ((AtendenteController) controller).baterPontoEntrada();
        } else if (controller instanceof MecanicoController) {
            registro = ((MecanicoController) controller).baterPontoEntrada();
        }

        if (registro != null) {
            Alerts.showAlert("Sucesso", "Ponto de entrada registrado!", Alert.AlertType.INFORMATION);
        } else {
            Alerts.showAlert("Erro", "Não foi possível registrar a entrada. Você já pode ter um ponto em aberto.", Alert.AlertType.ERROR);
        }
        carregarDados();
    }

    private void handleSaida() {
        RegistroPonto registro = null;
        if (controller instanceof AtendenteController) {
            registro = ((AtendenteController) controller).baterPontoSaida();
        } else if (controller instanceof MecanicoController) {
            registro = ((MecanicoController) controller).baterPontoSaida();
        }

        if (registro != null) {
            Alerts.showAlert("Sucesso", "Ponto de saída registrado!", Alert.AlertType.INFORMATION);
        } else {
            Alerts.showAlert("Erro", "Não foi possível registrar a saída. Não há ponto de entrada aberto.", Alert.AlertType.ERROR);
        }
      carregarDados();}
}
