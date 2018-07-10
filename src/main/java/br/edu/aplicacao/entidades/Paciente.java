package br.edu.aplicacao.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Entidade Pacient: id, nome, email, dt nascimento, e lista de telefones.
 * 
 * @author joelcio matias
 *
 */
@Entity
@Table(name = "tb_paciente")
public class Paciente {

	@Id
	@Column(name = "pac_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "pac_nome", length = 50, nullable = false)
	private String nome;
	
	@Column(name = "pac_cpf", length = 14, nullable = false)
	private String cpf;
	
	@Column(name = "pac_email", length = 50, nullable = true)
	private String email;
	
	@Column(name = "pac_telefone", length = 20, nullable = true)
	private String telefone;

	@Column(name = "pac_endereco", length = 100, nullable = true)
	private String endereco;

	@Column(name = "pac_nascimento", nullable = true)
	private Date dtNascimento;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "fk_usuario", referencedColumnName = "id_usuario", nullable = false)
	private Usuario usuario;
	
	@Column(name = "dt_inclusao", nullable=true)
	private Date dtInclusao;
	
	
	public Paciente() {
		
	}

	public Paciente(String nome, String cpf, String email, String telefone, String endereco, Date dtNascimento,
			Usuario usuario) {
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.telefone = telefone;
		this.endereco = endereco;
		this.dtNascimento = dtNascimento;
		this.usuario = usuario;
	}
	
	public Paciente(String nome, String cpf, String email, String telefone, String endereco, Date dtNascimento) {
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
		this.telefone = telefone;
		this.endereco = endereco;
		this.dtNascimento = dtNascimento;
		
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

	public Date getDtInclusao() {
		return dtInclusao;
	}

	public void setDtInclusao(Date dtInclusao) {
		this.dtInclusao = dtInclusao;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtNascimento == null) ? 0 : dtNascimento.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	// crtl + shift + R;;;; Crtl Shif O
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paciente other = (Paciente) obj;
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
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
}


