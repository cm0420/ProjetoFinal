package com.mycompany.oficina.ordemservico;

import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Entidades;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.loja.Produto;
import com.mycompany.oficina.ordemservico.ObserverOS.Assunto;
import com.mycompany.oficina.ordemservico.ObserverOS.Observador;
import com.mycompany.oficina.ordemservico.stateOS.EstadoAguardando;
import com.mycompany.oficina.ordemservico.stateOS.EstadoOS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Importar para formatar a data
import java.util.ArrayList;
import java.util.List;

public class OrdemDeServico implements Assunto, Entidades {

    private String numeroOS;
    private static int contadorNumeroOS;
    private final Cliente cliente;
    private final Carro carro;
    private final Funcionario mecanicoResponsavel;
    private final String defeitoRelatado;
    private final LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private final List<String> servicosRealizados;
    private final List<PecaUtilizada> pecasUtilizadas;
    private EstadoOS estadoAtual;
    private final List<Observador> observadores = new ArrayList<>();

    // 1. CONSTANTE PARA O VALOR FIXO DA MÃO DE OBRA
    private static final double VALOR_MAO_DE_OBRA = 150.0;

    public OrdemDeServico(String numeroOS, Cliente cliente, Carro carro, Funcionario mecanicoResponsavel, String defeitoRelatado, LocalDateTime dataAbertura, LocalDateTime dataFechamento, List servicosRealizados, List pecasUtilizadas) {
        this.numeroOS = "Ordem-Serviço" + String.format("%03d", contadorNumeroOS++);
        this.cliente = cliente;
        this.carro = carro;
        this.mecanicoResponsavel = mecanicoResponsavel;
        this.defeitoRelatado = defeitoRelatado;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.servicosRealizados = servicosRealizados;
        this.pecasUtilizadas = pecasUtilizadas;
        setEstado(new EstadoAguardando(this));
    }

    // 2. MÉTODO PARA CALCULAR O VALOR TOTAL
    public double calcularValorTotal() {
        double totalPecas = 0.0;
        for (PecaUtilizada peca : pecasUtilizadas) {
            totalPecas += peca.getSubtotal();
        }
        return totalPecas + VALOR_MAO_DE_OBRA;
    }

    // 3. METODO PARA GERAR UM EXTRATO COMPLETO E FORMATADO
    public String gerarExtrato() {
        StringBuilder extrato = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        extrato.append("\n================[ EXTRATO DA ORDEM DE SERVIÇO ]================\n");
        extrato.append("OS Nº: ").append(getNumeroOS()).append("\n");
        extrato.append("Data de Abertura: ").append(dtf.format(dataAbertura)).append("\n");
        extrato.append("Cliente: ").append(getCliente().getNome()).append("\n");
        extrato.append("Veículo: ").append(getCarro().getModelo()).append(" | Placa: ").append(getCarro().getPlaca()).append("\n");
        extrato.append("Status: ").append(getStatusAtual()).append("\n");
        extrato.append("-----------------------------------------------------------------\n");
        extrato.append("ITENS E SERVIÇOS:\n\n");

        if (pecasUtilizadas.isEmpty()) {
            extrato.append("  - Nenhuma peça utilizada.\n");
        } else {
            extrato.append(String.format("  %-15s %-5s %-10s %-10s\n", "Peça", "Qtd.", "Preço Un.", "Subtotal"));
            extrato.append("  -------------------------------------------------------------\n");
            for (PecaUtilizada peca : pecasUtilizadas) {
                extrato.append(String.format("  %-15s %-5d R$ %-8.2f R$ %-8.2f\n",
                        peca.getProdutoOriginal().getNome(),
                        peca.getQuantidadeUtilizada(),
                        peca.getPrecoNoMomentoDoUso(),
                        peca.getSubtotal()));
            }
        }

        extrato.append("\n");
        extrato.append(String.format("  + Mão de Obra (Valor Fixo): .................... R$ %.2f\n", VALOR_MAO_DE_OBRA));
        extrato.append("-----------------------------------------------------------------\n");
        extrato.append(String.format("  VALOR TOTAL A PAGAR: .......................... R$ %.2f\n", calcularValorTotal()));
        extrato.append("=================================================================\n");

        return extrato.toString();
    }


    // --- Métodos que delegam ações para o estado atual ---
    public void iniciarInspecao() { this.estadoAtual.iniciarInspecao(); }
    public void iniciarServico() { this.estadoAtual.iniciarServico(); }
    public void adicionarPeca(Produto produtoDoEstoque, int quantidade) { this.estadoAtual.adicionarPeca(produtoDoEstoque, quantidade); }
    public void finalizarServico() { this.estadoAtual.finalizarServico(); }
    public void cancelar(String motivo) { this.estadoAtual.cancelar(motivo); }
    public final void setEstado(EstadoOS novoEstado) {
        this.estadoAtual = novoEstado;
        System.out.println("\n>>> MUDANÇA DE ESTADO DA OS #" + this.numeroOS + ": " + this.estadoAtual.getStatus());
        this.notificarObservadores();
    }

    // --- Implementação do Padrão Observer ---
    @Override
    public void adicionarObservador(Observador observador) { this.observadores.add(observador); }
    @Override
    public void removerObservador(Observador observador) { this.observadores.remove(observador); }
    @Override
    public void notificarObservadores() { observadores.forEach(obs -> obs.atualizar(this)); }

    // --- Getters ---
    public List<PecaUtilizada> getListaDePecasUtilizadas() { return pecasUtilizadas; }
    public String getNumeroOS() { return numeroOS; }
    public Cliente getCliente() { return cliente; }
    public Carro getCarro() { return carro; }
    public String getStatusAtual() { return this.estadoAtual.getStatus(); }

    @Override
    public String getIdentificador() {
        return this.getNumeroOS();
    }
}