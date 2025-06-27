package com.mycompany.oficina.gui.tela;

import com.mycompany.oficina.controller.GerenteController;
import com.mycompany.oficina.gui.LoginGUI;
import com.mycompany.oficina.loja.Produto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

public class ManagerEstoqueGUI extends Stage {

    private final GerenteController controller;
    private TableView<Produto> tableView;

    public ManagerEstoqueGUI() {
        this.controller = new GerenteController();
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Módulo de Estoque");

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        tableView = new TableView<>();
        setupTableColumns();
        borderPane.setCenter(tableView);

        HBox hbox = new HBox(10);
        hbox.setPadding(new Insets(10, 0, 0, 0));
        Button btnCadastrarPeca = new Button("Cadastrar Nova Peça");
        Button btnReporEstoque = new Button("Repor Estoque");

        btnCadastrarPeca.setOnAction(e -> handleCadastrarPeca());
        btnReporEstoque.setOnAction(e -> handleReporEstoque());

        hbox.getChildren().addAll(btnCadastrarPeca, btnReporEstoque);
        borderPane.setBottom(hbox);

        Scene scene = new Scene(borderPane, 600, 400);
        setScene(scene);

        carregarDados();
    }

    private void setupTableColumns() {
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("ID", "idProduto", 80));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Nome", "nome", 200));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Preço Venda", "preco", 100));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Qtd.", "quantidade", 80));
        tableView.getColumns().add(LoginGUI.TableViewFactory.createColumn("Fornecedor", "fornecedor", 120));
    }

    private void carregarDados() {
        ObservableList<Produto> observableList = FXCollections.observableArrayList(controller.listarProdutos());
        tableView.setItems(observableList);
    }

    private void handleCadastrarPeca() {
        new ProdutoDialog(null).showAndWait().ifPresent(produto -> {
            if (controller.cadastrarNovaPeca(produto.getNome(), produto.getPreco(), produto.getQuantidade(), produto.getFornecedor())) {
                Alerts.showAlert("Sucesso", "Nova peça cadastrada e despesa registrada!", Alert.AlertType.INFORMATION);
                carregarDados();
            } else {
                Alerts.showAlert("Erro", "Não foi possível cadastrar a peça.", Alert.AlertType.ERROR);
            }
        });
    }

    private void handleReporEstoque() {
        Produto sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alerts.showAlert("Seleção Necessária", "Selecione um produto para repor o estoque.", Alert.AlertType.WARNING);
            return;
        }

        TextInputDialog qtdDialog = new TextInputDialog("10");
        qtdDialog.setTitle("Repor Estoque");
        qtdDialog.setHeaderText("Repor estoque para: " + sel.getNome());
        qtdDialog.setContentText("Quantidade a adicionar:");

        Optional<String> result = qtdDialog.showAndWait();
        result.ifPresent(qtdStr -> {
            try {
                int quantidade = Integer.parseInt(qtdStr);
                if (controller.reporEstoque(sel, quantidade)) {
                    Alerts.showAlert("Sucesso", "Estoque atualizado!", Alert.AlertType.INFORMATION);
                    carregarDados();
                } else {
                    Alerts.showAlert("Erro", "Não foi possível repor o estoque.", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException ex) {
                Alerts.showAlert("Erro de Formato", "Por favor, insira um número válido.", Alert.AlertType.ERROR);
            }
        });
    }
}