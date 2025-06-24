/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oficina.entidades;

/**
 * Representa um carro com informações sobre fabricante, modelo, placa e chassi.
 * Cada carro possui um identificador único gerado automaticamente.
 *
 * @author Miguel
 */
public class Carro implements Entidades {

    /**
     * Identificador único do carro no formato "Carro-XXX"
     */
    private String idCarro;

    /**
     * Contador estático para geração do idCarro sequencial
     */
    private static int contadorIdCarro = 1;

    /**
     * Fabricante do carro
     */
    private String fabricante;

    /**
     * Modelo do carro
     */
    private String modelo;

    /**
     * Placa do carro
     */
    private String placa;

    /**
     * Número do chassi do carro
     */
    private String chassi;

    /**
     * Número do id que identifica carros por cliente
     */
    private String cpfDono;
    
    private String nomeDono;

    /**
     * Construtor que inicializa o carro com as informações fornecidas.O
 identificador do carro é gerado automaticamente.
     *
     * @param nomeDono
     * @param cpfDono
     * @param fabricante Fabricante do carro
     * @param modelo Modelo do carro
     * @param placa Placa do carro
     * @param chassi Número do chassi do carro
     */
    public Carro( String nomeDono,String cpfDono, String fabricante, String modelo, String placa, String chassi) {
        this.nomeDono = nomeDono;
        this.cpfDono= cpfDono;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.placa = placa;
        this.chassi = chassi;
        this.idCarro = "Carro-" + String.format("%03d", contadorIdCarro++);
    }

    public static int getContadorIdCarro() {
        return contadorIdCarro;
    }

    public String getCpfDono() {
        return cpfDono;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    /**
     * Construtor padrão.
     */
    public Carro() {
    }

    /**
     * Retorna o identificador do carro.
     *
     * @return idCarro
     */
    public String getIdCarro() {
        return idCarro;
    }

    /**
     * Define o identificador do carro.
     *
     * @param idCarro Identificador único
     */
    public void setIdCarro(String idCarro) {
        this.idCarro = idCarro;
    }

    /**
     * Retorna o fabricante do carro.
     *
     * @return fabricante
     */
    public String getFabricante() {
        return fabricante;
    }

    /**
     * Define o fabricante do carro.
     *
     * @param fabricante Fabricante do carro
     */
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    /**
     * Retorna o modelo do carro.
     *
     * @return modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Define o modelo do carro.
     *
     * @param modelo Modelo do carro
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Retorna a placa do carro.
     *
     * @return placa
     */
    public String getPlaca() {
        return placa;
    }

    /**
     * Define a placa do carro.
     *
     * @param placa Placa do carro
     */
    public void setPlaca(String placa) {
        this.placa = placa;
    }

    /**
     * Retorna o número do chassi do carro.
     *
     * @return chassi
     */
    public String getChassi() {
        return chassi;
    }

    /**
     * Define o número do chassi do carro.
     *
     * @param chassi Número do chassi
     */
    public void setChassi(String chassi) {
        this.chassi = chassi;
    }
    
    @Override
    public String getIdentificador(){
    
    return this.getChassi();
    
    }

    @Override
    public String toString() {
        return "Carro{"
                + "cpf do cliente que é dono do carro" + cpfDono + '\''
                + "idCarro='" + idCarro + '\''
                + ", fabricante='" + fabricante + '\''
                + ", modelo='" + modelo + '\''
                + ", placa='" + placa + '\''
                + ", chassi='" + chassi + '\''
                + '}';
    }
}
