package gov.sgpr.fgv.osc.portalosc.user.client.controller;

import gov.sgpr.fgv.osc.portalosc.user.client.components.LogonWidget;
import gov.sgpr.fgv.osc.portalosc.user.client.components.PopupPassword;
import gov.sgpr.fgv.osc.portalosc.user.client.components.RepresentantFormWidget;
import gov.sgpr.fgv.osc.portalosc.user.client.components.SocialNetFormWidget;
import gov.sgpr.fgv.osc.portalosc.user.client.components.UnavailableFormWidget;
import gov.sgpr.fgv.osc.portalosc.user.client.components.UserFormWidget;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SearchService;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SearchServiceAsync;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SocialNetworkService;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SocialNetworkServiceAsync;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserServiceAsync;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.FacebookUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.RepresentantUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.SearchResult;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;

public class UserController {
	private static Logger logger = Logger.getLogger(UserController.class.getName());
	private final RootPanel loadingUserPanel = RootPanel.get("loading_user");
	private final RootPanel defaultUserPanel = RootPanel.get("modal_cadastro");
	private final RootPanel socialNetPanel = RootPanel.get("modal_redes");
	private final RootPanel organizationUserPanel = RootPanel.get("modal_entidade");
	private UserFormWidget defaultUser = new UserFormWidget();
	private SocialNetFormWidget socialNetUser = new SocialNetFormWidget();
	private RepresentantFormWidget organizationUserWidget = new RepresentantFormWidget();
	private UnavailableFormWidget unavailableSocialNet = new UnavailableFormWidget();
	private LogonWidget logon = new LogonWidget();
	private PopupPassword popupPassword = new PopupPassword();
	private UserServiceAsync userService = com.google.gwt.core.shared.GWT.create(UserService.class);
	private SocialNetworkServiceAsync netUserService = com.google.gwt.core.shared.GWT.create(SocialNetworkService.class);
	private SocialNetworkServiceAsync socialNetService = com.google.gwt.core.shared.GWT.create(SocialNetworkService.class);
	private SearchServiceAsync searchService = GWT.create(SearchService.class);
	private static DefaultUser currentUser = null;
	private static final int DELAY = 500;
	private static final String LOGON_CONTAINER = "topo_acesso";
	private static int LIMIT = 5;
	private static byte[] desKey;
	private Date changeDate;
	
	public void init() {
		logger.info("Iniciando módulo de Usuário");
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
		userService.getEncryptKey(callback);
		String facebookUserCode = Location.getParameter("code");

		logger.info("Buscando Cookies");
		String uid = Cookies.getCookie("oscUid");
		String snUid = Cookies.getCookie("oscSnUid");
		logger.info("Iniciando widget de logon");
		Element logonDiv = Document.get().getElementById(LOGON_CONTAINER);
		if (logonDiv != null) {
			if (uid != null && !uid.isEmpty()) {
				logonDefaultUser(uid);
			} else if (snUid != null && !snUid.isEmpty()) {
				logonFacebookUser(snUid);
			} else
				addLogonWidget(logonDiv);
		}
		if (defaultUserPanel != null) {
			addDefaultUserWidget();
		}
		if (socialNetPanel != null) {
			socialNetPanel.add(unavailableSocialNet);
			//addSocialNetUserWidget();
		}
		if (organizationUserPanel != null) {
			addOrganizationUserWidget();
		}
		if (facebookUserCode != null) {
			getSocialUserInfo(facebookUserCode);
		}
	}
	
