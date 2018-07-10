package br.edu.aplicacao.backingbeans.dentista;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.backingbeans.AutenticadorBB;
import br.edu.aplicacao.dtos.UsuarioLogadoDTO;
import br.edu.aplicacao.entidades.Dentista;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.persistencia.interfaces.IDentistaDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.DentistaDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@ViewScoped
public class PesquisarDentistaBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IDentistaDAO dao = new DentistaDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// campos para filtro/pesquisa
	private String nome;
	
	// campos usados no resultado da pesquisa
	private List<Dentista> listaDeDentistas;
	private Integer totalDeDentistasPesquisa = 0;
	
	public PesquisarDentistaBB() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Dentista> getListaDeDentistas() {
		return listaDeDentistas;
	}

	public void setListaDeDentistas(List<Dentista> listaDeDentistas) {
		this.listaDeDentistas = listaDeDentistas;
	}

	public Integer getTotalDeDentistasPesquisa() {
		return totalDeDentistasPesquisa;
	}

	public void setTotalDeDentistasPesquisa(Integer totalDeDentistasPesquisa) {
		this.totalDeDentistasPesquisa = totalDeDentistasPesquisa;
	}
	
	public void preparar() {	
		try {
			UsuarioLogadoDTO usuarioLogadoDto = AutenticadorBB.obterUsuarioLogadoDTODaSessao();
			
			listaDeDentistas = dao.listarDentistasPorUsuario( usuarioLogadoDto.getId() );
			
			atualizaTotalDeDentistasPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}	
	}
	
	private void atualizaTotalDeDentistasPesquisa() {
		if(listaDeDentistas != null)
			totalDeDentistasPesquisa = listaDeDentistas.size();
	}	
	
	public void buscar() {
		try {
			listaDeDentistas = dao.pesquisar(nome, AutenticadorBB.obterUsuarioLogadoDTODaSessao().getId());
			
			atualizaTotalDeDentistasPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}
	
	public void removerDentista(Long idDentista) {
		dao = null;
		
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new DentistaDAOImpl(em);

		try {					
			em.getTransaction().begin();
			
			Dentista dentista = dao.buscarPor(idDentista);
									
			dao.deletar(dentista);
			
			if(em.getTransaction().isActive())
				em.getTransaction().commit();						
						
			MensagensJSFUtils.adicionarMsgInfo("Dentista excluído com sucesso", "");
			
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
		
		dao = new DentistaDAOImpl();
	}
	
	public String redirecionarParaAlteracaoDentista(Long idDentista) {
		return "/pages/dentista/manter_dentista.jsf?faces-redirect=true&idDentistaPesquisa=" + idDentista;	
	}
}
