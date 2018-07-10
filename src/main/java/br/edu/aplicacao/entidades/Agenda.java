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
 * Entidade Agenda:
 * 
@author joelcio_psx@hotmail.com
 *
 */
@Entity
@Table(name="tb_agenda")
public class Agenda {
	
	@Id
	@Column(name = "id_agenda")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "ag_horario", length=70, nullable=false)
	private Date horario;
	
	@Column(name = "ag_data", nullable=false)
	private Date dtAgendamento;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "fk_usuario", referencedColumnName = "id_usuario", nullable = false)
	private Usuario usuario;	
	
	@OneToOne(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name = "fk_consulta", referencedColumnName = "id_consulta", nullable = true)
	private Consulta consulta;
		
	public Agenda() {		
	}
	
	public Agenda(Date horario, Date dtAgendamento, Consulta consulta, Usuario usuario) {
	
		this.horario = horario;
		this.dtAgendamento = dtAgendamento;
		this.consulta = consulta; 
		this.usuario = usuario;
	}
	
	public Agenda(Date horario, Date dtAgendamento, Consulta consulta) {
		
		this.horario = horario;
		this.dtAgendamento = dtAgendamento;
		this.consulta = consulta;
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getHorario() {
		return horario;
	}

	public void setHorario(Date horario) {
		this.horario = horario;
	}


	public Date getdtAgendamento() {
		return dtAgendamento;
	}

	public void setdtAgendamento(Date dtAgendamento) {
		this.dtAgendamento = dtAgendamento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public Consulta getconsulta() {
		return consulta;
	}

	public void setconsulta(Consulta consulta) {
		this.consulta = consulta;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtAgendamento == null) ? 0 : dtAgendamento.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((horario == null) ? 0 : horario.hashCode());
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
		Agenda other = (Agenda) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
	
}
