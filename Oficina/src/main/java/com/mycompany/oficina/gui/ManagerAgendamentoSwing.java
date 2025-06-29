package com.mycompany.oficina.gui;

import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.controller.AtendenteController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ManagerAgendamentoSwing extends JFrame {
    private final AtendenteController controller;
    private final JTable tableView;
    private final DefaultTableModel tableModel;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ManagerAgendamentoSwing(AtendenteController controller) {
        this.controller = controller;
        setTitle("Gerenciador de Agendamentos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Data/Hora", "Cliente", "Veículo", "Mecânico", "Serviço"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableView = new JTable(tableModel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnNovo = new JButton("Novo Agendamento");
        JButton btnCancelar = new JButton("Cancelar Agendamento");
        JButton btnAtualizar = new JButton("Atualizar Lista");
        buttonPanel.add(btnNovo);
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnAtualizar);

        btnNovo.addActionListener(e -> handleNovoAgendamento());
        btnCancelar.addActionListener(e -> handleCancelar());
        btnAtualizar.addActionListener(e -> carregarDados());

        setLayout(new BorderLayout());
        add(new JScrollPane(tableView), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        for (Agendamento ag : controller.listarTodosAgendamentos()) {
            tableModel.addRow(new Object[]{
                    ag.getDataHora().format(formatter),
                    ag.getCliente().getNome(),
                    ag.getCarro().getModelo() + " (" + ag.getCarro().getPlaca() + ")",
                    ag.getMecanico().getNome(),
                    ag.getTipoServico().name()
            });
        }
    }

    private void handleNovoAgendamento() {
        AgendamentoDialogo dialog = new AgendamentoDialogo(this, controller);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            Agendamento agendamento = dialog.getAgendamento();
            if (controller.criarAgendamento(agendamento.getCliente(), agendamento.getCarro(), agendamento.getMecanico(), agendamento.getTipoServico(), agendamento.getDataHora())) {
                AlertsSwing.showAlert("Sucesso", "Agendamento criado!", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível criar o agendamento. Verifique os dados e a disponibilidade.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleCancelar() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) {
            AlertsSwing.showAlert("Seleção Necessária", "Selecione um agendamento para cancelar.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Recuperar o objeto Agendamento da linha selecionada
        Agendamento sel = controller.listarTodosAgendamentos().get(selectedRow);

        int confirm = JOptionPane.showConfirmDialog(this, "Cancelar agendamento para " + sel.getCliente().getNome() + "?", "Confirmar Cancelamento", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.cancelarAgendamento(sel)) {
                AlertsSwing.showAlert("Sucesso", "Agendamento cancelado.", JOptionPane.INFORMATION_MESSAGE);
                carregarDados();
            } else {
                AlertsSwing.showAlert("Erro", "Não foi possível remover o agendamento.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}