package br.edu.aplicacao.persistencia.interfaces;



import java.util.List;

import br.edu.aplicacao.entidades.Pessoa;
import br.edu.javaee.persistencia.interfaces.IGenericaDAO;

public interface IPessoaDAO extends IGenericaDAO<Pessoa, Long> {

	public List<Pessoa> listarPessoasPorUsuario(Long id);

	public List<Pessoa> pesquisar(String nome, Long idUsuarioLogado);

	public Long qtdTotalPorUsuario(Long idUsuarioLogado);
}
