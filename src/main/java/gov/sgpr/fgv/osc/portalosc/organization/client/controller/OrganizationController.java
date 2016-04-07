package gov.sgpr.fgv.osc.portalosc.organization.client.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
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
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SearchService;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SearchServiceAsync;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserServiceAsync;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.SearchResult;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;

public class OrganizationController {
	private static Logger logger = Logger.getLogger(OrganizationController.class.getName());
	private OrganizationServiceAsync organizationService = com.google.gwt.core.shared.GWT.create(OrganizationService.class);
	private UserServiceAsync userService = com.google.gwt.core.shared.GWT.create(UserService.class);
	private FormularioWidget formularioWidget = null;
	private OrganizationModel organization = new OrganizationModel();
	private final RootPanel formularioElement = RootPanel.get("modal_formulario");
	private static byte[] desKey;
	private Date changeDate;
	private static final int DELAY = 500;
	private static int LIMIT = 5;
	private SearchServiceAsync searchService = GWT.create(SearchService.class);
	private List<Integer> excluirDir = new ArrayList<Integer>();
	private List<Integer> excluirLocalProj = new ArrayList<Integer>();
	private List<Integer> excluirLocalConv = new ArrayList<Integer>();
	private String oscId = "";
	private String href = "";
	private String oscInfo = "";
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
				openPopup("Página OSC", "Dados Salvos com sucesso!");
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
	
	private void removeLocalProj(Integer id){
		logger.info("Removendo Localização");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(Void result) {
				logger.info("Localização Removida");
			}
		};
		organizationService.removeLocalProj(id, callback);
	}
	
	private void removeLocalConv(Integer id){
		logger.info("Removendo Localização");
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
				Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
			}
			public void onSuccess(Void result) {
				logger.info("Localização Removida");
			}
		};
		organizationService.removeLocalConv(id, callback);
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
		
		formularioWidget.addFocusListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				String ele = event.getTarget().getId();
				formularioWidget.setValue(ele, "");
			}
		});
		
		formularioWidget.addSearchChangeListener(new EventListener() {
			public void onBrowserEvent(final Event event) {
				searchLocal(event);
			}
		});
		
		formularioWidget.addSalvarListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				logger.info("Salvando os Dados");
				for (int i = 0; i<excluirDir.size(); i++)
					removeDir(excluirDir.get(i));
				for (int i = 0; i<excluirLocalProj.size(); i++)
					removeLocalProj(excluirLocalProj.get(i));
				for (int i = 0; i<excluirLocalConv.size(); i++)
					removeLocalConv(excluirLocalConv.get(i));
				setOrganization(formularioWidget.getOrg());
			}
		});
		
		formularioWidget.addCancelListener(new EventListener() {
			public void onBrowserEvent(Event event) {
				Window.Location.reload();
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
							convertNumber(valorTotalvalue, "projeto_valor_total"+ formularioWidget.proj);
						}
					}
				});
				
				final Element elem = DOM.getElementById("enome"+count);
				Event.sinkEvents(elem, Event.ONKEYDOWN);
				Event.setEventListener(elem, new EventListener() {
					public void onBrowserEvent(final Event event) {
						searchLocal(event);
					}
				});
				
				Element addLocalizacao = DOM.getElementById("adicionar"+formularioWidget.proj);
				Event.sinkEvents(addLocalizacao, Event.ONCLICK);
				Event.setEventListener(addLocalizacao, new EventListener() {
					public void onBrowserEvent(final Event event) {
						localProj(event);
					}
				});
				
				Element recomendar = DOM.getElementById("ajuda"+count);
				Event.sinkEvents(recomendar, Event.ONCLICK);
				Event.setEventListener(recomendar, new EventListener() {
					public void onBrowserEvent(final Event event) {
						popupAjuda(event);
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
				excluirDir.add(id);
			}
		});
		
		formularioWidget.removeLocalProj(new EventListener() {
			public void onBrowserEvent(Event event) {
				String atr =event.getCurrentTarget().getAttribute("data-hasqtip");
				DOM.getElementById("qtip-" + atr).removeFromParent();
				Element ele = event.getCurrentTarget().getParentElement();
				ele.removeFromParent();
				Integer id = Integer.parseInt(event.getCurrentTarget().getAttribute("value"));
				excluirLocalProj.add(id);
			}
		});
		
		formularioWidget.removeLocalConv(new EventListener() {
			public void onBrowserEvent(Event event) {
				String atr =event.getCurrentTarget().getAttribute("data-hasqtip");
				DOM.getElementById("qtip-" + atr).removeFromParent();
				Element ele = event.getCurrentTarget().getParentElement();
				ele.removeFromParent();
				Integer id = Integer.parseInt(event.getCurrentTarget().getAttribute("value"));
				excluirLocalConv.add(id);
			}
		});
		
		formularioWidget.addLocalProj(new EventListener() {
			public void onBrowserEvent(Event event) {	
				localProj(event);
			}
		});
		
		formularioWidget.addLocalConv(new EventListener() {
			public void onBrowserEvent(Event event) {	
				localConv(event);
			}
		});
		
		formularioWidget.valorTotal(new EventListener() {
			public void onBrowserEvent(Event event) {	
				String valorTotalvalue = InputElement.as(event.getCurrentTarget()).getValue();
				if(valorTotalvalue != null && valorTotalvalue != ""){
					convertNumber(valorTotalvalue, event.getCurrentTarget().getId());
				}
			}
		});
		
		formularioWidget.ajuda(new EventListener() {
			public void onBrowserEvent(Event event) {	
				popupAjuda(event);
			}
		});
		
