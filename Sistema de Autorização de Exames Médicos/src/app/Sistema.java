package app;

import modelo.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        System.out.println("\nUsuário atual: " + (usuarioAtual != null ? usuarioAtual.getNome() : "Nenhum"));
        System.out.println("[ 1 ] - Selecionar usuário");
        switch (usuarioAtual != null ? usuarioAtual.getTipo() : "") {
            case "Administrador":
                System.out.println("[ 2 ] - Criar novo usuário");
                System.out.println("[ 3 ] - Listar autorizações por paciente ou médico");
                System.out.println("[ 4 ] - Estatísticas gerais do sistema");
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
            int opcao = lerOpcao();
            switch (opcao) {
                case 0: System.out.println("Saindo..."); return;
                case 1: selecionarUsuario(); continue;
                case 2:
                    if (usuarioAtual instanceof Paciente) marcarExameComoRealizado();
                    else if (usuarioAtual instanceof Medico) criarAutorizacao();
                    break;
                case 3:
                    if (usuarioAtual instanceof Medico) filtrarAutorizacoesMedico();
                    break;
                case 4: break;
                default: System.out.println("Opção inválida. Tente novamente."); break;
            }
        }
    }

    private void selecionarUsuario() {
        System.out.println("\n=== Selecionar Usuário ===");
        System.out.print("Nome: ");
        Usuario usuario = usuarios.buscarPorNome(scanner.nextLine());
        if (usuario != null) {
            usuarioAtual = usuario;
            System.out.println("Selecionado: " + usuario.getNome() + " (" + usuario.getTipo() + ")");
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    private void criarAutorizacao() {
        System.out.println("\n=== Criar Autorização de Exame ===");
        Medico medicoAtual = (Medico) usuarioAtual;

        List<Paciente> pacientes = new ArrayList<>();
        for (Usuario u : usuarios.getUsuarios())
            if (u instanceof Paciente) pacientes.add((Paciente) u);

        if (pacientes.isEmpty()) { System.out.println("Nenhum paciente cadastrado."); return; }

        System.out.println("Selecione o paciente:");
        for (int i = 0; i < pacientes.size(); i++)
            System.out.println("  [ " + (i + 1) + " ] - " + pacientes.get(i).getNome());

        int idxPaciente = lerOpcao() - 1;
        if (idxPaciente < 0 || idxPaciente >= pacientes.size()) { System.out.println("Opção inválida."); return; }
        Paciente paciente = pacientes.get(idxPaciente);

        TipoExame[] tipos = TipoExame.values();
        System.out.println("Selecione o tipo de exame:");
        for (int i = 0; i < tipos.length; i++)
            System.out.println("  [ " + (i + 1) + " ] - " + tipos[i].getDescricao());

        int idxExame = lerOpcao() - 1;
        if (idxExame < 0 || idxExame >= tipos.length) { System.out.println("Opção inválida."); return; }

        AutorizacaoExame nova = new AutorizacaoExame(medicoAtual, paciente, tipos[idxExame]);
        autorizacoes.adicionarAutorizacao(nova);
        System.out.println("Autorização criada! Código: " + nova.getCodigo()
                + " | Paciente: " + paciente.getNome()
                + " | Exame: " + tipos[idxExame].getDescricao());
    }

    private void filtrarAutorizacoesMedico() {
        System.out.println("\n=== Filtrar Autorizações ===");
        System.out.println("[ 1 ] - Por paciente  [ 2 ] - Por tipo de exame");
        int opcao = lerOpcao();

        List<AutorizacaoExame> minhas = autorizacoes.buscarPorMedico((Medico) usuarioAtual);
        if (minhas.isEmpty()) { System.out.println("Você não possui autorizações."); return; }

        List<AutorizacaoExame> resultado = new ArrayList<>();

        if (opcao == 1) {
            List<Paciente> pacientes = new ArrayList<>();
            for (AutorizacaoExame a : minhas) {
                boolean existe = false;
                for (Paciente p : pacientes)
                    if (p.getIdentificador() == a.getPaciente().getIdentificador()) { existe = true; break; }
                if (!existe) pacientes.add(a.getPaciente());
            }
            System.out.println("Selecione o paciente:");
            for (int i = 0; i < pacientes.size(); i++)
                System.out.println("  [ " + (i + 1) + " ] - " + pacientes.get(i).getNome());

            int idx = lerOpcao() - 1;
            if (idx < 0 || idx >= pacientes.size()) { System.out.println("Opção inválida."); return; }
            for (AutorizacaoExame a : minhas)
                if (a.getPaciente().getIdentificador() == pacientes.get(idx).getIdentificador()) resultado.add(a);

        } else if (opcao == 2) {
            List<TipoExame> tipos = new ArrayList<>();
            for (AutorizacaoExame a : minhas)
                if (!tipos.contains(a.getTipoExame())) tipos.add(a.getTipoExame());

            System.out.println("Selecione o tipo de exame:");
            for (int i = 0; i < tipos.size(); i++)
                System.out.println("  [ " + (i + 1) + " ] - " + tipos.get(i).getDescricao());

            int idx = lerOpcao() - 1;
            if (idx < 0 || idx >= tipos.size()) { System.out.println("Opção inválida."); return; }
            for (AutorizacaoExame a : minhas)
                if (a.getTipoExame() == tipos.get(idx)) resultado.add(a);

        } else {
            System.out.println("Opção inválida.");
            return;
        }

        resultado.sort((a, b) -> a.getDataCadastro().compareTo(b.getDataCadastro()));
        exibirAutorizacoes(resultado);
    }

    private void marcarExameComoRealizado() {
        System.out.println("\n=== Marcar Exame como Realizado ===");

        List<AutorizacaoExame> pendentes = new ArrayList<>();
        for (AutorizacaoExame a : autorizacoes.buscarPorPaciente((Paciente) usuarioAtual))
            if (!a.isRealizado()) pendentes.add(a);

        if (pendentes.isEmpty()) { System.out.println("Você não tem exames pendentes."); return; }

        System.out.println("Seus exames pendentes:");
        for (int i = 0; i < pendentes.size(); i++)
            System.out.println("  [ " + (i + 1) + " ] - "
                    + pendentes.get(i).getTipoExame().getDescricao()
                    + " | Médico: " + pendentes.get(i).getMedicoSolicitante().getNome()
                    + " | Cadastrado em: " + pendentes.get(i).getDataCadastro());

        int idx = lerOpcao() - 1;
        if (idx < 0 || idx >= pendentes.size()) { System.out.println("Opção inválida."); return; }

        pendentes.get(idx).setDataRealizacao(LocalDate.now());
        System.out.println("Exame " + pendentes.get(idx).getTipoExame().getDescricao() + " marcado como realizado!");
    }

    private void exibirAutorizacoes(List<AutorizacaoExame> lista) {
        if (lista.isEmpty()) { System.out.println("Nenhuma autorização encontrada."); return; }
        for (AutorizacaoExame a : lista)
            System.out.println("  Código: " + a.getCodigo()
                    + " | Paciente: " + a.getPaciente().getNome()
                    + " | Exame: " + a.getTipoExame().getDescricao()
                    + " | Data: " + a.getDataCadastro()
                    + " | Realizado: " + (a.isRealizado() ? "Sim (" + a.getDataRealizacao() + ")" : "Não"));
    }

    private int lerOpcao() {
        System.out.print("Opção: ");
        try { return Integer.parseInt(scanner.nextLine()); }
        catch (Exception e) { return -1; }
    }

    private void carregarDadosIniciais() {
        Medico m1 = new Medico(101, "Pedro"), m2 = new Medico(102, "Maria"), m3 = new Medico(103, "João");
        Paciente p1 = new Paciente(1001, "Ana"), p2 = new Paciente(1002, "Carlos"),
                 p3 = new Paciente(1003, "Beatriz"), p4 = new Paciente(1004, "Lucas"),
                 p5 = new Paciente(1005, "Fernanda");

        for (Usuario u : new Usuario[]{new Administrador(1, "Cláudio"), m1, m2, m3, p1, p2, p3, p4, p5})
            usuarios.adicionarUsuario(u);

        for (AutorizacaoExame a : new AutorizacaoExame[]{
                new AutorizacaoExame(m1, p1, TipoExame.RAIOX),
                new AutorizacaoExame(m2, p2, TipoExame.TOMOGRAFIA),
                new AutorizacaoExame(m3, p3, TipoExame.RESSONANCIA),
                new AutorizacaoExame(m1, p4, TipoExame.ULTRASSONOGRAFIA),
                new AutorizacaoExame(m2, p5, TipoExame.ELETROCARDIOGRAMA),
                new AutorizacaoExame(m1, p5, TipoExame.COLONOSCOPIA),
                new AutorizacaoExame(m3, p1, TipoExame.ECOCARDIOGRAMA),
                new AutorizacaoExame(m2, p3, TipoExame.ANGIOGRAFIA),
                new AutorizacaoExame(m3, p2, TipoExame.PUNCAO)})
            autorizacoes.adicionarAutorizacao(a);
    }
}