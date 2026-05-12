package modelo;

public class ValidadorDeDados {

    private ValidadorDeDados() {
    }

    public static boolean isTextoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean isIdentificadorValido(int identificador) {
        return identificador > 0;
    }

    public static boolean isNomeValido(String nome) {
        if (!isTextoValido(nome)) {
            return false;
        }
        return nome.trim().matches("[a-zA-ZÀ-ú ]+");
    }
}
