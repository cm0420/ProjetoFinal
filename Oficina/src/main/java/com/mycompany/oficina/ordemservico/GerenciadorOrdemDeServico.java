package com.mycompany.oficina.ordemservico;

import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.ordemservico.OrdemDeServico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GerenciadorOrdemDeServico {
    private final List<OrdemDeServico> listaDeOS = new ArrayList<>();

    public OrdemDeServico abrirOS(Cliente cliente, Carro carro, Funcionario mecanico, String defeito) {
        // Validação de dados de entrada
        if (cliente == null || carro == null || mecanico == null || defeito == null || defeito.trim().isEmpty()) {
            return null; // Retorna null para indicar falha
        }
        OrdemDeServico novaOS = new OrdemDeServico(null, cliente, carro, mecanico, defeito, LocalDateTime.now(), null, new ArrayList<>(), new ArrayList<>());
        this.listaDeOS.add(novaOS);
        return novaOS;
    }

    public OrdemDeServico buscarPorId(String id) {
        for (OrdemDeServico os : listaDeOS) {
            if (os.getNumeroOS().equalsIgnoreCase(id)) {
                return os;
            }
        }
        return null;
    }

    public List<OrdemDeServico> listarTodas() {
        return Collections.unmodifiableList(listaDeOS);
    }
}
