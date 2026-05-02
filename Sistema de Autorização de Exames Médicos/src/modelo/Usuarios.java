package modelo;

import java.util.ArrayList;
import java.util.List;

public class Usuarios {
    private List<Usuario> usuarios = new ArrayList<>();;

    public void adicionarUsuario(Usuario usuario) {
        this.usuarios.add(usuario);
    }

    public Usuario buscarPorID(int identificador) {
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

    public List<Usuario> getUsuarios() {
        List<Usuario> temp = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            temp.add(usuario);
        }
        return temp;
    }

}
