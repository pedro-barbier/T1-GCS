package modelo;

import java.util.List;

public class Estatisticas {
    public static int contarMedicos(List<Usuario> usuarios) {

        int total = 0;

        for (Usuario u: usuarios) {

            if (u instanceof Medico) {
                total++;
            }
        }

        return total;
    }

    public static int contarPacientes(List<Usuario> usuarios) {

        int total = 0;

        for (Usuario u : usuarios) {

            if (u instanceof Paciente) {
                total++;
            }
        }

        return total;
    }

    public static int contarAdministradores(List<Usuario> usuarios) {

        int total = 0;

        for (Usuario u : usuarios) {

            if (u instanceof Administrador) {
                total++;
            }
        }

        return total;
    }

    public static int contarUsuarios(List<Usuario> usuarios) {

        return usuarios.size();
    }

    public static double percentualExamesRealizados(List<AutorizacaoExame> autorizacoes) {
        int realizados = 0;

        for (AutorizacaoExame a : autorizacoes) {
            
            if (a.getDataRealizacao() != null) {
                realizados++;
            }
        }

        if (autorizacoes.isEmpty()) {
            return 0;
        }

        return (realizados * 100.0) / autorizacoes.size();
    }
}
