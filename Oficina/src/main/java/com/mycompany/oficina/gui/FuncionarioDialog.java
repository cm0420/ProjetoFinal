package com.mycompany.oficina.gui;

import com.mycompany.oficina.entidades.Funcionario;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class FuncionarioDialog extends Dialog<Funcionario> {

    public FuncionarioDialog(Funcionario func) {
        setTitle(func == null ? "Adicionar Funcionário" : "Editar Funcionário");
        setHeaderText("Preencha os dados do funcionário.");

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
        PasswordField senhaField = new PasswordField();
        ComboBox<String> cargoComboBox = new ComboBox<>(FXCollections.observableArrayList("Admin", "Atendente", "Mecanico"));

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
        grid.add(new Label("Cargo:"), 0, 5);
        grid.add(cargoComboBox, 1, 5);
        grid.add(new Label("Senha:"), 0, 6);
        grid.add(senhaField, 1, 6);

        if (func != null) {
            nomeField.setText(func.getNome());
            cpfField.setText(func.getCpf());
            telefoneField.setText(func.getTelefone());
            enderecoField.setText(func.getEndereco());
            emailField.setText(func.getEmail());
            cargoComboBox.setValue(func.getCargo());
            senhaField.setPromptText("Deixe em branco para não alterar");
        }

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Funcionario(senhaField.getText(), cargoComboBox.getValue(), nomeField.getText(), cpfField.getText(), telefoneField.getText(), enderecoField.getText(), emailField.getText());
            }
            return null;
        });
    }
}