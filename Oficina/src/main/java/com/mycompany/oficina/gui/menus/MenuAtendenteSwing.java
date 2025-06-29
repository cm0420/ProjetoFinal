package com.mycompany.oficina.gui.menus;

import com.mycompany.oficina.controller.AtendenteController;
import com.mycompany.oficina.gui.ClienteManagerSwing;
import com.mycompany.oficina.gui.ManagerAgendamentoSwing;
import com.mycompany.oficina.gui.ManagerPontoSwing;
import com.mycompany.oficina.gui.VeiculoManagerSwing;
import javax.swing.*;
import java.awt.*;

public class MenuAtendenteSwing extends JFrame {
    public MenuAtendenteSwing() {
        setTitle("Menu do Atendente");
        setSize(350, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnClientes = new JButton("Gerenciar Clientes");
        JButton btnVeiculos = new JButton("Gerenciar Veículos");
        JButton btnAgendamentos = new JButton("Gerenciar Agendamentos");
        JButton btnPonto = new JButton("Registrar Ponto");
        JButton btnSair = new JButton("Logout");

        formatButton(btnClientes);
        formatButton(btnVeiculos);
        formatButton(btnAgendamentos);
        formatButton(btnPonto);
        formatButton(btnSair);

        btnClientes.addActionListener(e -> new ClienteManagerSwing().setVisible(true));
        btnVeiculos.addActionListener(e -> new VeiculoManagerSwing().setVisible(true));
        btnAgendamentos.addActionListener(e -> new ManagerAgendamentoSwing(new AtendenteController()).setVisible(true));
        btnPonto.addActionListener(e -> new ManagerPontoSwing(new AtendenteController()).setVisible(true));
        btnSair.addActionListener(e -> dispose());

        panel.add(btnClientes);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnVeiculos);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnAgendamentos);
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