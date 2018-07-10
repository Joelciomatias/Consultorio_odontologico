package br.edu.aplicacao.entidades;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/** 
 * Entidade Dentista: id, nome, email, dt nascimento, Usuario (responsável pelo dentista) e 
 * 	lista de telefones.
 * 
 * @author joelcio
 *
 */
@Entity
@Table(name="tb_dentista")
public class Dentista {
	
	@Id
	@Column(name = "dent_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "dent_nome", length=50, nullable=false)
	private String nome;
	
	@Column(name = "dent_cpf", length = 14, nullable = false)
	private String cpf;
	
	@Column(name = "dent_email", length=50, nullable=true)
	private String email;
	
	@Column(name = "dent_nascimento", nullable=true)
	private Date dtNascimento;
	
	@Column(name = "dent_telefone", length=20, nullable=true)
	private String telefone;
	
	@Column(name = "dent_endereco", length=100, nullable=true)
	private String endereco;
	
	@Column(name = "dent_cro", length=11, nullable=true)
	private String cro;
	
	@OneToMany(mappedBy = "dentista", fetch=FetchType.LAZY)
	private List<Consulta> consultas;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "fk_usuario", referencedColumnName = "id_usuario", nullable = false)
	private Usuario usuario;	
	
	@Column(name = "dt_inclusao", nullable=true)
	private Date dtInclusao;
		
	public Dentista() {		
	}
	

	public Dentista(String nome, String cpf, String email, Date dtNascimento, String telefone, String endereco,
			String cro, Usuario usuario) {
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.dtNascimento = dtNascimento;
		this.telefone = telefone;
		this.endereco = endereco;
		this.cro = cro;
		this.usuario = usuario;
	}


	public Dentista(String nome, String cpf, String email, Date dtNascimento, String telefone, String endereco,
			String cro) {
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.dtNascimento = dtNascimento;
		this.telefone = telefone;
		this.endereco = endereco;
		this.cro = cro;
	}


	public Dentista(String nome, String email, Date dtNascimento, String telefone, String endereco,
			String cro, Usuario usuario) {
		this.nome = nome;
		
		this.email = email;
		this.dtNascimento = dtNascimento;
		this.telefone = telefone;
		this.endereco = endereco;
		this.cro = cro;
		this.usuario = usuario;
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

	
	
	public String getCpf() {
		return cpf;
	}


	public void setCpf(String cpf) {
		this.cpf = cpf;
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

	public void setDtNascimento(Date aniversario) {
		this.dtNascimento = aniversario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public List<Consulta> getConsultas() {
		return consultas;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCro() {
		return cro;
	}

	public void setCro(String cro) {
		this.cro = cro;
	}

	public Date getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtNascimento == null) ? 0 : dtNascimento.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dentista other = (Dentista) obj;
		if (dtNascimento == null) {
			if (other.dtNascimento != null)
				return false;
		} else if (!dtNascimento.equals(other.dtNascimento))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
	
}
