package com.mycompany.oficina.gui.menus;

import com.mycompany.oficina.controller.MecanicoController;
import com.mycompany.oficina.gui.ManagerGerenciarOsSwing;
import com.mycompany.oficina.gui.ManagerIniciarOsSwing;
import com.mycompany.oficina.gui.ManagerPontoSwing;
import javax.swing.*;
import java.awt.*;

public class MenuMecanicoSwing extends JFrame {
    public MenuMecanicoSwing() {
        setTitle("Menu do Mecânico");
        setSize(350, 280);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnAgendamentos = new JButton("Ver Agendamentos e Iniciar OS");
        JButton btnGerenciarOS = new JButton("Gerenciar OS Existente");
        JButton btnRegistrarPonto = new JButton("Registrar Ponto");
        JButton btnSair = new JButton("Logout");

        formatButton(btnAgendamentos);
        formatButton(btnGerenciarOS);
        formatButton(btnRegistrarPonto);
        formatButton(btnSair);

        btnAgendamentos.addActionListener(e -> new ManagerIniciarOsSwing().setVisible(true));
        btnGerenciarOS.addActionListener(e -> new ManagerGerenciarOsSwing().setVisible(true));
        btnRegistrarPonto.addActionListener(e -> new ManagerPontoSwing(new MecanicoController()).setVisible(true));
        btnSair.addActionListener(e -> dispose());

        panel.add(btnAgendamentos);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnGerenciarOS);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnRegistrarPonto);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnSair);

        add(panel);
    }

    private void formatButton(JButton button) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}