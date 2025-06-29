package com.mycompany.oficina.gui;

import javax.swing.*;
import java.awt.*;

public class AlertsSwing {
    public static void showAlert(String title, String content, int messageType) {
        JOptionPane.showMessageDialog(null, content, title, messageType);
    }

    public static void showExtrato(String title, String extratoContent) {
        JTextArea textArea = new JTextArea(extratoContent);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(null, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }
}