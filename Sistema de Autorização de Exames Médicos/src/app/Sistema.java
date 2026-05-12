package app;

import modelo.*;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

public class Sistema {
    private Scanner scanner = new Scanner(System.in);
    private Usuarios usuarios = new Usuarios();
    private Usuario usuarioAtual = null;
    private Autorizacoes autorizacoes = new Autorizacoes();

    public Sistema() {
        carregarDadosIniciais();
        System.out.println("=== Sistema de Autorização de Exames ===");
    }

    public void menu() {
        System.out.println("\nUsuário atual: " + (usuarioAtual != null ? usuarioAtual.toString() : "Nenhum"));
        System.out.println("[ 1 ] - Selecionar usuário");

        switch (usuarioAtual != null ? usuarioAtual.getTipo() : "") {
            case "Administrador":
                System.out.println("[ 2 ] - Criar novo usuário");
                System.out.println("[ 3 ] - Listar autorização de exames por paciente ou médico");
                System.out.println("[ 4 ] - Estatísticas gerais do sistema");
                System.out.println("[ 5 ] - Listar todos os usuários cadastrados no sistema.");
                break;
            case "Paciente":
                System.out.println("[ 2 ] - Marcar um exame como realizado");
                System.out.println("[ 3 ] - Listar as suas autorizações de exame");
                break;
            case "Médico":
                System.out.println("[ 2 ] - Criar autorização");
                System.out.println("[ 3 ] - Filtrar autorizações");
                break;
        }

        System.out.println("[ 0 ] - Sair");
        System.out.print("Opção: ");
    }

