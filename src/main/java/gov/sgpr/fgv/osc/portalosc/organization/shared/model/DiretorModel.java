package gov.sgpr.fgv.osc.portalosc.organization.shared.model;

import java.io.Serializable;

public class DiretorModel implements Serializable {
	private static final long serialVersionUID = 2229532108497640519L;
	
	private int id = -1;
	private String cargo = "";
	private String name = "";
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	public String getNome() {
		return name;
	}
	public void setNome(String name) {
		this.name = name;
	}

}
