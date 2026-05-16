package app;

import modelo.*;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

// Classe responsável por controlar o fluxo principal do sistema,
// incluindo menus, login, cadastro de usuários e operações sobre autorizações.
public class Sistema {
    private Scanner scanner = new Scanner(System.in);
    private Usuarios usuarios = new Usuarios(); // classe para gerenciar os usuários do sistema
    private Usuario usuarioAtual = null;
    private Autorizacoes autorizacoes = new Autorizacoes(); // classe para gerenciar as autorizações de exames

    public Sistema() {
        carregarDadosIniciais();
        System.out.println("=== Sistema de Autorização de Exames ===");
    }

    /*
     * Apresenta o menu de opções para o usuário.
     * As opções disponíveis dependem do tipo do usuário logado (administrador,
     * médico ou paciente).
     */
    public void menu() {
        System.out.println("\nUsuário atual: " + (usuarioAtual != null ? usuarioAtual.toString() : "Nenhum"));
        System.out.println("[ 1 ] - Selecionar usuário");

        switch (usuarioAtual != null ? usuarioAtual.getTipo() : "") {
            case "Administrador":
                System.out.println("[ 2 ] - Criar novo usuário");
                System.out.println("[ 3 ] - Listar autorização de exames por paciente ou médico"); // deve ser possivel
                                                                                                   // buscar por apenas
                                                                                                   // parte do nome
                                                                                                   // (usar startWith)
                System.out.println("[ 4 ] - Estatísticas gerais do sistema");
                System.out.println("[ 5 ] - Listar todos os usuários cadastrados no sistema.");
                System.out.println("[ 6 ] - Remover um usuário.");
                break;
            case "Paciente":
                System.out.println("[ 2 ] - Marcar um exame como realizado");
                System.out.println("[ 3 ] - Listar as suas autorizações de exame"); // deve ser em ordem de mais recente
                                                                                    // para mais antiga
                break;
            case "Médico":
                System.out.println("[ 2 ] - Criar autorização");
                System.out.println("[ 3 ] - Filtrar autorizações");
                break;
        }

        System.out.println("[ 0 ] - Sair");
        System.out.print("Opção: ");
    }

    /*
     * Executa o sistema.
     * O sistema continua em execução até que o usuário escolha sair.
     */
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
                        criarAutorizacao(); // Feature feita por Levi e Pedro Barbieri
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
                        removerUsuario(); // Feature feita por Levi
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

    /*
     * Seleciona um usuário do sistema.
     * O usuário é buscado pelo nome e definido como o usuário atual.
     */
    private void selecionarUsuario() {
        System.out.println("\n=== Selecionar Usuário ===");
        System.out.print("Digite o Nome do usuário: ");
        String nome = scanner.nextLine().trim();

        if (!ValidadorDeDados.isNomeValido(nome)) {
            System.out.println("Nome inválido. Informe apenas letras e espaços.");
            return;
        }

        Usuario usuario = usuarios.buscarPorNome(nome);
        if (usuario != null) {
            System.out.println("Usuário selecionado: " + usuario.toString());
            usuarioAtual = usuario;
        } else {
            System.out.println("Usuário não encontrado. Tente novamente.");
        }
    }

    /*
     * Cria uma nova autorização de exame para um paciente.
     * O método lista os pacientes cadastrados, permite selecionar o tipo de exame
     * e registra a autorização vinculada ao médico atualmente logado.
     */
    private void criarAutorizacao() {
        int temp = 0;
        List<Usuario> pacientes = new ArrayList<>();

        System.out.println("\n=== Criar autorização ===");

        System.out.println("\n--- Selecionar paciente ---");
        int contador = 1;
        for (Usuario user : usuarios.getUsuarios()) {
            if (user instanceof Paciente) {
                pacientes.add(user);
                System.out.println("[ " + contador + " ] " + user.getNome());
                contador++;
            }
        }
        System.out.print("A qual paciente deseja conceder a autorização? ");
        try {
            temp = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("\nOpção inválida.");
            return;
        }

        Usuario paciente;
        try {
            paciente = pacientes.get(temp - 1);
        } catch (Exception e) {
            System.out.println("Opção inválida.");
            return;
        }

        System.out.println("\n--- Selecionar tipo de exame ---");
        contador = 1;
        for (TipoExame exame : TipoExame.values()) {
            System.out.println("[ " + contador + " ] " + exame.getDescricao());
            contador++;
        }
        System.out.print("Qual seria o exame para o paciente? ");
        try {
            temp = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("\nOpção inválida.");
            return;
        }

        TipoExame exame;
        try {
            exame = TipoExame.values()[temp - 1];
        } catch (Exception e) {
            System.out.println("Opção inválida.");
            return;
        }

        AutorizacaoExame aut_exame = new AutorizacaoExame(LocalDate.now(), (Medico) usuarioAtual, (Paciente) paciente,
                exame);
        autorizacoes.adicionarAutorizacao(aut_exame);

        System.out.println("\nAutorização criada com sucesso!");
        System.out.println("Identificador: " + aut_exame.getCodigo());
        System.out.println("Data de registro: " + aut_exame.getDataCadastro());
        System.out.println("Paciente: " + aut_exame.getPaciente().getNome());
        System.out.println("Exame pedido: " + aut_exame.getTipoExame().getDescricao());
        System.out.println("Médico solicitante: " + aut_exame.getMedicoSolicitante().getNome());
    }

