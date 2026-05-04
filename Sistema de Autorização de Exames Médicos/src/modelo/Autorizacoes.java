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
        for (AutorizacaoExame a : autorizacoes) {
            if (a.getCodigo() == codigo) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public List<AutorizacaoExame> buscarPorPaciente(Paciente paciente) {
        List<AutorizacaoExame> resultado = new ArrayList<>();
        for (AutorizacaoExame a : autorizacoes) {
            if (a.getPaciente().getIdentificador() == paciente.getIdentificador()) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public List<AutorizacaoExame> filtrarPorPaciente(Paciente paciente) {
        List<AutorizacaoExame> resultado = new ArrayList<>();
        for (AutorizacaoExame a : autorizacoes) {
            String nomePaciente = a.getPaciente().getNome().toLowerCase();
            String nomeBuscado = paciente.getNome().toLowerCase();
            if (nomePaciente.startsWith(nomeBuscado)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public List<AutorizacaoExame> buscarPorMedico(Medico medico) {
        List<AutorizacaoExame> resultado = new ArrayList<>();
        for (AutorizacaoExame a : autorizacoes) {
            if (a.getMedicoSolicitante().getIdentificador() == medico.getIdentificador()) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public List<AutorizacaoExame> filtrarPorMedico(Medico medico) {
        List<AutorizacaoExame> resultado = new ArrayList<>();
        for (AutorizacaoExame a : autorizacoes) {
            String nomeMedico = a.getMedicoSolicitante().getNome().toLowerCase();
            String nomeBuscado = medico.getNome().toLowerCase();
            if (nomeMedico.startsWith(nomeBuscado)) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    public int contarAutorizacoes() {
        return this.autorizacoes.size();
    }

    public double percentualRealizados() {
        if (autorizacoes.isEmpty()) {
            return 0.0;
        }

        int realizados = 0;
        for (AutorizacaoExame a : autorizacoes) {
            if (a.isRealizado()) {
                realizados++;
            }
        }

        return (realizados * 100.0) / autorizacoes.size();
    }


    public List<AutorizacaoExame> getAutorizacoes() {
        List<AutorizacaoExame> copia = new ArrayList<>();
        for (AutorizacaoExame a : autorizacoes) {
            copia.add(a);
        }
        return copia;
    }
}