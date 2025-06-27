package com.mycompany.oficina.gui.tela;

import javafx.stage.Stage;
import com.mycompany.oficina.controller.GerenteController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.YearMonth;
public class ManagerFinanceiroGUI extends Stage {
    private final GerenteController controller;

    public ManagerFinanceiroGUI(GerenteController controller) {
        this.controller = new GerenteController();
        initModality(Modality.APPLICATION_MODAL);
        setTitle("Módulo Financeiro");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Relatórios e Ações Financeiras");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button btnBalancoDiario = new Button("Emitir Balanço do Dia");
        Button btnBalancoMensal = new Button("Emitir Balanço do Mês Atual");
        Button btnRelatorioDespesas = new Button("Relatório de Despesas do Mês");
        Button btnPagarSalarios = new Button("Pagar Salários");

        // --- Configuração das Ações ---

        btnBalancoDiario.setOnAction(e -> {
            // Captura a saída do console para exibir na GUI
            String relatorio = captureConsoleOutput(() ->
                    controller.emitirBalanco(LocalDate.now(), LocalDate.now())
            );
            Alerts.showExtrato("Balanço Diário", relatorio);
        });

        btnBalancoMensal.setOnAction(e -> {
            String relatorio = captureConsoleOutput(() -> {
                YearMonth mes = YearMonth.now();
                controller.emitirBalanco(mes.atDay(1), mes.atEndOfMonth());
            });
            Alerts.showExtrato("Balanço Mensal", relatorio);
        });

        btnRelatorioDespesas.setOnAction(e -> {
            String relatorio = captureConsoleOutput(() -> {
                YearMonth mes = YearMonth.now();
                controller.emitirRelatorioDespesas(mes.atDay(1), mes.atEndOfMonth());
            });
            Alerts.showExtrato("Relatório de Despesas", relatorio);
        });

        btnPagarSalarios.setOnAction(e -> {
            controller.pagarSalarios();
            Alerts.showAlert("Sucesso", "Folha de pagamento registrada com sucesso no sistema financeiro.", Alert.AlertType.INFORMATION);
        });

        vbox.getChildren().addAll(
                titleLabel,
                btnBalancoDiario,
                btnBalancoMensal,
                btnRelatorioDespesas,
                btnPagarSalarios
        );
        vbox.getChildren().forEach(node -> {
            if (node instanceof Button) {
                ((Button) node).setMaxWidth(Double.MAX_VALUE);
            }
        });

        Scene scene = new Scene(vbox, 350, 300);
        setScene(scene);
    }

    /**
     * Captura a saída que seria impressa no console (System.out) e a retorna como String.
     * Útil para exibir relatórios baseados em console em uma interface gráfica.
     */
    private String captureConsoleOutput(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newOut = new PrintStream(baos);
        System.setOut(newOut);

        action.run(); // Executa a ação (que imprime no console)

        System.out.flush();
        System.setOut(originalOut); // Restaura a saída original do console
        return baos.toString();
    }
}
