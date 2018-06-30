package br.edu.aplicacao.backingbeans.pessoa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.edu.aplicacao.backingbeans.AutenticadorBB;
import br.edu.aplicacao.dtos.ItemListaTelefoneDTO;
import br.edu.aplicacao.dtos.UsuarioLogadoDTO;
import br.edu.aplicacao.entidades.Contato;
import br.edu.aplicacao.entidades.Pessoa;
import br.edu.aplicacao.entidades.Endereco;
import br.edu.aplicacao.entidades.Grupo;
import br.edu.aplicacao.entidades.Paciente;
import br.edu.aplicacao.entidades.Telefone;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.enums.CategoriaEnderecoEnum;
import br.edu.aplicacao.enums.CategoriaTelefoneEnum;
import br.edu.aplicacao.enums.TipoEnderecoEnum;
import br.edu.aplicacao.enums.UnidadeFederacaoEnum;
import br.edu.aplicacao.exceptions.CamposObrigatoriosNaoInformadosException;
import br.edu.aplicacao.exceptions.CategoriaTelefoneEnumInvalidaException;
import br.edu.aplicacao.exceptions.ConfirmacaoDeEmailInvalidaException;

import br.edu.aplicacao.persistencia.interfaces.IUsuarioDAO;
import br.edu.aplicacao.persistencia.interfaces.IPessoaDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.ContatoDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.PessoaDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.GrupoDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.PacienteDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.TelefoneDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.UsuarioDAOImpl;
import br.edu.java.utils.DataEHoraUtils;
import br.edu.java.utils.StringsUtils;
import br.edu.javaee.persistencia.EMFactorySingleton;
import br.edu.javaee.web.utils.MensagensJSFUtils;

/**
 * Esta classe auxilia na implementação dos seguintes requisitos/cenários:
 * 
 * - Pesquisar, Incluir, Alterar e Excluir contato(s) associado ao usuário

 *
 */
@Named
@ViewScoped
public class ManterPessoaBB implements Serializable {

	private static final long serialVersionUID = 1L;

	private IPessoaDAO daoPessoa = new PessoaDAOImpl();

	
	private IUsuarioDAO daoUsuario = new UsuarioDAOImpl();

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private Boolean ehAlteracao = false;

	private Long id;
	private String nome;
	private String genero;
	private Date dtNascimento;


	public ManterPessoaBB() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public Date getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}


	public Boolean getEhAlteracao() {
		return ehAlteracao;
	}

	public void setEhAlteracao(Boolean ehAlteracao) {
		this.ehAlteracao = ehAlteracao;
	}

	@PostConstruct
	public void init() {
		
		
	}
	public void incluir() throws ConfirmacaoDeEmailInvalidaException {
		try {
			validarCamposObrigatorios();

			

			incluirObjeto();

			MensagensJSFUtils.adicionarMsgInfo("Pessoa inclu�da com sucesso", "");
		} catch (CamposObrigatoriosNaoInformadosException e) {

			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}


	private void incluirObjeto() {
		anulaAsDAOs();

		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();

		daoPessoa = new PessoaDAOImpl(em);

		try {
			em.getTransaction().begin();

			// Cria/monta objetos e relacionamentos
			//
			// Pessoa (apenas os atributos básicos)
			Pessoa pessoa = criarObjPessoaApartirDaView();
			//pessoa.setDtInclusao(DataEHoraUtils.hoje());

			// Usuário logado
			Usuario usuarioLogadoBD = daoUsuario.buscarPor(AutenticadorBB.obterUsuarioLogadoDTODaSessao().getId());

			if (usuarioLogadoBD != null)
				pessoa.setUsuario(usuarioLogadoBD);

			

			// Persistência
			//
			// - Pessoa com usuário (e/ou endereço e/ou grupos)
			pessoa = daoPessoa.inserir(pessoa);


			if (em.getTransaction().isActive())
				em.getTransaction().commit();
		} catch (Exception e) {

			try {
				if (em.getTransaction().isActive())
					em.getTransaction().rollback();
			} catch (Exception e2) {
				throw e2;
			}

			throw e;
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}

		inicializaDAOsValorPadrao();
	}

	
	private Pessoa criarObjPessoaApartirDaView() {
		Pessoa pessoa;

	pessoa = new Pessoa(nome,dtNascimento,genero);

		return pessoa;
	}

	

	private void validarCamposObrigatorios() throws CamposObrigatoriosNaoInformadosException {
		if (StringsUtils.ehStringVazia(nome))
			throw new CamposObrigatoriosNaoInformadosException("nome");
	}

	public void prepararAlteracao(Long idPessoaPesquisa) {
		if (idPessoaPesquisa != null) {

			Pessoa pessoa = daoPessoa.buscarPor(idPessoaPesquisa);

			if (pessoa == null) {
				String msgErro = "Código/Id da pessoa inválido!";
				MensagensJSFUtils.adicionarMsgErro(msgErro, "");

				logger.error(msgErro);

				return;
			}

			this.id = idPessoaPesquisa;
			this.nome = pessoa.getNome();
			this.genero = pessoa.getGenero();
			this.dtNascimento = pessoa.getDtNascimento();
			this.ehAlteracao = true;

		
		}
	}

	public void salvarAlteracao() {
		try {
			validarCamposObrigatorios();

			alterarObjeto();

			MensagensJSFUtils.adicionarMsgInfo("Pessoa alterada com sucesso", "");
		} catch (CamposObrigatoriosNaoInformadosException  e) {

			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}

	private void alterarObjeto() {
		anulaAsDAOs();

		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();

		daoPessoa = new PessoaDAOImpl(em);

		try {
			em.getTransaction().begin();

			Pessoa pessoa = daoPessoa.buscarPor(id);

			pessoa.setNome(nome);
			pessoa.setGenero(genero);
			pessoa.setDtNascimento(dtNascimento);


			pessoa = daoPessoa.alterar(pessoa);

	
			if (em.getTransaction().isActive())
				em.getTransaction().commit();
		} catch (Exception e) {

			try {
				if (em.getTransaction().isActive())
					em.getTransaction().rollback();
			} catch (Exception e2) {
				throw e2;
			}

			throw e;
		} finally {
			if (em != null && em.isOpen())
				em.close();
		}

		inicializaDAOsValorPadrao();
	}

	private void inicializaDAOsValorPadrao() {
		daoPessoa = new PessoaDAOImpl();
		
	}

	private void anulaAsDAOs() {
		daoPessoa = null;

	}

}
