package com.mycompany.oficina.entidades;

/**
 * Representa um elevador da oficina utilizado para realização de serviços nos veículos.
 * Esta classe possui um vetor estático com 3 elevadores pré-definidos, que podem ser acessados
 * e manipulados diretamente através dos métodos estáticos fornecidos.
 *
 * Cada elevador possui um ID único, uma descrição e um estado de disponibilidade.
 *
 * Exemplo de uso:
 * <pre>
 *     Elevador[] todosElevadores = Elevador.getElevadores();
 *     Elevador elevador1 = Elevador.getElevadorPorId(1);
 *     elevador1.setDisponivel(false); // Marcar como ocupado
 * </pre>
 * 
 * A criação de novos elevadores fora do vetor estático não é permitida.
 * 
 * @author Miguel
 */
public class Elevador {

    private int id;
    private String descricao;
    private boolean disponivel;

    // Questao 5 - O sistema deverá armazenar de forma estática (Vetor com tamanho fixo)
    // as informações dos 3 elevadores da oficina.
    private static final Elevador[] elevadores = new Elevador[3];

  //Questão 5- O sistema deverá armazenar de forma estática
  // (Vetor com tamanho fixo) as informações dos 3 elevadores da oficina.
    static {
        elevadores[0] = new Elevador(1, "Elevador Alinhamento e Balanceamento", true);
        elevadores[1] = new Elevador(2, "Elevador 2", true);
        elevadores[2] = new Elevador(3, "Elevador 3", true);
    }

    /**
     * Construtor privado. Não permite criação externa de elevadores,
     * garantindo controle centralizado pela classe.
     *
     * @param id identificador único do elevador
     * @param descricao descrição do elevador
     * @param disponivel se o elevador está disponível
     */
    private Elevador(int id, String descricao, boolean disponivel) {
        this.id = id;
        this.descricao = descricao;
        this.disponivel = disponivel;
    }

    /** @return o identificador único do elevador */
    public int getId() {
        return id;
    }

    /** @return a descrição do elevador */
    public String getDescricao() {
        return descricao;
    }

    /** @return true se o elevador está disponível para uso */
    public boolean isDisponivel() {
        return disponivel;
    }

    /**
     * Define se o elevador está disponível.
     *
     * @param disponivel novo estado de disponibilidade
     */
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    /**
     * Retorna o vetor contendo os três elevadores da oficina.
     *
     * @return array dos elevadores registrados
     */
    public static Elevador[] getElevadores() {
        return elevadores;
    }

    /**
     * Busca um elevador específico pelo seu ID.
     *
     * @param id identificador do elevador
     * @return o elevador correspondente ou null se não encontrado
     */
    public static Elevador getElevadorPorId(int id) {
        for (Elevador e : elevadores) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    /**
     * Retorna uma representação textual do elevador, incluindo ID, descrição e disponibilidade.
     * 
     * @return string com os dados do elevador
     */
    @Override
    public String toString() {
        return "Elevador{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", disponivel=" + disponivel +
                '}';
    }
}