	private void addLogonWidget(Element elem) {
		// elem.addClassName("deslogado");
		logger.info("Adicionando widget de logon");
		
		elem.appendChild(logon.getElement());
		
		logon.addLogonListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Validando logon");
				if (logon.isValid()) {
					statusUser(logon.getEmail());
				}
			}
		});
		
		logon.addLogonLabel(new EventListener() {
			public void onBrowserEvent(Event event) {
				if(event.getKeyCode() == KeyCodes.KEY_ENTER){
					logger.info("Validando logon");
					if (logon.isValid()) {
						statusUser(logon.getEmail());
					}
				}
			}
		});
		
		logon.addEsqueci(new EventListener() {
			public void onBrowserEvent(Event event) {
				addEsqueciSenha();
			}			
		});
	}
	
	private void addEsqueciSenha() {
		popupPassword.onModuleLoad();
		
		popupPassword.addSubmitListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Validando email de usuário");
				if (popupPassword.isValid()) {
					logger.info("Buscando email do cadastro");
					validateEmail(popupPassword.getEmail());
				}
			}
		});
		
		popupPassword.addSubmitcemail(new EventListener() {
			public void onBrowserEvent(Event event) {
				if (event.getKeyCode() == KeyCodes.KEY_ENTER){
					event.preventDefault();
					logger.info("Validando email de usuário");
					if (popupPassword.isValid()) {
						logger.info("Buscando email do cadastro");
						validateEmail(popupPassword.getEmail());
					}
				}
			}
		});
		
		popupPassword.addStopPropagation(new EventListener() {
			public void onBrowserEvent(Event event) {
				event.stopPropagation();
			}
		});
		
		popupPassword.addCancelListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				popupPassword.close();
			}
		});
	}

	private void addLoggedInWidget(Element elem) {
		// elem.addClassName("deslogado");
		logger.info("Adicionando widget de usuário logado");
		elem.appendChild(logon.getElement());
		logon.addLogoutListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Logout");
				logout();
			}
		});
	}

	private void addDefaultUserWidget() {
		logger.info("Adicionando widget de usuário padrão");
		
		defaultUserPanel.add(defaultUser);
		
		defaultUser.addSubmitListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Validando cadastro de usuário padrão");
				if (defaultUser.isValid()) {
					logger.info("Buscando dados do cadastro de usuário padrão");
					validateUser(defaultUser.getUser());
				}
			}
		});
		
		defaultUser.addSubmitform(new EventListener() {
			public void onBrowserEvent(Event event) {
				if (event.getKeyCode() == KeyCodes.KEY_ENTER){
					logger.info("Validando cadastro de usuário padrão");
					if (defaultUser.isValid()) {
						logger.info("Buscando dados do cadastro de usuário padrão");
						validateUser(defaultUser.getUser());
					}
				}
			}
		});
		
		defaultUser.addCancelListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Fechando janela de cadastro de usuário padrão");
				defaultUser.close();
			}
		});
	}
	
	@SuppressWarnings("unused")
	private void addSocialNetUserWidget() {
		logger.info("Adicionando widget de usuário de rede social");
		
		socialNetPanel.add(socialNetUser);
		
		socialNetUser.enterFacebookUser(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Buscando AppId do facebook");
				getAppID();
				setLoading(true);
			}
		});
		
		socialNetUser.addCancelListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Fechando janela de cadastro de usuário de rede social");
				socialNetUser.close();
			}
		});

		socialNetUser.addSubmitListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Validando cadastro de usuário de rede social");
				if (socialNetUser.isValid()) {
					validateNetUser();
				}
			}
		});
	}
	
	private void addOrganizationUserWidget() {
		logger.info("Adicionando widget de usuário de representante da OSC");
		
		organizationUserPanel.add(organizationUserWidget);

		organizationUserWidget.addFocusListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				organizationUserWidget.setValue("");
			}
		});
		
		organizationUserWidget.addSearchChangeListener(new EventListener() {
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
							String criteria = organizationUserWidget.getValue();
							search(criteria);
						}
					}
				};
				t.schedule(DELAY);
			}
		});
		
