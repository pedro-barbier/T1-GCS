package modelo;

import java.util.ArrayList;
import java.util.List;

public class Autorizacoes {
    private List<AutorizacaoExame> autorizacoes = new ArrayList<>();

    public void adicionarAutorizacao(AutorizacaoExame autorizacao) {
        this.autorizacoes.add(autorizacao);
    }

    public List<AutorizacaoExame> buscarPorCodigo(int codigo) {
        List<AutorizacaoExame> resultado = new ArrayList<>();

        for (AutorizacaoExame autorizacao : autorizacoes) {

            if (autorizacao.getCodigo() == codigo) {
                resultado.add(autorizacao);
            }
        }

        return resultado;
    }

    public List<AutorizacaoExame> buscarPorPaciente(Paciente paciente) {

        List<AutorizacaoExame> resultado = new ArrayList<>();

        for (AutorizacaoExame autorizacao : autorizacoes) {

            if (autorizacao.getPaciente().equals(paciente)) {
                resultado.add(autorizacao);
            }
        }

        return resultado;
    }

    public List<AutorizacaoExame> filtrarPorPaciente(Paciente paciente) {
        
        return buscarPorPaciente(paciente);
    }

    public List<AutorizacaoExame> buscarPorMedico(Medico medico) {
        List<AutorizacaoExame> resultado = new ArrayList<>();

        for (AutorizacaoExame autorizacao : autorizacoes) {

            if (autorizacao.getMedicoSolicitante().equals(medico)) {
                resultado.add(autorizacao);
            }
        }
        return resultado;
    }

    public List<AutorizacaoExame> filtrarPorMedico(Medico medico) {
        
        return buscarPorMedico(medico);
    }

    public int contarAutorizacoes() {
        return this.autorizacoes.size();
    }

    public double percentualRealizados() {

        int realizados = 0;

        for (AutorizacaoExame autorizacao : autorizacoes) {

            if (autorizacao.isRealizado()) {
                realizados++;
            }
        }

        if (autorizacoes.isEmpty()) {
            return 0.0;
        }

        return (realizados * 100.0) / autorizacoes.size();
    }

    public List<AutorizacaoExame> getAutorizacoes() {
        List<AutorizacaoExame> temp = new ArrayList<>();
        for (AutorizacaoExame autorizacao : autorizacoes) {
            temp.add(autorizacao);
        }
        return temp;
    }

}