    public void executar() {
        while (true) {
            menu();

            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Opção inválida. Tente novamente.");
                continue;
            }

            switch (opcao) {
                case 0:
                    System.out.println("Saindo...");
                    return;
                case 1:
                    selecionarUsuario();
                    continue;
                case 2:
                    if (usuarioAtual instanceof Administrador) {
                        criarNovoUsuario();
                    } else if (usuarioAtual instanceof Paciente) {
                        marcarExameComoRealizado();
                    }
                    break;
                case 3:
                    if (usuarioAtual instanceof Medico) {
                        filtraAutorizacoes();
                    } else if (usuarioAtual instanceof Paciente) {
                        listarAutorizacoesPaciente();
                    }
                    break;
                case 4:
                    if (usuarioAtual instanceof Administrador) {
                        mostrarEstatisticasDoSistema();
                    }
                    break;
                case 5:
                    if (usuarioAtual instanceof Administrador) {
                        usuarios.listarTodosOsUsuariosCadastradosNoSistema();
                    }
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    private void selecionarUsuario() {
        System.out.println("\n=== Selecionar Usuário ===");
        System.out.print("Digite o Nome do usuário: ");
        String nome = scanner.nextLine();

        Usuario usuario = usuarios.buscarPorNome(nome);
        if (usuario != null) {
            System.out.println("Usuário selecionado: " + usuario.toString());
            usuarioAtual = usuario;
        } else {
            System.out.println("Usuário não encontrado. Tente novamente.");
        }
    }

    private void criarNovoUsuario() {
        System.out.print("\nDeseja criar qual tipo de Usuário [Administrador, Médico, Paciente]: ");
        String tipo = scanner.nextLine().trim();

        Integer id = lerIdUsuario();
        if (id == null) { return; }

        System.out.print("Nome do novo Usuário: ");
        String nome = scanner.nextLine().trim();

        Usuario usuario = criarUsuario(tipo, id, nome);
        if (usuario == null) {
            System.out.println("Usuário não foi criado. Tipo de usuário não existente.");
            return;
        }

        usuarios.adicionarUsuario(usuario);
        System.out.printf("Novo usuário criado: " + usuarios.descreveUsuario(usuario) + "\n");
    }

    private Integer lerIdUsuario() {
        System.out.print("Id do novo Usuário: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();

            if (usuarios.buscarPorID(id) != null) { return null; }

            return id;
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Erro: Id deve ser composto apenas por números.");
            return null;
        }
    }

    private Usuario criarUsuario(String tipo, int id, String nome) {
        switch (tipo.toLowerCase()) {
            case "administrador":
                return new Administrador(id, nome);
            case "paciente":
                return new Paciente(id, nome);
            case "médico":
            case "medico":
                return new Medico(id, nome);
            default:
                return null;
        }
    }

    private void filtraAutorizacoes() {
        Medico medico = (Medico) usuarioAtual;

        System.out.println("\n=== AUTORIZAÇÕES DO MÉDICO ===");

        for (AutorizacaoExame autorizacao : autorizacoes.buscarPorMedico(medico)) {
            System.out.println(
                "Código: " + autorizacao.getCodigo()
                + " | Paciente: " + autorizacao.getPaciente().getNome()
                + " | Exame: " + autorizacao.getTipoExame()
            );
        }
    }

    private void mostrarEstatisticasDoSistema() {
        System.out.println("\n=== ESTATISTICAS DO SISTEMA ===");

        System.out.println("Total de médicos: " +
            Estatisticas.contarMedicos(usuarios.getUsuarios()));

        System.out.println("Total de pacientes: " +
            Estatisticas.contarPacientes(usuarios.getUsuarios()));

        System.out.println("Total de administradores: " +
            Estatisticas.contarAdministradores(usuarios.getUsuarios()));

        System.out.println("Total de usuários: " +
            Estatisticas.contarUsuarios(usuarios.getUsuarios()));

        System.out.println("Percentual de exames realizados: " +
            Estatisticas.percentualExamesRealizados(autorizacoes.getAutorizacoes()) + "%");
    }

    private void marcarExameComoRealizado() {
        Paciente paciente = (Paciente) usuarioAtual;

        System.out.println("\n=== MARCAR EXAME COMO REALIZADO ===");

        List<AutorizacaoExame> lista = autorizacoes.buscarPorPaciente(paciente);

        if (lista.isEmpty()) {
            System.out.println("Nenhuma autorização encontrada.");
            return;
        }

        for (AutorizacaoExame autorizacao : lista) {
            System.out.println(
                "Código: " + autorizacao.getCodigo()
                + " | Exame: " + autorizacao.getTipoExame()
                + " | Data Solicitação: " + autorizacao.getDataCadastro()
                + " | Realizado: " + autorizacao.isRealizado()
            );
        }

        try {
            System.out.print("Digite o código da autorização: ");
            int codigo = Integer.parseInt(scanner.nextLine());

            System.out.print("Digite a data de realização (AAAA-MM-DD): ");
            String dataTexto = scanner.nextLine();

            java.time.LocalDate dataRealizacao = java.time.LocalDate.parse(dataTexto);

            autorizacoes.marcarComoRealizado(codigo, paciente, dataRealizacao);
        } catch (Exception e) {
            System.out.println("Dados inválidos.");
        }
    }

    private void listarAutorizacoesPaciente() {
        Paciente paciente = (Paciente) usuarioAtual;

        System.out.println("\n=== SUAS AUTORIZAÇÕES ===");

        List<AutorizacaoExame> lista = autorizacoes.buscarPorPaciente(paciente);

        if (lista.isEmpty()) {
            System.out.println("Nenhuma autorização encontrada.");
            return;
        }

        for (AutorizacaoExame autorizacao : lista) {
            System.out.println(
                "Código: " + autorizacao.getCodigo()
                + " | Exame: " + autorizacao.getTipoExame()
                + " | Data Solicitação: " + autorizacao.getDataCadastro()
                + " | Realizado: "
                + (autorizacao.isRealizado() ? autorizacao.getDataRealizacao() : "Não")
            );
        }
    }

    private void carregarDadosIniciais() {
        Administrador admin = new Administrador(1, "Cláudio");

        Medico medico1 = new Medico(101, "Pedro");
        Medico medico2 = new Medico(102, "Maria");
        Medico medico3 = new Medico(103, "João");

        Paciente paciente1 = new Paciente(1001, "Ana");
        Paciente paciente2 = new Paciente(1002, "Carlos");
        Paciente paciente3 = new Paciente(1003, "Beatriz");
        Paciente paciente4 = new Paciente(1004, "Lucas");
        Paciente paciente5 = new Paciente(1005, "Fernanda");

        usuarios.adicionarUsuario(admin);
        usuarios.adicionarUsuario(medico1);
        usuarios.adicionarUsuario(medico2);
        usuarios.adicionarUsuario(medico3);
        usuarios.adicionarUsuario(paciente1);
        usuarios.adicionarUsuario(paciente2);
        usuarios.adicionarUsuario(paciente3);
        usuarios.adicionarUsuario(paciente4);
        usuarios.adicionarUsuario(paciente5);

        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(LocalDate.of(2026, 1, 21) ,medico1, paciente1, TipoExame.RAIOX));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(LocalDate.of(2026, 2, 15), medico2, paciente2, TipoExame.TOMOGRAFIA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(LocalDate.of(2026, 3, 10), medico3, paciente3, TipoExame.RESSONANCIA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(LocalDate.of(2026, 3, 5), medico1, paciente4, TipoExame.ULTRASSONOGRAFIA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(LocalDate.of(2026, 2, 12), medico2, paciente5, TipoExame.ELETROCARDIOGRAMA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(LocalDate.of(2025, 10, 18), medico1, paciente5, TipoExame.COLONOSCOPIA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(LocalDate.of(2026, 4, 22), medico3, paciente1, TipoExame.ECOCARDIOGRAMA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(LocalDate.of(2026, 5, 11), medico2, paciente3, TipoExame.ANGIOGRAFIA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(LocalDate.of(2025, 12, 15), medico3, paciente2, TipoExame.PUNCAO));
    }
}