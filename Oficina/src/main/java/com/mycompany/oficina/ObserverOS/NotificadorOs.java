/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.ObserverOS;

import com.mycompany.oficina.ordemservico.OrdemDeServico;

/**
 *
 * @author Miguel
 */
public class NotificadorOs implements Observador {

    @Override
    public void atualizar(OrdemDeServico os) {
        String status = os.getStatusAtual();
        String mensagem = null;

        // Usamos um 'switch' para determinar a mensagem apropriada para cada estado.
        // Isso torna o código mais limpo e fácil de estender.
        switch (status) {
            case "Aguardando":
                mensagem = String.format("Olá, %s! Recebemos seu veículo %s. A OS (#%s) foi aberta e em breve iniciaremos a inspeção.",
                        os.getCliente().getNome(), os.getCarro().getModelo(), os.getNumeroOS());
                break;

            case "Em Inspeção":
                mensagem = String.format("Olá, %s! A inspeção do seu veículo %s (OS #%s) foi iniciada por nossa equipe.",
                        os.getCliente().getNome(), os.getCarro().getModelo(), os.getNumeroOS());
                break;

            case "Em Serviço":
                mensagem = String.format("Boas notícias, %s! O serviço no seu veículo (OS #%s) foi iniciado.",
                        os.getCliente().getNome(), os.getNumeroOS());
                break;

            case "Finalizada":
                mensagem = String.format("Serviço Concluído! Olá, %s, o serviço no seu veículo %s (placa %s) foi finalizado e ele está pronto para retirada.",
                        os.getCliente().getNome(), os.getCarro().getModelo(), os.getCarro().getPlaca());
                break;

            case "Cancelada":
                mensagem = String.format("Atenção, %s. A OS (#%s) do seu veículo foi cancelada.",
                        os.getCliente().getNome(), os.getNumeroOS());
                break;
        }

        if (mensagem != null) {
            enviarNotificacao(os.getCliente().getTelefone(), mensagem);
        }
    }

    private void enviarNotificacao(String telefone, String mensagem) {
        System.out.println("\n=================[ SIMULADOR DE NOTIFICAÇÃO ]=================");
        System.out.println("DESTINATÁRIO: " + telefone + " | MENSAGEM: " + mensagem);
        System.out.println("==============================================================\n");
    }
}
