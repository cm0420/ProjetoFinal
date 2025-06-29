package com.mycompany.oficina.gui.menus;

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.controller.GerenteController;
import com.mycompany.oficina.gui.*;
import javax.swing.*;
import java.awt.*;

public class MenuGerenteSwing extends JFrame {
    public MenuGerenteSwing() {
        setTitle("Menu do Gerente");
        setSize(350, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnClientes = new JButton("Gerenciar Clientes");
        JButton btnVeiculos = new JButton("Gerenciar Veículos");
        JButton btnAgendamentos = new JButton("Gerenciar Agendamentos");
        JButton btnFuncionarios = new JButton("Gerenciar Funcionários");
        JButton btnFinanceiro = new JButton("Módulo Financeiro");
        JButton btnEstoque = new JButton("Módulo de Estoque");
        JButton btnPonto = new JButton("Registrar Ponto");
        JButton btnSair = new JButton("Logout");

        formatButton(btnClientes);
        formatButton(btnVeiculos);
        formatButton(btnAgendamentos);
        formatButton(btnFuncionarios);
        formatButton(btnFinanceiro);
        formatButton(btnEstoque);
        formatButton(btnPonto);
        formatButton(btnSair);

        btnClientes.addActionListener(e -> new ClienteManagerSwing().setVisible(true));
        btnVeiculos.addActionListener(e -> new VeiculoManagerSwing().setVisible(true));
        btnAgendamentos.addActionListener(e -> new ManagerAgendamentoSwing(new AtendenteController()).setVisible(true));
        btnFuncionarios.addActionListener(e -> new FuncionarioManagerSwing().setVisible(true));
        btnFinanceiro.addActionListener(e -> new ManagerFinanceiroSwing(new GerenteController()).setVisible(true));
        btnEstoque.addActionListener(e -> new ManagerEstoqueSwing().setVisible(true));
        btnPonto.addActionListener(e -> new ManagerPontoSwing(new GerenteController()).setVisible(true));
        btnSair.addActionListener(e -> dispose());

        panel.add(btnClientes);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnVeiculos);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnAgendamentos);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnFuncionarios);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnEstoque);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnFinanceiro);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnPonto);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnSair);

        add(panel);
    }

    private void formatButton(JButton button) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}