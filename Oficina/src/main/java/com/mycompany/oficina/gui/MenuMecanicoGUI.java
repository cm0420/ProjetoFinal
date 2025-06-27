package com.mycompany.oficina.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuMecanicoGUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Menu do Mecânico");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(javafx.geometry.Pos.CENTER);

        Button btnAgendamentos = new Button("Ver Agendamentos e Iniciar OS");
        Button btnGerenciarOS = new Button("Gerenciar OS Existente");
        Button btnRegistrarPonto = new Button("Registrar Ponto");
        Button btnSair = new Button("Logout");

        btnAgendamentos.setOnAction(e -> Alerts.showAlert("Aviso", "Funcionalidade não implementada.", Alert.AlertType.INFORMATION));
        btnGerenciarOS.setOnAction(e -> Alerts.showAlert("Aviso", "Funcionalidade não implementada.", Alert.AlertType.INFORMATION));
        btnRegistrarPonto.setOnAction(e -> Alerts.showAlert("Aviso", "Funcionalidade não implementada.", Alert.AlertType.INFORMATION));

        btnSair.setOnAction(e -> primaryStage.close());

        vbox.getChildren().addAll(btnAgendamentos, btnGerenciarOS, btnRegistrarPonto, btnSair);
        vbox.getChildren().forEach(node -> ((Button)node).setMaxWidth(Double.MAX_VALUE));

        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}