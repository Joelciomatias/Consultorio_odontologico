package br.edu.aplicacao.persistencia.interfaces.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.edu.aplicacao.entidades.Consulta;
import br.edu.aplicacao.entidades.Usuario;
import br.edu.aplicacao.persistencia.interfaces.IConsultaDAO;

import br.edu.javaee.persistencia.interfaces.impl.GenericaDAOImpl;

public class ConsultaDAOImpl extends GenericaDAOImpl<Consulta, Long> implements IConsultaDAO {

	public ConsultaDAOImpl() {
		super();
	}
	
	public ConsultaDAOImpl(EntityManager em) {
		super(em);
	}


	
}
