package modelo;

public class Medico extends Usuario {

    public Medico(int identificador, String nome) {
        super(identificador, nome);
    }

    @Override
    public String getTipo() {
        return "Médico";
    }
}
