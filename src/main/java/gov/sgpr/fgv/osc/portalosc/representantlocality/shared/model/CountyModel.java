package gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model;

import java.io.Serializable;

public class CountyModel implements Serializable {
	private static final long serialVersionUID = 625252978092470603L;
	
	Integer id;
	String name;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
