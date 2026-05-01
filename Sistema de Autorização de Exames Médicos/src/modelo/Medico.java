package modelo;

public class Medico extends Usuario {
    private String tipo;

    public Medico(int id, String nome, String tipo){
        super(id, nome);
        this.tipo = tipo;
    }

    @Override
    public String getTipo(){
        return this.tipo;
    }
}