    /*
     * Cria um novo usuário no sistema a partir dos dados informados pelo
     * administrador.
     * O método valida o tipo, o identificador e o nome antes de cadastrar o
     * usuário.
     */
    private void criarNovoUsuario() {
        System.out.print("\nDeseja criar qual tipo de Usuário [Administrador, Médico, Paciente]: ");
        String tipo = scanner.nextLine().trim();
        if (!ValidadorDeDados.isTextoValido(tipo)) {
            System.out.println("Tipo de usuário inválido.");
            return;
        }

        Integer id = lerIdUsuario();
        if (id == null) {
            return;
        }

        System.out.print("Nome do novo Usuário: ");
        String nome = scanner.nextLine().trim();
        if (!ValidadorDeDados.isNomeValido(nome)) {
            System.out.println("Nome inválido. Informe apenas letras e espaços.");
            return;
        }

        Usuario usuario = criarUsuario(tipo, id, nome);
        if (usuario == null) {
            System.out.println("Usuário não foi criado. Tipo de usuário não existente.");
            return;
        }

        usuarios.adicionarUsuario(usuario);
        System.out.printf("Novo usuário criado: " + usuarios.descreveUsuario(usuario) + "\n");
    }

    /*
     * Remove um usuário cadastrado a partir do identificador informado.
     * A operação é disponibilizada para administradores e utiliza a lista de
     * usuários do sistema.
     */
    private void removerUsuario() {
        System.out.println("\n=== REMOVER UM USUÁRIO ===");
        usuarios.listarTodosOsUsuariosCadastradosNoSistema();

        System.out.print("ID do usuario a remover: ");
        int idUsuario;
        try {
            idUsuario = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Opção inválida.");
            return;
        }

        Usuario user = usuarios.buscarPorID(idUsuario);

        if (usuarios.removerUsuario(idUsuario)) {
            System.out.println("Usuario removido com sucesso: " + user.toString());
        }
    }

    /*
     * Lê e valida o identificador informado para um novo usuário.
     * Retorna null quando o valor é inválido ou quando já existe um usuário com o
     * mesmo Id.
     */
    private Integer lerIdUsuario() {
        System.out.print("Id do novo Usuário: ");
        try {
            int id = scanner.nextInt();
            scanner.nextLine();
            if (!ValidadorDeDados.isIdentificadorValido(id)) {
                System.out.println("Erro: Id deve ser maior que zero.");
                return null;
            }

            if (usuarios.buscarPorID(id) != null) {
                System.out.println("Erro: Já existe um usuário com esse Id.");
                return null;
            }

            return id;
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Erro: Id deve ser composto apenas por números.");
            return null;
        }
    }

    /*
     * Instancia o tipo correto de usuário com base no texto informado.
     * Retorna null quando o tipo não corresponde a administrador, médico ou
     * paciente.
     */
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

