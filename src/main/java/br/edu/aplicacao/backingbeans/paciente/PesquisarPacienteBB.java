package br.edu.aplicacao.backingbeans.paciente;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.backingbeans.AutenticadorBB;
import br.edu.aplicacao.dtos.UsuarioLogadoDTO;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.entidades.Paciente;
import br.edu.aplicacao.persistencia.interfaces.IPacienteDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.PacienteDAOImpl;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

@Named
@ViewScoped
public class PesquisarPacienteBB implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private IPacienteDAO dao = new PacienteDAOImpl();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// campos para filtro/pesquisa
	private String nome;
	
	// campos usados no resultado da pesquisa
	private List<Paciente> listaDePacientes;
	private Integer totalDePacientesPesquisa = 0;
	
	public PesquisarPacienteBB() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Paciente> getListaDePacientes() {
		return listaDePacientes;
	}

	public void setListaDePacientes(List<Paciente> listaDePacientes) {
		this.listaDePacientes = listaDePacientes;
	}

	public Integer getTotalDePacientesPesquisa() {
		return totalDePacientesPesquisa;
	}

	public void setTotalDePacientesPesquisa(Integer totalDePacientesPesquisa) {
		this.totalDePacientesPesquisa = totalDePacientesPesquisa;
	}
	
	public void preparar() {	
		try {
			UsuarioLogadoDTO usuarioLogadoDto = AutenticadorBB.obterUsuarioLogadoDTODaSessao();
			
			listaDePacientes = dao.listarPacientesPorUsuario( usuarioLogadoDto.getId() );
			
			atualizaTotalDePacientesPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);		
		}	
	}
	
	private void atualizaTotalDePacientesPesquisa() {
		if(listaDePacientes != null)
			totalDePacientesPesquisa = listaDePacientes.size();
	}	
	
	public void buscar() {
		try {
			listaDePacientes = dao.pesquisar(nome, AutenticadorBB.obterUsuarioLogadoDTODaSessao().getId());
			
			atualizaTotalDePacientesPesquisa();			
		} catch (Exception e) {
			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}
	
	public void removerPaciente(Long idPaciente) {
		dao = null;
		
		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();
		
		dao = new PacienteDAOImpl(em);

		try {					
			em.getTransaction().begin();
			
			Paciente paciente = dao.buscarPor(idPaciente);
									
			dao.deletar(paciente);
			
			if(em.getTransaction().isActive())
				em.getTransaction().commit();						
						
			MensagensJSFUtils.adicionarMsgInfo("Paciente excluído com sucesso", "");
			
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
		
		dao = new PacienteDAOImpl();
	}
	
	public String redirecionarParaAlteracaoPaciente(Long idPaciente) {
		return "/pages/paciente/manter_paciente.jsf?faces-redirect=true&idPacientePesquisa=" + idPaciente;	
	}
}
