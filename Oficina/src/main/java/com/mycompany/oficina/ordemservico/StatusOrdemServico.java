/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.oficina.ordemservico;

/**
 *
 * @author Miguel
 */
public enum StatusOrdemServico {
    ABERTA,
    EM_ANDAMENTO,
    AGUARDANDO_PECAS,
    CONCLUIDA,
    CANCELADA;

    public static StatusOrdemServico getABERTA() {
        return ABERTA;
    }

    public static StatusOrdemServico getEM_ANDAMENTO() {
        return EM_ANDAMENTO;
    }

    public static StatusOrdemServico getAGUARDANDO_PECAS() {
        return AGUARDANDO_PECAS;
    }

    public static StatusOrdemServico getCONCLUIDA() {
        return CONCLUIDA;
    }

    public static StatusOrdemServico getCANCELADA() {
        return CANCELADA;
    }
}
