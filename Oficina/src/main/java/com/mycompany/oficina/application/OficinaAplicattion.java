/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.application;

import com.mycompany.oficina.agendamento.*;
import com.mycompany.oficina.controlador.*;
import com.mycompany.oficina.entidades.*;
import com.mycompany.oficina.interpreter.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Miguel
 */
public class OficinaAplicattion {
    
    private final GerenciadorCliente gerenciadorCliente;
    private final GerenciadorFuncionario gerenciadorFuncionario;
    private final GerenciadorCarros gerenciadorCarros;
    private final AgendaOficina agenda;

    public OficinaAplicattion() {
        this.gerenciadorCliente = new GerenciadorCliente();
        this.gerenciadorFuncionario = new GerenciadorFuncionario();
        this.gerenciadorCarros = new GerenciadorCarros(); 
        this.agenda = new AgendaOficina();
        
        
    }
    
    
    
    
    
    
}
