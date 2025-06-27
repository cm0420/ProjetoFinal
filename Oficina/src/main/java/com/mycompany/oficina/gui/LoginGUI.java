package com.mycompany.oficina.gui;

import com.mycompany.oficina.application.OficinaAplicattion;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.seguranca.Sessao;
import com.mycompany.oficina.seguranca.ServicoAutenticacao;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginGUI extends Application {

    private ServicoAutenticacao servicoAutenticacao;

    @Override
    public void init() {
        // Inicializa o serviço de autenticação
        this.servicoAutenticacao = OficinaAplicattion.getInstance().getServicoAutenticacao();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login - Sistema da Oficina");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label lblCpf = new Label("CPF:");
        grid.add(lblCpf, 0, 1);

        TextField txtCpf = new TextField();
        grid.add(txtCpf, 1, 1);

        Label lblSenha = new Label("Senha:");
        grid.add(lblSenha, 0, 2);

        PasswordField pwfSenha = new PasswordField();
        grid.add(pwfSenha, 1, 2);

        Button btnLogin = new Button("Login");
        VBox hbBtn = new VBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btnLogin);
        grid.add(hbBtn, 1, 4);

        btnLogin.setOnAction(e -> {
            String cpf = txtCpf.getText();
            String senha = pwfSenha.getText();
            Funcionario funcionario = servicoAutenticacao.autenticar(cpf, senha);

            if (funcionario != null) {
                Sessao.getInstance().login(funcionario);
                primaryStage.close(); // Fecha a tela de login
                abrirMenuPrincipal(funcionario.getCargo());
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro de Login", "CPF ou senha inválidos.");
            }
        });

        Scene scene = new Scene(grid, 350, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void abrirMenuPrincipal(String cargo) {
        Stage menuStage = new Stage();
        Application menuApp;

        switch (cargo) {
            case "Admin":
                menuApp = new MenuGerenteGUI();
                break;
            case "Atendente":
                menuApp = new MenuAtendenteGUI();
                break;
            case "Mecanico":
                menuApp = new MenuMecanicoGUI();
                break;
            default:
                showAlert(Alert.AlertType.WARNING, "Cargo não reconhecido", "Nenhum menu disponível para o seu cargo.");
                return;
        }

        try {
            menuApp.start(menuStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}