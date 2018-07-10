package br.edu.aplicacao.enums;

/**
 * Fonte: lista criada a partir de http://www.buscacep.correios.com.br/sistemas/buscacep/buscaCep.cfm, combo/seleÃ§Ã£o Tipo.
 * 
 * @author 
 * @author 
 *
 */
public enum TipoEnderecoEnum {
	AEROPORTO(1,"Aeroporto"),
	ALAMEDA(2,"Alameda"),
	AREA(3,"Ã�rea"),
	AVENIDA(4,"Avenida"),
	CAMPO(5,"Campo"),
	CHACARA(6,"ChÃ¡cara"),
	COLONIA(7,"ColÃ´nia"),
	CONDOMINIO(8,"CondomÃ­nio"),
	CONJUNTO(9,"Conjunto"),
	DISTRITO(10,"Distrito"),
	ESPLANADA(11,"Esplanada"),
	ESTACAO(12,"EstaÃ§Ã£o"),
	ESTRADA(13,"Estrada"),
	FAVELA(14,"Favela"),
	FAZENDA(15,"Fazenda"),
	FEIRA(16,"Feira"),
	JARDIM(17,"Jardim"),
	LADEIRA(18,"Ladeira"),
	LAGO(19,"Lago"),
	LAGOA(20,"Lagoa"),
	LARGO(21,"Largo"),
	LOTEAMENTO(22,"Loteamento"),
	MORRO(23,"Morro"),
	NUCLEO(24,"NÃºcleo"),
	PARQUE(25,"Parque"),
	PASSARELA(26,"Passarela"),
	PATIO(27,"PÃ¡tio"),
	PRACA(28,"PraÃ§a"),
	QUADRA(29,"Quadra"),
	RECANTO(30,"Recanto"),
	RESIDENCIAL(31,"Residencial"),
	RODOVIA(32,"Rodovia"),
	RUA(33,"Rua"),
	SETOR(34,"Setor"),
	SITIO(35,"SÃ­tio"),
	TRAVESSA(36,"Travessa"),
	TRECHO(37,"Trecho"),
	TREVO(38,"Trevo"),
	VALE(39,"Vale"),
	VEREDA(40,"Vereda"),
	VIA(41,"Via"),
	VIADUTO(42,"Viaduto"),
	VIELA(43,"Viela"),
	VILA(44,"Vila");
	
	private final int codigo;
	private final String descricao;
	
	private TipoEnderecoEnum(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoEnderecoEnum codigoIntParaEnum(int codigoInt) {
		
		TipoEnderecoEnum tipoEnum = null;
		
		TipoEnderecoEnum[] valores = TipoEnderecoEnum.values();
		
		for (int i = 0; i < valores.length && tipoEnum == null; i++) {
			TipoEnderecoEnum _enum = valores[i];
			
			if(_enum.getCodigo() == codigoInt)
				tipoEnum = _enum;
		}
		
		return tipoEnum;
	}	

}
