package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.GerenteController;
import com.mycompany.oficina.loja.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManagerEstoqueSwing extends JFrame {

    private final GerenteController controller;
    private final JTable tableView;
    private final DefaultTableModel tableModel;

    public ManagerEstoqueSwing() {
        this.controller = new GerenteController();
        setTitle("Módulo de Estoque");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Nome", "Preço Venda", "Qtd.", "Fornecedor"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableView = new JTable(tableModel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnCadastrarPeca = new JButton("Cadastrar Nova Peça");
        JButton btnReporEstoque = new JButton("Repor Estoque");
        buttonPanel.add(btnCadastrarPeca);
        buttonPanel.add(btnReporEstoque);

        btnCadastrarPeca.addActionListener(e -> handleCadastrarPeca());
        btnReporEstoque.addActionListener(e -> handleReporEstoque());

        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        for (Produto produto : controller.listarProdutos()) {
            tableModel.addRow(new Object[]{
                    produto.getIdProduto(),
                    produto.getNome(),
                    String.format("%.2f", produto.getPreco()),
                    produto.getQuantidade(),
                    produto.getFornecedor()
            });
        }
    }

    private void handleCadastrarPeca() {
        ProdutoDialogo dialog = new ProdutoDialogo(this, null);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            Produto p = dialog.getProduto();
            if (controller.cadastrarNovaPeca(p.getNome(), p.getPreco(), p.getQuantidade(), p.getFornecedor())) {
                AlertsSwing.showAlert("Sucesso", "Nova peça cadastrada e despesa registrada!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível cadastrar a peça.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleReporEstoque() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um produto para repor o estoque.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idProduto = (String) tableModel.getValueAt(selectedRow, 0);
        Produto sel = controller.buscarProduto(idProduto);

        String qtdStr = JOptionPane.showInputDialog(this, "Quantidade a adicionar:", "Repor Estoque para: " + sel.getNome(), JOptionPane.QUESTION_MESSAGE);

        if (qtdStr != null) {
            try {
                int quantidade = Integer.parseInt(qtdStr);
                if (controller.reporEstoque(sel, quantidade)) {
                    AlertsSwing.showAlert("Sucesso", "Estoque atualizado!", JOptionPane.INFORMATION_MESSAGE);
                    carregarDados();
                } else {
                    AlertsSwing.showAlert("Erro", "Não foi possível repor o estoque.", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                AlertsSwing.showAlert("Erro de Formato", "Por favor, insira um número válido.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}