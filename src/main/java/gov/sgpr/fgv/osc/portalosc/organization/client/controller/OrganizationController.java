package gov.sgpr.fgv.osc.portalosc.organization.client.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.gwtbootstrap3.extras.datepicker.client.ui.DatePicker;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.constants.DatePickerLanguage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwt.crypto.bouncycastle.DataLengthException;
import com.googlecode.gwt.crypto.bouncycastle.InvalidCipherTextException;
import com.googlecode.gwt.crypto.client.TripleDesCipher;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Event;

import gov.sgpr.fgv.osc.portalosc.organization.client.components.FormularioWidget;
import gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationService;
import gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationServiceAsync;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.OrganizationModel;
import gov.sgpr.fgv.osc.portalosc.user.client.controller.UserController;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserServiceAsync;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;

public class OrganizationController {
	private static Logger logger = Logger.getLogger(OrganizationController.class.getName());
	private OrganizationServiceAsync organizationService = com.google.gwt.core.shared.GWT.create(OrganizationService.class);
	private UserServiceAsync userService = com.google.gwt.core.shared.GWT.create(UserService.class);
	private FormularioWidget formularioWidget = null;
	private OrganizationModel organization = new OrganizationModel();
	private final RootPanel formularioElement = RootPanel.get("modal_formulario");
	private static byte[] desKey;
	private Integer idOSC;
	
	public void init() {
		logger.info("Iniciando módulo de configuração");
		
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
		
		logger.info("Buscando Chave de criptografia");
		organizationService.getEncryptKey(callback);
		
		idOSC = Integer.parseInt(History.getToken().substring(1));
		getOrganization(idOSC);
	}
	
