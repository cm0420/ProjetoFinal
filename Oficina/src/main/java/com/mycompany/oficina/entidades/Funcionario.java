package com.mycompany.oficina.entidades;

/**
 * Representa um funcionário da oficina, herdando os dados básicos de uma pessoa
 * e adicionando informações específicas como ID de usuário, senha e cargo.
 * 
 * O ID de usuário é gerado automaticamente com base em um contador estático.
 * 
 * Exemplo de ID: User-001, User-002, etc.
 * 
 * @author Miguel
 */
public class Funcionario extends Pessoa implements Entidades {

    private static int contadorIdUsuario = 1;

    private String idUsuario;
    private String senha;
    private String cargo;

    /**
     * Construtor da classe Funcionario.
     * 
     * @param senha    Senha de acesso do funcionário.
     * @param cargo    Cargo ocupado pelo funcionário.
     * @param nome     Nome completo.
     * @param cpf      CPF do funcionário.
     * @param telefone Número de telefone.
     * @param endereco Endereço completo.
     * @param email    E-mail para contato.
     */
    public Funcionario(String senha, String cargo, String nome, String cpf, String telefone, String endereco, String email) {
        super(nome, cpf, telefone, endereco, email);
        this.senha = senha;
        this.cargo = cargo;
        this.idUsuario = "User-" + String.format("%03d", contadorIdUsuario++);
    }

    /**
     * Retorna o ID do usuário.
     * 
     * @return ID do usuário (ex: User-001).
     */
    public String getIdUsuario() {
        return idUsuario;
    }

    /**
     * Define manualmente o ID do usuário.
     * (Normalmente não é necessário alterar isso.)
     * 
     * @param idUsuario Novo ID.
     */
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Retorna a senha do funcionário.
     * 
     * @return Senha.
     */
    public String getSenha() {
        return senha;
    }

    /**
     * Define uma nova senha.
     * 
     * @param senha Nova senha.
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * Retorna o cargo atual do funcionário.
     * 
     * @return Cargo.
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * Define um novo cargo para o funcionário.
     * 
     * @param cargo Novo cargo.
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    /**
     * Representação textual do funcionário.
     * 
     * @return String com os dados do funcionário.
     */
    
    @Override
    public String getIdentificador() {
        return this.getCpf();

    }
}