//		formularioWidget.addSocial(new EventListener() {
//			public void onBrowserEvent(Event event) {
//				Element social = DOM.getElementById("social");
//				social.appendChild(getHtmlSocial().getElement());
//			}
//		});
	
//		formularioWidget.addFonteRec(new EventListener() {
//			public void onBrowserEvent(Event event) {
//				Element social = DOM.getElementById("fonte");
//				social.appendChild(getHtmlRec().getElement());
//			}
//		});
	}
	
	private void convertNumber(String valorTotalvalue, String idElement){
		String[] valorSplit = valorTotalvalue.split(",",0);
		String valorReplace = valorSplit[0].replaceAll("[.,]", "");
		Number valor = formularioWidget.convertStringToNumber(valorReplace);
		String valorConv = formularioWidget.convertNumberToCurrencyString(valor);
		InputElement input = InputElement.as(DOM.getElementById(idElement));
		input.setValue(valorConv);
	}
	
	public void popupAjuda(Event event){
		PopupPanel helpPanel = new PopupPanel();
		HTML html = new HTML("Digite a localização desejada, clique em um dos resultados e depois clique no botão \"Adicionar\".");
		ScrollPanel scPanel = new ScrollPanel(html);
		helpPanel.add(scPanel);
		helpPanel.setWidth("210px");
		helpPanel.setHeight("60px");
		helpPanel.setAutoHideEnabled(true);
		helpPanel.setPopupPosition(event.getCurrentTarget().getAbsoluteLeft()
				+ event.getCurrentTarget().getOffsetWidth(),
				event.getCurrentTarget().getAbsoluteTop() - 60);
		DOM.setIntStyleAttribute(helpPanel.getElement(), "zIndex",
				110);
		DOM.setStyleAttribute(helpPanel.getElement(), "background",
				"black");
		DOM.setStyleAttribute(helpPanel.getElement(), "color",
				"white");
		DOM.setStyleAttribute(helpPanel.getElement(), "padding",
				"5px");
		helpPanel.show();
	}
	
	public void localProj(Event event){
		formularioWidget.addLocalProj++;
		String id = event.getTarget().getId().substring(9);
		String idenome = event.getTarget().getPreviousSiblingElement().getId();
		localizacao(id, idenome, "locais", "localizacaoProj", formularioWidget.addLocalProj, false);
	}
	
	public void localConv(Event event){
		formularioWidget.addLocalConv++;
		String id = event.getTarget().getId().substring(12);
		String idenome = event.getTarget().getPreviousSiblingElement().getId().toString();
		localizacao(id, idenome, "locaisConv", "localizacaoConv", formularioWidget.addLocalConv, true);
	}
	
	private void localizacao(String id, String idenome, String locais, String tipo, Integer addLocal, Boolean conv){
		InputElement input = InputElement.as(DOM.getElementById(idenome));
		input.setValue("");
		formularioWidget.setValue(idenome, "");
		Element locaisElement = DOM.getElementById(locais+id);
		
		final Element excluirLocal;
		if(conv){
			locaisElement.appendChild(getHtmlLocal(oscInfo, tipo, href, id, addLocal, "excluirLocalConv").getElement());
			excluirLocal = DOM.getElementById("excluirLocalConv"+addLocal);
		}
		else{
			locaisElement.appendChild(getHtmlLocal(oscInfo, tipo, href, id, addLocal, "excluirLocal").getElement());
			excluirLocal = DOM.getElementById("excluirLocal"+addLocal);
		}
		Event.sinkEvents(excluirLocal, Event.ONCLICK);
		Event.setEventListener(excluirLocal, new EventListener() {
			public void onBrowserEvent(Event event) {
				String atr = excluirLocal.getAttribute("data-hasqtip");
				DOM.getElementById("qtip-" + atr).removeFromParent();
				Element ele = event.getCurrentTarget().getParentElement();
				Element div = ele.getParentElement();
				div.removeFromParent();
			}
		});
	}
	
	public void searchLocal(final Event event){
		logger.info("Inserindo temporizador");
		changeDate = new Date();
		final Date thisDate = changeDate;
		Timer t = new Timer() {
			@Override
			public void run() {
				logger.info("Buscando OSC no cadastro de usuário representante da OSC");
				if (changeDate.equals(thisDate)) {
					logger.info("Executando busca");
					String ele = event.getTarget().getId();
					String criteria = formularioWidget.getValue(ele);
					search(criteria, ele);
				}
			}
		};
		t.schedule(DELAY);
	}
	
	public void search(String criteria, final String idElement) {
		logger.info("Realizando busca");
		AsyncCallback<List<SearchResult>> callbackSearch = new AsyncCallback<List<SearchResult>>() {
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			public void onSuccess(List<SearchResult> result) {
				//if (!result.isEmpty()) {
					addResultItems(result,idElement);
				//}
			}
		};
		if (!criteria.trim().isEmpty()){
			searchService.search(criteria, true,  LIMIT, callbackSearch);
		}
	}
	
	public void addResultItems(final List<SearchResult> items, final String idElement) {
		logger.info("Adicionando resultados da busca");
		EventListener listener = new EventListener() {
			public void onBrowserEvent(Event event) {
				Element elem = Element.as(event.getCurrentTarget().getParentElement());
				oscId = elem.getAttribute("value");
				href = elem.getAttribute("name");
				oscInfo = elem.getInnerText();
				formularioWidget.showOrganization(oscInfo, oscId, idElement);
			}
		};
		formularioWidget.addResultItems(items, listener, idElement);
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
		htmlBuilder.append("<div class='nome'>");	
		htmlBuilder.append("<span class='no_margin_right'>"+ count +" |</span>");
		htmlBuilder.append("<span class='no_margin titulo_projeto'><input type='text' name='addProj' id='projeto_nome_projeto"+ proj +"' placeholder='Informação não disponível' /></span>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<span class='clear_both'></span>");
		htmlBuilder.append("</div>");
		
		htmlBuilder.append("<div class='projeto_body collapse-hidden'>");
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
		htmlBuilder.append("<p><input id='link" + proj + "' name='link" + proj + "' placeholder='Informação não disponível' /></p>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
		
		htmlBuilder.append("<div class='clearfix linha'>");
		htmlBuilder.append("<div class='col1_3'>");
		htmlBuilder.append("<strong class='separador left-radius'>Público Alvo do Projeto</strong>");
		htmlBuilder.append("<textarea name='projeto_publico_alvo' id='projeto_publico_alvo"+ proj +"'  placeholder='Informação não disponível'></textarea>");
		htmlBuilder.append("</div>");

		htmlBuilder.append("<div class='col1_6'>");
		htmlBuilder.append("<strong class='separador'>Abrangência</strong>");
		htmlBuilder.append("<p id='projeto_abrang"+ proj +"' >");
		htmlBuilder.append("<select>");
		htmlBuilder.append("<option value='' selected='selected'></option>");
		htmlBuilder.append("<option value='Nacional' > Nacional </option>");
		htmlBuilder.append("<option value='Regional' > Regional </option>");
		htmlBuilder.append("<option value='Estadual' > Estadual </option>");
		htmlBuilder.append("<option value='Municipal' > Municipal </option>");
		htmlBuilder.append("</select>");
		htmlBuilder.append("</p>");
		htmlBuilder.append("</div>");
		
		htmlBuilder.append("<div class='col1_2'>");
		htmlBuilder.append("<strong class='right-radius'>Localização do Projeto</strong>");
		htmlBuilder.append("<div class='localizacao_projeto'>");
		htmlBuilder.append("<ul id='locais"+proj+"' class='locais'>");
		htmlBuilder.append("</ul>");
		htmlBuilder.append("<div>");
		htmlBuilder.append("<label for='campobusca' class='esconder' style='background: none;'>Buscar localização</label>");
		htmlBuilder.append("<input title='Campo de Busca para a Localização do Projeto' type='text' name='campobusca' id='enome"+ count +"' placeholder='Informe a localização desejada...'>");
		htmlBuilder.append("<button type='button' name='adicionar' id='adicionar"+proj+"' class='adicionar'>Adicionar</button>");
		htmlBuilder.append("<button type='button' name='ajuda' class='ajuda' id='ajuda"+count+"' style='padding: 5px 8px;' >?</button></div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
		
		htmlBuilder.append("<div class='clearfix linha'>");
		htmlBuilder.append("<div class='col1_3'>");
		htmlBuilder.append("<strong class='separador left-radius'>Financiadores do Projeto</strong>");
		htmlBuilder.append("<textarea name='financiadores' id='financiadores"+ proj +"'  placeholder='Informação não disponível'></textarea>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div class='col2_3'>");
		htmlBuilder.append("<strong class='right-radius'>Descrição do Projeto</strong>");
		htmlBuilder.append("<textarea name='descprojeto' id='descprojeto"+ proj +"'  placeholder='Informação não disponível'></textarea>");
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
		htmlBuilder.append("<input type='text' name='addDir' id='cargo" + dir + "' placeholder='Informação não disponível'>");
		htmlBuilder.append("<input type='text' name='addDir' id='nome" + dir + "' placeholder='Informação não disponível' />");
		htmlBuilder.append("<div class='botoes'><button id='removedir"+ dir +"' type='button' class='excluir participacao'>Excluir</button></div>");
		htmlBuilder.append("</div>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}
	
	private HTML getHtmlLocal(String oscInfo, String idElement, String href, String proj, Integer id, String excluir) {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<li style='margin-bottom: -7px; margin-top: 4px;' ><a id='"+ excluir + id +"' name='addLocal' value='"+ href.substring(1) +"' class='excluir tooltip' title='Excluir'>Excluir</a> <a id='"+ idElement +""+ proj + id +"' href='Map.html#"+href+"' target='_blank' >"+ oscInfo +"</a></li>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}
	
//	private HTML getHtmlRec() {
//		StringBuilder htmlBuilder = new StringBuilder();
//		htmlBuilder.append("<li id='participacao' >");
//		htmlBuilder.append("<select name='erecursos'>");
//		htmlBuilder.append("<option value='' selected></option>");
//		htmlBuilder.append("<option value='privada'>Privada</option>");
//		htmlBuilder.append("<option value='doacao'>Doação</option>");
//		htmlBuilder.append("<option value='publica'>Pública</option>");
//		htmlBuilder.append("</select>");
//		htmlBuilder.append("<div class='botoes'><button type='button' class='excluir participacao'>Excluir</button></div>");
//		htmlBuilder.append("<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
//		htmlBuilder.append("</li>");
//		HTML html = new HTML(htmlBuilder.toString());
//		return html;
//	}

	
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