    /*
     * Filtra as autorizações de exame conforme o critério escolhido pelo médico.
     * Permite buscar autorizações por paciente ou por tipo de exame.
     */
    private void filtraAutorizacoes() {

        System.out.println("\nFiltrar por:");
        System.out.println("[1] Paciente");
        System.out.println("[2] Tipo de exame");

        int filtro;
        try {
            filtro = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Opção inválida.");
            return;
        }

        if (filtro == 1) {

            System.out.print("Nome do paciente: ");
            String nomePaciente = scanner.nextLine();

            Paciente paciente = (Paciente) usuarios.buscarPorNome(nomePaciente);

            if (paciente != null) {

                for (AutorizacaoExame autorizacao : autorizacoes.listarOrdenadoPorData(paciente)) {

                    System.out.println(
                            "Código: " + autorizacao.getCodigo()
                                    + " | Exame: " + autorizacao.getTipoExame()
                                    + " | Data de cadastro: " + autorizacao.getDataCadastro()
                                    + " | Realizado: "
                                    + (autorizacao.isRealizado() ? autorizacao.getDataRealizacao() : "Não"));
                }

            } else {
                System.out.println("Paciente não encontrado.");
            }

        } else if (filtro == 2) {

            System.out.print("Tipo do exame: ");
            String tipo = scanner.nextLine();

            TipoExame tipoExame = TipoExame.validaTipoExame(tipo);

            for (AutorizacaoExame autorizacao : autorizacoes.listarOrdenadoPorData(tipoExame)) {

                System.out.println(
                        "Código: " + autorizacao.getCodigo()
                                + " | Paciente: " + autorizacao.getPaciente().getNome()
                                + " | Data de cadastro: " + autorizacao.getDataCadastro()
                                + " | Realizado: "
                                + (autorizacao.isRealizado() ? autorizacao.getDataRealizacao() : "Não"));
            }

        } else {
            System.out.println("Filtro inválido.");
        }
    }

