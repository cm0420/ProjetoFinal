package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.controller.MecanicoController;
import com.mycompany.oficina.sistemaponto.RegistroPonto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class ManagerPontoSwing extends JFrame {
    private final Object controller;
    private final JTable tableView;
    private final DefaultTableModel tableModel;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public ManagerPontoSwing(Object controller) {
        this.controller = controller;
        setTitle("Registro de Ponto");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Funcionário", "Entrada", "Saída"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableView = new JTable(tableModel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnEntrada = new JButton("Bater Ponto de Entrada");
        JButton btnSaida = new JButton("Bater Ponto de Saída");
        JButton btnAtualizar = new JButton("Atualizar");
        buttonPanel.add(btnEntrada);
        buttonPanel.add(btnSaida);
        buttonPanel.add(btnAtualizar);

        btnEntrada.addActionListener(e -> handleEntrada());
        btnSaida.addActionListener(e -> handleSaida());
        btnAtualizar.addActionListener(e -> carregarDados());

        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        List<RegistroPonto> registros = Collections.emptyList();
        if (controller instanceof AtendenteController) {
            registros = ((AtendenteController) controller).verRegistrosDeHoje();
        } else if (controller instanceof MecanicoController) {
            registros = ((MecanicoController) controller).verRegistrosDeHoje();
        }

        for (RegistroPonto r : registros) {
            String saidaFormatada = (r.getDataHoraSaida() != null) ? r.getDataHoraSaida().format(formatter) : "PONTO EM ABERTO";
            tableModel.addRow(new Object[]{
                    r.getFuncionario().getNome(),
                    r.getDataHoraEntrada().format(formatter),
                    saidaFormatada
            });
        }
    }

    private void handleEntrada() {
        RegistroPonto registro = null;
        if (controller instanceof AtendenteController) {
            registro = ((AtendenteController) controller).baterPontoEntrada();
        } else if (controller instanceof MecanicoController) {
            registro = ((MecanicoController) controller).baterPontoEntrada();
        }

        if (registro != null) {
            AlertsSwing.showAlert("Sucesso", "Ponto de entrada registrado!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            AlertsSwing.showAlert("Erro", "Não foi possível registrar a entrada. Você já pode ter um ponto em aberto.", JOptionPane.ERROR_MESSAGE);
        }
        carregarDados();
    }

    private void handleSaida() {
        RegistroPonto registro = null;
        if (controller instanceof AtendenteController) {
            registro = ((AtendenteController) controller).baterPontoSaida();
        } else if (controller instanceof MecanicoController) {
            registro = ((MecanicoController) controller).baterPontoSaida();
        }

        if (registro != null) {
            AlertsSwing.showAlert("Sucesso", "Ponto de saída registrado!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            AlertsSwing.showAlert("Erro", "Não foi possível registrar a saída. Não há ponto de entrada aberto.", JOptionPane.ERROR_MESSAGE);
        }
        carregarDados();
    }
}