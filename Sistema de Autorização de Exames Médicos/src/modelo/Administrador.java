package modelo;

public class Administrador extends Usuario {
    private String tipo;

    public Administrador(int id, String nome, String tipo){
        super(id, nome);
        this.tipo = tipo;
    }

    @Override
    public String getTipo(){
        return this.tipo;
    }
}
