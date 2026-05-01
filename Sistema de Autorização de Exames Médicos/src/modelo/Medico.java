package modelo;
public class Medico extends Usuario {
    
    private final String crm;

    public Medico(int identificador, String nome, String crm) {
    
        super(identificador, nome);
        
        if (!ValidadorDeDados.isTextoValido(crm)) {
            throw new IllegalArgumentException("O CRM do médico é obrigatório e não pode ser vazio.");
        }
        
        if (!ValidadorDeDados.isCrmValido(crm)) {
            throw new IllegalArgumentException("O CRM informado é muito curto ou inválido.");
        }
        
        this.crm = crm;
    }

    @Override
    public String getTipo() {
        return "Médico";
    }
    
    public String getCrm() {
        return this.crm;
    }
}