	private void getOrganization(Integer id){
		logger.info("Buscando dados da organização");
		AsyncCallback<OrganizationModel> callback = new AsyncCallback<OrganizationModel>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
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
		logger.info("Salvando dados da organização");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(Void result) {
				logger.info("Dados salvos");
				Window.Location.reload();
			}
		};
		organizationService.setOrganization(organizationModel, callback);
	}
	
	private void removeDir(Integer id){
		logger.info("Removendo Diretor");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(Void result) {
				logger.info("Diretor Removido");
			}
		};
		organizationService.removeDiretor(id, callback);
	}
	
	private void checkUser(){
		logger.info("Verfificando se usuário é representante da OSC");
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(Boolean result) {
				logger.info("Organização encontrado");
				if(result){
					addFormularioWidget(true);
				}else{
					addFormularioWidget(false);
				}
				searchUserReccomendInit();
			}
		};
		String userId = Cookies.getCookie("idUser");
		if (userId != null && !userId.isEmpty()) {
			organizationService.searchOSCbyUser(Integer.parseInt(userId), organization.getId(), callback);
		}else{
			addFormularioWidget(false);
		}
	}
	
	private void searchUserReccomendInit(){
		String idUser = Cookies.getCookie("idUser");
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(Boolean result) {
				Element btnRecomendar = DOM.getElementById("recomendar");
				if(result){
					btnRecomendar.setInnerText("Recomendar (desfazer)");
				}else{
					btnRecomendar.setInnerText("Recomendar");
				}
			}
		};		
		if(idUser != null) {
			userService.searchUserReccomend(idOSC, Integer.parseInt(idUser), callback);
		}
	}
	
	private void insertReccomendation(Integer idOSC, Integer idUser){
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(Void result) {
				logger.info("OSC recomendada");
			}
		};
		userService.insertRecommendation(idOSC, idUser, callback);
	}
	
	private void deleteReccomendation(Integer idOSC, Integer idUser){
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(Void result) {
				logger.info("Recomendação excluída");
			}
		};
		userService.deleteRecommendation(idOSC, idUser, callback);
	}
	
	private void addFormularioWidget(Boolean editable) {
		logger.info("Adicionando widget do formulário");
		formularioWidget = new FormularioWidget(organization, editable);
		formularioElement.add(formularioWidget);
		formularioWidget.addDate(organization,"data_inicio","projeto_data_inicio","Inicio",editable);
		formularioWidget.addDate(organization,"data_final","projeto_data_final","Final", editable);
		
		formularioWidget.addRecomendar(new EventListener() {
			public void onBrowserEvent(Event event) {
				String idUser = Cookies.getCookie("idUser");
				if(idUser != null) {
					if (UserController.getCurrentUser().getType() == UserType.DEFAULT || UserController.getCurrentUser().getType() == UserType.OSC_AGENT){
						Element likeCounter = DOM.getElementById("like_counter");
						Element btnRecomendar = DOM.getElementById("recomendar");
						Integer count = Integer.valueOf(likeCounter.getInnerText());
						if(btnRecomendar.getInnerText() == "Recomendar"){
							logger.info("Adicionando recomendação");
							count++;
							likeCounter.setInnerText(count.toString());
							likeCounter.setTitle(count.toString() + (count == 1 ? " recomendação" : " recomendações"));
							btnRecomendar.setInnerText("Recomendar (desfazer)");
							insertReccomendation(idOSC, Integer.parseInt(idUser));
						}else{
							logger.info("Removendo recomendação");
							count--;
							likeCounter.setInnerText(count.toString());
							likeCounter.setTitle(count.toString() + (count == 1 ? " recomendação" : " recomendações"));
							btnRecomendar.setInnerText("Recomendar");
							deleteReccomendation(idOSC, Integer.parseInt(idUser));
						}
					}else{
						openPopup("Recomendar Organização", "Para recomendar uma Organização é necessário ter o CPF cadastrado no sistema. Para cadastrar seu CPF entre em Configurações.");
					}
				}else{
					openPopup("Recomendar Organização", "Para recomendar uma Organização é necessário realizar o Login.");
				}
			}
		});
		
		formularioWidget.addSalvarListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Salvando os Dados");
				setOrganization(formularioWidget.getOrg());
			}
		});
		
		formularioWidget.addDiretor(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Adicionando Diretor");
				formularioWidget.dir++;
				Element indDir = DOM.getElementById("indDir");
				if(indDir != null){
					Element div = indDir.getParentElement();
					div.removeAllChildren();
				}
				Element diretores = DOM.getElementById("diretores");
				diretores.appendChild(getHtmlDir(formularioWidget.dir).getElement());
				Element removeDir = DOM.getElementById("removedir"+ formularioWidget.dir);
				Event.sinkEvents(removeDir, Event.ONCLICK);
				Event.setEventListener(removeDir, new EventListener() {
					public void onBrowserEvent(Event event) {
						Element ele = event.getCurrentTarget().getParentElement();
						Element div = ele.getParentElement();
						div.removeAllChildren();
					}
				});
			}
		});
		
		formularioWidget.addProjetos(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Adicionando Projeto");
				
				formularioWidget.proj++;
				Integer count = formularioWidget.conv + formularioWidget.proj;
				
				Element indProj = DOM.getElementById("indProj");
				if(indProj != null){
					Element div = indProj.getParentElement();
					div.removeAllChildren();
				}
				
				Element projetos = DOM.getElementById("addFormProj");
				projetos.appendChild(getHtmlProj(formularioWidget.proj,count).getElement());
				
				addDate("data_inicio", "projeto_data_inicio");
				addDate("data_final", "projeto_data_final");
				
				final Element valorTotal = DOM.getElementById("projeto_valor_total"+ formularioWidget.proj);
				Event.sinkEvents(valorTotal, Event.ONBLUR);
				Event.setEventListener(valorTotal, new EventListener() {
					public void onBrowserEvent(Event event) {
						String valorTotalvalue = InputElement.as(valorTotal).getValue();
						if(valorTotalvalue != null && valorTotalvalue != ""){
							String[] valorSplit = valorTotalvalue.split(",",0);
							String valorReplace = valorSplit[0].replaceAll("[.,]", "");
							Number valor = formularioWidget.convertStringToNumber(valorReplace);
							String valorConv = formularioWidget.convertNumberToCurrencyString(valor);
							InputElement input = InputElement.as(DOM.getElementById("projeto_valor_total"+ formularioWidget.proj));
							input.setValue(valorConv);
						}
					}
				});
			}
		});
		
		formularioWidget.removeDir(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Removendo Diretor");
				Element ele = event.getCurrentTarget().getParentElement();
				Element div = ele.getParentElement();
				div.removeAllChildren();
				Integer id = Integer.parseInt(event.getCurrentTarget().getAttribute("value"));
				removeDir(id);
			}
		});
		
