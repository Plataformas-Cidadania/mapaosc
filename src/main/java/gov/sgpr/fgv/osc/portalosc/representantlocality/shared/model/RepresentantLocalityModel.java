package gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model;

import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;

public class RepresentantLocalityModel extends DefaultUser {
	private static final long serialVersionUID = -870848362681079195L;
	
	private Integer state;
	private Integer county;
	private String organ;
	private String function;
	private Integer phone;
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getCounty() {
		return county;
	}
	public void setCounty(Integer county) {
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
	public Integer getPhone() {
		return phone;
	}
	public void setPhone(Integer phone) {
		this.phone = phone;
	}
}
