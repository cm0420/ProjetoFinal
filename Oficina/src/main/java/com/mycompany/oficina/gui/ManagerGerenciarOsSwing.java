package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.MecanicoController;
import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class ManagerGerenciarOsSwing extends JFrame {

    private final MecanicoController controller;
    private final JTable tableView;
    private final DefaultTableModel tableModel;
    private final OrdemDeServico osInicial;

    public ManagerGerenciarOsSwing() {
        this(null);
    }

    public ManagerGerenciarOsSwing(OrdemDeServico os) {
        this.controller = new MecanicoController();
        this.osInicial = os;
        setTitle("Gerenciar Ordens de Serviço Ativas");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tabela de OS
        String[] columnNames = {"Nº OS", "Cliente", "Veículo", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableView = new JTable(tableModel);

        // Painel de ações
        JPanel actionsPanel = createActionsPanel();

        // Layout
        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(tableView), BorderLayout.CENTER);
        add(actionsPanel, BorderLayout.EAST);

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        for (OrdemDeServico os : controller.listarOSAtivas()) {
            tableModel.addRow(new Object[]{
                    os.getNumeroOS(),
                    os.getCliente().getNome(),
                    os.getCarro().getModelo(),
                    os.getStatusAtual()
            });
        }
        // Se uma OS foi passada ao abrir a janela, seleciona-a
        if (osInicial != null) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(osInicial.getNumeroOS())) {
                    tableView.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }
    }

    private JPanel createActionsPanel() {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Ações da OS"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JButton btnIniciarInspecao = new JButton("Iniciar Inspeção");
        JButton btnIniciarServico = new JButton("Iniciar Serviço");
        JButton btnAdicionarPeca = new JButton("Adicionar Peça");
        JButton btnFinalizarServico = new JButton("Finalizar Serviço");
        JButton btnVerExtrato = new JButton("Ver Extrato");

        // Formatação
        formatButton(btnIniciarInspecao);
        formatButton(btnIniciarServico);
        formatButton(btnAdicionarPeca);
        formatButton(btnFinalizarServico);
        formatButton(btnVerExtrato);

        // Ações
        btnIniciarInspecao.addActionListener(e -> {
            OrdemDeServico sel = getSelectedOS();
            if(sel != null) {
                controller.iniciarInspecaoOS(sel);
                carregarDados();
            }
        });

        btnIniciarServico.addActionListener(e -> {
            OrdemDeServico sel = getSelectedOS();
            if(sel != null) {
                controller.iniciarServicoOS(sel);
                carregarDados();
            }
        });

        btnAdicionarPeca.addActionListener(e -> handleAdicionarPeca());

        btnFinalizarServico.addActionListener(e -> {
            OrdemDeServico sel = getSelectedOS();
            if(sel != null) {
                controller.finalizarServicoOS(sel);
                AlertsSwing.showExtrato("Serviço Finalizado", controller.gerarExtratoOS(sel));
                carregarDados();
            }
        });

        btnVerExtrato.addActionListener(e -> {
            OrdemDeServico sel = getSelectedOS();
            if (sel != null) {
                AlertsSwing.showExtrato("Extrato da OS: " + sel.getNumeroOS(), controller.gerarExtratoOS(sel));
            }
        });

        // Lógica para habilitar/desabilitar botões
        tableView.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                OrdemDeServico sel = getSelectedOS();
                boolean isSelected = sel != null;
                String status = isSelected ? sel.getStatusAtual() : "";

                btnIniciarInspecao.setEnabled(isSelected && "Aguardando".equals(status));
                btnIniciarServico.setEnabled(isSelected && "Em Inspeção".equals(status));
                btnAdicionarPeca.setEnabled(isSelected && "Em Serviço".equals(status));
                btnFinalizarServico.setEnabled(isSelected && "Em Serviço".equals(status));
                btnVerExtrato.setEnabled(isSelected);
            }
        });

        // Adiciona ao painel
        actionsPanel.add(btnIniciarInspecao);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        actionsPanel.add(btnIniciarServico);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        actionsPanel.add(btnAdicionarPeca);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        actionsPanel.add(btnFinalizarServico);
        actionsPanel.add(Box.createVerticalStrut(20)); // Espaço maior
        actionsPanel.add(btnVerExtrato);

        return actionsPanel;
    }

    private OrdemDeServico getSelectedOS() {
        int selectedRow = tableView.getSelectedRow();
        if (selectedRow < 0) return null;
        String osId = (String) tableModel.getValueAt(selectedRow, 0);
        return controller.buscarOS(osId);
    }

    private void handleAdicionarPeca() {
        OrdemDeServico osSel = getSelectedOS();
        if (osSel == null) return;

        // Diálogo para selecionar peça e quantidade
        JComboBox<Produto> pecaCombo = new JComboBox<>(new Vector<>(controller.listarProdutosEstoque()));
        JTextField quantidadeField = new JTextField("1", 5);

        // Custom renderer para o ComboBox
        pecaCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Produto) {
                    setText(((Produto) value).getNome() + " (Qtd: " + ((Produto) value).getQuantidade() + ")");
                }
                return this;
            }
        });

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Peça:"));
        panel.add(pecaCombo);
        panel.add(new JLabel("Quantidade:"));
        panel.add(quantidadeField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Peça à OS", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            Produto produto = (Produto) pecaCombo.getSelectedItem();
            try {
                int quantidade = Integer.parseInt(quantidadeField.getText());
                if (produto != null && quantidade > 0) {
                    controller.adicionarPecaOS(osSel, produto, quantidade);
                    AlertsSwing.showAlert("Sucesso", "Peça adicionada!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    AlertsSwing.showAlert("Erro", "Peça ou quantidade inválida.", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                AlertsSwing.showAlert("Erro de Formato", "A quantidade deve ser um número.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void formatButton(JButton button) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}