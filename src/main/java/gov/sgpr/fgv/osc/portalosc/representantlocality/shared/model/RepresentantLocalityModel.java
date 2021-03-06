package gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model;

import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;

public class RepresentantLocalityModel extends DefaultUser {
	private static final long serialVersionUID = -870848362681079195L;
	
	private Integer state;
	private Integer county;
	private String organ;
	private String function;
	private Long phone;
	private UserType typeRepresentant;
	
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
	public Long getPhone() {
		return phone;
	}
	public void setPhone(Long phone) {
		this.phone = phone;
	}
	public UserType getTypeRepresentant() {
		return typeRepresentant;
	}
	public void setTypeRepresentant(UserType typeRepresentant) {
		this.typeRepresentant = typeRepresentant;
	}
}
