package modelo;

public class Paciente extends Usuario {
    private String tipo;

    public Paciente(int id, String nome, String tipo){
        super(id, nome);
        this.tipo = tipo;
    }

    @Override
    public String getTipo(){
        return this.tipo;
    }
}
