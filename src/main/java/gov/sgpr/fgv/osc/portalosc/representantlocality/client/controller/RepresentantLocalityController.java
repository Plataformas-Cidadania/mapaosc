package gov.sgpr.fgv.osc.portalosc.representantlocality.client.controller;

import gov.sgpr.fgv.osc.portalosc.representantlocality.client.components.RepresentantLocalityFormWidget;
import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService;
import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityServiceAsync;
import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model.RepresentantLocalityUser;
import gov.sgpr.fgv.osc.portalosc.user.client.components.PopupPassword;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserServiceAsync;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
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
	private PopupPassword popupPassword = new PopupPassword();
	private UserServiceAsync userService = GWT.create(UserService.class);
	private RepresentantLocalityServiceAsync representantLocalityService = GWT.create(RepresentantLocalityService.class);
	private static byte[] desKey;
	
	public void init() {
		logger.info("Iniciando módulo de representante de estados ou munícipios");
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
		userService.getEncryptKey(callback);
		
		if (formPanel != null) {
			addFormWidget();
		}
	}
	
	private void addFormWidget() {
		logger.info("Adicionando widget de formulário de representante de estados ou munícipios");
		
		formPanel.add(formWidget);
		
		formWidget.addSubmitListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Validando cadastro de usuário padrão");
				if (formWidget.isValid()) {
					logger.info("Buscando dados do cadastro de usuário padrão");
					validateRepresentantLocality(formWidget.getUser());
				}
			}
		});
		
		formWidget.addSubmitform(new EventListener() {
			public void onBrowserEvent(Event event) {
				if (event.getKeyCode() == KeyCodes.KEY_ENTER){
					logger.info("Validando cadastro de usuário padrão");
					if (formWidget.isValid()) {
						logger.info("Buscando dados do cadastro de usuário padrão");
						validateRepresentantLocality(formWidget.getUser());
					}
				}
			}
		});
		
		formWidget.addCancelListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Fechando janela de cadastro de representantes de estados e munícipios");
				formWidget.close();
				Window.Location.replace(GWT.getHostPageBaseURL() + "Map.html");
			}
		});
	}
	
	private void addRepresentantLocalityUser(final RepresentantLocalityUser user) {
		logger.info("Adicionando representante da OSC");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(Void result) {
				logger.info("Fechando tela de cadastro");
				formWidget.close();
				
				popupPassword.onModuleLoad();
				Element pop = DOM.getElementById("popup");
				pop.removeAllChildren();
				Element header = DOM.createElement("h2");
				header.setInnerText("Cadastre-se no Mapa");
				Element div = DOM.createDiv();
				Element p = DOM.createElement("p");
				p.setInnerText("Um e-mail foi enviado com as instruções para confirmação do seu cadastro. Outro e-mail foi enviado a OSC informando o seu cadastro.");
				Element a = DOM.createAnchor();
				a.setInnerText("Ok");
				a.setAttribute("href", "#");
				Event.sinkEvents(a, Event.ONCLICK);
				Event.setEventListener(a, new EventListener() {
					public void onBrowserEvent(Event event) {
						popupPassword.close();
						logger.info("Redirecionando para tela principal");
						String url = GWT.getHostPageBaseURL() + "Map.html";
						Window.Location.replace(url);
					}
				});
				div.appendChild(p);
				div.appendChild(a);
				pop.appendChild(header);
				pop.appendChild(div);
			}
		};
		representantLocalityService.addUser(user, callback);
	}
	
	private void validateEmail(final String email) {
		logger.info("Validando email");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(DefaultUser result) {
				if(result != null){
					logger.info("Email encontrado");
					statusUserPassword(email, result.getId());
				}
				else{
					logger.info("Email não foi encontrado");
					popupPassword.addInvalidEmail();
				}
			}
		};
		userService.getUser(email, callback);
	}
	
	private void statusUserPassword(final String email, final Integer idUser){
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Boolean result) {
				if(result == true){
					addTokenPassword(idUser);
				}else{
					popupPassword.addConfirme();
				}
			}
		};
		userService.getStatus(email, callback);
	}
	
	public void addTokenPassword(Integer idUser) {
		logger.info("Adicionando Token Esqueci Senha");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(Void result) {
				logger.info("Token Adicionado");
				Element pop = DOM.getElementById("popup");
				pop.removeAllChildren();
				Element header = DOM.createElement("h2");
				header.setInnerText("Esqueceu a senha?");
				Element div = DOM.createDiv();
				Element p = DOM.createElement("p");
				p.setInnerText("Um e-mail foi enviado para você com instruções para redefinir sua senha.");
				Element a = DOM.createAnchor();
				a.setInnerText("Ok");
				a.setAttribute("href", "#");
				Event.sinkEvents(a, Event.ONCLICK);
				Event.setEventListener(a, new EventListener() {
					public void onBrowserEvent(Event event) {
						popupPassword.close();
					}
				});
				div.appendChild(p);
				div.appendChild(a);
				pop.appendChild(header);
				pop.appendChild(div);
			}
		};
		userService.addTokenPassword(idUser, callback);
	}
	
	private void validateRepresentantLocality(final RepresentantLocalityUser user) {
		logger.info("Validando usuário padrão");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(DefaultUser result) {
				logger.info("Usuário encontrado");
				if (result != null) {
					formWidget.addInvalidEmail(user.getEmail());
				}
				validateCpf(user);
			}
		};
		userService.getUser(user.getEmail(), callback);
	}
	
	private void validateCpf(final DefaultUser user) {
		logger.info("Validando CPF");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(DefaultUser result) {
				logger.info("Usuário encontrado pelo CPF");
				if (result != null) {
					formWidget.addInvalidCpf(String.valueOf(user.getCpf()));
				}
				logger.info("Validando usuário padrão");
				if (formWidget.isValid()){
					addRepresentantLocalityUser(user);
				}
			}
		};
		userService.getUser(user.getCpf(), callback);
	}
	
	private void addRepresentantLocalityUser(final DefaultUser user) {
		logger.info("Adicionando usuário");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(Void result) {
				logger.info("Fechando tela de cadastro");
				formWidget.close();
				
				popupPassword.onModuleLoad();
				Element pop = DOM.getElementById("popup");
				pop.removeAllChildren();
				Element header = DOM.createElement("h2");
				header.setInnerText("Cadastre-se no Mapa");
				Element div = DOM.createDiv();
				Element p = DOM.createElement("p");
				p.setInnerText("Um e-mail foi enviado com as instruções para confirmação do seu cadastro.");
				Element a = DOM.createAnchor();
				a.setInnerText("Ok");
				a.setAttribute("href", "#");
				Event.sinkEvents(a, Event.ONCLICK);
				Event.setEventListener(a, new EventListener() {
					public void onBrowserEvent(Event event) {
						popupPassword.close();
						logger.info("Redirecionando para tela principal");
						String url = GWT.getHostPageBaseURL() + "Map.html";
						Window.Location.replace(url);
					}
				});
				div.appendChild(p);
				div.appendChild(a);
				pop.appendChild(header);
				pop.appendChild(div);
			}
		};
		userService.addUser(user, callback);
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