package com.mycompany.oficina.gui; // Ou gui.swing

import com.mycompany.oficina.entidades.Carro;
import javax.swing.*;
import java.awt.*;

public class VeiculoDialogo extends JDialog {
    private final JTextField fabricanteField = new JTextField(20);
    private final JTextField modeloField = new JTextField(20);
    private final JTextField placaField = new JTextField(20);
    private final JTextField chassiField = new JTextField(20);

    private Carro carro;
    private boolean confirmado;

    public VeiculoDialogo(Frame owner, Carro carro) {
        super(owner, carro == null ? "Adicionar Veículo" : "Editar Veículo", true);
        this.carro = carro;

        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGbc();

        addField(panel, gbc, 0, "Fabricante:", fabricanteField);
        addField(panel, gbc, 1, "Modelo:", modeloField);
        addField(panel, gbc, 2, "Placa:", placaField);
        addField(panel, gbc, 3, "Chassi:", chassiField);

        if (carro != null) {
            fabricanteField.setText(carro.getFabricante());
            modeloField.setText(carro.getModelo());
            placaField.setText(carro.getPlaca());
            chassiField.setText(carro.getChassi());
            chassiField.setEditable(false); // Chassi não pode ser editado
        }

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> onSave());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnSalvar);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private void onSave() {
        if (fabricanteField.getText().isBlank() || modeloField.getText().isBlank() ||
                placaField.getText().isBlank() || chassiField.getText().isBlank()) {
            AlertsSwing.showAlert("Erro de Validação", "Todos os campos do veículo são obrigatórios.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Retorna um objeto Carro temporário para transferir os dados
        this.carro = new Carro("", "", fabricanteField.getText(), modeloField.getText(), placaField.getText(), chassiField.getText());
        this.confirmado = true;
        dispose();
    }

    public Carro getCarro() {
        return carro;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent component) {
        gbc.gridx = 0; gbc.gridy = y; panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridy = y; panel.add(component, gbc);
    }

    private GridBagConstraints createGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }
}