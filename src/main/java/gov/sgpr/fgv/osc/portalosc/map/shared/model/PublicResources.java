package gov.sgpr.fgv.osc.portalosc.map.shared.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.i18n.client.NumberFormat;

import gov.sgpr.fgv.osc.portalosc.map.client.components.model.KeyValueRenderer;

/**
 * @author victor
 * 
 *         Recursos públicos obtidos pela OSC
 * 
 */
public class PublicResources implements Serializable,
		KeyValueRenderer<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3690121841730224543L;

	private Integer partnershipsEnded;
	private Integer inExecutionPartnership;
	private Double globalValue;
	private Double transferValue;
	private Double financialCounterpartValue;
	private Double othersCounterpartValue;
	private Double committedValue;
	private Double disbursedValue;
	private Integer technologicalAsProposer;
	private Integer technologicalAsExecutor;
	private Integer technologicalAsCoExecutor;
	private Integer technologicalAsIntervenient;
	private Double culturalRequestedValue;
	private Double culturalApprovedValue;
	private Double culturalRaisedValue;
	private DataSource[] dataSources = null;

	public Integer getPartnershipsEnded() {
		return partnershipsEnded;
	}

	public void setPartnershipsEnded(Integer partnershipsEnded) {
		this.partnershipsEnded = partnershipsEnded;
	}

	public Integer getInExecutionPartnership() {
		return inExecutionPartnership;
	}

	public void setInExecutionPartnership(Integer inExecutionPartnership) {
		this.inExecutionPartnership = inExecutionPartnership;
	}

	public Double getGlobalValue() {
		return globalValue;
	}

	public void setGlobalValue(Double globalValue) {
		this.globalValue = globalValue;
	}

	public Double getTransferValue() {
		return transferValue;
	}

	public void setTransferValue(Double transferValue) {
		this.transferValue = transferValue;
	}

	public Double getFinancialCounterpartValue() {
		return financialCounterpartValue;
	}

	public void setFinancialCounterpartValue(Double financialCounterpartValue) {
		this.financialCounterpartValue = financialCounterpartValue;
	}

	public Double getOthersCounterpartValue() {
		return othersCounterpartValue;
	}

	public void setOthersCounterpartValue(Double othersCounterpartValue) {
		this.othersCounterpartValue = othersCounterpartValue;
	}

	public Double getCommittedValue() {
		return committedValue;
	}

	public void setCommittedValue(Double committedValue) {
		this.committedValue = committedValue;
	}

	public Double getDisbursedValue() {
		return disbursedValue;
	}

	public void setDisbursedValue(Double disbursedValue) {
		this.disbursedValue = disbursedValue;
	}

	public Integer getTechnologicalAsProposer() {
		return technologicalAsProposer;
	}

	public void setTechnologicalAsProposer(Integer technologicalAsProposer) {
		this.technologicalAsProposer = technologicalAsProposer;
	}

	public Integer getTechnologicalAsExecutor() {
		return technologicalAsExecutor;
	}

	public void setTechnologicalAsExecutor(Integer technologicalAsExecutor) {
		this.technologicalAsExecutor = technologicalAsExecutor;
	}

	public Integer getTechnologicalAsCoExecutor() {
		return technologicalAsCoExecutor;
	}

	public void setTechnologicalAsCoExecutor(Integer technologicalAsCoExecutor) {
		this.technologicalAsCoExecutor = technologicalAsCoExecutor;
	}

	public Integer getTechnologicalAsIntervenient() {
		return technologicalAsIntervenient;
	}

	public void setTechnologicalAsIntervenient(
			Integer technologicalAsIntervenient) {
		this.technologicalAsIntervenient = technologicalAsIntervenient;
	}

	public Double getCulturalRequestedValue() {
		return culturalRequestedValue;
	}

	public void setCulturalRequestedValue(Double culturalRequestedValue) {
		this.culturalRequestedValue = culturalRequestedValue;
	}

	public Double getCulturalApprovedValue() {
		return culturalApprovedValue;
	}

	public void setCulturalApprovedValue(Double culturalApprovedValue) {
		this.culturalApprovedValue = culturalApprovedValue;
	}

	public Double getCulturalRaisedValue() {
		return culturalRaisedValue;
	}

	public void setCulturalRaisedValue(Double culturalRaisedValue) {
		this.culturalRaisedValue = culturalRaisedValue;
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
				+ ((othersCounterpartValue == null) ? 0
						: othersCounterpartValue.hashCode());
		result = prime * result
				+ ((committedValue == null) ? 0 : committedValue.hashCode());
		result = prime
				* result
				+ ((culturalApprovedValue == null) ? 0 : culturalApprovedValue
						.hashCode());
		result = prime
				* result
				+ ((culturalRaisedValue == null) ? 0 : culturalRaisedValue
						.hashCode());
		result = prime
				* result
				+ ((culturalRequestedValue == null) ? 0
						: culturalRequestedValue.hashCode());
		result = prime * result + Arrays.hashCode(dataSources);
		result = prime * result
				+ ((disbursedValue == null) ? 0 : disbursedValue.hashCode());
		result = prime
				* result
				+ ((financialCounterpartValue == null) ? 0
						: financialCounterpartValue.hashCode());
		result = prime * result
				+ ((globalValue == null) ? 0 : globalValue.hashCode());
		result = prime
				* result
				+ ((inExecutionPartnership == null) ? 0
						: inExecutionPartnership.hashCode());
		result = prime
				* result
				+ ((partnershipsEnded == null) ? 0 : partnershipsEnded
						.hashCode());
		result = prime
				* result
				+ ((technologicalAsCoExecutor == null) ? 0
						: technologicalAsCoExecutor.hashCode());
		result = prime
				* result
				+ ((technologicalAsExecutor == null) ? 0
						: technologicalAsExecutor.hashCode());
		result = prime
				* result
				+ ((technologicalAsIntervenient == null) ? 0
						: technologicalAsIntervenient.hashCode());
		result = prime
				* result
				+ ((technologicalAsProposer == null) ? 0
						: technologicalAsProposer.hashCode());
		result = prime * result
				+ ((transferValue == null) ? 0 : transferValue.hashCode());
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
		PublicResources other = (PublicResources) obj;
		if (othersCounterpartValue == null) {
			if (other.othersCounterpartValue != null)
				return false;
		} else if (!othersCounterpartValue.equals(other.othersCounterpartValue))
			return false;
		if (committedValue == null) {
			if (other.committedValue != null)
				return false;
		} else if (!committedValue.equals(other.committedValue))
			return false;
		if (culturalApprovedValue == null) {
			if (other.culturalApprovedValue != null)
				return false;
		} else if (!culturalApprovedValue.equals(other.culturalApprovedValue))
			return false;
		if (culturalRaisedValue == null) {
			if (other.culturalRaisedValue != null)
				return false;
		} else if (!culturalRaisedValue.equals(other.culturalRaisedValue))
			return false;
		if (culturalRequestedValue == null) {
			if (other.culturalRequestedValue != null)
				return false;
		} else if (!culturalRequestedValue.equals(other.culturalRequestedValue))
			return false;
		if (!Arrays.equals(dataSources, other.dataSources))
			return false;
		if (disbursedValue == null) {
			if (other.disbursedValue != null)
				return false;
		} else if (!disbursedValue.equals(other.disbursedValue))
			return false;
		if (financialCounterpartValue == null) {
			if (other.financialCounterpartValue != null)
				return false;
		} else if (!financialCounterpartValue
				.equals(other.financialCounterpartValue))
			return false;
		if (globalValue == null) {
			if (other.globalValue != null)
				return false;
		} else if (!globalValue.equals(other.globalValue))
			return false;
		if (inExecutionPartnership == null) {
			if (other.inExecutionPartnership != null)
				return false;
		} else if (!inExecutionPartnership.equals(other.inExecutionPartnership))
			return false;
		if (partnershipsEnded == null) {
			if (other.partnershipsEnded != null)
				return false;
		} else if (!partnershipsEnded.equals(other.partnershipsEnded))
			return false;
		if (technologicalAsCoExecutor == null) {
			if (other.technologicalAsCoExecutor != null)
				return false;
		} else if (!technologicalAsCoExecutor
				.equals(other.technologicalAsCoExecutor))
			return false;
		if (technologicalAsExecutor == null) {
			if (other.technologicalAsExecutor != null)
				return false;
		} else if (!technologicalAsExecutor
				.equals(other.technologicalAsExecutor))
			return false;
		if (technologicalAsIntervenient == null) {
			if (other.technologicalAsIntervenient != null)
				return false;
		} else if (!technologicalAsIntervenient
				.equals(other.technologicalAsIntervenient))
			return false;
		if (technologicalAsProposer == null) {
			if (other.technologicalAsProposer != null)
				return false;
		} else if (!technologicalAsProposer
				.equals(other.technologicalAsProposer))
			return false;
		if (transferValue == null) {
			if (other.transferValue != null)
				return false;
		} else if (!transferValue.equals(other.transferValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PublicResources [partnershipsEnded=" + partnershipsEnded
				+ ", inExecutionPartnership=" + inExecutionPartnership
				+ ", globalValue=" + globalValue + ", transferValue="
				+ transferValue + ", financialCounterpartValue="
				+ financialCounterpartValue + ", OthersCounterpartValue="
				+ othersCounterpartValue + ", committedValue=" + committedValue
				+ ", disbursedValue=" + disbursedValue
				+ ", technologicalAsProposer=" + technologicalAsProposer
				+ ", technologicalAsExecutor=" + technologicalAsExecutor
				+ ", technologicalAsCoExecutor=" + technologicalAsCoExecutor
				+ ", technologicalAsIntervenient="
				+ technologicalAsIntervenient + ", culturalRequestedValue="
				+ culturalRequestedValue + ", culturalApprovedValue="
				+ culturalApprovedValue + ", culturalRaisedValue="
				+ culturalRaisedValue + ", dataSources="
				+ Arrays.toString(dataSources) + "]";
	}

	
	public Map<String, String> getContent() {

		NumberFormat fmtCurrency = NumberFormat.getCurrencyFormat();
		NumberFormat fmtNumber = NumberFormat.getDecimalFormat();

		Map<String, String> content = new LinkedHashMap<String, String>();
		content.put("Parcerias em execução",
				fmtNumber.format(inExecutionPartnership));
		content.put("Parcerias finalizadas",
				fmtNumber.format(partnershipsEnded));
		content.put("Valor total aprovado", fmtCurrency.format(globalValue));
		content.put("Valor de repasse", fmtCurrency.format(transferValue));
		content.put("Valor empenhado", fmtCurrency.format(committedValue));
		content.put("Valor desembolsado", fmtCurrency.format(disbursedValue));
		content.put("Contrapartida financeira",
				fmtCurrency.format(financialCounterpartValue));
		content.put("Outras contrapartidas",
				fmtCurrency.format(othersCounterpartValue));
		content.put("Valor Solicitado (Incentivo)",
				fmtCurrency.format(culturalRequestedValue));
		content.put("Valor Aprovado (Incentivo)",
				fmtCurrency.format(culturalApprovedValue));
		content.put("Valor Captado (Incentivo)",
				fmtCurrency.format(culturalRaisedValue));
		content.put("Correção de Inflação", 
				"Os valores exibidos estão corrigidos pelo IPCA do mês corrente.");		
				
		/*content.put("Proponente (P&D)",
				fmtNumber.format(technologicalAsProposer));
		content.put("Executor (P&D)",
				fmtNumber.format(technologicalAsExecutor));
		content.put("Coexecutor (P&D)",
				fmtNumber.format(technologicalAsCoExecutor));
		content.put("Interveniente (P&D)",
				fmtNumber.format(technologicalAsIntervenient));*/

		return content;
	}

}
