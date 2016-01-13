package gov.sgpr.fgv.osc.portalosc.organization.client.controller;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;

import gov.sgpr.fgv.osc.portalosc.organization.client.components.FormularioWidget;
import gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationService;
import gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationServiceAsync;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.ConfigurationModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.OrganizationModel;
import gov.sgpr.fgv.osc.portalosc.organization.client.components.LogonWidget;

public class OrganizationController {
	private static Logger logger = Logger.getLogger(OrganizationController.class.getName());
	private OrganizationServiceAsync organizationService = com.google.gwt.core.shared.GWT.create(OrganizationService.class);
	private FormularioWidget formularioWidget = null;
	private final RootPanel formularioElement = RootPanel.get("modal_formulario");
	private static byte[] desKey;
	
	private OrganizationModel organization = new OrganizationModel();
	
	public void init() {
		logger.info("Iniciando módulo de configuração");
		
		AsyncCallback<Byte[]> callback = new AsyncCallback<Byte[]>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(Byte[] result) {
				logger.info("Chave de criptografia encontrada");
				desKey = new byte[result.length];
				for (int i = 0; i < result.length; i++) {
					desKey[i] = result[i];
				}
			}
		};
		
		logger.info("Buscando Chave de criptografia");
		organizationService.getEncryptKey(callback);
		
		getOrganization(Integer.parseInt(History.getToken().substring(1)));
	}
	
	private void getOrganization(Integer id){
		logger.info("Buscando dados da organização");
		AsyncCallback<OrganizationModel> callback = new AsyncCallback<OrganizationModel>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			public void onSuccess(OrganizationModel result) {
				logger.info("Organização encontrada");
				organization = result;
				checkUser();
			}
		};
		organizationService.getOrganizationByID(id, callback);
	}
	
	private void setOrganization(OrganizationModel organizationModel){
		logger.info("Buscando dados da organização");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			public void onSuccess(Void result) {
				logger.info("Organização encontrado");
				Window.Location.replace(GWT.getHostPageBaseURL() + "Map.html");
			}
		};
		organizationService.setOrganization(organizationModel, callback);
	}
	
	private void checkUser(){
		logger.info("Verfificando se usuário é representante da OSC");
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			public void onSuccess(Boolean result) {
				logger.info("Organização encontrado");
				if(result){
					addFormularioWidget(true);
				}else{
					addFormularioWidget(false);
				}
			}
		};
		String userId = Cookies.getCookie("oscUid");
		if (userId != null && !userId.isEmpty()) {
			organizationService.searchOSCbyUser(Integer.parseInt(userId), organization.getId(), callback);
		}else{
			addFormularioWidget(false);
		}
	}
	
	private void addFormularioWidget(Boolean editable) {
		logger.info("Adicionando widget do formulário");
		formularioWidget = new FormularioWidget(organization, editable);
		formularioElement.add(formularioWidget);
	}
	
	private void addLoggedInWidget(ConfigurationModel user) {
		logger.info("Adicionando widget de usuário logado");
		
		Element element = Document.get().getElementById("topo_acesso");
		
		LogonWidget logonWidget = new LogonWidget(user);
		element.appendChild(logonWidget.getElement());
		logonWidget.addLogoutListener(new EventListener(){
			public void onBrowserEvent(Event event) {
				logger.info("Logout");
				logout();
			}
		});
	}
	
	private void logout() {
		logger.info("Realizando logout");
		Cookies.removeCookie("oscUid");
		Cookies.removeCookie("oscSnUid");
		Window.Location.replace(GWT.getHostPageBaseURL() + "Map.html");
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
