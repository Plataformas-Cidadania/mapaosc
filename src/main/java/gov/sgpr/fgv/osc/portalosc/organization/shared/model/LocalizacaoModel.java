package gov.sgpr.fgv.osc.portalosc.organization.shared.model;

import java.io.Serializable;

public class LocalizacaoModel implements Serializable {
	private static final long serialVersionUID = 3637580547899908739L;
	private int id = -1;
	private int idOsc = -1;
	private int idProj = -1;
	private int idLocal = -1;
	private int idmunicipio = -1;
	private int idregiao = -1;
	private int iduf = -1;
	private String municipio = "";
	private String regiao = "";
	private String uf = "";
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getidOsc() {
		return idOsc;
	}
	
	public void setidOsc(int idOsc) {
		this.idOsc = idOsc;
	}
	
	public int getidProj() {
		return idProj;
	}
	
	public void setidProj(int idProj) {
		this.idProj = idProj;
	}
	
	public int getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(int idLocal) {
		this.idLocal = idLocal;
	}
	
	public int getIdMunicipio() {
		return idmunicipio;
	}

	public void setIdMunicipio(int idmunicipio) {
		this.idmunicipio = idmunicipio;
	}
	
	public String getMunicipio() {
		return municipio;
	}
	
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	
	public int getIdRegiao() {
		return idregiao;
	}

	public void setIdRegiao(int idregiao) {
		this.idregiao = idregiao;
	}
	
	public String getRegiao() {
		return regiao;
	}
	
	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}
	
	public int getIdUf() {
		return iduf;
	}

	public void setIdUf(int iduf) {
		this.iduf = iduf;
	}
	
	public String getUf() {
		return uf;
	}
	
	public void setUf(String uf) {
		this.uf = uf;
	}

}
