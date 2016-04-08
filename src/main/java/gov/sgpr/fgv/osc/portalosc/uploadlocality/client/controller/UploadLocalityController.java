package gov.sgpr.fgv.osc.portalosc.uploadlocality.client.controller;

import gov.sgpr.fgv.osc.portalosc.uploadlocality.client.components.UploadLocalityFormWidget;
import gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.interfaces.UploadLocalityService;
import gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.interfaces.UploadLocalityServiceAsync;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
		
		final FormElement form = FormElement.as(DOM.getElementById("form_upload"));
//		final Element denvio_arquivo = DOM.getElementById("denvio_arquivo");
//		final Element denvio_servico = DOM.getElementById("denvio_servico");
		final Element envio_de_arquivo = DOM.getElementById("envio_de_arquivo");
		final Element envio_de_servico = DOM.getElementById("envio_de_servico");
		final Element btnEnviar = DOM.getElementById("btnEnviar");
		
		Label titleLabel = new Label("Selecione o arquivo a ser enviado");
		Label fileLabel = new Label("Arquivo Enviado:");
		FileUpload fileUpload = new FileUpload();
		fileUpload.setName("Selecionar arquivo");
		Label typeLabel = new Label("Tipo de Arquivo:");
		final ListBox typeListBox = new ListBox();
		typeListBox.addItem("CSV");
		typeListBox.addItem("XLS");
		
		formPanel.add(titleLabel);
		formPanel.add(fileLabel);
		formPanel.add(fileUpload);
		formPanel.add(typeLabel);
		formPanel.add(typeListBox);
		
//		Event.sinkEvents(denvio_arquivo, Event.ONCHANGE);
//		Event.setEventListener(denvio_arquivo, new EventListener() {
//			public void onBrowserEvent(Event event) {
//				envio_de_arquivo.getStyle().setDisplay(Display.BLOCK);
//				envio_de_servico.getStyle().setDisplay(Display.NONE);
//			}
//		});
//		
//		Event.sinkEvents(denvio_servico, Event.ONCHANGE);
//		Event.setEventListener(denvio_servico, new EventListener() {
//			public void onBrowserEvent(Event event) {
//				envio_de_arquivo.getStyle().setDisplay(Display.NONE);
//				envio_de_servico.getStyle().setDisplay(Display.BLOCK);
//			}
//		});
		
		Event.sinkEvents(btnEnviar, Event.ONCLICK);
		Event.setEventListener(btnEnviar, new EventListener() {
			public void onBrowserEvent(Event event) {
				form.submit();
			}
		});
	}
}