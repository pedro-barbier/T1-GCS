package modelo;

public class Paciente extends Usuario {

    // O construtor não pede mais o 'tipo', pois um Paciente é sempre um Paciente.
    public Paciente(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getTipo() {
        return "Paciente";
    }
}