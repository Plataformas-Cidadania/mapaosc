package gov.sgpr.fgv.osc.portalosc.map.shared.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;

import gov.sgpr.fgv.osc.portalosc.map.client.components.model.KeyValueRenderer;

/**
 * @author victor
 * 
 *         Certificações Federais da OSC
 */
public class Certifications implements Serializable,
		KeyValueRenderer<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3401384808074893490L;
	private Date cneaPublication;
	private Date cebasMecBeginning;
	private Date cebasMecEnd;
	private Date cebasSusBeginning;
	private Date cebasSusEnd;
	private Date cebasMdsBeginning;
	private Date cebasMdsEnd;
	private Date oscipPublication;
	private Date upfDeclaration;
	private DataSource[] dataSources = null;

	public Date getCneaPublication() {
		return cneaPublication;
	}

	public void setCneaPublication(Date cneaPublication) {
		this.cneaPublication = cneaPublication;
	}

	public Date getCebasMecBeginning() {
		return cebasMecBeginning;
	}

	public void setCebasMecBeginning(Date cebasMecBeginning) {
		this.cebasMecBeginning = cebasMecBeginning;
	}

	public Date getCebasMecEnd() {
		return cebasMecEnd;
	}

	public void setCebasMecEnd(Date cebasMecEnd) {
		this.cebasMecEnd = cebasMecEnd;
	}

	public Date getCebasSusBeginning() {
		return cebasSusBeginning;
	}

	public void setCebasSusBeginning(Date cebasSusBeginning) {
		this.cebasSusBeginning = cebasSusBeginning;
	}

	public Date getCebasSusEnd() {
		return cebasSusEnd;
	}

	public void setCebasSusEnd(Date cebasSusEnd) {
		this.cebasSusEnd = cebasSusEnd;
	}

	public Date getCebasMdsBeginning() {
		return cebasMdsBeginning;
	}

	public void setCebasMdsBeginning(Date cebasMdsBeginning) {
		this.cebasMdsBeginning = cebasMdsBeginning;
	}

	public Date getCebasMdsEnd() {
		return cebasMdsEnd;
	}

	public void setCebasMdsEnd(Date cebasMdsEnd) {
		this.cebasMdsEnd = cebasMdsEnd;
	}

	public Date getOscipPublication() {
		return oscipPublication;
	}

	public void setOscipPublication(Date oscipPublication) {
		this.oscipPublication = oscipPublication;
	}

	public Date getUpfDeclaration() {
		return upfDeclaration;
	}

	public void setUpfDeclaration(Date upfDeclaration) {
		this.upfDeclaration = upfDeclaration;
	}

	/**
	 * @return Fontes de dados da Informação
	 */
	public DataSource[] getDataSources() {
		return dataSources;
	}

	/**
	 * @param dataSources
	 *            {@link #getDataSources()}
	 */
	public void setDataSources(DataSource[] dataSources) {
		this.dataSources = dataSources;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cebasMdsBeginning == null) ? 0 : cebasMdsBeginning
						.hashCode());
		result = prime * result
				+ ((cebasMdsEnd == null) ? 0 : cebasMdsEnd.hashCode());
		result = prime
				* result
				+ ((cebasMecBeginning == null) ? 0 : cebasMecBeginning
						.hashCode());
		result = prime * result
				+ ((cebasMecEnd == null) ? 0 : cebasMecEnd.hashCode());
		result = prime
				* result
				+ ((cebasSusBeginning == null) ? 0 : cebasSusBeginning
						.hashCode());
		result = prime * result
				+ ((cebasSusEnd == null) ? 0 : cebasSusEnd.hashCode());
		result = prime * result
				+ ((cneaPublication == null) ? 0 : cneaPublication.hashCode());
		result = prime * result + Arrays.hashCode(dataSources);
		result = prime
				* result
				+ ((oscipPublication == null) ? 0 : oscipPublication.hashCode());
		result = prime * result
				+ ((upfDeclaration == null) ? 0 : upfDeclaration.hashCode());
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
		Certifications other = (Certifications) obj;
		if (cebasMdsBeginning == null) {
			if (other.cebasMdsBeginning != null)
				return false;
		} else if (!cebasMdsBeginning.equals(other.cebasMdsBeginning))
			return false;
		if (cebasMdsEnd == null) {
			if (other.cebasMdsEnd != null)
				return false;
		} else if (!cebasMdsEnd.equals(other.cebasMdsEnd))
			return false;
		if (cebasMecBeginning == null) {
			if (other.cebasMecBeginning != null)
				return false;
		} else if (!cebasMecBeginning.equals(other.cebasMecBeginning))
			return false;
		if (cebasMecEnd == null) {
			if (other.cebasMecEnd != null)
				return false;
		} else if (!cebasMecEnd.equals(other.cebasMecEnd))
			return false;
		if (cebasSusBeginning == null) {
			if (other.cebasSusBeginning != null)
				return false;
		} else if (!cebasSusBeginning.equals(other.cebasSusBeginning))
			return false;
		if (cebasSusEnd == null) {
			if (other.cebasSusEnd != null)
				return false;
		} else if (!cebasSusEnd.equals(other.cebasSusEnd))
			return false;
		if (cneaPublication == null) {
			if (other.cneaPublication != null)
				return false;
		} else if (!cneaPublication.equals(other.cneaPublication))
			return false;
		if (!Arrays.equals(dataSources, other.dataSources))
			return false;
		if (oscipPublication == null) {
			if (other.oscipPublication != null)
				return false;
		} else if (!oscipPublication.equals(other.oscipPublication))
			return false;
		if (upfDeclaration == null) {
			if (other.upfDeclaration != null)
				return false;
		} else if (!upfDeclaration.equals(other.upfDeclaration))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Certifications [cneaPublication=" + cneaPublication
				+ ", cebasMecBeginning=" + cebasMecBeginning + ", cebasMecEnd="
				+ cebasMecEnd + ", cebasSusBeginning=" + cebasSusBeginning
				+ ", cebasSusEnd=" + cebasSusEnd + ", cebasMdsBeginning="
				+ cebasMdsBeginning + ", cebasMdsEnd=" + cebasMdsEnd
				+ ", oscipPublication=" + oscipPublication
				+ ", upfDeclaration=" + upfDeclaration + ", dataSources="
				+ Arrays.toString(dataSources) + "]";
	}

	
	public Map<String, String> getContent() {

		DateTimeFormat Dtfmt = DateTimeFormat.getFormat("dd/MM/yyyy");

		Map<String, String> content = new LinkedHashMap<String, String>();
		String key = "CEBAS Educação";
		Date begin = this.cebasMecBeginning;
		Date end = this.cebasMecEnd;
		String value;
		if (end != null && end.after(new Date())) {
			value = "Válido até " + Dtfmt.format(end);
		} else if (begin != null) {
			value = "Válido desde " + Dtfmt.format(begin);
		} else {
			value = "Não possui";
		}
		content.put(key, value);

		key = "CEBAS Saúde";
		begin = this.cebasSusBeginning;
		end = this.cebasSusEnd;

		if (end != null && end.after(new Date())) {
			value = "Válido até " + Dtfmt.format(end);
		} else if (begin != null) {
			value = "Válido desde " + Dtfmt.format(begin);
		} else {
			value = "Não possui";
		}
		content.put(key, value);

		key = "CEBAS MDS";
		begin = this.cebasMdsBeginning;
		end = this.cebasMdsEnd;

		if (end != null && end.after(new Date())) {
			value = "Válido até " + Dtfmt.format(end);
		} else if (begin != null) {
			value = "Válido desde " + Dtfmt.format(begin);
		} else {
			value = "Não possui";
		}
		content.put(key, value);

		key = "CNEA";
		begin = this.cneaPublication;
		if (begin != null) {
			value = "Válido desde " + Dtfmt.format(begin);
		} else {
			value = "Não possui";
		}
		content.put(key, value);

		key = "OSCIP";
		begin = this.oscipPublication;
		if (Dtfmt.format(begin) == "01/01/1900") {
			value = "Validade não informada";
		}
		if (begin != null || Dtfmt.format(begin) != "01/01/1900") {
			value = "Válido desde " + Dtfmt.format(begin);
		} 
		else {
			value = "Não possui";
		}
		content.put(key, value);

		key = "UPF";
		begin = this.upfDeclaration;
		if (begin != null) {
			value = "Válido desde " + Dtfmt.format(begin);
		} else {
			value = "Não possui";
		}
		content.put(key, value);

		return content;
	}

}
