package gov.sgpr.fgv.osc.portalosc.uploadlocality.client.controller;

import gov.sgpr.fgv.osc.portalosc.uploadlocality.client.components.UploadLocalityFormWidget;
import gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.interfaces.UploadLocalityService;
import gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.interfaces.UploadLocalityServiceAsync;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.RootPanel;

public class UploadLocalityController {
	private static Logger logger = Logger.getLogger(UploadLocalityController.class.getName());
	private final RootPanel formPanel = RootPanel.get("form_upload_locality");
	private UploadLocalityFormWidget formWidget = new UploadLocalityFormWidget();
	private UploadLocalityServiceAsync uploadService = GWT.create(UploadLocalityService.class);
	
	public void init() {
		logger.info("Iniciando módulo de representante de estados ou munícipios");
		if (formPanel != null) {
			addFormWidget();
		}
	}
	
	private void addFormWidget() {
		logger.info("Adicionando widget de formulário de representante de estados ou munícipios");
		
		formPanel.add(formWidget);
		
		final Element denvio_arquivo = DOM.getElementById("denvio_arquivo");
		final Element denvio_servico = DOM.getElementById("denvio_servico");
		final Element btnEnviar = DOM.getElementById("btnEnviar");
		final Element envio_de_arquivo = DOM.getElementById("envio_de_arquivo");
		final Element envio_de_servico = DOM.getElementById("envio_de_servico");
		
		Event.sinkEvents(denvio_arquivo, Event.ONCHANGE);
		Event.setEventListener(denvio_arquivo, new EventListener() {
			public void onBrowserEvent(Event event) {
				envio_de_arquivo.getStyle().setDisplay(Display.BLOCK);
				envio_de_servico.getStyle().setDisplay(Display.NONE);
			}
		});
		
		Event.sinkEvents(denvio_servico, Event.ONCHANGE);
		Event.setEventListener(denvio_servico, new EventListener() {
			public void onBrowserEvent(Event event) {
				envio_de_arquivo.getStyle().setDisplay(Display.NONE);
				envio_de_servico.getStyle().setDisplay(Display.BLOCK);
			}
		});
		
		Event.sinkEvents(btnEnviar, Event.ONCLICK);
		Event.setEventListener(btnEnviar, new EventListener() {
			public void onBrowserEvent(Event event) {
				if(denvio_arquivo.getPropertyBoolean("checked")){
					if(validateArquivo()){
						
					}
				}else if(denvio_servico.getPropertyBoolean("checked")){
					if(validateServico()){
						
					}
				}
			}
		});
	}
	
	private Boolean validateArquivo(){
		String darquivo = InputElement.as(DOM.getElementById("darquivo")).getValue().toLowerCase();
		String dtipo_arquivo = SelectElement.as(DOM.getElementById("dtipo_arquivo")).getValue().toLowerCase();
		
//		String format = darquivo.substring(darquivo.lastIndexOf(".") + 1, darquivo.length());
		
		return true;
	}
	
	private Boolean validateServico(){
		String dservico = InputElement.as(DOM.getElementById("dservico")).getValue().toLowerCase();
		String dtipo_servico = SelectElement.as(DOM.getElementById("dtipo_servico")).getValue().toLowerCase();
		String dtipo_periodo = SelectElement.as(DOM.getElementById("dtipo_periodo")).getValue().toLowerCase();
		
		return true;
	}
}