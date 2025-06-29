package com.mycompany.oficina;

import com.mycompany.oficina.application.OficinaAplicattion;
import com.mycompany.oficina.gui.*; // MUDANÇA AQUI
import com.mycompany.oficina.gui.menus.LoginSwing;

import javax.swing.SwingUtilities;

public class Oficina {
    public static void main(String[] args) {
        // Garante que a aplicação inicie de forma segura
        OficinaAplicattion.getInstance().verificarECriarAdminPadrao();

        // Inicia a interface gráfica Swing na thread de eventos apropriada
        SwingUtilities.invokeLater(() -> {
            new LoginSwing().setVisible(true);
        });
    }
}