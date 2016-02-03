package gov.sgpr.fgv.osc.portalosc.map.shared.model;

import java.io.Serializable;

public class SearchGoogleResult implements Serializable {
	private static final long serialVersionUID = -1039212228061477762L;
	
	private Integer id;
	private String address;
	private String latitude;
	private String longetude;
	
	@Override
	public String toString() {
		return "SearchGoogleResult [id=" + id + ", address=" + address + ", latitude=" + latitude + ", longetude=" + longetude + "]";
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongetude() {
		return longetude;
	}
	public void setLongetude(String longetude) {
		this.longetude = longetude;
	}
}
