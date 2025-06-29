package com.mycompany.oficina.gui;

import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.controller.MecanicoController;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ManagerIniciarOsSwing extends JFrame {
    private final MecanicoController controller;
    private final JTable tableView;
    private final DefaultTableModel tableModel;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ManagerIniciarOsSwing() {
        this.controller = new MecanicoController();
        setTitle("Agendamentos do Dia para Iniciar OS");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Data/Hora", "Cliente", "Veículo", "Serviço"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableView = new JTable(tableModel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnIniciarOS = new JButton("Iniciar OS do Agendamento Selecionado");
        JButton btnAtualizar = new JButton("Atualizar Lista");
        buttonPanel.add(btnIniciarOS);
        buttonPanel.add(btnAtualizar);

        btnIniciarOS.addActionListener(e -> handleIniciarOS());
        btnAtualizar.addActionListener(e -> carregarDados());

        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        for (Agendamento ag : controller.listarAgendamentosDeHoje()) {
            tableModel.addRow(new Object[]{
                    ag.getDataHora().format(formatter),
                    ag.getCliente().getNome(),
                    ag.getCarro().getModelo() + " (" + ag.getCarro().getPlaca() + ")",
                    ag.getTipoServico().name()
            });
        }
    }

    private void handleIniciarOS() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um agendamento para iniciar a OS.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Agendamento agendamentoSel = controller.listarAgendamentosDeHoje().get(selectedRow);

        String defeito = JOptionPane.showInputDialog(this, "Iniciando OS para: " + agendamentoSel.getCliente().getNome() + "\n\nPor favor, digite o defeito relatado:", "Iniciar Ordem de Serviço", JOptionPane.QUESTION_MESSAGE);

        if (defeito != null && !defeito.isBlank()) {
            OrdemDeServico novaOS = controller.abrirOS(agendamentoSel, defeito);
            if (novaOS != null) {
                AlertsSwing.showAlert("Sucesso", "Ordem de Serviço " + novaOS.getNumeroOS() + " aberta com sucesso!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados(); // Atualiza a lista, removendo o agendamento

                // Abre a tela de gerenciamento para a OS recém-criada
                new com.mycompany.oficina.gui.ManagerGerenciarOsSwing(novaOS).setVisible(true);
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível abrir a Ordem de Serviço.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}