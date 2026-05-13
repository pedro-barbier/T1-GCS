package app;

import modelo.*;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

public class Sistema {
    private Scanner scanner = new Scanner(System.in);
    private Usuarios usuarios = new Usuarios(); // classe para gerenciar os usuários do sistema
    private Usuario usuarioAtual = null;
    private Autorizacoes autorizacoes = new Autorizacoes(); // classe para gerenciar as autorizações de exames

    public Sistema() {
        carregarDadosIniciais();
        System.out.println("=== Sistema de Autorização de Exames ===");
    }

    /* Apresenta o menu de opções para o usuário.
    As opções disponíveis dependem do tipo do usuário logado (administrador, médico ou paciente). */
    public void menu() {
        System.out.println("\nUsuário atual: " + (usuarioAtual != null ? usuarioAtual.toString() : "Nenhum"));
        System.out.println("[ 1 ] - Selecionar usuário");

        switch (usuarioAtual != null ? usuarioAtual.getTipo() : "") {
            case "Administrador":
                System.out.println("[ 2 ] - Criar novo usuário");
                System.out.println("[ 3 ] - Listar autorização de exames por paciente ou médico"); // deve ser possivel buscar por apenas parte do nome (usar startWith)
                System.out.println("[ 4 ] - Estatísticas gerais do sistema");
                System.out.println("[ 5 ] - Listar todos os usuários cadastrados no sistema.");
                System.out.println("[ 6 ] - Remover um usuário.");
                break;
            case "Paciente":
                System.out.println("[ 2 ] - Marcar um exame como realizado");
                System.out.println("[ 3 ] - Listar as suas autorizações de exame"); // deve ser em ordem de mais recente para mais antiga
                break;
            case "Médico":
                System.out.println("[ 2 ] - Criar autorização");
                System.out.println("[ 3 ] - Filtrar autorizações");
                break;
        }

        System.out.println("[ 0 ] - Sair");
        System.out.print("Opção: ");
    }

    /* Executa o sistema.
    O sistema continua em execução até que o usuário escolha sair. */
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
                    selecionarUsuario(); // Feature feita por Pedro Barbieri
                    continue;
                case 2:
                    if (usuarioAtual instanceof Administrador) {
                        criarNovoUsuario(); // Feature feita por Luiz Felipe
                    } else if (usuarioAtual instanceof Paciente) {
                        marcarExameComoRealizado(); // Feature feita por Lucas Mocelin e Eduardo Hoffmann
                    } else if (usuarioAtual instanceof Medico) {
                        // Feature feita por Levi
                    }
                    break;
                case 3:
                    if (usuarioAtual instanceof Medico) {
                        filtraAutorizacoes(); // Feature feita por Henrique Rolim
                    } else if (usuarioAtual instanceof Paciente) {
                        listarAutorizacoesPaciente(); // Feature feita por Lucas Mocelin, Eduardo Hoffmann e Letícia
                    } else if (usuarioAtual instanceof Administrador) {
                        buscarUsuarioEListarAutorizacoes(); // Feature feita por Letícia e Eduardo Hoffmann
                    }
                    break;
                case 4:
                    if (usuarioAtual instanceof Administrador) {
                        mostrarEstatisticasDoSistema(); // Feature feita por Henrique Rolim
                    } else {
                        System.out.println("Opção inválida. Tente novamente.");
                    }
                    break;
                case 5:
                    if (usuarioAtual instanceof Administrador) {
                        usuarios.listarTodosOsUsuariosCadastradosNoSistema(); // Feature feita por Luiz Felipe
                    } else {
                        System.out.println("Opção inválida. Tente novamente.");
                    }
                    break;
                case 6:
                    if (usuarioAtual instanceof Administrador) {
                        // Feature feita por Levi
                    } else {
                        System.out.println("Opção inválida. Tente novamente.");
                    }
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        }
    }

    /* Seleciona um usuário do sistema.
    O usuário é buscado pelo nome e definido como o usuário atual. */
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

    // Como Administrador criar um novo usuario.
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

        // falta implementar filtragem por paciente ou tipo de exame, de acordo com enunciado
    }
    private void buscarUsuarioEListarAutorizacoes() { //letícia e Eduardo Hoffmann
    System.out.println("\n=== BUSCAR MÉDICO OU PACIENTE E LISTAR AUTORIZAÇÕES ===");
    System.out.print("Digite parte do nome do usuário (médico ou paciente): ");
    String parte = scanner.nextLine().trim();

    List<Usuario> encontrados = usuarios.buscarPorParteDoNome(parte);

    // Filtra apenas médicos e pacientes
    List<Usuario> filtrados = new ArrayList<>();
    for (Usuario u : encontrados) {
        if (u instanceof Medico || u instanceof Paciente) {
            filtrados.add(u);
        }
    }

    if (filtrados.isEmpty()) {
        System.out.println("Nenhum médico ou paciente encontrado com esse nome.");
        return;
    }

    System.out.println("\nUsuários encontrados:");
    for (int i = 0; i < filtrados.size(); i++) {
        System.out.println("[" + i + "] " + usuarios.descreveUsuario(filtrados.get(i)));
    }
System.out.print("Escolha o número do usuário: ");
    int escolha;
    try {
        escolha = Integer.parseInt(scanner.nextLine());
        if (escolha < 0 || escolha >= filtrados.size()) {
            System.out.println("Opção inválida.");
            return;
        }
    } catch (Exception e) {
        System.out.println("Opção inválida.");
        return;
    }

    Usuario selecionado = filtrados.get(escolha);
    List<AutorizacaoExame> listaAut;

    if (selecionado instanceof Medico) {
        listaAut = autorizacoes.buscarPorMedico((Medico) selecionado);
    } else {
        listaAut = autorizacoes.buscarPorPaciente((Paciente) selecionado);
    }

    // Ordenação por data (mais antiga para mais recente)
    listaAut.sort((a, b) -> a.getDataCadastro().compareTo(b.getDataCadastro()));

    System.out.println("\nAutorizações de " + selecionado.getNome() + ":");
    if (listaAut.isEmpty()) {
        System.out.println("Nenhuma autorização encontrada.");
        return;
    }

    for (AutorizacaoExame aut : listaAut) {
        System.out.println(
            "Código: " + aut.getCodigo()
            + " | Data: " + aut.getDataCadastro()
            + " | Paciente: " + aut.getPaciente().getNome()
            + " | Médico: " + aut.getMedicoSolicitante().getNome()
            + " | Exame: " + aut.getTipoExame()
            + " | Realizado: " + (aut.isRealizado() ? aut.getDataRealizacao() : "Não")
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

        // aplicar total de autorizações emitidas que está faltando, de acordo com enunciado

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

    /* Carrega dados iniciais para o sistema.
    São criados um administrador, três médicos e cinco pacientes, além de várias autorizações de exames para demonstrar o funcionamento do sistema. */
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
