package com.mycompany.oficina.gui;

import com.mycompany.oficina.entidades.Cliente;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class ClienteDialog extends Dialog<Cliente> {

    public ClienteDialog(Cliente cliente) {
        setTitle(cliente == null ? "Adicionar Cliente" : "Editar Cliente");
        setHeaderText("Preencha os dados do cliente.");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomeField = new TextField();
        TextField cpfField = new TextField();
        TextField telefoneField = new TextField();
        TextField enderecoField = new TextField();
        TextField emailField = new TextField();

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeField, 1, 0);
        grid.add(new Label("CPF:"), 0, 1);
        grid.add(cpfField, 1, 1);
        grid.add(new Label("Telefone:"), 0, 2);
        grid.add(telefoneField, 1, 2);
        grid.add(new Label("Endereço:"), 0, 3);
        grid.add(enderecoField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);

        if (cliente != null) {
            nomeField.setText(cliente.getNome());
            cpfField.setText(cliente.getCpf());
            telefoneField.setText(cliente.getTelefone());
            enderecoField.setText(cliente.getEndereco());
            emailField.setText(cliente.getEmail());
        }

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Cliente(nomeField.getText(), cpfField.getText(), telefoneField.getText(), enderecoField.getText(), emailField.getText());
            }
            return null;
        });
    }
}