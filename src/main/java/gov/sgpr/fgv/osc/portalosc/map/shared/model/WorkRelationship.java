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
 *         Relações de trabalho da OSC
 * 
 */
public class WorkRelationship implements Serializable,
		KeyValueRenderer<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6290275393708090637L;

	private String oscLength;
	private int activeWorkers;
	private int legalWorkers;
	private int publicSectorWorkers;
	private int assignedWorkers;
	private int trainees;
	private int volunteers;
	private int temporaryWorkers;
	private int freelances;
	private int coop;
	private int medicalResidency;
	private int others;
	private DataSource[] dataSources = null;

	/**
	 * @return Tamanho da OSC em faixa de número de vínculos.
	 */
	public String getOscLength() {
		return oscLength;
	}

	/**
	 * @param oscLength
	 *            {@link #getOscLength()}
	 */
	public void setOscLength(String oscLength) {
		this.oscLength = oscLength;
	}

	public int getActiveWorkers() {
		return activeWorkers;
	}

	public void setActiveWorkers(int activeWorkers) {
		this.activeWorkers = activeWorkers;
	}

	public int getLegalWorkers() {
		return legalWorkers;
	}

	public void setLegalWorkers(int legalWorkers) {
		this.legalWorkers = legalWorkers;
	}

	public int getPublicSectorWorkers() {
		return publicSectorWorkers;
	}

	public void setPublicSectorWorkers(int publicSectorWorkers) {
		this.publicSectorWorkers = publicSectorWorkers;
	}

	public int getAssignedWorkers() {
		return assignedWorkers;
	}

	public void setAssignedWorkers(int assignedWorkers) {
		this.assignedWorkers = assignedWorkers;
	}

	public int getTrainees() {
		return trainees;
	}

	public void setTrainees(int trainees) {
		this.trainees = trainees;
	}

	public int getVolunteers() {
		return volunteers;
	}

	public void setVolunteers(int volunteers) {
		this.volunteers = volunteers;
	}

	public int getTemporaryWorkers() {
		return temporaryWorkers;
	}

	public void setTemporaryWorkers(int temporaryWorkers) {
		this.temporaryWorkers = temporaryWorkers;
	}

	public int getFreelances() {
		return freelances;
	}

	public void setFreelances(int freelances) {
		this.freelances = freelances;
	}

	public int getCoop() {
		return coop;
	}

	public void setCoop(int coop) {
		this.coop = coop;
	}

	public int getMedicalResidency() {
		return medicalResidency;
	}

	public void setMedicalResidency(int medicalResidency) {
		this.medicalResidency = medicalResidency;
	}

	public int getOthers() {
		return others;
	}

	public void setOthers(int others) {
		this.others = others;
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
		result = prime * result + activeWorkers;
		result = prime * result + assignedWorkers;
		result = prime * result + coop;
		result = prime * result + Arrays.hashCode(dataSources);
		result = prime * result + freelances;
		result = prime * result + legalWorkers;
		result = prime * result + medicalResidency;
		result = prime * result
				+ ((oscLength == null) ? 0 : oscLength.hashCode());
		result = prime * result + others;
		result = prime * result + publicSectorWorkers;
		result = prime * result + temporaryWorkers;
		result = prime * result + trainees;
		result = prime * result + volunteers;
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
		WorkRelationship other = (WorkRelationship) obj;
		if (activeWorkers != other.activeWorkers)
			return false;
		if (assignedWorkers != other.assignedWorkers)
			return false;
		if (coop != other.coop)
			return false;
		if (!Arrays.equals(dataSources, other.dataSources))
			return false;
		if (freelances != other.freelances)
			return false;
		if (legalWorkers != other.legalWorkers)
			return false;
		if (medicalResidency != other.medicalResidency)
			return false;
		if (oscLength == null) {
			if (other.oscLength != null)
				return false;
		} else if (!oscLength.equals(other.oscLength))
			return false;
		if (others != other.others)
			return false;
		if (publicSectorWorkers != other.publicSectorWorkers)
			return false;
		if (temporaryWorkers != other.temporaryWorkers)
			return false;
		if (trainees != other.trainees)
			return false;
		if (volunteers != other.volunteers)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WorkRelationship [oscLength=" + oscLength + ", activeWorkers="
				+ activeWorkers + ", legalWorkers=" + legalWorkers
				+ ", publicSectorWorkers=" + publicSectorWorkers
				+ ", assignedWorkers=" + assignedWorkers + ", trainees="
				+ trainees + ", volunteers=" + volunteers
				+ ", temporaryWorkers=" + temporaryWorkers + ", freelances="
				+ freelances + ", coop=" + coop + ", medicalResidency="
				+ medicalResidency + ", others=" + others + ", dataSources="
				+ Arrays.toString(dataSources) + "]";
	}

	
	public Map<String, String> getContent() {

		NumberFormat fmtNumber = NumberFormat.getDecimalFormat();

		Map<String, String> content = new LinkedHashMap<String, String>();
		content.put("Tamanho da OSC", oscLength + " vínculos");
		content.put("Vínculos ativos", fmtNumber.format(activeWorkers));
		content.put("Vínculos CLT", fmtNumber.format(legalWorkers));
		content.put("Servidores públicos",
				fmtNumber.format(publicSectorWorkers));
		content.put("Cedidos", fmtNumber.format(getAssignedWorkers()));
		content.put("Estagiários", fmtNumber.format(trainees));
		content.put("Voluntários", fmtNumber.format(volunteers));
		content.put("Autônomos", String.valueOf(fmtNumber.format(freelances)));
		content.put("Temporários",
				String.valueOf(fmtNumber.format(this.temporaryWorkers)));
		content.put("Cooperados", String.valueOf(fmtNumber.format(this.coop)));
		content.put("Residência médica",
				String.valueOf(fmtNumber.format(this.medicalResidency)));

		content.put("Outros", String.valueOf(fmtNumber.format(this.others)));

		return content;
	}

}
