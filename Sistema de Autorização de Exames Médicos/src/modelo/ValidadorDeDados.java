package modelo;

public class ValidadorDeDados {

    private ValidadorDeDados() {
    }

    public static boolean isTextoValido(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean isCpfValido(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        String cpfApenasNumeros = cpf.replace(".", "").replace("-", "");
        if (cpfApenasNumeros.length() == 11) {
            return true;
        }
        return false;
    }

    public static boolean isCrmValido(String crm) {
        if (crm == null || crm.trim().isEmpty()) {
            return false;
        }
        if (crm.length() >= 5) {
            return true;
        }
        return false;
    }
}