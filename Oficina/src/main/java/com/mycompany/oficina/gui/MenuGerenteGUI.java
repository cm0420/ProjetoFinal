package com.mycompany.oficina.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuGerenteGUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Menu do Gerente");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Button btnClientes = new Button("Gerenciar Clientes");
        Button btnVeiculos = new Button("Gerenciar Veículos");
        Button btnAgendamentos = new Button("Gerenciar Agendamentos");
        Button btnFuncionarios = new Button("Gerenciar Funcionários");
        Button btnFinanceiro = new Button("Módulo Financeiro");
        Button btnEstoque = new Button("Módulo de Estoque");
        Button btnBaterPonto = new Button("Gerenciar Ponto");
        Button btnSair = new Button("Logout");

        btnClientes.setOnAction(e -> new ClienteManagerGUI().show());
        btnFuncionarios.setOnAction(e -> new FuncionarioManagerGUI().show());

        // As funcionalidades restantes podem ser implementadas com o mesmo padrão
        btnVeiculos.setOnAction(e -> Alerts.showAlert("Aviso", "Funcionalidade não implementada.", Alert.AlertType.INFORMATION));
        btnAgendamentos.setOnAction(e -> Alerts.showAlert("Aviso", "Funcionalidade não implementada.", Alert.AlertType.INFORMATION));
        btnFinanceiro.setOnAction(e -> Alerts.showAlert("Aviso", "Funcionalidade não implementada.", Alert.AlertType.INFORMATION));
        btnEstoque.setOnAction(e -> Alerts.showAlert("Aviso", "Funcionalidade não implementada.", Alert.AlertType.INFORMATION));
        btnBaterPonto.setOnAction(e -> Alerts.showAlert("Aviso", "Funcionalidade não implementada.", Alert.AlertType.INFORMATION));

        btnSair.setOnAction(e -> primaryStage.close());

        vbox.getChildren().addAll(btnClientes, btnVeiculos, btnAgendamentos, btnFuncionarios, btnFinanceiro, btnEstoque, btnSair);
        vbox.getChildren().forEach(node -> ((Button)node).setMaxWidth(Double.MAX_VALUE));

        Scene scene = new Scene(vbox, 300, 380);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}