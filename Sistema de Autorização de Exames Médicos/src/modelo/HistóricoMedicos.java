package modelo;

public class Atendimento {

    private Paciente paciente
    private Medico medico
    private StatusAtendimento status;

    public Atendimento(Paciente paciente, Medico medico){
        this.paciente = paciente;
        this.medico = medico;
        this.status =  StatusAtendimento.NAO_ATENDIDO;
    }

    public void concluirAtendimento(){
        this.status = StatusAtendimento.ATENDIDO;
    }

    public void cancelarAtendimento(){
        this.status = StatusAtendimento.CANCELADO;
    }

    public StatusAtendimento getStatus(){
        return status;
    }
}