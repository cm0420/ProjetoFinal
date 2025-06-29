package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.GerenteController;
import com.mycompany.oficina.entidades.Funcionario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FuncionarioManagerSwing extends JFrame {

    private final GerenteController controller;
    private final JTable tableView;
    private final DefaultTableModel tableModel;

    public FuncionarioManagerSwing() {
        this.controller = new GerenteController();
        setTitle("Gerenciador de Funcionários");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Nome", "CPF", "Cargo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableView = new JTable(tableModel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);

        btnAdicionar.addActionListener(e -> handleAdicionar());
        btnEditar.addActionListener(e -> handleEditar());
        btnRemover.addActionListener(e -> handleRemover());

        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        for (Funcionario func : controller.listarFuncionarios()) {
            tableModel.addRow(new Object[]{
                    func.getIdUsuario(),
                    func.getNome(),
                    func.getCpf(),
                    func.getCargo()
            });
        }
    }

    private void handleAdicionar() {
        FuncionarioDialogo dialog = new FuncionarioDialogo(this, null);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            Funcionario f = dialog.getFuncionario();
            if (controller.cadastrarFuncionario(f.getSenha(), f.getCargo(), f.getNome(), f.getCpf(), f.getTelefone(), f.getEndereco(), f.getEmail())) {
                AlertsSwing.showAlert("Sucesso", "Funcionário cadastrado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível cadastrar. Verifique se o CPF já existe.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEditar() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um funcionário para editar.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cpfAntigo = (String) tableModel.getValueAt(selectedRow, 2);
        Funcionario funcSelecionado = controller.buscarFuncionario(cpfAntigo);

        FuncionarioDialogo dialog = new FuncionarioDialogo(this, funcSelecionado);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            Funcionario editado = dialog.getFuncionario();
            if (controller.editarFuncionario(cpfAntigo, editado.getCpf(), editado.getSenha(), editado.getCargo(), editado.getNome(), editado.getTelefone(), editado.getEndereco(), editado.getEmail())) {
                AlertsSwing.showAlert("Sucesso", "Funcionário atualizado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível atualizar o funcionário.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemover() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um funcionário para remover.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = (String) tableModel.getValueAt(selectedRow, 1);
        String cpf = (String) tableModel.getValueAt(selectedRow, 2);

        int confirm = JOptionPane.showConfirmDialog(this, "Remover " + nome + "?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.removerFuncionario(cpf)) {
                AlertsSwing.showAlert("Sucesso", "Funcionário removido.", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível remover o funcionário.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}