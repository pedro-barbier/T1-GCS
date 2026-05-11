package app;

import modelo.*;

import java.util.Scanner;

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
        System.out.println("\nUsuário atual: " + (usuarioAtual != null ? usuarioAtual : "Nenhum"));
        System.out.println("[ 1 ] - Selecionar usuário");

        switch (usuarioAtual != null ? usuarioAtual.getTipo() : "") {
            case "Administrador":
                System.out.println("[ 2 ] - Criar novo usuário");
                System.out.println("[ 3 ] - Listar autorização de exames por paciente ou médico"); // deve ser possivel buscar por apenas parte do nome (usar startWith)
                System.out.println("[ 4 ] - Estatísticas gerais do sistema");
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
                    selecionarUsuario();
                    continue;
                case 2:
                    break; // aplicar ações específicas para cada tipo de usuário
                case 3:

                    if (usuarioAtual instanceof Medico) {

                        Medico medico = (Medico) usuarioAtual;

                        System.out.println("\n=== AUTORIZAÇÕES DO MÉDICO ===");

                        for (AutorizacaoExame autorizacao :
                                autorizacoes.buscarPorMedico(medico)) {

                            System.out.println(
                               "Código: " + autorizacao.getCodigo()
                                + " | Paciente: " + autorizacao.getPaciente().getNome()
                                + " | Exame: " + autorizacao.getTipoExame()
                            );
                        }

                        // falta implementar filtragem por paciente ou tipo de exame, de acordo com enunciado
                    }
                    
                    break; // aplicar ações específicas para cada tipo de usuário
                case 4:

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
                    
                    break; // aplicar ações específicas para cada tipo de usuário
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
            System.out.println("Usuário selecionado: " + usuario);
            usuarioAtual = usuario;
        } else {
            System.out.println("Usuário não encontrado. Tente novamente.");
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

        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(medico1, paciente1, TipoExame.RAIOX));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(medico2, paciente2, TipoExame.TOMOGRAFIA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(medico3, paciente3, TipoExame.RESSONANCIA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(medico1, paciente4, TipoExame.ULTRASSONOGRAFIA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(medico2, paciente5, TipoExame.ELETROCARDIOGRAMA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(medico1, paciente5, TipoExame.COLONOSCOPIA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(medico3, paciente1, TipoExame.ECOCARDIOGRAMA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(medico2, paciente3, TipoExame.ANGIOGRAFIA));
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(medico3, paciente2, TipoExame.PUNCAO));
    }
}
