package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.gui.AlertsSwing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VeiculoManagerSwing extends JFrame {

    private final AtendenteController controller;
    private final JTable tableView;
    private final DefaultTableModel tableModel;

    public VeiculoManagerSwing() {
        this.controller = new AtendenteController();
        setTitle("Gerenciador de Veículos");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Chassi", "Fabricante", "Modelo", "Placa", "Proprietário"};
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
        for (Carro carro : controller.listarVeiculos()) {
            tableModel.addRow(new Object[]{
                    carro.getChassi(),
                    carro.getFabricante(),
                    carro.getModelo(),
                    carro.getPlaca(),
                    carro.getNomeDono()
            });
        }
    }

    private void handleAdicionar() {
        String cpf = JOptionPane.showInputDialog(this, "Para adicionar um veículo, informe o CPF do proprietário:", "Buscar Cliente", JOptionPane.QUESTION_MESSAGE);

        if (cpf == null || cpf.isBlank()) return;

        Cliente proprietario = controller.buscarCliente(cpf);
        if (proprietario == null) {
            AlertsSwing.showAlert("Erro", "Cliente não encontrado. Cadastre o cliente primeiro.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        VeiculoDialogo dialog = new VeiculoDialogo(this, null);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            Carro carro = dialog.getCarro();
            if (controller.cadastrarVeiculo(proprietario.getCpf(), carro.getFabricante(), carro.getModelo(), carro.getPlaca(), carro.getChassi())) {
                AlertsSwing.showAlert("Sucesso", "Veículo cadastrado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                AlertsSwing.showAlert("Erro", "Chassi já existe ou dados inválidos.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEditar() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um veículo para editar.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String chassi = (String) tableModel.getValueAt(selectedRow, 0);
        Carro carroSelecionado = controller.buscarVeiculo(chassi);

        VeiculoDialogo dialog = new VeiculoDialogo(this, carroSelecionado);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            Carro editado = dialog.getCarro();
            if (controller.editarVeiculo(chassi, editado.getFabricante(), editado.getModelo(), editado.getPlaca())) {
                AlertsSwing.showAlert("Sucesso", "Veículo atualizado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível atualizar o veículo.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRemover() {
        AlertsSwing.showAlert("Aviso", "Funcionalidade de remoção deve ser implementada com validações no backend.", JOptionPane.INFORMATION_MESSAGE);
    }
}