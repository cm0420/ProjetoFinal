package com.mycompany.oficina.controlador;

import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;

/**
 * Classe responsável pelo gerenciamento dos carros da oficina.
 * Permite buscar, listar, cadastrar, editar e remover carros.
 * Mantém uma lista interna de objetos {@link Carro}.
 * 
 * @author Miguel
 */
public class GerenciadorCarros extends GerenciadorGenerico<Carro> {

  /**
     * Cria e cadastra um novo carro, associando-o a um cliente.
     * Este método é específico pois sabe como construir um objeto Carro.
     *
     * @param clienteSelecionado O objeto Cliente que é o dono do carro.
     * @param fabricante         Fabricante do carro.
     * @param modelo             Modelo do carro.
     * @param placa              Placa do carro.
     * @param chassi             Número do chassi (usado como identificador único).
     * @return O objeto {@link Carro} recém-criado, ou null se o chassi já existir.
     */
    public Carro cadastrarCarro(Cliente clienteSelecionado, String fabricante, String modelo, String placa, String chassi) {

        Carro novoCarro = new Carro(clienteSelecionado.getNome(), clienteSelecionado.getCpf(), fabricante, modelo, placa, chassi);
        
        // Usa o método "adicionar" herdado da classe pai
        super.adicionar(novoCarro);
        
        return novoCarro;
    }

    /**
     * Edita os dados de um carro existente, identificado pelo seu chassi.
     *
     * @param chassi           Chassi do carro a ser editado.
     * @param novoFabricante   Novo fabricante.
     * @param novoModelo       Novo modelo.
     * @param novaPlaca        Nova placa.
     * @return true se o carro foi encontrado e editado com sucesso, false caso contrário.
     */
    public boolean editarCarro(String chassi, String novoFabricante, String novoModelo, String novaPlaca) {
        Carro carroParaEditar = this.buscarPorIdentificador(chassi);

        if (carroParaEditar != null) {
            carroParaEditar.setFabricante(novoFabricante);
            carroParaEditar.setModelo(novoModelo);
            carroParaEditar.setPlaca(novaPlaca);
            return true;
        }
        return false;
    }
}
