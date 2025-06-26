package com.mycompany.oficina.Menus;
import com.mycompany.oficina.agendamento.AgendaOficina;
import com.mycompany.oficina.agendamento.Agendamento;
import com.mycompany.oficina.agendamento.TipoServico;
import com.mycompany.oficina.controlador.GerenciadorCarros;
import com.mycompany.oficina.controlador.GerenciadorCliente;
import com.mycompany.oficina.controlador.GerenciadorFuncionario;
import com.mycompany.oficina.entidades.Carro;
import com.mycompany.oficina.entidades.Cliente;
import com.mycompany.oficina.entidades.Funcionario;
import com.mycompany.oficina.financeiro.CancelamentoAgendamento;
import com.mycompany.oficina.financeiro.GerenciadorFinanceiro;
import com.mycompany.oficina.interpreter.*;
import com.mycompany.oficina.ordemservico.GerenciadorOrdemDeServico;
import com.mycompany.oficina.seguranca.Sessao;
import com.mycompany.oficina.utilidades.FormatadorCpf;
import com.mycompany.oficina.strategy.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuAtendente implements Menu {
    private final GerenciadorCliente gerenciadorCliente;
    private final GerenciadorCarros gerenciadorCarros;
    private final GerenciadorFuncionario gerenciadorFuncionario;
    private final GerenciadorOrdemDeServico gerenciadorOS;
    private final AgendaOficina agenda;
    private final Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MenuAtendente(GerenciadorCliente gCliente, GerenciadorCarros gCarros, GerenciadorFuncionario gFunc, AgendaOficina ag, GerenciadorOrdemDeServico gOS) {
        this.gerenciadorCliente = gCliente;
        this.gerenciadorCarros = gCarros;
        this.gerenciadorFuncionario = gFunc;
        this.gerenciadorOS = gOS;
        this.agenda = ag;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void exibirMenu() {
        if (!"Atendente".equals(Sessao.getInstance().getCargoUsuarioLogado())) {
            System.out.println("Acesso negado.");
            return;
        }

        boolean executando = true;
        while (executando) {
            System.out.println("\n--- (Acesso Atendente) BEM-VINDO, " + Sessao.getInstance().getUsuarioLogado().getNome() + " ---");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Veículos");
            System.out.println("3. Gerenciar Agendamentos");
            System.out.println("0. Voltar ao Menu Principal (Logout)");
            System.out.print("Escolha uma opção: ");

            switch (scanner.nextLine()) {
                case "1": menuClientes(); break;
                case "2": menuVeiculos(); break;
                case "3": menuAgendamentos(); break;
                case "0": executando = false; break;
                default: System.out.println("Opção inválida.");
            }
        }
        Navegador.getInstance().voltarPara();
    }
    // --- METODO HELPER QUE IMPLEMENTA O PADRÃO STRATEGY ---
    private String obterDadoValido(String prompt, Validate estrategia) {
        while (true) {
            System.out.print(prompt);
            String dado = scanner.nextLine();
            if (estrategia.validar(dado)) {
                return dado;
            } else {
                System.out.println(estrategia.getMensagemErro());
            }
        }
    }

    // --- SUBMENUS PRINCIPAIS ---

    private void menuClientes() {
        System.out.println("\n--- Gerenciar Clientes ---");
        System.out.println("1. Cadastrar Novo Cliente");
        System.out.println("2. Consultar Cliente");
        System.out.println("3. Editar Cliente");
        System.out.println("4. Remover Cliente");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        switch(scanner.nextLine()) {
            case "1": cadastrarCliente(); break;
            case "2": consultarCliente(null); break;
            case "3": editarCliente(); break;
            case "4": removerCliente(); break;
            case "0": return;
            default: System.out.println("Opção inválida.");
        }
    }

    private void menuVeiculos() {
        System.out.println("\n--- Gerenciar Veículos ---");
        System.out.println("1. Cadastrar Novo Veículo");
        System.out.println("2. Consultar Veículo");
        System.out.println("3. Editar Veículo");
        System.out.println("4. Remover Veículo");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        switch (scanner.nextLine()) {
            case "1": cadastrarVeiculo(); break;
            case "2": consultarVeiculo(); break;
            case "3": editarVeiculo(); break;
            case "4": removerVeiculo(); break;
            case "0": return;
            default: System.out.println("Opção inválida.");
        }
    }

    private void menuAgendamentos() {
        System.out.println("\n--- Gerenciar Agendamentos ---");
        System.out.println("1. Novo Agendamento");
        System.out.println("2. Consultar / Cancelar Agendamento");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        switch (scanner.nextLine()) {
            case "1": agendarServico(); break;
            case "2": consultarEGerenciarAgendamento(); break;
            case "0": return;
            default: System.out.println("Opção inválida.");
        }
    }

    // --- LÓGICA DE CLIENTES ---

    private Cliente cadastrarCliente() {
        System.out.println("\n--- Cadastro de Novo Cliente ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        String cpf;
        while (true) {
            cpf = obterDadoValido("CPF (11 dígitos): ", new CpfValidate());
            cpf = cpf.replaceAll("\\D", "");
            if (gerenciadorCliente.buscarPorIdentificador(cpf) == null) {
                break;
            } else {
                System.out.println("ERRO: Já existe um cliente com este CPF.");
            }
        }

        String telefone = obterDadoValido("Telefone (11 dígitos, DDD+número): ", new TelefoneValidate());
        telefone = telefone.replaceAll("\\D", "");

        String email = obterDadoValido("E-mail (ex: nome@dominio.com): ", new EmailValidate());

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        Cliente novoCliente = gerenciadorCliente.addCliente(nome, cpf, telefone, endereco, email);
        if (novoCliente != null) {
            System.out.println("Cliente " + novoCliente.getNome() + " cadastrado com sucesso!");
        }
        return novoCliente;
    }

    private Cliente consultarCliente(String cpfBusca) {
        String cpf = cpfBusca;
        if (cpf == null) {
            System.out.println("\n--- Consulta de Cliente ---");
            cpf = obterDadoValido("Digite o CPF do cliente para buscar: ", new CpfValidate());
        }
        cpf = cpf.replaceAll("\\D", "");

        Cliente cliente = gerenciadorCliente.buscarPorIdentificador(cpf);
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
        } else {
            System.out.println("\nDados do Cliente:");
            System.out.println("Nome: " + cliente.getNome());
            System.out.println("CPF: " + FormatadorCpf.anonimizar(cliente.getCpf()));
            System.out.println("Telefone: " + cliente.getTelefone());
            System.out.println("Endereço: " + cliente.getEndereco());
            System.out.println("E-mail: " + cliente.getEmail());
        }
        return cliente;
    }


    private void editarCliente() {
        System.out.println("\n--- Edição de Cliente ---");
        Cliente cliente = consultarCliente(null);
        if (cliente == null) return;

        System.out.print("Novo Nome (ou Enter para manter): ");
        String novoNome = scanner.nextLine();
        if (novoNome.isEmpty()) novoNome = cliente.getNome();

        String novoTelefone = cliente.getTelefone();
        System.out.print("Novo Telefone (ou Enter para manter): ");
        String telefoneInput = scanner.nextLine();
        if (!telefoneInput.isEmpty()) {
            novoTelefone = obterDadoValido("Confirme o novo Telefone: ", new TelefoneValidate());
            novoTelefone = novoTelefone.replaceAll("\\D", "");
        }

        System.out.print("Novo Endereço (ou Enter para manter): ");
        String novoEndereco = scanner.nextLine();
        if (novoEndereco.isEmpty()) novoEndereco = cliente.getEndereco();

        String novoEmail = cliente.getEmail();
        System.out.print("Novo E-mail (ou Enter para manter): ");
        String emailInput = scanner.nextLine();
        if(!emailInput.isEmpty()){
            novoEmail = obterDadoValido("Confirme o novo E-mail: ", new EmailValidate());
        }

        boolean sucesso = gerenciadorCliente.editarCliente(novoNome, cliente.getCpf(), cliente.getCpf(), novoTelefone, novoEndereco, novoEmail);
        if (sucesso) System.out.println("Cliente atualizado com sucesso!");
    }

    private void removerCliente() {
        System.out.println("\n--- Remoção de Cliente ---");
        Cliente cliente = consultarCliente(null);
        if (cliente == null) return;

        boolean temCarros = gerenciadorCarros.listarTodos().stream().anyMatch(c -> c.getCpfDono().equals(cliente.getCpf()));
        if (temCarros) {
            System.out.println("ERRO: Não é possível remover este cliente, pois ele possui veículos cadastrados.");
            return;
        }

        System.out.print("Tem certeza que deseja remover o cliente " + cliente.getNome() + "? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            if (gerenciadorCliente.removerItemPorIdentificador(cliente.getCpf()))
                System.out.println("Cliente removido com sucesso.");
        } else {
            System.out.println("Remoção cancelada.");
        }
    }

    // --- LÓGICA DE VEÍCULOS ---

    private void cadastrarVeiculo() {
        System.out.println("\n--- Cadastro de Novo Veículo ---");
        // CORREÇÃO: Aplica a validação de CPF aqui
        String cpf = obterDadoValido("Digite o CPF do proprietário: ", new CpfValidate());
        cpf = cpf.replaceAll("\\D", "");
        Cliente cliente = gerenciadorCliente.buscarPorIdentificador(cpf);

        if (cliente == null) {
            System.out.print("Cliente não encontrado. Deseja cadastrar um novo cliente agora? (S/N): ");
            if (scanner.nextLine().equalsIgnoreCase("S")) {
                cliente = cadastrarCliente();
                if (cliente == null) return;
            } else {
                System.out.println("Cadastro de veículo cancelado.");
                return;
            }
        }
        // O resto da lógica permanece o mesmo
        System.out.println("Cliente selecionado: " + cliente.getNome());
        System.out.print("Fabricante: "); String fabricante = scanner.nextLine();
        System.out.print("Modelo: "); String modelo = scanner.nextLine();
        System.out.print("Placa: "); String placa = scanner.nextLine();
        System.out.print("Chassi: "); String chassi = scanner.nextLine();

        if (gerenciadorCarros.buscarPorIdentificador(chassi) != null) {
            System.out.println("ERRO: Já existe um veículo com este chassi.");
            return;
        }

        Carro novoCarro = gerenciadorCarros.cadastrarCarro(cliente, fabricante, modelo, placa, chassi);
        if (novoCarro != null) System.out.println("Veículo cadastrado com sucesso!");
    }


    private Carro consultarVeiculo() {
        System.out.println("\n--- Consulta de Veículo ---");
        System.out.print("Digite o chassi do veículo para buscar: ");
        String chassi = scanner.nextLine();
        Carro carro = gerenciadorCarros.buscarPorIdentificador(chassi);

        if (carro == null) {
            System.out.println("Veículo não encontrado.");
        } else {
            System.out.println("\nDados do Veículo:");
            System.out.println("Proprietário: " + carro.getNomeDono() + " (CPF: " + FormatadorCpf.anonimizar(carro.getCpfDono()) + ")");
            System.out.println("Fabricante: " + carro.getFabricante());
            System.out.println("Modelo: " + carro.getModelo());
            System.out.println("Placa: " + carro.getPlaca());
            System.out.println("Chassi: " + carro.getChassi());
        }
        return carro;
    }

    private void editarVeiculo() {
        Carro carro = consultarVeiculo();
        if (carro == null) return;

        System.out.print("Novo Fabricante (Enter para manter): ");
        String novoFab = scanner.nextLine();
        if (novoFab.isEmpty()) novoFab = carro.getFabricante();

        System.out.print("Novo Modelo (Enter para manter): ");
        String novoMod = scanner.nextLine();
        if (novoMod.isEmpty()) novoMod = carro.getModelo();

        System.out.print("Nova Placa (Enter para manter): ");
        String novaPlaca = scanner.nextLine();
        if (novaPlaca.isEmpty()) novaPlaca = carro.getPlaca();

        if (gerenciadorCarros.editarCarro(carro.getChassi(), novoFab, novoMod, novaPlaca)) {
            System.out.println("Veículo atualizado com sucesso!");
        }
    }

    private void removerVeiculo() {
        Carro carro = consultarVeiculo();
        if (carro == null) return;

        boolean temAgendamento = agenda.getDatasAgendadas().stream()
                .flatMap(data -> List.of(agenda.getHorariosDoDia(data)).stream())
                .anyMatch(ag -> ag != null && ag.getCarro().equals(carro));
        boolean temOS = gerenciadorOS.listarTodas().stream()
                .anyMatch(os -> os.getCarro().equals(carro) && !os.getStatusAtual().equals("Finalizada"));

        if (temAgendamento || temOS) {
            System.out.println("ERRO: Este veículo possui agendamentos ou Ordens de Serviço ativas.");
            return;
        }

        System.out.print("Tem certeza que deseja remover o veículo " + carro.getModelo() + "? (S/N): ");
        if (scanner.nextLine().equalsIgnoreCase("S")) {
            if (gerenciadorCarros.removerItemPorIdentificador(carro.getChassi())) {
                System.out.println("Veículo removido com sucesso.");
            }
        } else {
            System.out.println("Remoção cancelada.");
        }
    }

    // --- LÓGICA DE AGENDAMENTOS ---

    private void agendarServico() {
        System.out.println("\n--- Novo Agendamento ---");

        // CORREÇÃO: Aplica a validação de CPF aqui
        String cpf = obterDadoValido("Digite o CPF do cliente: ", new CpfValidate());
        Cliente cliente = consultarCliente(cpf); // Reutiliza o método de consulta
        if (cliente == null) return;

        // O resto da lógica permanece a mesma
        List<Carro> carrosDoCliente = gerenciadorCarros.listarTodos().stream()
                .filter(c -> c.getCpfDono().equals(cliente.getCpf()))
                .toList();
        if (carrosDoCliente.isEmpty()) {
            System.out.println("Este cliente não possui veículos. Cadastre um no menu 'Gerenciar Veículos'.");
            return;
        }

        System.out.println("Veículos do cliente:");
        for (int i = 0; i < carrosDoCliente.size(); i++) {
            Carro c = carrosDoCliente.get(i);
            System.out.printf("%d. %s %s (Placa: %s)\n", i + 1, c.getFabricante(), c.getModelo(), c.getPlaca());
        }
        System.out.print("Escolha o veículo (pelo número): ");
        int carroIndex = Integer.parseInt(scanner.nextLine()) - 1;
        Carro carroSelecionado = carrosDoCliente.get(carroIndex);

        List<Funcionario> mecanicos = gerenciadorFuncionario.listarTodos().stream()
                .filter(f -> "Mecanico".equals(f.getCargo()))
                .toList();

        System.out.println("Mecânicos disponíveis:");
        for (int i = 0; i < mecanicos.size(); i++) System.out.printf("%d. %s\n", i + 1, mecanicos.get(i).getNome());
        System.out.print("Escolha o mecânico (pelo número): ");
        Funcionario mecanicoSelecionado = mecanicos.get(Integer.parseInt(scanner.nextLine()) - 1);

        System.out.println("Tipos de Serviço:");
        for (TipoServico tipo : TipoServico.values()) System.out.printf("%d. %s\n", tipo.ordinal() + 1, tipo.name());
        System.out.print("Escolha o tipo de serviço (pelo número): ");
        TipoServico tipoSelecionado = TipoServico.values()[Integer.parseInt(scanner.nextLine()) - 1];

        System.out.print("Digite a data e hora do agendamento (dd/MM/yyyy HH:mm): ");
        try {
            LocalDateTime dataHora = LocalDateTime.parse(scanner.nextLine(), formatter);
            Agendamento novoAgendamento = new Agendamento(cliente, carroSelecionado, mecanicoSelecionado, tipoSelecionado, null, dataHora);
            if (agenda.agendar(novoAgendamento)) {
                System.out.println("Agendamento realizado com sucesso!");
            } else {
                System.out.println("ERRO: Horário indisponível ou inválido.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("ERRO: Formato de data/hora inválido.");
        }
    }


    private void consultarEGerenciarAgendamento() {
        System.out.println("\n--- Consulta de Agendamentos ---");
        System.out.print("Digite o CPF do cliente OU a data (dd/MM/yyyy) para buscar: ");
        String termoBusca = scanner.nextLine();

        Expressao expressaoDeBusca;
        try {
            expressaoDeBusca = new ExpressaoPorData(LocalDate.parse(termoBusca, dateFormatter));
        } catch (DateTimeParseException e) {
            Cliente cliente = gerenciadorCliente.buscarPorIdentificador(termoBusca.replaceAll("\\D",""));
            if (cliente == null) {
                System.out.println("Termo de busca inválido.");
                return;
            }
            expressaoDeBusca = new ExpressaoPorCliente(cliente);
        }

        List<Agendamento> resultados = expressaoDeBusca.interpreter(new ContextoDeBusca(agenda));
        if (resultados.isEmpty()) {
            System.out.println("Nenhum agendamento encontrado.");
            return;
        }

        System.out.println("Agendamentos encontrados:");
        for (int i = 0; i < resultados.size(); i++) {
            Agendamento ag = resultados.get(i);
            System.out.printf("%d. %s - %s - %s\n", i + 1, ag.getDataHora().format(formatter), ag.getCliente().getNome(), ag.getCarro().getModelo());
        }

        System.out.print("Escolha um agendamento para gerenciar (ou 0 para voltar): ");
        int escolha = Integer.parseInt(scanner.nextLine());
        if (escolha > 0 && escolha <= resultados.size()) {
            gerenciarAgendamentoSelecionado(resultados.get(escolha - 1));
        }
    }

    private void gerenciarAgendamentoSelecionado(Agendamento agendamento) {
        System.out.println("\n--- Gerenciando Agendamento de " + agendamento.getCliente().getNome() + " ---");
        System.out.println("1. Cancelar Agendamento");
        System.out.println("0. Voltar");
        System.out.print("Escolha: ");

        if ("1".equals(scanner.nextLine())) {
            confirmarCancelamento(agendamento);
        }
    }

    private void confirmarCancelamento(Agendamento agendamento) {
        System.out.print("Tem certeza que deseja cancelar este agendamento? (S/N): ");
        if (!scanner.nextLine().equalsIgnoreCase("S")) {
            System.out.println("Operação cancelada.");
            return;
        }

        // Regra de Negócio: Taxa de 20% para cancelamento no mesmo dia
        if (agendamento.getDataHora().toLocalDate().isEqual(LocalDate.now())) {
            double taxa = 150.0 * 0.20; // 20% da mão de obra fixa
            System.out.printf("AVISO: Cancelamento no dia do serviço gerou uma taxa de R$ %.2f.\n", taxa);
            String motivo = "Cancelamento no dia do serviço (" + dateFormatter.format(LocalDate.now()) + ")";
            GerenciadorFinanceiro.getInstance().registrarCobrancaCancelamento(new CancelamentoAgendamento(agendamento.getCliente(), taxa, motivo));
        }

        if(agenda.cancelarAgendamento(agendamento)) {
            System.out.println("Agendamento cancelado com sucesso.");
        } else {
            System.out.println("ERRO: Não foi possível cancelar o agendamento no sistema.");
        }
    }
}
