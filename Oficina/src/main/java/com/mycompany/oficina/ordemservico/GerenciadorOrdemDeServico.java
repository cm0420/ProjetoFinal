package com.mycompany.oficina.ordemservico;

import com.google.gson.reflect.TypeToken;
import com.mycompany.oficina.controlador.GerenciadorGenerico;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.ordemservico.OrdemDeServico;
import com.mycompany.oficina.persistencia.PersistenciaJson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenciadorOrdemDeServico extends GerenciadorGenerico<OrdemDeServico> {
    public GerenciadorOrdemDeServico(PersistenciaJson persistencia) {
        // Apenas passa as informações necessárias para a classe pai
        super(persistencia, "ordens_servico", new TypeToken<ArrayList<OrdemDeServico>>() {});
    }

    public OrdemDeServico abrirOS(Cliente cliente, Carro carro, Funcionario mecanico, String defeito) {
        if (cliente == null || carro == null || mecanico == null || defeito == null || defeito.trim().isEmpty()) {
            return null;
        }

        OrdemDeServico novaOS = new OrdemDeServico(null, cliente, carro, mecanico, defeito, LocalDateTime.now(), null, new ArrayList<>(), new ArrayList<>());

        // O método 'adicionar' da classe pai já se encarrega de adicionar à lista e salvar no JSON
        super.adicionar(novaOS);

        return novaOS;
    }
}
