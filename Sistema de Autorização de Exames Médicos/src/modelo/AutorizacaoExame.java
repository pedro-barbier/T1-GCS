package modelo;

import java.time.LocalDate;

public class AutorizacaoExame {
    private int codigo;
    private LocalDate dataCadastro;
    private Medico medicoSolicitante;
    private Paciente paciente;
    private TipoExame tipoExame;
    private LocalDate dataRealizacao;
    private static int contadorCodigo = 1000;

    public AutorizacaoExame(LocalDate data, Medico medico, Paciente paciente, TipoExame tipoExame) {
        this.codigo = contadorCodigo;
        contadorCodigo++;
        this.dataCadastro = data;
        this.medicoSolicitante = medico;
        this.paciente = paciente;
        this.tipoExame = tipoExame;
        this.dataRealizacao = null;
    }

    public boolean isRealizado() {
        return dataRealizacao != null;
    }

    public int getCodigo() {
        return codigo;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public Medico getMedicoSolicitante() {
        return medicoSolicitante;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public TipoExame getTipoExame() {
        return tipoExame;
    }

    public LocalDate getDataRealizacao() {
        return dataRealizacao;
    }

    public void setDataRealizacao(LocalDate data) {
        this.dataRealizacao = data;
    }
}