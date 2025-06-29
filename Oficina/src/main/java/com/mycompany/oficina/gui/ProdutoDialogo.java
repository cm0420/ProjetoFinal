package com.mycompany.oficina.gui;

import com.mycompany.oficina.loja.Produto;
import javax.swing.*;
import java.awt.*;

public class ProdutoDialogo extends JDialog {
    private final JTextField nomeField = new JTextField(20);
    private final JTextField precoField = new JTextField(10);
    private final JTextField quantidadeField = new JTextField(10);
    private final JTextField fornecedorField = new JTextField(20);

    private Produto produto;
    private boolean confirmado;

    public ProdutoDialogo(Frame owner, Produto produto) {
        super(owner, produto == null ? "Adicionar Peça" : "Editar Peça", true);
        this.produto = produto;

        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(nomeField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Preço de Venda:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(precoField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(quantidadeField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Fornecedor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(fornecedorField, gbc);

        if (produto != null) {
            nomeField.setText(produto.getNome());
            precoField.setText(String.valueOf(produto.getPreco()));
            quantidadeField.setText(String.valueOf(produto.getQuantidade()));
            fornecedorField.setText(produto.getFornecedor());
            if (produto.getIdProduto() != null) {
                quantidadeField.setEditable(false);
            }
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
        if (nomeField.getText().isBlank() || precoField.getText().isBlank() || quantidadeField.getText().isBlank() || fornecedorField.getText().isBlank()) {
            AlertsSwing.showAlert("Erro de Validação", "Todos os campos do produto são obrigatórios.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String nome = nomeField.getText();
            double preco = Double.parseDouble(precoField.getText());
            int quantidade = Integer.parseInt(quantidadeField.getText());

            if (preco <= 0 || quantidade < 0) {
                AlertsSwing.showAlert("Erro de Validação", "Preço e quantidade devem ser valores positivos.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            this.produto = new Produto(nome, preco, quantidade, fornecedorField.getText());
            this.confirmado = true;
            dispose();
        } catch (NumberFormatException e) {
            AlertsSwing.showAlert("Erro de Formato", "Preço e quantidade devem ser números válidos.", JOptionPane.ERROR_MESSAGE);
        }
    }

    public Produto getProduto() {
        return produto;
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}