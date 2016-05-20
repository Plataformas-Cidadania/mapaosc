package gov.sgpr.fgv.osc.portalosc.map.shared.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import gov.sgpr.fgv.osc.portalosc.map.client.components.model.KeyValueRenderer;

/**
 * @author victor Dados principais da OSC
 */
public class OscMain implements Serializable, KeyValueRenderer<String, String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5461128724763374612L;

	private int id;
	private long code;
	private String formattedCode;
	private String name;
	private String address;
	private int countyId;
	private String cnaeCode;
	private String cnaeDescription;
	private String legalTypeCode;
	private String legalTypeDescription;
	private String description;
	private Integer foundationYear;
	private String site;
	private OscCoordinate coordinate;
	private Map<String, String> contacts;
	private DataSource[] dataSources = null;
	private String county;
	private String state;

	/**
	 * @return identificador da OSC
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            {@link #getId()}
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return CNPJ/CEI da OSC
	 */
	public long getCode() {
		return code;
	}

	/**
	 * @param code
	 *            {@link #getCode()}
	 */
	public void setCode(long code) {
		this.code = code;
	}

	/**
	 * @return Código formatado no caso de tipo CNPJ
	 */
	public String getFormattedCode() {
		return formattedCode;
	}

	/**
	 * @param code
	 *            {@link #getFormattedCode()}
	 */
	public void setFormattedCode(String formattedCode) {
		this.formattedCode = formattedCode;
	}
	public void setFormattedCNPJ(String formattedCode) {
		this.formattedCode = formatCNPJ(formattedCode);
	}
	public void setFormattedCEI(String formattedCode) {
		this.formattedCode = formatCEI(formattedCode);
	}
	
	/**
	 * @return Nome da OSC
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            {@link #getName()}
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Endereço da OSC
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            {@link #getAddress()}
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return Código Nacional de Atividade Econômica 2.1
	 */
	public String getCnaeCode() {
		return cnaeCode;
	}

	/**
	 * @param cnaeCode
	 *            {@link #getCnaeCode()}
	 */
	public void setCnaeCode(String cnaeCode) {
		this.cnaeCode = cnaeCode;
	}

	/**
	 * @return Descrição da atividade econômica
	 */
	public String getCnaeDescription() {
		return cnaeDescription;
	}

	/**
	 * @param cnaeDescription
	 *            {@link #getCnaeDescription()}
	 */
	public void setCnaeDescription(String cnaeDescription) {
		this.cnaeDescription = cnaeDescription;
	}

	/**
	 * @return Descrição da OSC
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            {@link #getDescription()}
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Ano de fundação da OSC
	 */
	public Integer getFoundationYear() {
		return foundationYear;
	}

	/**
	 * @param foundationYear
	 *            {@link #getFoundationYear()}
	 */
	public void setFoundationYear(Integer foundationYear) {
		this.foundationYear = foundationYear;
	}

	/**
	 * @return Endereço da página eletrônica da entidade
	 */
	public String getSite() {
		return site;
	}

	/**
	 * @param site
	 *            {@link #getSite()}
	 */
	public void setSite(String site) {
		this.site = site;
	}

	/**
	 * @return Código da Natureza Juridica da Entidade no CONCLA
	 */
	public String getLegalTypeCode() {
		return legalTypeCode;
	}

	/**
	 * @param legalTypeCode
	 *            {@link #getLegalTypeCode()}
	 */
	public void setLegalTypeCode(String legalTypeCode) {
		this.legalTypeCode = legalTypeCode;
	}

	/**
	 * @return Descrição da Natureza Jurídica da Entidade
	 */
	public String getLegalTypeDescription() {
		return legalTypeDescription;
	}

	/**
	 * @param legalTypeDescription
	 *            {@link #getLegalTypeCode()}
	 */
	public void setLegalTypeDescription(String legalTypeDescription) {
		this.legalTypeDescription = legalTypeDescription;
	}

	/**
	 * @return Informação de contatos da Entidade
	 */
	public Map<String, String> getContacts() {
		return contacts;
	}

	/**
	 * @param contacts
	 *            {@link #getLegalTypeCode()}
	 */
	public void setContacts(Map<String, String> contacts) {
		this.contacts = contacts;
	}

	/**
	 * @return Identificador do município
	 */
	public int getCountyId() {
		return countyId;
	}

	/**
	 * @param countyId
	 *            {@link #getCountyId()}
	 */
	public void setCountyId(int countyId) {
		this.countyId = countyId;
	}

	/**
	 * @return Coordenada da OSC no mapa
	 */
	protected OscCoordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * @param coordinate
	 *            {@link #getCoordinate()}
	 */
	public void setCoordinate(OscCoordinate coordinate) {
		this.coordinate = coordinate;
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

	/**
	 * @return Estado da OSC
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            {@link #getState()}
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return Município da OSC
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @param county
	 *            {@link #getSite()}
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((cnaeCode == null) ? 0 : cnaeCode.hashCode());
		result = prime * result
				+ ((cnaeDescription == null) ? 0 : cnaeDescription.hashCode());
		result = prime * result + (int) (code ^ (code >>> 32));
		result = prime * result
				+ ((contacts == null) ? 0 : contacts.hashCode());
		result = prime * result
				+ ((coordinate == null) ? 0 : coordinate.hashCode());
		result = prime * result + countyId;
		result = prime * result + Arrays.hashCode(dataSources);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((foundationYear == null) ? 0 : foundationYear.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((legalTypeCode == null) ? 0 : legalTypeCode.hashCode());
		result = prime
				* result
				+ ((legalTypeDescription == null) ? 0 : legalTypeDescription
						.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((county == null) ? 0 : county.hashCode());
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
		OscMain other = (OscMain) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (cnaeCode == null) {
			if (other.cnaeCode != null)
				return false;
		} else if (!cnaeCode.equals(other.cnaeCode))
			return false;
		if (cnaeDescription == null) {
			if (other.cnaeDescription != null)
				return false;
		} else if (!cnaeDescription.equals(other.cnaeDescription))
			return false;
		if (code != other.code)
			return false;
		if (contacts == null) {
			if (other.contacts != null)
				return false;
		} else if (!contacts.equals(other.contacts))
			return false;
		if (coordinate == null) {
			if (other.coordinate != null)
				return false;
		} else if (!coordinate.equals(other.coordinate))
			return false;
		if (countyId != other.countyId)
			return false;
		if (!Arrays.equals(dataSources, other.dataSources))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (foundationYear == null) {
			if (other.foundationYear != null)
				return false;
		} else if (!foundationYear.equals(other.foundationYear))
			return false;
		if (id != other.id)
			return false;
		if (legalTypeCode == null) {
			if (other.legalTypeCode != null)
				return false;
		} else if (!legalTypeCode.equals(other.legalTypeCode))
			return false;
		if (legalTypeDescription == null) {
			if (other.legalTypeDescription != null)
				return false;
		} else if (!legalTypeDescription.equals(other.legalTypeDescription))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;

		if (county == null) {
			if (other.county != null)
				return false;
		} else if (!county.equals(other.county))
			return false;

		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "OscMain [id=" + id + ", code=" + code + ", name=" + name
				+ ", address=" + address + ", countyId=" + countyId
				+ ", cnaeCode=" + cnaeCode + ", cnaeDescription="
				+ cnaeDescription + ", legalTypeCode=" + legalTypeCode
				+ ", legalTypeDescription=" + legalTypeDescription
				+ ", description=" + description + ", foundationYear="
				+ foundationYear + ", site=" + site + ", coordinate="
				+ coordinate + ", contacts=" + contacts + ", dataSources="
				+ Arrays.toString(dataSources) + "]" + ",county =" + county
				+ ", state =" + state;
	}

	
	public Map<String, String> getContent() {
		Map<String, String> content = new LinkedHashMap<String, String>();
		content.put("CNPJ/CEI", formattedCode);
		content.put("Endereço", address);
		content.put("Atividade Econômica", cnaeDescription);
		content.put("Natureza Juridica", legalTypeDescription);
		content.put("Descrição da OSC", (description != null && description != "") ? description
				: "Não disponível");
		content.put("Ano de fundação",
				foundationYear.equals("0") ? "Não disponível"
						: String.valueOf(foundationYear));
		content.put("Website", (site != null && site != "") ? site : "Não disponível");
		for (Map.Entry<String, String> entry : contacts.entrySet()) {
			content.put(entry.getKey(), entry.getValue());
		}
		return content;
	}
	
	private String formatCNPJ(String cnpj) {
		while(cnpj.length() <= 14){
			cnpj = "0" + cnpj;
		}
		
		String bloco1 = cnpj.substring(0, 2);
		String bloco2 = cnpj.substring(2, 5);
		String bloco3 = cnpj.substring(5, 8);
		String bloco4 = cnpj.substring(8, 12);
		String bloco5 = cnpj.substring(12, 14);
		
		cnpj = bloco1 + "." + bloco2 + "." + bloco3 + "/" + bloco4 + "-" + bloco5;
		
		return cnpj;
	}
	
	private String formatCEI(String cei) {
		while(cei.length() <= 12){
			cei = "0" + cei;
		}
		
		String bloco1 = cei.substring(0, 2);
		String bloco2 = cei.substring(2, 5);
		String bloco3 = cei.substring(5, 10);
		String bloco4 = cei.substring(10, 12);
		
		cei = bloco1 + "." + bloco2 + "." + bloco3 + "/" + bloco4;
		
		return cei;
	}
}
