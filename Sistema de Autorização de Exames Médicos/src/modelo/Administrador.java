package modelo;

public class Administrador extends Usuario {

    public Administrador(int identificador, String nome) {
        super(identificador, nome);
    }

    @Override
    public String getTipo() {
        return "Administrador";
    }
}