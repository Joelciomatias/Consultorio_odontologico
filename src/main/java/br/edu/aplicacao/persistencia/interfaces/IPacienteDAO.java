package br.edu.aplicacao.persistencia.interfaces;

import java.util.List;

import br.edu.aplicacao.entidades.Paciente;
import br.edu.javaee.persistencia.interfaces.IGenericaDAO;

public interface IPacienteDAO extends IGenericaDAO<Paciente, Long> {

	public List<Paciente> listarPacientesPorUsuario(Long id);

	public List<Paciente> pesquisar(String nome, Long idUsuarioLogado);
	
	public Long qtdTotalPorUsuario(Long idUsuarioLogado);
}
