package com.mycompany.oficina.gui;

import com.mycompany.oficina.controller.GerenteController;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.YearMonth;

public class ManagerFinanceiroSwing extends JFrame {
    private final GerenteController controller;

    public ManagerFinanceiroSwing(GerenteController controller) {
        this.controller = controller;
        setTitle("Módulo Financeiro");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Relatórios e Ações Financeiras");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnBalancoDiario = new JButton("Emitir Balanço do Dia");
        JButton btnBalancoMensal = new JButton("Emitir Balanço do Mês Atual");
        JButton btnRelatorioDespesas = new JButton("Relatório de Despesas do Mês");
        JButton btnPagarSalarios = new JButton("Pagar Salários");

        formatButton(btnBalancoDiario);
        formatButton(btnBalancoMensal);
        formatButton(btnRelatorioDespesas);
        formatButton(btnPagarSalarios);

        btnBalancoDiario.addActionListener(e -> {
            String relatorio = captureConsoleOutput(() ->
                    controller.emitirBalanco(LocalDate.now(), LocalDate.now())
            );
            AlertsSwing.showExtrato("Balanço Diário", relatorio);
        });

        btnBalancoMensal.addActionListener(e -> {
            String relatorio = captureConsoleOutput(() -> {
                YearMonth mes = YearMonth.now();
                controller.emitirBalanco(mes.atDay(1), mes.atEndOfMonth());
            });
            AlertsSwing.showExtrato("Balanço Mensal", relatorio);
        });

        btnRelatorioDespesas.addActionListener(e -> {
            String relatorio = captureConsoleOutput(() -> {
                YearMonth mes = YearMonth.now();
                controller.emitirRelatorioDespesas(mes.atDay(1), mes.atEndOfMonth());
            });
            AlertsSwing.showExtrato("Relatório de Despesas", relatorio);
        });

        btnPagarSalarios.addActionListener(e -> {
            controller.pagarSalarios();
            AlertsSwing.showAlert("Sucesso", "Folha de pagamento registrada com sucesso no sistema financeiro.", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnBalancoDiario);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnBalancoMensal);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnRelatorioDespesas);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnPagarSalarios);

        add(panel);
    }

    private void formatButton(JButton button) {
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private String captureConsoleOutput(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PrintStream newOut = new PrintStream(baos)) {
            System.setOut(newOut);
            action.run();
        } finally {
            System.out.flush();
            System.setOut(originalOut);
        }
        return baos.toString();
    }
}