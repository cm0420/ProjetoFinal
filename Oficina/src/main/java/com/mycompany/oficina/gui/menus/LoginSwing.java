package com.mycompany.oficina.gui.menus;

import com.mycompany.oficina.application.OficinaAplicattion;
import com.mycompany.oficina.entidades.Funcionario;

import com.mycompany.oficina.seguranca.Sessao;
import com.mycompany.oficina.seguranca.ServicoAutenticacao;
import javax.swing.*;
import java.awt.*;

public class LoginSwing extends JFrame {

    private final ServicoAutenticacao servicoAutenticacao;
    private final JTextField txtCpf = new JTextField(20);
    private final JPasswordField pwfSenha = new JPasswordField(20);

    public LoginSwing() {
        this.servicoAutenticacao = OficinaAplicattion.getInstance().getServicoAutenticacao();

        setTitle("Login - Sistema da Oficina");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(txtCpf, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(pwfSenha, gbc);

        JButton btnLogin = new JButton("Login");
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; panel.add(btnLogin, gbc);

        btnLogin.addActionListener(e -> handleLogin());

        add(panel);
    }

    private void handleLogin() {
        String cpf = txtCpf.getText();
        String senha = new String(pwfSenha.getPassword());
        Funcionario funcionario = servicoAutenticacao.autenticar(cpf, senha);

        if (funcionario != null) {
            Sessao.getInstance().login(funcionario);
            dispose(); // Fecha a tela de login
            abrirMenuPrincipal(funcionario.getCargo());
        } else {
            JOptionPane.showMessageDialog(this, "CPF ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirMenuPrincipal(String cargo) {
        SwingUtilities.invokeLater(() -> {
            JFrame menu;
            switch (cargo) {
                case "Admin":
                    menu = new MenuGerenteSwing();
                    break;
                case "Atendente":
                    menu = new MenuAtendenteSwing();
                    break;
                case "Mecanico":
                    menu = new MenuMecanicoSwing();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Cargo não reconhecido.", "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
            }
            menu.setVisible(true);
        });
    }
}