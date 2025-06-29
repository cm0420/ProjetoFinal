package com.mycompany.oficina.gui; // Ou gui.swing

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.entidades.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClienteManagerSwing extends JFrame {

    private final AtendenteController controller;
    private final JTable tableView;
    private final DefaultTableModel tableModel;

    public ClienteManagerSwing() {
        this.controller = new AtendenteController();
        setTitle("Gerenciador de Clientes");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Modelo da Tabela
        String[] columnNames = {"ID", "Nome", "CPF", "Telefone", "Email"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Torna as células não editáveis
            }
        };
        tableView = new JTable(tableModel);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);

        // Ações dos botões
        btnAdicionar.addActionListener(e -> handleAdicionar());
        btnEditar.addActionListener(e -> handleEditar());
        btnRemover.addActionListener(e -> handleRemover());

        // Layout
        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        carregarDados();
    }

    private void carregarDados() {
        // Limpa a tabela antes de carregar
        tableModel.setRowCount(0);
        for (Cliente cliente : controller.listarClientes()) {
            tableModel.addRow(new Object[]{
                    cliente.getIdCliente(),
                    cliente.getNome(),
                    cliente.getCpf(),
                    cliente.getTelefone(),
                    cliente.getEmail()
            });
        }
    }

    private void handleAdicionar() {
        ClienteDialogo dialog = new ClienteDialogo(this, null);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            Cliente novoCliente = dialog.getCliente();
            if (controller.cadastrarCliente(novoCliente.getNome(), novoCliente.getCpf(), novoCliente.getTelefone(), novoCliente.getEndereco(), novoCliente.getEmail())) {
                AlertsSwing.showAlert("Sucesso", "Cliente cadastrado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                AlertsSwing.showAlert("Erro", "CPF já existe ou dados inválidos.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEditar() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um cliente para editar.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cpfAntigo = (String) tableModel.getValueAt(selectedRow, 2);
        Cliente clienteSelecionado = controller.buscarCliente(cpfAntigo);

        if (clienteSelecionado == null) {
            AlertsSwing.showAlert("Erro", "Não foi possível encontrar o cliente selecionado.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClienteDialogo dialog = new ClienteDialogo(this, clienteSelecionado);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            Cliente clienteEditado = dialog.getCliente();
            if (controller.editarCliente(cpfAntigo, clienteEditado.getNome(), clienteEditado.getCpf(), clienteEditado.getTelefone(), clienteEditado.getEndereco(), clienteEditado.getEmail())) {
                AlertsSwing.showAlert("Sucesso", "Cliente atualizado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível atualizar o cliente.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemover() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um cliente para remover.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = (String) tableModel.getValueAt(selectedRow, 1);
        String cpf = (String) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this, "Remover " + nome + "?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.removerCliente(cpf)) {
                AlertsSwing.showAlert("Sucesso", "Cliente removido.", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível remover. Verifique se o cliente possui veículos.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}