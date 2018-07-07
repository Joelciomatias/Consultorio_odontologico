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
 Class criada para a prova de programação visual.
 *
 */
@Entity
@Table(name="tb_pessoa")
public class Pessoa {
	
	@Id
	@Column(name = "id_pessoa")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "nome", length=150, nullable=false, unique=true)
	private String nome;
	
	@Column(name = "genero", length=70, nullable=false)
	private String genero;
	
	@Column(name = "dt_nascimento", nullable=true)
	private Date dtNascimento;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "fk_usuario", referencedColumnName = "id_usuario", nullable = false)
	private Usuario usuario;	

	public Pessoa() {		
	}
	
	public Pessoa(String nome,Date dtNascimento, String genero, Usuario usuario) {
		this.nome = nome;
		this.genero = genero;
		this.dtNascimento = dtNascimento;
		this.usuario = usuario;
	}
	
	public Pessoa(String nome,Date dtNascimento, String genero) {
		this.nome = nome;
		this.genero = genero;
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

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
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
		result = prime * result + ((genero == null) ? 0 : genero.hashCode());
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
		Pessoa other = (Pessoa) obj;
		if (dtNascimento == null) {
			if (other.dtNascimento != null)
				return false;
		} else if (!dtNascimento.equals(other.dtNascimento))
			return false;
		if (genero == null) {
			if (other.genero != null)
				return false;
		} else if (!genero.equals(other.genero))
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