//		formularioWidget.addSocial(new EventListener() {
//		public void onBrowserEvent(Event event) {
//			Element social = DOM.getElementById("social");
//			social.appendChild(getHtmlSocial().getElement());
//		}
//	});
	
//	formularioWidget.addFonteRec(new EventListener() {
//		public void onBrowserEvent(Event event) {
//			Element social = DOM.getElementById("fonte");
//			social.appendChild(getHtmlRec().getElement());
//		}
//	});
	}
	
	private void openPopup(String title, String message){
		final PopupPanel popup = new PopupPanel();
		popup.setStyleName("overlay");
		popup.add(getHtmlPopup(title,message));
		popup.show();
		
		Element ok = DOM.getElementById("ok");
		Event.sinkEvents(ok, Event.ONCLICK);
		Event.setEventListener(ok, new EventListener() {
			public void onBrowserEvent(Event event) {
				popup.hide();
			}
		});
	}
	
	private void addDate(String ele, String idElement){
		logger.info("Adicionando Input Date");
		Element element = DOM.getElementById(ele + formularioWidget.proj);
		PopupPanel p = new PopupPanel();
		DatePicker picker = new DatePicker();
		picker.setSize("120px", "");
		picker.setLanguage(DatePickerLanguage.PT_BR);
		picker.setFormat("dd/mm/yyyy");
		picker.setId(idElement + formularioWidget.proj);
		p.add(picker);
		p.show();
		element.appendChild(picker.getElement());
//		Element date = DOM.getElementById(idElement + formularioWidget.proj);
//		date.setAttribute("required", "required");
	}
	
	private static HTML getHtmlPopup(String titulo, String msg) {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<div  id='popup' class='pop_up_alert clearfix'>");
		htmlBuilder.append("<h2>"+ titulo +"</h2>");
		htmlBuilder.append("<div>");
		htmlBuilder.append("<p>"+ msg +"</p>");
		htmlBuilder.append("<form id='form_esqueci_senha' method='post'>");
		htmlBuilder.append("<div class='botoes'>");
		htmlBuilder.append("<input type='button' name='ok' id='ok'  value='Ok' style='margin-left: 180px;' /></div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</form>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
	
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}
	
	private HTML getHtmlProj(Integer proj, Integer count) {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<div class='clearfix projeto collapsable'>");
		htmlBuilder.append("<div class='projeto_header'>");
//		htmlBuilder.append("<div id='recolherProj' class='collapse-click collapse-button' data-toggle='collapse' data-target='#recProj"+ proj +"' ><div></div></div>");
		htmlBuilder.append("<div class='nome'>");	
		htmlBuilder.append("<span class='no_margin_right'>"+ count +" |</span>");
		htmlBuilder.append("<span class='no_margin titulo_projeto'><input type='text' name='addProj' id='projeto_nome_projeto"+ proj +"' placeholder='Título do Projeto' /></span>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<span class='clear_both'></span>");
		htmlBuilder.append("</div>");
		
		htmlBuilder.append("<div class='projeto_body collapse-hidden'>");
//		htmlBuilder.append("<div id='recProj"+ proj +"' class='collapse'>");
		htmlBuilder.append("<div class='linha clearfix'>");
		htmlBuilder.append("<div class='col1_6'>");
		htmlBuilder.append("<strong class='separador left-radius'>Status</strong>");
		htmlBuilder.append("<p id='projeto_status"+ proj +"' >");
		htmlBuilder.append("<select>");
		htmlBuilder.append("<option value=''></option>");
		htmlBuilder.append("<option value='Planejado'> Planejado </option>");
		htmlBuilder.append("<option value='Em Execução' > Em Execução </option>");
		htmlBuilder.append("<option value='Finalizado'> Finalizado </option>");
		htmlBuilder.append("</select>");
		htmlBuilder.append("</p>");
		htmlBuilder.append("</div>");
				
		htmlBuilder.append("<div class='col1_6'>");
		htmlBuilder.append("<strong class='separador'>Data Início</strong>");
		htmlBuilder.append("<p id='data_inicio"+ proj +"' ></p>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div class='col1_6'>");
		htmlBuilder.append("<strong class='separador'>Data Final</strong>");
		htmlBuilder.append("<p id='data_final"+ proj +"' ></p>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div class='col1_6'>");
		htmlBuilder.append("<strong class='separador'>Valor Total (R$)</strong>");
		htmlBuilder.append("<p><input type='text' name='projeto_valor_total' id='projeto_valor_total"+proj+"'/></p>");
		htmlBuilder.append("</div>");
				
		htmlBuilder.append("<div class='col1_6'>");
		htmlBuilder.append("<strong class='separador'>Fonte de recurso</strong>");
		htmlBuilder.append("<p id='projeto_fonte"+ proj +"' ><select>");
		htmlBuilder.append("<option value='' selected='selected'></option>");
		htmlBuilder.append("<option value='Público' > Público </option>");
		htmlBuilder.append("<option value='Privado' > Privado </option>");
		htmlBuilder.append("</select>");
		htmlBuilder.append("</p>");
		htmlBuilder.append("</div>");
		
		htmlBuilder.append("<div class='col1_6'>");
		htmlBuilder.append("<strong class='right-radius'>Link</strong>");
		htmlBuilder.append("<p><input id='link" + proj + "' name='link" + proj + "' placeholder='Digite a página do projeto' /></p>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
		
		htmlBuilder.append("<div class='clearfix linha'>");
		htmlBuilder.append("<div class='col1_3'>");
		htmlBuilder.append("<strong class='separador left-radius'>Público Alvo do Projeto</strong>");
		htmlBuilder.append("<textarea name='projeto_publico_alvo' id='projeto_publico_alvo"+ proj +"'  placeholder='Público Alvo do Projeto....'></textarea>");
		htmlBuilder.append("</div>");

//		htmlBuilder.append("<div class='col1_6'>");
//		htmlBuilder.append("<strong class='separador'>Abrangência</strong>");
//		htmlBuilder.append("<p>");
//		htmlBuilder.append("<select>");
//		htmlBuilder.append("<option value='nacional'> Nacional </option>");
//		htmlBuilder.append("<option value='regional'> Regional </option>");
//		htmlBuilder.append("<option value='estadual'> Estadual </option>");
//		htmlBuilder.append("<option value='municipal'> Municipal </option>");
//		htmlBuilder.append("</select>");
//		htmlBuilder.append("</p>");
//		htmlBuilder.append("</div>");
		
//		htmlBuilder.append("<div class='col1_2'>");
//		htmlBuilder.append("<strong class='right-radius'>Localização do Projeto</strong>");
//		htmlBuilder.append("<div class='localizacao_projet'>");
//		htmlBuilder.append("<ul class='locais'>");
//		htmlBuilder.append("<li><a href='mapa.html#5' class='excluir tooltip' title='Excluir'>Excluir</a><a href='mapa.html#5'>Nordeste</a></li>");
//		htmlBuilder.append("</ul>");
//		htmlBuilder.append("<div><label for='campobusca' class='esconder' style='background: none;'>Buscar localização</label>	<input title='Campo de Busca para a Localização do Projeto' type='text' name='campobusca' id='campobusca' placeholder='Informe a localização desejada...'>	<button type='button' name='adicionar' id='adicionar' class='adicionar'>Adicionar</button>	<button type='button' name='ajuda' class='ajuda' id='ajuda'>?</button></div>");
//		htmlBuilder.append("</div>");
//		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
		
		htmlBuilder.append("<div class='clearfix linha'>");
		htmlBuilder.append("<div class='col1_3'>");
		htmlBuilder.append("<strong class='separador left-radius'>Financiadores do Projeto</strong>");
		htmlBuilder.append("<textarea name='financiadores' id='financiadores"+ proj +"'  placeholder='Financiadores do Projeto....'></textarea>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div class='col2_3'>");
		htmlBuilder.append("<strong class='right-radius'>Descrição do Projeto</strong>");
		htmlBuilder.append("<textarea name='descprojeto' id='descprojeto"+ proj +"'  placeholder='Descrição do Projeto...'></textarea>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");						
		htmlBuilder.append("</div>");
		htmlBuilder.append("<hr />");
		htmlBuilder.append("</div>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}
	
	private HTML getHtmlDir(Integer dir) {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<div id='incluirDir" + dir + " '>");
		htmlBuilder.append("<input type='text' name='addDir' id='cargo" + dir + "' placeholder='Diretor...'>");
		htmlBuilder.append("<input type='text' name='addDir' id='nome" + dir + "' placeholder='Nome do Diretor' />");
		htmlBuilder.append("<div class='botoes'><button id='removedir"+ dir +"' type='button' class='excluir participacao'>Excluir</button></div>");
		htmlBuilder.append("</div>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}
	
//	private HTML getHtmlRec() {
//	StringBuilder htmlBuilder = new StringBuilder();
//	htmlBuilder.append("<li id='participacao' >");
//	htmlBuilder.append("<select name='erecursos'>");
//	htmlBuilder.append("<option value='' selected></option>");
//	htmlBuilder.append("<option value='privada'>Privada</option>");
//	htmlBuilder.append("<option value='doacao'>Doação</option>");
//	htmlBuilder.append("<option value='publica'>Pública</option>");
//	htmlBuilder.append("</select>");
//	htmlBuilder.append("<div class='botoes'><button type='button' class='excluir participacao'>Excluir</button></div>");
//	htmlBuilder.append("<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
//	htmlBuilder.append("</li>");
//	HTML html = new HTML(htmlBuilder.toString());
//	return html;
//}
	
//	private HTML getHtmlSocial() {
//		StringBuilder htmlBuilder = new StringBuilder();
//		htmlBuilder.append("<li>");
//		htmlBuilder.append("<select name='oconselhos'>");
//		htmlBuilder.append("<option value='' selected></option>");
//		htmlBuilder.append("<option value='privada'>CONJUVE</option>");
//		htmlBuilder.append("<option value='doacao'>CONAJE</option>");
//		htmlBuilder.append("<option value='publica'>CONSELHO</option>");
//		htmlBuilder.append("</select>");
//		htmlBuilder.append("<div class='botoes'><button type='button' class='excluir participacao'>Excluir</button></div>");
//		htmlBuilder.append("<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
//		htmlBuilder.append("</li>");
//		HTML html = new HTML(htmlBuilder.toString());
//		return html;
//	}
	
//	private void addLoggedInWidget(ConfigurationModel user) {
//		logger.info("Adicionando widget de usuário logado");
//		
//		Element element = Document.get().getElementById("topo_acesso");
//		
//		LogonWidget logonWidget = new LogonWidget(user);
//		element.appendChild(logonWidget.getElement());
//		logonWidget.addLogoutListener(new EventListener(){
//			public void onBrowserEvent(Event event) {
//				logger.info("Logout");
//				logout();
//			}
//		});
//	}
//	
//	private void logout() {
//		logger.info("Realizando logout");
//		Cookies.removeCookie("oscUid");
//		Cookies.removeCookie("oscSnUid");
//		Window.Location.replace(GWT.getHostPageBaseURL() + "Map.html");
//	}
	
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
