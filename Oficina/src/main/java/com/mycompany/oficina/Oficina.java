/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.oficina;

import com.google.gson.Gson;
import com.mycompany.oficina.agendamento.*;
import com.mycompany.oficina.application.OficinaAplicattion;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Funcionario;

import java.time.LocalDateTime;

import com.mycompany.oficina.gui.LoginGUI;
import com.mycompany.oficina.menus.MenuPrincipal;
import com.mycompany.oficina.menus.Navegador;
import com.mycompany.oficina.loja.Estoque;
import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.seguranca.ServicoAutenticacao;
import com.mycompany.oficina.seguranca.*;
import com.mycompany.oficina.sistemaponto.GerenciadorPonto;
import javafx.application.Application;
import java.util.Scanner;

/**
 * @author Miguel
 */
public class Oficina {
    public static void main(String[] args) {
        OficinaAplicattion.getInstance().verificarECriarAdminPadrao();
        Application.launch(LoginGUI.class, args);
    }
}


