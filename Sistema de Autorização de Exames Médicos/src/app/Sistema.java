package app;

import modelo.*;

import java.util.Scanner;

public class Sistema {
    private Scanner scanner = new Scanner(System.in);
    private Usuarios usuarios = new Usuarios();
    private Usuario usuarioAtual = null;
    private Autorizacoes autorizacoes = new Autorizacoes();

    public Sistema() {
        carregarDadosIniciais();
    }

    public void executar() {
        System.out.println("=== Sistema de Autorização de Exames ===");
        while (true) {
            System.out.println("\nUsuário atual: " 
                + (usuarioAtual != null ? usuarioAtual : "Nenhum"));
            System.out.println("1 - Selecionar usuário");
            System.out.println("0 - Sair");
            

            System.out.print("Opção: ");
            String opcao = scanner.nextLine();
            if (opcao.equals("0")) break;
        }
        

    }

    private void carregarDadosIniciais() {
        Administrador admin = new Administrador(01, "Cláudio", "Administrador");
        Medico medico1 = new Medico(101, "Pedro", "Médico");
        Medico medico2 = new Medico(102, "Maria", "Médico");
        Medico medico3 = new Medico(103, "João", "Médico");
        Paciente paciente1 = new Paciente(1001, "Ana", "Paciente");
        Paciente paciente2 = new Paciente(1002, "Carlos", "Paciente");
        Paciente paciente3 = new Paciente(1003, "Beatriz", "Paciente");
        Paciente paciente4 = new Paciente(1004, "Lucas", "Paciente");
        Paciente paciente5 = new Paciente(1005, "Fernanda", "Paciente");
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