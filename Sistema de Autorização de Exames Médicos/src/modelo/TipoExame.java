package modelo;

public enum TipoExame {
    RAIOX(1, "Raio-X"),
    TOMOGRAFIA(2, "Tomografia"),
    RESSONANCIA(3, "Ressonância Magnética"),
    ULTRASSONOGRAFIA(4, "Ultrassonografia"),
    ELETROCARDIOGRAMA(5, "Eletrocardiograma"),
    ENDOSCOPIA(6, "Endoscopia"),
    COLONOSCOPIA(7, "Colonoscopia"),
    MAMOGRAFIA(8, "Mamografia"),
    DENSITOMETRIA(9, "Densitometria Óssea"),
    ECOCARDIOGRAMA(10, "Ecocardiograma"),
    ANGIOGRAFIA(11, "Angiografia"),
    PUNCAO(12, "Punção"),
    BIOMBO(13, "Biombo"),
    POLISSONOGRAFIA(14, "Polissonografia"),
    ESPIROMETRIA(15, "Espirometria");

    private final int codigo;
    private final String descricao;

    private TipoExame(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
