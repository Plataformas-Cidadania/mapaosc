package gov.sgpr.fgv.osc.portalosc.configuration.client.controller;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;

import gov.sgpr.fgv.osc.portalosc.configuration.client.components.FormularioWidget;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.ConfigurationService;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.ConfigurationServiceAsync;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.model.ConfigurationModel;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.model.SearchResult;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.SearchService;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.SearchServiceAsync;

public class ConfigurationController {
	private static Logger logger = Logger.getLogger(ConfigurationController.class.getName());
	private ConfigurationServiceAsync configurationService = com.google.gwt.core.shared.GWT.create(ConfigurationService.class);
	private FormularioWidget formularioWidget = new FormularioWidget();
	private final RootPanel formularioElement = RootPanel.get("modal_formulario");
	private static byte[] desKey;
	private Date changeDate;
	private static final int DELAY = 500;
	private static int LIMIT = 5;
	private SearchServiceAsync searchService = GWT.create(SearchService.class);
	
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
		configurationService.getEncryptKey(callback);
		
		addFormularioWidget();
		
		logger.info("Buscando Cookies");
		Integer idUser = Integer.valueOf(Cookies.getCookie("idUser"));
		logger.info(" ========== " + String.valueOf(idUser) + " ==========");
		if(idUser != null){
			setConfiguration(idUser);
		}
	}
	
	private void addFormularioWidget() {
		logger.info("Adicionando widget do formulário");
		
		formularioElement.add(formularioWidget);
		
		formularioWidget.addFocusListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				formularioWidget.setValue("");
			}
		});
		
		formularioWidget.addSearchChangeListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Inserindo temporizador");
				changeDate = new Date();
				final Date thisDate = changeDate;
				Timer t = new Timer() {
					@Override
					public void run() {						
						logger.info("Buscando OSC no cadastro de usuário representante da OSC");
						if (changeDate.equals(thisDate)) {
							logger.info("Executando busca");
							String criteria = formularioWidget.getValue();
							search(criteria);
						}
					}
				};
				t.schedule(DELAY);
			}
		});
		
//		formularioWidget.addSearchClickListener(new EventListener() {
//			public void onBrowserEvent(Event event) {
//				logger.info("entityUser.addSearchClickListener");
//				String criteria = formularioWidget.getValue();
//				AsyncCallback<List<SearchResult>> callbackSearch = new AsyncCallback<List<SearchResult>>() {
//					public void onFailure(Throwable caught) {
//						logger.log(Level.SEVERE, caught.getMessage());
//					}
//					public void onSuccess(List<SearchResult> result) {
//						if (!result.isEmpty()) {
//							formularioWidget.showOrganization(result.get(0).getValue(), String.valueOf(result.get(0).getId()));
//						}
//					}
//				};
//				if (!criteria.trim().isEmpty()){
//					searchService.search(criteria, LIMIT, callbackSearch);
//				}
//			}
//		});
		
		formularioWidget.addCancelOSCClickListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				formularioWidget.clearOSC();
			}
		});
		
		formularioWidget.addSalvarListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Salvar configuracao");
				validateConfiguration(formularioWidget.getUser());
			}
		});
		
		formularioWidget.addCancelarListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Redirecionando para tela principal");
				String url = GWT.getHostPageBaseURL() + "Map.html";
				Window.Location.replace(url);
			}
		});
	}
	
	public void search(String criteria) {
		logger.info("Realizando busca");
		AsyncCallback<List<SearchResult>> callbackSearch = new AsyncCallback<List<SearchResult>>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			public void onSuccess(List<SearchResult> result) {
				if (!result.isEmpty()) {
					addResultItems(result);
				}
			}
		};
		if (!criteria.trim().isEmpty()){
			searchService.search(criteria, LIMIT, callbackSearch);
		}
	}
	
	public void addResultItems(final List<SearchResult> items) {
		logger.info("Adicionando resultados da busca");
		EventListener listener = new EventListener() {
			public void onBrowserEvent(Event event) {
				Element elem = Element.as(event.getCurrentEventTarget());
				final String oscId = elem.getAttribute("value");
				formularioWidget.showOrganization(elem.getInnerText(), oscId);
			}
		};
		formularioWidget.addResultItems(items, listener);
	}
	
	/**
	 * @return Busca o usuário no banco de dados.
	 */
	public void setConfiguration(Integer id){
		logger.info("Buscando dados do usuário");
		AsyncCallback<ConfigurationModel> callback = new AsyncCallback<ConfigurationModel>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			public void onSuccess(ConfigurationModel result) {
				logger.info("Usuário encontrado");
				formularioWidget.setUser(result);
				setOrganization(Integer.valueOf(result.getIdOsc()));
			}
		};
		configurationService.readConfigurationByID(id, callback);
	};
	
	private void setOrganization(Integer idOSC){
		logger.info("Buscando dados da organização");
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			public void onSuccess(String result) {
				logger.info("Organização encontrado");
				DOM.getElementById("oscName").setInnerText(result);
			}
		};
		configurationService.readNameOSC(idOSC, callback);
	}
	
	private void validateConfiguration(final ConfigurationModel configuration) {
		logger.info("Validando configuracao");
		AsyncCallback<ConfigurationModel> callback = new AsyncCallback<ConfigurationModel>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			public void onSuccess(ConfigurationModel result) {
				logger.info("Usuário encontrado");
				if (formularioWidget.isValid()) {
					validateEmail(configuration);
				}
			}
		};
		configurationService.readConfigurationByID(configuration.getId(), callback);
	}
	
	private void validateEmail(final ConfigurationModel user) {
		logger.info("Validando email");
		AsyncCallback<ConfigurationModel> callback = new AsyncCallback<ConfigurationModel>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			public void onSuccess(ConfigurationModel result) {
				logger.info("Usuário encontrado");
				if (result != null) {
					formularioWidget.addInvalidEmail(user.getEmail());
				}
				validateCpf(user);
			}
		};
		configurationService.readConfigurationByEmail(user.getEmail(), user.getId(), callback);
	}
	
	private void validateCpf(final ConfigurationModel configuration) {
		logger.info("Validando CPF");
		AsyncCallback<ConfigurationModel> callback = new AsyncCallback<ConfigurationModel>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(ConfigurationModel result) {
				logger.info("Usuário encontrado pelo CPF");
				if (result != null) {
					formularioWidget.addInvalidCpf(String.valueOf(configuration.getCPF()));
				}
				else{
					updateConfiguration(configuration);
				}
			}
		};
		configurationService.readConfigurationByCPF(configuration.getCPF(), configuration.getId(), callback);
	}
	
	private void updateConfiguration(final ConfigurationModel configuration) {
		logger.info("Atualizando usuário");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				DOM.getElementById("cnome").setAttribute("value", "Failure");
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(Void result) {
				DOM.getElementById("cemail").setAttribute("value", "Sucess");
				logger.info("Redirecionando para tela principal");
				if(configuration.getTipoUsuario() != 3){
					Cookies.setCookie("oscUid", configuration.getEmail());
				}
				else if(configuration.getTipoUsuario() == 3){
					Cookies.setCookie("oscSnUid", configuration.getEmail());
				}
				setConfiguration(configuration.getId());
				String url = GWT.getHostPageBaseURL() + "Map.html";
				Window.Location.replace(url);
			}
		};
		configurationService.updateConfiguration(configuration, formularioWidget.getEmail(), callback);
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
