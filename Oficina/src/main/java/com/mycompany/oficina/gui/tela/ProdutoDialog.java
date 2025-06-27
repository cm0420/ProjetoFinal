package com.mycompany.oficina.gui.tela;
import com.mycompany.oficina.loja.Produto;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ProdutoDialog extends Dialog<Produto> {
    public ProdutoDialog(Produto produto) {
        setTitle(produto == null ? "Adicionar Peça" : "Editar Peça");
        setHeaderText("Preencha os dados da peça/produto.");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomeField = new TextField();
        TextField precoField = new TextField();
        TextField quantidadeField = new TextField();
        TextField fornecedorField = new TextField();

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeField, 1, 0);
        grid.add(new Label("Preço de Venda:"), 0, 1);
        grid.add(precoField, 1, 1);
        grid.add(new Label("Quantidade:"), 0, 2);
        grid.add(quantidadeField, 1, 2);
        grid.add(new Label("Fornecedor:"), 0, 3);
        grid.add(fornecedorField, 1, 3);

        if (produto != null) {
            nomeField.setText(produto.getNome());
            precoField.setText(String.valueOf(produto.getPreco()));
            quantidadeField.setText(String.valueOf(produto.getQuantidade()));
            fornecedorField.setText(produto.getFornecedor());
            if (produto.getIdProduto() != null) { // Se estiver editando
                quantidadeField.setEditable(false); // Quantidade só muda via reposição
            }
        }

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String nome = nomeField.getText();
                    double preco = Double.parseDouble(precoField.getText());
                    int quantidade = Integer.parseInt(quantidadeField.getText());
                    String fornecedor = fornecedorField.getText();
                    return new Produto(nome, preco, quantidade, fornecedor);
                } catch (NumberFormatException e) {
                    Alerts.showAlert("Erro de Formato", "Preço e quantidade devem ser números válidos.", Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });
    }
}
