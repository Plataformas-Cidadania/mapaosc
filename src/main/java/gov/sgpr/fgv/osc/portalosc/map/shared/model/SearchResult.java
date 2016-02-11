package gov.sgpr.fgv.osc.portalosc.map.shared.model;

import java.io.Serializable;

/**
 * @author victor
 * Resultado da Busca
 *
 */
public class SearchResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1137638417039862584L;
	
	private int id;
	private String value;
	private SearchResultType type;
	private String latitude;
	private String longetude;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public SearchResultType getType() {
		return type;
	}
	public void setType(SearchResultType type) {
		this.type = type;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		SearchResult other = (SearchResult) obj;
		if (id != other.id)
			return false;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "SearchResult [id=" + id + ", value=" + value + ", type=" + type + ", latitude=" + latitude
				+ ", longetude=" + longetude + "]";
	}
}
