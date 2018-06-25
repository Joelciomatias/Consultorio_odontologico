package br.edu.aplicacao.backingbeans.paciente;

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
import br.edu.aplicacao.entidades.Paciente;
import br.edu.aplicacao.entidades.Endereco;
import br.edu.aplicacao.entidades.Grupo;
import br.edu.aplicacao.entidades.Telefone;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.enums.CategoriaEnderecoEnum;
import br.edu.aplicacao.enums.CategoriaTelefoneEnum;
import br.edu.aplicacao.enums.TipoEnderecoEnum;
import br.edu.aplicacao.enums.UnidadeFederacaoEnum;
import br.edu.aplicacao.exceptions.CamposObrigatoriosNaoInformadosException;
import br.edu.aplicacao.exceptions.CategoriaTelefoneEnumInvalidaException;
import br.edu.aplicacao.exceptions.ConfirmacaoDeEmailInvalidaException;
import br.edu.aplicacao.exceptions.IdTelefoneInvalidoException;
import br.edu.aplicacao.exceptions.TamanhoCampoInvalidoException;
import br.edu.aplicacao.persistencia.interfaces.IContatoDAO;
import br.edu.aplicacao.persistencia.interfaces.IGrupoDAO;
import br.edu.aplicacao.persistencia.interfaces.ITelefoneDAO;
import br.edu.aplicacao.persistencia.interfaces.IUsuarioDAO;
import br.edu.aplicacao.persistencia.interfaces.IPacienteDAO;
import br.edu.aplicacao.persistencia.interfaces.impl.ContatoDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.PacienteDAOImpl;
import br.edu.aplicacao.persistencia.interfaces.impl.GrupoDAOImpl;
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
 * logado no sistema.
 * 
 * - Incluir, Alterar e/ou Excluir telefone(s) para/de um determinado contato.
 * Atenção: a inclusão, a alteração e a exclusão de um determinado telefone na
 * lista de telefones são mantidas em memória, elas são apenas efetivadas em
 * banco de dados quando o usuário clica em Incluir ou Alterar do contato.
 * 
 * @author vagner.l@uninter.com
 * @author vagnercml@hotmail.com
 *
 */
@Named
@ViewScoped
public class ManterPacienteBB implements Serializable {

	private static final long serialVersionUID = 1L;

	private IPacienteDAO daoPaciente = new PacienteDAOImpl();

	
	private IUsuarioDAO daoUsuario = new UsuarioDAOImpl();

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private Boolean ehAlteracao = false;

	private Long id;
	private String nome;
	private String email;
	private String emailConfirmacao;
	private Date dtNascimento;


	public ManterPacienteBB() {
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(Date dtNascimento) {
		this.dtNascimento = dtNascimento;
	}
	public String getEmailConfirmacao() {
		return emailConfirmacao;
	}

	public void setEmailConfirmacao(String emailConfirmacao) {
		this.emailConfirmacao = emailConfirmacao;
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

			validarPreenchimentoEmail();

			incluirObjeto();

			MensagensJSFUtils.adicionarMsgInfo("Paciente incluído com sucesso", "");
		} catch (CamposObrigatoriosNaoInformadosException e) {

			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}

	private void incluirObjeto() {
		anulaAsDAOs();

		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();

		daoPaciente = new PacienteDAOImpl(em);

		try {
			em.getTransaction().begin();

			// Cria/monta objetos e relacionamentos
			//
			// Paciente (apenas os atributos básicos)
			Paciente paciente = criarObjPacienteApartirDaView();
			//paciente.setDtInclusao(DataEHoraUtils.hoje());

			// Usuário logado
			Usuario usuarioLogadoBD = daoUsuario.buscarPor(AutenticadorBB.obterUsuarioLogadoDTODaSessao().getId());

			if (usuarioLogadoBD != null)
				paciente.setUsuario(usuarioLogadoBD);

			

			// Persistência
			//
			// - paciente com usuário (e/ou endereço e/ou grupos)
			paciente = daoPaciente.inserir(paciente);


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

	
	private Paciente criarObjPacienteApartirDaView() {
		Paciente paciente;

		paciente = new Paciente(nome, email, dtNascimento);

		return paciente;
	}

	
	private void validarPreenchimentoEmail()
			throws CamposObrigatoriosNaoInformadosException, ConfirmacaoDeEmailInvalidaException {
		if (!StringsUtils.ehStringVazia(email)) {
			if (StringsUtils.ehStringVazia(emailConfirmacao))
				throw new CamposObrigatoriosNaoInformadosException("Email (confirmação)");

			if (email.compareTo(emailConfirmacao) != 0)
				throw new ConfirmacaoDeEmailInvalidaException();
		}
	}

	private void validarCamposObrigatorios() throws CamposObrigatoriosNaoInformadosException {
		if (StringsUtils.ehStringVazia(nome))
			throw new CamposObrigatoriosNaoInformadosException("nome");
	}

	public void prepararAlteracao(Long idPacientePesquisa) {
		if (idPacientePesquisa != null) {

			Paciente paciente = daoPaciente.buscarPor(idPacientePesquisa);

			if (paciente == null) {
				String msgErro = "Código/Id de contato inválido!";
				MensagensJSFUtils.adicionarMsgErro(msgErro, "");

				logger.error(msgErro);

				return;
			}

			this.id = idPacientePesquisa;
			this.nome = paciente.getNome();
			this.email = paciente.getEmail();
			this.emailConfirmacao = this.email;
			this.dtNascimento = paciente.getDtNascimento();
			this.ehAlteracao = true;

		
		}
	}

	public void salvarAlteracao() {
		try {
			validarCamposObrigatorios();

			validarPreenchimentoEmail();

			alterarObjeto();

			MensagensJSFUtils.adicionarMsgInfo("Contato alterado com sucesso", "");
		} catch (CamposObrigatoriosNaoInformadosException  e) {

			MensagensJSFUtils.adicionarMsgErro(e.getMessage(), "");
		} catch (Exception e) {

			MensagensJSFUtils.msgELogDeERROInternoEOuSistema(logger, e);
		}
	}

	private void alterarObjeto() {
		anulaAsDAOs();

		EntityManager em = EMFactorySingleton.obterInstanciaUnica().criarEM();

		daoPaciente = new PacienteDAOImpl(em);

		try {
			em.getTransaction().begin();

			Paciente paciente = daoPaciente.buscarPor(id);

			paciente.setNome(nome);
			paciente.setEmail(email);
			paciente.setDtNascimento(dtNascimento);


			paciente = daoPaciente.alterar(paciente);

	
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
		daoPaciente = new PacienteDAOImpl();
		
	}

	private void anulaAsDAOs() {
		daoPaciente = null;

	}

}
