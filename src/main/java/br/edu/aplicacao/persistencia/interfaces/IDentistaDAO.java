package br.edu.aplicacao.persistencia.interfaces;


import java.util.List;

import br.edu.aplicacao.entidades.Dentista;
import br.edu.javaee.persistencia.interfaces.IGenericaDAO;

public interface IDentistaDAO extends IGenericaDAO<Dentista, Long> {

	public List<Dentista> listarDentistasPorUsuario(Long id);

	public List<Dentista> pesquisar(String nome, Long idUsuarioLogado);
	
	public Long qtdTotalPorUsuario(Long idUsuarioLogado);
}
