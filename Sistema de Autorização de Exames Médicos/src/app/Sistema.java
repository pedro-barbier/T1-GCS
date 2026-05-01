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
        // Administrador só recebe ID e Nome
        Administrador admin = new Administrador(1, "Cláudio");
        
        // Médicos recebem ID, Nome e um CRM válido!
        Medico medico1 = new Medico(101, "Pedro", "CRM-RS 12345");
        Medico medico2 = new Medico(102, "Maria", "CRM-SP 54321");
        Medico medico3 = new Medico(103, "João", "CRM-RJ 98765");
        
        // Pacientes recebem só ID e Nome (já que ainda não colocamos o CPF no construtor)
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

        // A parte das autorizações continua igualzinha!
        autorizacoes.adicionarAutorizacao(new AutorizacaoExame(medico1, paciente1, TipoExame.RAIOX));
        // ... (mantenha o resto das suas autorizações)
    }
}