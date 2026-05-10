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
}
