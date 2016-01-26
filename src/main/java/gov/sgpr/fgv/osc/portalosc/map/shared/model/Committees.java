package gov.sgpr.fgv.osc.portalosc.map.shared.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import gov.sgpr.fgv.osc.portalosc.map.client.components.model.KeyValueRenderer;

public class Committees implements Serializable,
		KeyValueRenderer<String, String> {

	/**
	 * @author Augusto
	 * 
	 *         Conselhos e comissões
	 * 
	 */
	private static final long serialVersionUID = -466512555666038500L;

	// private String councilName;
	// private String participationType;
	private Map<String, String> values;
	private DataSource[] dataSources = null;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DataSource[] getDataSources() {
		return dataSources;
	}

	public void setDataSources(DataSource[] dataSources) {
		this.dataSources = dataSources;
	}

	public Map<String, String> getCommittees() {
		return values;
	}

	public void setCommittees(Map<String, String> committees) {
		this.values = committees;
	}

	@Override
	public String toString() {
		return "Committees [committees=" + values + ", dataSources="
				+ Arrays.toString(dataSources) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((values == null) ? 0 : values.hashCode());
		result = prime * result + Arrays.hashCode(dataSources);
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
		Committees other = (Committees) obj;
		if (values == null) {
			if (other.values != null)
				return false;
		} else if (!values.equals(other.values))
			return false;
		if (!Arrays.equals(dataSources, other.dataSources))
			return false;
		return true;
	}

	
	public Map<String, String> getContent() {
		
		return values;
	}

}
