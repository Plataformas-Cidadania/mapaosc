package gov.sgpr.fgv.osc.portalosc.representantlocality.client.controller;

import gov.sgpr.fgv.osc.portalosc.representantlocality.client.components.RepresentantLocalityFormWidget;
import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService;
import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityServiceAsync;
import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model.CountyModel;
import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model.RepresentantLocalityModel;
import gov.sgpr.fgv.osc.portalosc.user.client.components.PopupPassword;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserServiceAsync;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;

public class RepresentantLocalityController {
	private static Logger logger = Logger.getLogger(RepresentantLocalityController.class.getName());
	
	private final RootPanel formPanel = RootPanel.get("form_representant_locality");
	private RepresentantLocalityFormWidget formWidget = new RepresentantLocalityFormWidget();
	private PopupPassword popup = new PopupPassword();
	private RepresentantLocalityServiceAsync representantLocalityService = GWT.create(RepresentantLocalityService.class);
	private UserServiceAsync userService = GWT.create(UserService.class);
	private static byte[] desKey;
	
	public void init() {
		logger.info("Iniciando módulo de representante de estados ou munícipios");
		AsyncCallback<Byte[]> callback = new AsyncCallback<Byte[]>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(Byte[] result) {
				logger.info("Chave de criptografia encontrada");
				desKey = new byte[result.length];
				for (int i = 0; i < result.length; i++) {
					desKey[i] = result[i];
				}
			}
		};
		representantLocalityService.getEncryptKey(callback);
		
		addFormWidget();
	}
	
	private void addFormWidget() {
		logger.info("Adicionando widget de formulário de representante de estados ou munícipios");
		
		formPanel.add(formWidget);
		
		formWidget.addSubmitListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Validando cadastro de representante de localidade");
				if (formWidget.isValid()) {
					validateEmail(formWidget.getUser());
				}
				if (event.getKeyCode() == KeyCodes.KEY_ENTER){
					validateEmail(formWidget.getUser());
				}
			}
		});
		
		final SelectElement municipio = SelectElement.as(DOM.getElementById("cmun"));
		
		final Element tipo_representante_estado = DOM.getElementById("tipo_representante_estado");
		Event.sinkEvents(tipo_representante_estado, Event.ONCHANGE);
		Event.setEventListener(tipo_representante_estado, new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Habilitando município");
				municipio.setSelectedIndex(0);
				municipio.setAttribute("disabled", "");
			}
		});
		
		final Element tipo_representante_municipio = DOM.getElementById("tipo_representante_municipio");
		Event.sinkEvents(tipo_representante_municipio, Event.ONCHANGE);
		Event.setEventListener(tipo_representante_municipio, new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Habilitando município");
				municipio.removeAttribute("disabled");
			}
		});
		
		final SelectElement estado = SelectElement.as(DOM.getElementById("cest"));
		Event.sinkEvents(estado, Event.ONCHANGE);
		Event.setEventListener(estado, new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Buscando municípios do estado");
				addCounty(Integer.valueOf(estado.getValue()));
			}
		});
	}
	
	private void validateEmail(final RepresentantLocalityModel user) {
		logger.info("Validando email");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(DefaultUser result) {
				if (result != null) {
					formWidget.addInvalidEmail(user.getEmail());
				}
				validateCpf(user);
			}
		};
		userService.getUser(user.getEmail(), callback);
	}
	
	private void validateCpf(final RepresentantLocalityModel user) {
		logger.info("Validando CPF");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(DefaultUser result) {
				if (result != null) {
					formWidget.addInvalidCpf(String.valueOf(user.getCpf()));
				}
				if (formWidget.isValid()){
					addRepresentantLocalityUser(user);
				}
			}
		};
		userService.getUser(user.getCpf(), callback);
	}
	
	private void addRepresentantLocalityUser(final RepresentantLocalityModel user) {
		logger.info("Adicionando representante de localidades");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(Void result) {
				openPopup("Confirme o cadastro", "Um e-mail foi enviado com as instruções para confirmação do seu cadastro.");
			}
		};
		representantLocalityService.addRepresentantLocality(user, callback);
	}
	
	private void addCounty(final Integer idState) {
		logger.info("Adicionando representante de localidades");
		AsyncCallback<List<CountyModel>> callback = new AsyncCallback<List<CountyModel>>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(List<CountyModel> result) {
				SelectElement municipio = SelectElement.as(DOM.getElementById("cmun"));
				OptionElement o = municipio.getOptions().getItem(0);
				municipio.clear();
				municipio.add(o, null);
				for(CountyModel p : result){
					OptionElement opt = OptionElement.as(DOM.createOption());
					opt.setText(String.valueOf(p.getName()));
					opt.setValue(String.valueOf(p.getId()));
					municipio.add(opt, null);
				}
			}
		};
		representantLocalityService.getCounty(idState, callback);
	}
	
	private void openPopup(String title, String message){
		logger.info("Abrindo popup de confirmação do cadastro");
		popup.onModuleLoad();
		Element pop = DOM.getElementById("popup");
		pop.removeAllChildren();
		Element header = DOM.createElement("h2");
		header.setInnerText(title);
		Element div = DOM.createDiv();
		Element p = DOM.createElement("p");
		p.setInnerText(message);
		Element a = DOM.createAnchor();
		a.setInnerText("Ok");
		a.setAttribute("href", "#");
		Event.sinkEvents(a, Event.ONCLICK);
		Event.setEventListener(a, new EventListener() {
			public void onBrowserEvent(Event event) {
				popup.close();
			}
		});
		div.appendChild(p);
		div.appendChild(a);
		pop.appendChild(header);
		pop.appendChild(div);
	}
	
	public static String encrypt(String passwd) {
		logger.info("Encriptando senha");
		TripleDesCipher cipher = new TripleDesCipher();
		cipher.setKey(desKey);
		try {
			return cipher.encrypt(passwd);
		} catch (DataLengthException caught) {
			logger.log(Level.SEVERE, caught.getMessage());
			return null;
		} catch (IllegalStateException caught) {
			logger.log(Level.SEVERE, caught.getMessage());
			return null;
		} catch (InvalidCipherTextException caught) {
			logger.log(Level.SEVERE, caught.getMessage());
			return null;
		}
	}
	
	public static String decrypt(String passwd) {
		logger.info("Decriptando senha");
		TripleDesCipher cipher = new TripleDesCipher();
		cipher.setKey(desKey);
		try {
			return cipher.decrypt(passwd);
		} catch (DataLengthException caught) {
			logger.log(Level.SEVERE, caught.getMessage());
			return null;
		} catch (IllegalStateException caught) {
			logger.log(Level.SEVERE, caught.getMessage());
			return null;
		} catch (InvalidCipherTextException caught) {
			logger.log(Level.SEVERE, caught.getMessage());
			return null;
		}
	}
}