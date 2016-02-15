package gov.sgpr.fgv.osc.portalosc.map.client.components.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * @author Thassae Santos
 * 
 */
public class PaperClipCheckListSection extends
		//PaperClipAbstractSection<Map<String, Boolean>> {
		PaperClipAbstractSection<Map<String, String>> {

	private String sectionTitle;
	private Map<String, Boolean> checkList = new LinkedHashMap<String, Boolean>();
	private LinkedHashMap<String, String> keyValueContent = new LinkedHashMap<String, String>();

	/**
	 * Adiciona um elemento String à seção.
	 * 
	 * @param element
	 *            O elemento a ser adicionado.
	 */
	public void addElementToList(String element, boolean checked) {
		this.checkList.put(element, checked);
	}

	/**
	 * Remove um elemento String da seção
	 * 
	 * @param element
	 *            O elemento a ser removido.
	 */
	public void removeElementFromList(String element) {
		if (this.checkList.containsKey(element)) {
			this.checkList.remove(element);
		}
	}
	
	/**
	 * Adiciona um par chave/valor à seção.
	 * 
	 * @param key
	 *            A String que servirá como chave.
	 * @param value
	 *            A String que servirá como valor.
	 */
	public void addKeyValue(String key, String value) {
		this.keyValueContent.put(key, value);
	}

	/**
	 * Remove um par chave/valor da seção
	 * 
	 * @param key
	 *            A String chave.
	 */
	public void removeKeyValue(String key) {
		if (this.keyValueContent.containsKey(key)) {
			this.keyValueContent.remove(key);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.model.PaperClipAbstractSection#
	 * getSectionContent()
	 */
	@Override
	/*public Map<String, Boolean> getSectionContent() {
		return checkList;
	}*/
	public Map<String, String> getSectionContent() {
		return keyValueContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.model.PaperClipAbstractSection#
	 * getSectionTitle()
	 */
	@Override
	public String getSectionTitle() {
		return sectionTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.model.PaperClipAbstractSection#
	 * setSectionTitle(java.lang.String)
	 */
	@Override
	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}

}