    /*
     * Busca médicos ou pacientes por parte do nome e lista suas autorizações.
     * O método permite ao administrador selecionar um usuário encontrado
     * e visualizar as autorizações vinculadas a ele.
     */
    private void buscarUsuarioEListarAutorizacoes() {
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
                            + " | Realizado: " + (aut.isRealizado() ? aut.getDataRealizacao() : "Não"));
        }
    }

    /*
     * Exibe estatísticas gerais do sistema para o administrador.
     * As informações são calculadas a partir das listas de usuários e autorizações
     * cadastradas.
     */
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

        System.out.println("Total de autorizações: " +
                autorizacoes.getAutorizacoes().size());

        System.out.println("Percentual de exames realizados: " +
                Estatisticas.percentualExamesRealizados(autorizacoes.getAutorizacoes()) + "%");
    }

    /*
     * Permite ao paciente marcar uma autorização de exame como realizada.
     * O método lista as autorizações do paciente atual, solicita o código e
     * registra a data de realização.
     */
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
                            + " | Realizado: " + autorizacao.isRealizado());
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

    /*
     * Lista as autorizações de exame do paciente atualmente logado.
     * As autorizações são exibidas da mais recente para a mais antiga.
     */
    private void listarAutorizacoesPaciente() {
        Paciente paciente = (Paciente) usuarioAtual;

        System.out.println("\n=== SUAS AUTORIZAÇÕES ===");

        List<AutorizacaoExame> lista = autorizacoes.buscarPorPaciente(paciente);

        if (lista.isEmpty()) {
            System.out.println("Nenhuma autorização encontrada.");
            return;
        }

        // Ordena da mais recente para a mais antiga pela data de cadastro
        lista.sort((a, b) -> b.getDataCadastro().compareTo(a.getDataCadastro()));

        for (AutorizacaoExame autorizacao : lista) {
            System.out.println(
                    "Código: " + autorizacao.getCodigo()
                            + " | Exame: " + autorizacao.getTipoExame()
                            + " | Data Solicitação: " + autorizacao.getDataCadastro()
                            + " | Realizado: "
                            + (autorizacao.isRealizado() ? autorizacao.getDataRealizacao() : "Não"));
        }
    }

    /*
     * Carrega dados iniciais para o sistema.
     * São criados um administrador, três médicos e cinco pacientes, além de várias
     * autorizações de exames para demonstrar o funcionamento do sistema.
     */
    private void carregarDadosIniciais() {
        Administrador admin = new Administrador(1, "Cláudio");
        Administrador admin2 = new Administrador(2, "Renata");

        Medico medico1 = new Medico(101, "Pedro");
        Medico medico2 = new Medico(102, "Maria");
        Medico medico3 = new Medico(103, "João");
        Medico medico4 = new Medico(104, "Juliana");
        Medico medico5 = new Medico(105, "Ricardo");
        Medico medico6 = new Medico(106, "Patrícia");

        Paciente paciente1 = new Paciente(1001, "Ana");
        Paciente paciente2 = new Paciente(1002, "Carlos");
        Paciente paciente3 = new Paciente(1003, "Beatriz");
        Paciente paciente4 = new Paciente(1004, "Lucas");
        Paciente paciente5 = new Paciente(1005, "Fernanda");
        Paciente paciente6 = new Paciente(1006, "Rafael");
        Paciente paciente7 = new Paciente(1007, "Camila");
        Paciente paciente8 = new Paciente(1008, "Thiago");
        Paciente paciente9 = new Paciente(1009, "Larissa");
        Paciente paciente10 = new Paciente(1010, "Bruno");
        Paciente paciente11 = new Paciente(1011, "Mariana");
        Paciente paciente12 = new Paciente(1012, "Felipe");

        usuarios.adicionarUsuario(admin);
        usuarios.adicionarUsuario(admin2);
        usuarios.adicionarUsuario(medico1);
        usuarios.adicionarUsuario(medico2);
        usuarios.adicionarUsuario(medico3);
        usuarios.adicionarUsuario(medico4);
        usuarios.adicionarUsuario(medico5);
        usuarios.adicionarUsuario(medico6);
        usuarios.adicionarUsuario(paciente1);
        usuarios.adicionarUsuario(paciente2);
        usuarios.adicionarUsuario(paciente3);
        usuarios.adicionarUsuario(paciente4);
        usuarios.adicionarUsuario(paciente5);
        usuarios.adicionarUsuario(paciente6);
        usuarios.adicionarUsuario(paciente7);
        usuarios.adicionarUsuario(paciente8);
        usuarios.adicionarUsuario(paciente9);
        usuarios.adicionarUsuario(paciente10);
        usuarios.adicionarUsuario(paciente11);
        usuarios.adicionarUsuario(paciente12);

        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 1, 21), medico1, paciente1, TipoExame.RAIOX));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 2, 15), medico2, paciente2, TipoExame.TOMOGRAFIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 3, 10), medico3, paciente3, TipoExame.RESSONANCIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 3, 5), medico1, paciente4, TipoExame.ULTRASSONOGRAFIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 2, 12), medico2, paciente5, TipoExame.ELETROCARDIOGRAMA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2025, 10, 18), medico1, paciente5, TipoExame.COLONOSCOPIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 4, 22), medico3, paciente1, TipoExame.ECOCARDIOGRAMA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 5, 11), medico2, paciente3, TipoExame.ANGIOGRAFIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2025, 12, 15), medico3, paciente2, TipoExame.PUNCAO));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 1, 8), medico4, paciente6, TipoExame.RAIOX));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 1, 30), medico5, paciente7, TipoExame.TOMOGRAFIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 2, 3), medico6, paciente8, TipoExame.RESSONANCIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 2, 20), medico4, paciente9, TipoExame.ULTRASSONOGRAFIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 3, 17), medico5, paciente10, TipoExame.ELETROCARDIOGRAMA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 3, 25), medico6, paciente11, TipoExame.COLONOSCOPIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 4, 1), medico4, paciente12, TipoExame.ECOCARDIOGRAMA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 4, 14), medico5, paciente1, TipoExame.ANGIOGRAFIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 4, 29), medico6, paciente2, TipoExame.PUNCAO));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 5, 2), medico1, paciente7, TipoExame.TOMOGRAFIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 5, 6), medico2, paciente8, TipoExame.RAIOX));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 5, 9), medico3, paciente9, TipoExame.ECOCARDIOGRAMA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2025, 11, 4), medico4, paciente3, TipoExame.RESSONANCIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2025, 11, 22), medico5, paciente4, TipoExame.COLONOSCOPIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2025, 12, 1), medico6, paciente5, TipoExame.ULTRASSONOGRAFIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2025, 12, 28), medico1, paciente10, TipoExame.ELETROCARDIOGRAMA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 1, 15), medico2, paciente11, TipoExame.ANGIOGRAFIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 2, 7), medico3, paciente12, TipoExame.PUNCAO));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 3, 30), medico4, paciente10, TipoExame.RAIOX));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 4, 8), medico5, paciente12, TipoExame.RESSONANCIA));
        autorizacoes.adicionarAutorizacao(
                new AutorizacaoExame(LocalDate.of(2026, 5, 13), medico6, paciente6, TipoExame.TOMOGRAFIA));
    }
}