//		organizationUserWidget.addSearchClickListener(new EventListener() {
//			public void onBrowserEvent(Event event) {
//				logger.info("entityUser.addSearchClickListener");
//				String criteria = organizationUserWidget.getValue();
//				AsyncCallback<List<SearchResult>> callbackSearch = new AsyncCallback<List<SearchResult>>() {
//					public void onFailure(Throwable caught) {
//						logger.log(Level.SEVERE, caught.getMessage());
//					}
//					public void onSuccess(List<SearchResult> result) {
//						if (!result.isEmpty()) {
//							organizationUserWidget.showOrganization(result.get(0).getValue(), String.valueOf(result.get(0).getId()));
//						}
//					}
//				};
//				if (!criteria.trim().isEmpty()){
//					searchService.search(criteria, LIMIT, callbackSearch);
//				}
//			}
//		});
		
		organizationUserWidget.addCancelListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Fechando tela de cadastro de representante da OSC");
				organizationUserWidget = new RepresentantFormWidget();
				organizationUserWidget.close();
			}
		});
		
		organizationUserWidget.addSubmitListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Validando tela de cadastro de representante da OSC");
				if (organizationUserWidget.isValid()) {
					validateRepresentant(organizationUserWidget.getUser());
				}
			}
		});
	}
	
	private void addUser(final DefaultUser user) {
		logger.info("Adicionando usuário");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Void result) {
				logger.info("Fechando tela de cadastro");
				defaultUser.close();
				
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
	
	private void addUser(final FacebookUser user) {
		logger.info("Adicionando usuário do facebook");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Void result) {
				logger.info("Fechando tela de cadastro");
				socialNetUser.close();
				logger.info("Realizando logon");
				logonFacebokUser(user);
				logger.info("Redirecionando para tela principal");
				String url = GWT.getHostPageBaseURL() + "Map.html";
				Window.Location.replace(url);
			}
		};
		netUserService.addUser(user, callback);
	}
	
	/**
	 * Adiciona o email e o código da OSC para vincular o usuário à mesma
	 */
	private void addRepresentantUser(final RepresentantUser user) {
		logger.info("Adicionando representante da OSC");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(Void result) {
				logger.info("Fechando tela de cadastro");
				organizationUserWidget.close();
				
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
		userService.addRepresentantUser(user, callback);
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
				}
				else{
					popupPassword.addConfirme();
				}
			}
		};
		userService.getStatus(email, callback);
	}

	private void validateUser(final DefaultUser user) {
		logger.info("Validando usuário padrão");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(DefaultUser result) {
				logger.info("Usuário encontrado");
				if (result != null) {
					defaultUser.addInvalidEmail(user.getEmail());
				}
				validateCpf(user);
			}
		};
		userService.getUser(user.getEmail(), callback);
	}

	/**
	 * Realiza a validação dos dados fornecidos pelo representante da entidade a
	 * ser cadastrado E verificar se o usuário já está cadastrado na base de
	 * dados do mapa
	 */
	private void validateRepresentant(final RepresentantUser user) {
		logger.info("Validando dados do representante da OSC");
		AsyncCallback<RepresentantUser> callback = new AsyncCallback<RepresentantUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(RepresentantUser result) {
				if(result != null){
					organizationUserWidget.addInvalidEmail(user.getEmail());
				}
				if(organizationUserWidget.isValid()){
					validateCpfRepresentant(user);
				}
			}
		};
		userService.getRepresentantUser(user.getEmail(), callback);
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
					defaultUser.addInvalidCpf(String.valueOf(user.getCpf()));
				}
				logger.info("Validando usuário padrão");
				if (defaultUser.isValid()){
					addUser(user);
				}
			}
		};
		userService.getUser(user.getCpf(), callback);
	}
	
	private void validateCpfRepresentant(final RepresentantUser user) {
		logger.info("Validando CPF");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(DefaultUser result) {
				logger.info("Usuário encontrado pelo CPF");
				if (result != null) {
					organizationUserWidget.addInvalidCpf(String.valueOf(user.getCpf()));
				}
				logger.info("Validando representante");
				if (organizationUserWidget.isValid()){
					addRepresentantUser(user);
				}
			}
		};
		userService.getUser(user.getCpf(), callback);
	}
	
	/**
	 * Método que verifica se o usuário já está cadastrado no mapa ao clicar no
	 * botão do Facebook, após login já efetuado
	 */
	public void verifyFacebookUser(final FacebookUser user) {
		logger.info("Verificando se usuário do facebook já está cadastrado");
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(Boolean result) {
				setLoading(false);
				if(!result){
					logger.info("Usuário do facebook não está cadastrado");
					socialNetUser.showSocialUser(user);
				}
				else{
					logger.info("Usuário do facebook já está cadastrado");
					socialNetUser.addUserAlreadyExists(user.getEmail().toString());
					socialNetUser.showSocialUser(user);
				}
			}
		};
		netUserService.hasUser(user, callback);
	}

	private void validateNetUser() {
		logger.info("Validando usuário do facebook");
		FacebookUser user = socialNetUser.getUser();
		validateNetUser(user);
	}

	/**
	 * Realiza a validação dos dados fornecidos pelo usuário a ser cadastrado E
	 * verificar se o usuário já está cadastrado na base de dados do mapa
	 */
	private void validateNetUser(final FacebookUser user) {
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Boolean result) {
				if(result){
					socialNetUser.closeWidget();
				}
				else if(user.isDefaultUser()){
					validateNetUser((DefaultUser) user);
				}
				else{
					addUser(user);
				}
			}
		};
		netUserService.hasUser(user, callback);
	}

	private void validateNetUser(final DefaultUser user) {
		logger.info("Validando usuário padrão");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(DefaultUser result) {
				validateNetCpf(user);
			}
		};
		userService.getUser(user.getEmail(), callback);
	}

	private void validateNetCpf(final DefaultUser user) {
		logger.info("Validando CPF do usuário padrão");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(DefaultUser result) {
				if (result != null) {
					socialNetUser.addInvalidCpf(String.valueOf(user.getCpf()));
				}
				if (socialNetUser.isValid()){
					addUser(user);
				}
			}
		};
		userService.getUser(user.getCpf(), callback);
	}
	
	private void statusUser(final String email){
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(Boolean result) {
				if(result == null || result == true){
					validateLogin(logon.getEmail(), logon.getPassword());
				}
				else{
					Element html = DOM.createLabel();
					html.addClassName("error");
					html.setInnerText("Confirme seu Cadastro!");
					Element div = DOM.getElementById("floatingLoginErrorList");
					div.getParentElement().addClassName("formError");
					div.appendChild(html);
				}
			}
		};
		userService.getStatus(email, callback);
	}

	private void validateLogin(final String email, final String passwd) {
		logger.info("Validando email do usuário e senha");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(DefaultUser result) {
				if (result == null) {
					logon.addInvalidEmail(email);
				}
				else{
					String encriptedPasswd = encrypt(passwd);
					if (!encriptedPasswd.equals(result.getPassword())){
						logon.addInvalidPassword(passwd);
					}
				}
				if (logon.isValid()) {
					logon(result);
				}
			}
		};
		userService.getUser(email, callback);
	}
	
	public void validateLoginToken(final String email, final String passwd) {
		logger.info("Validando Token");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(DefaultUser result) {
				if(result == null){
					logon.addInvalidEmail(email);
				}
				else{
					if(!passwd.equals(result.getPassword())){
						logon.addInvalidPassword(passwd);
					}
				}
				logon(result);
				
				popupPassword.onModuleLoad();
				Element pop = DOM.getElementById("popup");
				pop.removeAllChildren();
				Element header = DOM.createElement("h2");
				header.setInnerText("Cadastre-se no Mapa");
				Element div = DOM.createDiv();
				Element p = DOM.createElement("p");
				p.setInnerText("Cadastro confirmado com sucesso!");
				Element a = DOM.createAnchor();
				a.setInnerText("Ok");
				a.setAttribute("href", "#");
				Event.sinkEvents(a, Event.ONCLICK);
				Event.setEventListener(a, new EventListener() {
					public void onBrowserEvent(Event event) {
						popupPassword.close();
						Window.Location.replace(GWT.getHostPageBaseURL() + "Map.html");
					}
				});
				div.appendChild(p);
				div.appendChild(a);
				pop.appendChild(header);
				pop.appendChild(div);
			}
		};
		userService.getUser(email, callback);
	}

	private void logon(final DefaultUser user) {
		logger.info("Realizando logon do usuário padrão");
		logon.getElement().removeFromParent();
		logon = new LogonWidget(user);
		Element logonDiv = Document.get().getElementById(LOGON_CONTAINER);
		if(logonDiv != null){
			addLoggedInWidget(logonDiv);
		}
		currentUser = user;
		
		if(user.getType() == UserType.FACEBOOK){
			FacebookUser userF = new FacebookUser();
			userF = (FacebookUser) user;
			Element userImage = DOM.getElementById("netUserImage");
			userImage.setAttribute("src", userF.getSmallPictureUrl());
		}
		Cookies.setCookie("oscUid", user.getEmail());
		Cookies.setCookie("idUser", String.valueOf(user.getId()));
	}

	private void logonFacebokUser(final FacebookUser user) {
		logger.info("Realizando logon do usuário do facebook");
		logon.getElement().removeFromParent();
		logon = new LogonWidget(user);

		Element logonDiv = Document.get().getElementById(LOGON_CONTAINER);

		if(logonDiv != null){
			addLoggedInWidget(logonDiv);
		}
		currentUser = user;
		Element userImage = DOM.getElementById("netUserImage");
		userImage.setAttribute("src", user.getSmallPictureUrl());

		Cookies.setCookie("oscSnUid", user.getEmail());
	}

	public static boolean hasLoggedUser() {
		logger.info("Verificando se existe usuário logado");
		String email = Cookies.getCookie("oscUid");
		if(email != null && !email.isEmpty()){
			return true;
		}
		String snUid = Cookies.getCookie("oscSnUid");
		if(snUid != null && !snUid.isEmpty()){
			return true;
		}
		return false;
	}

	public static DefaultUser getCurrentUser() {
		return currentUser;
	}

	public static boolean isMasterUser() {
		logger.info("Verificando se é usuário master");
		if(currentUser == null){
			return false;
		}
		else{
			return currentUser.getType().equals(UserType.MASTER);
		}
	}

	private void logout() {
		logger.info("Realizando logout");
		logon.getElement().removeFromParent();
		logon = new LogonWidget();
		Element logonDiv = Document.get().getElementById(LOGON_CONTAINER);

		if(logonDiv != null){
			addLogonWidget(logonDiv);
		}
		
		Cookies.removeCookie("idUser");
		Cookies.removeCookie("oscUid");
		Cookies.removeCookie("oscSnUid");
		currentUser = null;
		Window.Location.replace(GWT.getHostPageBaseURL() + "Map.html");
	}

	private void logonDefaultUser(final String email) {
		logger.info("Logon do usuário padrão");
		AsyncCallback<DefaultUser> callback = new AsyncCallback<DefaultUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(DefaultUser result) {
				if(result != null){
					logon(result);
				}
			}
		};
		userService.getUser(email, callback);
	}

	/**
	 * Passando o "code" como parâmentro, obtém as informações do usuário do
	 * Facebook para colocar no logon
	 */
	private void logonFacebookUser(final String email) {
		logger.info("Logon do usuário do facebook");
		String urlUser = GWT.getHostPageBaseURL() + "User.html";
		if(urlUser.contains("127.0.0.1")){
			urlUser = urlUser.replaceAll("127.0.0.1", "localhost");
		}

		AsyncCallback<FacebookUser> callback = new AsyncCallback<FacebookUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(FacebookUser user) {
				if(user != null){
					logger.info("chamou o getFacebookuser... para chamar o logon");
					logonFacebokUser(user);
				}
			}
		};
		socialNetService.getFacebookuser(email, callback);
	}

	/**
	 * Redireciona para a página de login do Facebook
	 */
	public void facebookRedirect(String appId) {
		logger.info("Redirecionando para a página de login do facebook");
		String urlUser = GWT.getHostPageBaseURL() + "User.html";
		if(urlUser.contains("127.0.0.1")){
			urlUser = urlUser.replaceAll("127.0.0.1", "localhost");
		}
		String linkOAuth = "http://www.facebook.com/dialog/oauth/?client_id="
				+ appId + "&redirect_uri=" + urlUser
				+ "&scope=user_about_me,user_birthday,email,publish_stream";

		Window.Location.replace(linkOAuth);
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

	public void getSocialUserInfo(String facebookUserCode) {
		logger.info("Buscando dados do usuário no facebook");
		setLoading(true);
		String urlUser = GWT.getHostPageBaseURL() + "User.html";
		if(urlUser.contains("127.0.0.1")){
			urlUser = urlUser.replaceAll("127.0.0.1", "localhost");
		}
		AsyncCallback<FacebookUser> callback = new AsyncCallback<FacebookUser>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(FacebookUser user) {
				verifyFacebookUser(user);
			}
		};
		socialNetService.getAppAccessTokenAndUserInfo(facebookUserCode,
				urlUser, callback);
	}

	public void getAppID() {
		logger.info("Buscando AppID no servidor");
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}

			public void onSuccess(String appId) {
				if(appId != null){
					facebookRedirect(appId);
				}
			}
		};
		netUserService.getFacebookAppId(callback);
	}

	/**
	 * Método para mostrr gif "loading" enquanto as informações de usuário são
	 * carregadas
	 */
	private void setLoading(boolean isLoading) {
		logger.info("Loanding bar");
		if(loadingUserPanel != null){
			addLoadingBar();
		}
		if(isLoading == true){
			loadingUserPanel.setVisible(true);
		}
		else{
			loadingUserPanel.setVisible(false);
		}
	}

	private void addLoadingBar() {
		String url = "imagens/loading_big.gif";
		String htmlBuilder = "<img src='" + url + "'>";
		HTML html = new HTML(htmlBuilder.toString());
		loadingUserPanel.add(html);
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
				organizationUserWidget.showOrganization(elem.getInnerText(), oscId);
			}
		};
		organizationUserWidget.addResultItems(items, listener);
	}
}