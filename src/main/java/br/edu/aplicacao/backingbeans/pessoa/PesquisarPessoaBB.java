package br.edu.aplicacao.backingbeans.pessoa;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.backingbeans.AutenticadorBB;
import br.edu.aplicacao.dtos.UsuarioLogadoDTO;
import br.edu.aplicacao.entidades.Contato;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.entidades.Pessoa;
import br.edu.aplicacao.persistencia.interfaces.IContatoDAO;
import br.edu.aplicacao.persistencia.interfaces.IPessoaDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.ContatoDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.PessoaDAOImpl;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@ViewScoped
public class PesquisarPessoaBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IPessoaDAO dao = new PessoaDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// campos para filtro/pesquisa
	private String nome;
	
	// campos usados no resultado da pesquisa
	private List<Pessoa> listaDePessoas;
	private Integer totalDePessoasPesquisa = 0;
	
	public PesquisarPessoaBB() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Pessoa> getListaDePessoas() {
		return listaDePessoas;
	}

	public void setListaDePessoas(List<Pessoa> listaDePessoas) {
		this.listaDePessoas = listaDePessoas;
	}

	public Integer getTotalDePessoasPesquisa() {
		return totalDePessoasPesquisa;
	}

	public void setTotalDePessoasPesquisa(Integer totalDePessoasPesquisa) {
		this.totalDePessoasPesquisa = totalDePessoasPesquisa;
	}
	
	public void preparar() {	
		try {
			UsuarioLogadoDTO usuarioLogadoDto = AutenticadorBB.obterUsuarioLogadoDTODaSessao();
			
			listaDePessoas = dao.listarPessoasPorUsuario( usuarioLogadoDto.getId() );
			
			atualizaTotalDePessoasPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}	
	}
	
	private void atualizaTotalDePessoasPesquisa() {
		if(listaDePessoas != null)
			totalDePessoasPesquisa = listaDePessoas.size();
	}	
	
	public void buscar() {
		try {
			listaDePessoas = dao.pesquisar(nome, AutenticadorBB.obterUsuarioLogadoDTODaSessao().getId());
			
			atualizaTotalDePessoasPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}
	
	public void removerPessoa(Long idPessoa) {
		dao = null;
		
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new PessoaDAOImpl(em);

		try {					
			em.getTransaction().begin();
			
			Pessoa pessoa = dao.buscarPor(idPessoa);
									
			dao.deletar(pessoa);
			
			if(em.getTransaction().isActive())
				em.getTransaction().commit();						
						
			MensagensJSFUtils.adicionarMsgInfo("Pessoa excluída com sucesso", "");
			
			preparar();
		} catch (Exception e) {
			
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
			
			try {
				if(em.getTransaction().isActive())
					em.getTransaction().rollback();	
			} catch (Exception e2) {
				throw e2;
			}		
			
			throw e;			
		} finally {
			if(em != null && em.isOpen())
				em.close();
		}	
		
		dao = new PessoaDAOImpl();
	}
	
	public String redirecionarParaAlteracaoPessoa(Long idPessoa) {
		return "/pages/pessoa/manter_pessoa.jsf?faces-redirect=true&idPessoaPesquisa=" + idPessoa;	
	}
}
