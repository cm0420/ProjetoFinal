package com.mycompany.oficina.gui.tela;

import com.mycompany.oficina.entidades.Carro;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class VeiculoDialog extends Dialog<Carro> {

    public VeiculoDialog(Carro carro) {
        setTitle(carro == null ? "Adicionar Veículo" : "Editar Veículo");
        setHeaderText("Preencha os dados do veículo.");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField fabricanteField = new TextField();
        TextField modeloField = new TextField();
        TextField placaField = new TextField();
        TextField chassiField = new TextField();

        grid.add(new Label("Fabricante:"), 0, 0);
        grid.add(fabricanteField, 1, 0);
        grid.add(new Label("Modelo:"), 0, 1);
        grid.add(modeloField, 1, 1);
        grid.add(new Label("Placa:"), 0, 2);
        grid.add(placaField, 1, 2);
        grid.add(new Label("Chassi:"), 0, 3);
        grid.add(chassiField, 1, 3);

        if (carro != null) {
            fabricanteField.setText(carro.getFabricante());
            modeloField.setText(carro.getModelo());
            placaField.setText(carro.getPlaca());
            chassiField.setText(carro.getChassi());
            chassiField.setEditable(false); // Chassi não pode ser editado
        }

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Apenas retorna um objeto Carro temporário para transferir os dados
                return new Carro("", "", fabricanteField.getText(), modeloField.getText(), placaField.getText(), chassiField.getText());
            }
            return null;
        });
    }
}