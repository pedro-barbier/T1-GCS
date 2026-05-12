package modelo;

import java.util.ArrayList;
import java.util.List;

public class Usuarios {
    private List<Usuario> usuarios = new ArrayList<>();

    public void adicionarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public Usuario buscarPorID(int identificador) {

        for (Usuario usuario : usuarios){

            if (usuario.getIdentificador() == identificador) {
                return usuario;
            }
        }
        
        return null;
    }

    public Usuario buscarPorNome(String nome) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNome().equals(nome)) {
                return usuario;
            }
        }
        return null;
    }
    public List<Usuario> buscarPorParteDoNome(String parte) {
    List<Usuario> resultado = new ArrayList<>();
    for (Usuario usuario : usuarios) {
        if (usuario.getNome().toLowerCase().contains(parte.toLowerCase())) {
            resultado.add(usuario);
        }
    }
    return resultado;
}
    public List<Usuario> getUsuarios() {
        List<Usuario> temp = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            temp.add(usuario);
        }
        return temp;
    }

    public String descreveUsuario(Usuario usuario){
        return "Id:" + usuario.getIdentificador() + ", Nome:" + usuario.getNome() + ", Tipo:" + usuario.getTipo();
    }

    public void listarTodosOsUsuariosCadastradosNoSistema(){
        List<Usuario> administradores = new ArrayList<>();
        List<Usuario> pacientes = new ArrayList<>();
        List<Usuario> medicos = new ArrayList<>();
        for (Usuario usuario : usuarios){
            switch (usuario.getTipo()) {
                case "Administrador":
                    administradores.add(usuario);
                    break;
                case "Paciente":
                    pacientes.add(usuario);
                    break;
                case "Médico":
                    medicos.add(usuario);
                    break;
            }
        }

        System.out.println("\nUsuarios Cadastrados no Sistema:");
        System.out.println("Administradores:");
        for (Usuario usuario : administradores){
            System.out.println(descreveUsuario(usuario));
        }
        System.out.println();

        System.out.println("Paciente:");
        for (Usuario usuario : pacientes){
            System.out.println(descreveUsuario(usuario));
        }
        System.out.println();

        System.out.println("Médicos:");
        for (Usuario usuario : medicos){
            System.out.println(descreveUsuario(usuario));
        }

    }

}
