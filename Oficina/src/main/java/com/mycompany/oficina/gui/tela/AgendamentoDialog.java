package com.mycompany.oficina.gui.tela;

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.agendamento.TipoServico;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Funcionario;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class AgendamentoDialog extends Dialog<Agendamento> {
    public AgendamentoDialog(AtendenteController controller) {
        setTitle("Novo Agendamento");
        setHeaderText("Preencha os dados para realizar o agendamento.");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // --- Componentes da Interface ---
        ComboBox<Cliente> clienteCombo = new ComboBox<>(FXCollections.observableArrayList(controller.listarClientes()));
        ComboBox<Carro> carroCombo = new ComboBox<>();
        ComboBox<Funcionario> mecanicoCombo = new ComboBox<>(FXCollections.observableArrayList(controller.listarMecanicosDisponiveis()));
        ComboBox<TipoServico> servicoCombo = new ComboBox<>(FXCollections.observableArrayList(TipoServico.values()));
        TextField dataHoraField = new TextField();
        dataHoraField.setPromptText("dd/MM/yyyy HH:mm");

        // --- Configurando a exibição dos nomes nos ComboBoxes ---
        clienteCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Cliente cliente) {
                return cliente == null ? "" : cliente.getNome();
            }
            @Override
            public Cliente fromString(String string) { return null; }
        });

        carroCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Carro carro) {
                return carro == null ? "" : carro.getModelo() + " (" + carro.getPlaca() + ")";
            }
            @Override
            public Carro fromString(String string) { return null; }
        });

        mecanicoCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Funcionario f) {
                return f == null ? "" : f.getNome();
            }
            @Override
            public Funcionario fromString(String string) { return null; }
        });


        // --- Lógica de Ação ---
        // Quando um cliente é selecionado, carrega os carros dele no ComboBox de carros.
        clienteCombo.setOnAction(e -> {
            Cliente selecionado = clienteCombo.getValue();
            if (selecionado != null) {
                carroCombo.setItems(FXCollections.observableArrayList(controller.listarVeiculosDoCliente(selecionado.getCpf())));
                carroCombo.setDisable(false);
            } else {
                carroCombo.getItems().clear();
                carroCombo.setDisable(true);
            }
        });

        // --- Layout dos Componentes ---
        grid.add(new Label("Cliente:"), 0, 0);
        grid.add(clienteCombo, 1, 0);
        grid.add(new Label("Veículo:"), 0, 1);
        grid.add(carroCombo, 1, 1);
        grid.add(new Label("Mecânico Responsável:"), 0, 2);
        grid.add(mecanicoCombo, 1, 2);
        grid.add(new Label("Tipo de Serviço:"), 0, 3);
        grid.add(servicoCombo, 1, 3);
        grid.add(new Label("Data e Hora:"), 0, 4);
        grid.add(dataHoraField, 1, 4);

        // Inicialmente, o ComboBox de carros fica desativado até um cliente ser escolhido
        carroCombo.setDisable(true);

        getDialogPane().setContent(grid);

        // --- Conversão do Resultado ---
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Validação de campos
                if (clienteCombo.getValue() == null || carroCombo.getValue() == null || mecanicoCombo.getValue() == null || servicoCombo.getValue() == null || dataHoraField.getText().isEmpty()) {
                    Alerts.showAlert("Campos Vazios", "Todos os campos são obrigatórios.", Alert.AlertType.WARNING);
                    return null;
                }

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    LocalDateTime dataHora = LocalDateTime.parse(dataHoraField.getText(), formatter);

                    // Cria um novo objeto Agendamento com os dados selecionados
                    return new Agendamento(
                            clienteCombo.getValue(),
                            carroCombo.getValue(),
                            mecanicoCombo.getValue(),
                            servicoCombo.getValue(),
                            null, // Elevador não é selecionado nesta interface
                            dataHora
                    );
                } catch (DateTimeParseException e) {
                    Alerts.showAlert("Erro de Formato", "O formato da data/hora está inválido. Use dd/MM/yyyy HH:mm.", Alert.AlertType.ERROR);
                    return null;
                }
            }
            return null; // Retorna nulo se o botão Cancelar for pressionado
        });
    }
}
