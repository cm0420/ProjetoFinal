package com.mycompany.oficina.gui;

import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.strategy.*;
import javax.swing.*;
import java.awt.*;

public class ClienteDialogo extends JDialog {
    private final JTextField nomeField = new JTextField(20);
    private final JTextField cpfField = new JTextField(20);
    private final JTextField telefoneField = new JTextField(20);
    private final JTextField enderecoField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);

    private Cliente cliente;
    private boolean confirmado;

    public ClienteDialogo(Frame owner, Cliente cliente) {
        super(owner, cliente == null ? "Adicionar Cliente" : "Editar Cliente", true);
        this.cliente = cliente;

        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(nomeField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(cpfField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(telefoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(enderecoField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(emailField, gbc);

        if (cliente != null) {
            nomeField.setText(cliente.getNome());
            cpfField.setText(cliente.getCpf());
            telefoneField.setText(cliente.getTelefone());
            enderecoField.setText(cliente.getEndereco());
            emailField.setText(cliente.getEmail());
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
        if (!new CpfValidate().validar(cpfField.getText()) ||
                !new TelefoneValidate().validar(telefoneField.getText()) ||
                !new EmailValidate().validar(emailField.getText())) {
            AlertsSwing.showAlert("Erro de Validação", "Verifique os dados inseridos.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (this.cliente == null) {
            this.cliente = new Cliente(nomeField.getText(), cpfField.getText(), telefoneField.getText(), enderecoField.getText(), emailField.getText());
        } else {
            this.cliente.setNome(nomeField.getText());
            this.cliente.setCpf(cpfField.getText());
            this.cliente.setTelefone(telefoneField.getText());
            this.cliente.setEndereco(enderecoField.getText());
            this.cliente.setEmail(emailField.getText());
        }
        confirmado = true;
        dispose();
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}