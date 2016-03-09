package gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model;

import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;

public class RepresentantLocalityUser extends DefaultUser {
	private static final long serialVersionUID = -870848362681079195L;
	
	private String uf;
	private String county;
	private String organ;
	private String function;
	private String phone;
	
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getOrgan() {
		return organ;
	}
	public void setOrgan(String organ) {
		this.organ = organ;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}	
}
