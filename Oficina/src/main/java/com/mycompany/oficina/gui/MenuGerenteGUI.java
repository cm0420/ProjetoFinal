package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.controller.GerenteController;
import com.mycompany.oficina.gui.tela.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
        Button btnPonto = new Button("Registrar Ponto");
        Button btnSair = new Button("Logout");

        // --- Configuração das Ações dos Botões ---

        // Funções já implementadas
        btnClientes.setOnAction(e -> new ClienteManagerGUI().show());
        btnFuncionarios.setOnAction(e -> new FuncionarioManagerGUI().show());
        btnVeiculos.setOnAction(e -> new VeiculoManagerGUI().show());
        btnAgendamentos.setOnAction(e -> new ManagerAgendamentoGUI(new AtendenteController()).show());

        // ATUALIZADO: Funções do financeiro e ponto
        btnFinanceiro.setOnAction(e -> new ManagerFinanceiroGUI(new GerenteController()).show());
        btnEstoque.setOnAction(e -> new ManagerEstoqueGUI().show());
        btnPonto.setOnAction(e -> new ManagerPontoGUI(new GerenteController()).show());

        btnSair.setOnAction(e -> {
            primaryStage.close();
            // O ideal seria ter uma tela de login para voltar
            // Application.launch(LoginGUI.class);
        });

        vbox.getChildren().addAll(btnClientes, btnVeiculos, btnAgendamentos, btnFuncionarios, btnFinanceiro, btnEstoque, btnPonto, btnSair);
        vbox.getChildren().forEach(node -> {
            if (node instanceof Button) {
                ((Button) node).setMaxWidth(Double.MAX_VALUE);
            }
        });

        Scene scene = new Scene(vbox, 300, 420);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}