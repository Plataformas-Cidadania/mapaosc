package gov.sgpr.fgv.osc.portalosc.user.client.components;

import gov.sgpr.fgv.osc.portalosc.user.client.controller.UserController;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.RepresentantUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.SearchResult;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class RepresentantFormWidget extends Composite {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private List<SearchResult> items = new ArrayList<SearchResult>();
	private Button okButton;
	private Anchor cancelButton;
	private TextBox email;
	private PasswordTextBox passwd;
	private PasswordTextBox passConfirm;
	private TextBox name;
	private TextBox cpf;
	private CheckBox mailing;
	private Element searchTextField;
	private Element oscItem;
	private SearchWidget searchWidget = new SearchWidget();
	
	public RepresentantFormWidget() {
		initWidget(getHtml());
	}
	
	public RepresentantFormWidget(RepresentantUser user) {
		initWidget(getHtml());
		setUser(user);
	}
	
	/**
	 * @param listener
	 *            Controla o evento de busca de OSC.
	 */
	public void addFocusListener(EventListener listener) {
		final Element elem = DOM.getElementById("enome");
		Event.sinkEvents(elem, Event.ONFOCUS);
		Event.setEventListener(elem, listener);
	}
	
	/**
	 * @param listener
	 *            Controla o evento de busca de OSC.
	 */
	public void addSearchChangeListener(EventListener listener) {
		final Element elem = DOM.getElementById("enome");
		Event.sinkEvents(elem, Event.ONKEYDOWN);
		Event.setEventListener(elem, listener);
	}
	
	public void addSubmitccpf(EventListener listener) {
		Element elem = DOM.getElementById("ecpf");
		Event.sinkEvents(elem, Event.ONKEYPRESS);
		Event.setEventListener(elem, listener);
		validate();
	}
	
	/**
	 * @param listener
	 *            Controla o evento de click do botão de cadastrar usuário.
	 */
	public void addSubmitListener(EventListener listener) {
		Element btnFillIn = DOM.getElementById("btnCadastroOSC");
		Event.sinkEvents(btnFillIn, Event.ONCLICK);
		Event.setEventListener(btnFillIn, listener);
		validate();
	}
	
	/**
	 * @param listener
	 *            Controla o evento de click do botão de cancelar.
	 */
	public void addCancelListener(EventListener listener) {
		Element btnCancel = DOM.getElementById("btnCancelarOSC");
		Event.sinkEvents(btnCancel, Event.ONCLICK);
		Event.setEventListener(btnCancel, listener);
	}
	
	public void setValue(String value) {
		searchTextField.setAttribute("value", value);
	}
	
	public String getValue() {
		return searchTextField.getPropertyString("value");
	}
	
	/**
	 * @return Usuário a ser cadastrado.
	 */
	public RepresentantUser getUser(){
		InputElement representantEmail = InputElement.as(DOM.getElementById("eemail"));
		InputElement passwd = InputElement.as(DOM.getElementById("esenha"));
		InputElement name = InputElement.as(DOM.getElementById("representantName"));
		InputElement cpf = InputElement.as(DOM.getElementById("ecpf"));
		Element oscId = DOM.getElementById("oscCode");	
		boolean mailingListMember = DOM.getElementById("einscrever").getPropertyBoolean("checked");
		String password = UserController.encrypt(passwd.getValue());
		
		RepresentantUser user = new RepresentantUser();
		user.setEmail(representantEmail.getValue());
		user.setName(name.getValue());
		user.setCpf(Long.parseLong(String.valueOf(cpf.getValue()).replaceAll("\\D", "")));
		user.setPassword(password);
		user.setMailingListMember(mailingListMember);
		user.setType(UserType.OSC_AGENT);
		user.setOscId(Integer.parseInt(oscId.getInnerHTML()));	
		
		return user;
	}
	
	/**
	 * @return Usuário a ser cadastrado.
	 */
	public void setUser(RepresentantUser user){
		if(DOM.getElementById("etermo").getPropertyBoolean("checked")){
			InputElement representantEmail = InputElement.as(DOM.getElementById("eemail"));
			InputElement passwd = InputElement.as(DOM.getElementById("esenha"));
			InputElement cpasswd = InputElement.as(DOM.getElementById("ecsenha"));
			InputElement name = InputElement.as(DOM.getElementById("representantName"));
			InputElement cpf = InputElement.as(DOM.getElementById("ecpf"));
			representantEmail.setValue(user.getEmail());
			passwd.setValue(user.getPassword());
			cpasswd.setValue(user.getPassword());
			name.setValue(user.getName());
			cpf.setValue(String.valueOf(user.getCpf()));
			DOM.getElementById("einscrever").setPropertyBoolean("checked", user.isMailingListMember());
		}
	}
	
	private HTML getHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		
		htmlBuilder.append("<h3 class='b-entidade'>Represente sua entidade</h3>");
		htmlBuilder.append("<div>Sendo um representante da organização, você poderá:");
		htmlBuilder.append("	<ul>");
		htmlBuilder.append("		<li>Informar dados da organização</li>");
		htmlBuilder.append("		<li>Compartilhar informações com seus amigos</li>");
		htmlBuilder.append("		<li>Definir suas preferências no mapa</li>");
		htmlBuilder.append("	</ul>");
		htmlBuilder.append("	<div class='entidade_validacao'>");
		htmlBuilder.append("		<div id = 'div-representant' class='clearfix'>");
		htmlBuilder.append("			<img src='imagens/cadastro_01.png' width='20' height='20' alt='Etapa 1'/>");
		htmlBuilder.append("			<div>");
		htmlBuilder.append("				<form>");
		htmlBuilder.append("					Verifique se a organização já está cadastrada no Mapa, informando o nome ou CNPJ: ");
		htmlBuilder.append("					<input type='text' name='enome' id='enome' placeholder='Nome ou CNPJ da entidade' required='required' class='entidade' />");
		htmlBuilder.append("					<div class='botoes'>");
		htmlBuilder.append("						<label><strong><span id='oscCode' name='oscCode'></span></strong></label>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("				</form>");
		htmlBuilder.append("			</div>");
		htmlBuilder.append("		</div>");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("	<div id = 'entity_name' style=' display:none'>");
		htmlBuilder.append("		<img src='imagens/cadastro_01.png' width='20' height='20' alt='Etapa 1'>");
		htmlBuilder.append("		<label><strong><span id='oscName' name='oscName'></span></strong></label>");
		htmlBuilder.append("		<img src='imagens/cadastro_check.png'  alt='Cadastro check'></p>");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("	<form id='form_representante' name='form_representante' method='post' class='entidade_validacao'>");
		htmlBuilder.append("		<div id='cadastro_rep' style='display:none'>");
		htmlBuilder.append("			<img src='imagens/cadastro_02.png' width='20' height='20' alt='Etapa 3'>");
		htmlBuilder.append("			<div class='clearfix' id='representante_identificacao'>");
		htmlBuilder.append("				<strong>Informe seus dados de identificação</strong>");
		htmlBuilder.append("				<label for='cnome'>Nome:</label>");
		htmlBuilder.append("				<input type='text' name='representantName' id='representantName' placeholder='Nome' class='nome' required='required' />");
		htmlBuilder.append("				<label for='ecpf'>CPF</label> ");
		htmlBuilder.append("				<input type='text' name='ecpf' id='ecpf' placeholder='CPF' class='cpf' required='required' />");
		htmlBuilder.append("				<label for='eemail'>Email:</label>");
		htmlBuilder.append("				<input type='text' name='eemail' id='eemail' required='required'");
		htmlBuilder.append("				placeholder='E-mail' class='email'/>");
		htmlBuilder.append("				<label for='esenha'>Senha:</label>");
		htmlBuilder.append("				<input type='password' required='required' name='esenha' id='esenha' placeholder='Senha' class='senha'/>");
		htmlBuilder.append("				<label for='ecsenha'>Confirmar Senha:</label>");
		htmlBuilder.append("				<input type='password' required='required' name='ecsenha' id='ecsenha' placeholder='Confirmar Senha' class='senha'/>");
		htmlBuilder.append("			</div>");
		htmlBuilder.append("			<div>");
		htmlBuilder.append("				<input type='checkbox' name='etermo' value='etermo' />");
		htmlBuilder.append("				Concordo com os <a href='static.html?page=termos' target='_blank' >termos de uso</a>");
		htmlBuilder.append("			</div>");
		htmlBuilder.append("			<div>");
		htmlBuilder.append("				<input type='checkbox' id='einscrever' name='einscrever'/>");
		htmlBuilder.append("				<div>Desejo receber email sobre as novidades do Mapa das Organizações da Sociedade Civil.</div>");
		htmlBuilder.append("			</div>");
		htmlBuilder.append("		</div>");
		htmlBuilder.append("		<div class='botoes'>");
		htmlBuilder.append("			<a href='#' class='cancelar' name= 'btnCancelar'id='btnCancelarOSC'>Cancelar</a> ou");
		htmlBuilder.append("			<input type='button' id= 'btnCadastroOSC' name= 'btnCadastroOSC' value='Entrar' class='botao_entrar'/>");
		htmlBuilder.append("		</div>");		
		htmlBuilder.append("	</form>");
		htmlBuilder.append("</div>");
		
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		searchTextField = DOM.getElementById("enome");
	}
	
	public void addResultItems(List<SearchResult> items, EventListener listener) {
		searchWidget.setItems(items);
		searchWidget.addSearchListener(listener);
	}
	
	public void showOrganization(String oscInfo, String oscId) {
		final Element searchWidget = DOM.getElementById("resultado_busca");
		final Element representant = DOM.getElementById("div-representant");
		final Element fieldOscName = DOM.getElementById("oscName");
		final Element fieldOscCode = DOM.getElementById("oscCode");
		final Element representantName = DOM.getElementById("entity_name");
		final Element cadastroRepresent = DOM.getElementById("cadastro_rep");
		fieldOscName.setInnerText(oscInfo);
		fieldOscCode.setInnerText(oscId);

		representant.getStyle().setDisplay(Display.NONE);
		searchWidget.getStyle().setDisplay(Display.NONE);
		representantName.getStyle().setDisplay(Display.BLOCK);
		cadastroRepresent.getStyle().setDisplay(Display.BLOCK);
	}
	
	public native void close() /*-{
								$wnd.jQuery.fancybox.close();
								}-*/;
	
	/**
	 * Valida formulário sem considerar o email.
	 * 
	 * @return true se válido
	 */
	private native void validate() /*-{
		$wnd.jQuery.validator.addMethod("notEqual", function(value, element,
				param) {
			return value != param;
		}, "Por favor, informe um email não cadastrado.");
		
		$wnd.jQuery.validator.addMethod("notEqualCPF", function(value, element,
				param) {
			value = value.replace('.', '');
			value = value.replace('.', '');
			value = value.replace('-', '');
			value = parseInt(value);
			return value != param;
		}, "Por favor, informe um cpf não cadastrado.");
	
		$wnd.jQuery.validator
				.addMethod(
						"verificaCPF",
						function(value, element) {
							value = value.replace('.', '');
							value = value.replace('.', '');
							cpf = value.replace('-', '');
							while (cpf.length < 11)
								cpf = "0" + cpf;
							var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
							var a = [];
							var b = new Number;
							var c = 11;
							for (i = 0; i < 11; i++) {
								a[i] = cpf.charAt(i);
								if (i < 9)
									b += (a[i] * --c);
							}
							if ((x = b % 11) < 2) {
								a[9] = 0
							} else {
								a[9] = 11 - x
							}
							b = 0;
							c = 11;
							for (y = 0; y < 10; y++)
								b += (a[y] * c--);
							if ((x = b % 11) < 2) {
								a[10] = 0;
							} else {
								a[10] = 11 - x;
							}
							if ((cpf.charAt(9) != a[9])
									|| (cpf.charAt(10) != a[10])
									|| cpf.match(expReg))
								return false;
							return true;
						}, "Informe um CPF válido."); // Mensagem padrão
		
		var validate = $wnd.jQuery('#form_representante').validate({
			ignore : [],
			rules : {
				eemail : {
					required : true,
					email : true
				},
				representantName : {
					required : true,
					minlength : 5
				},
				ecpf : {
					verificaCPF : true,
					required : true
				},
				esenha : {
					required : true,
					minlength : 6
				},
				ecsenha : {
					required : true,
					equalTo : '#esenha'
				},
				einscrever : {
					required : false
				},
				etermo : {
					required : true
				}
			},
			messages : {
				eemail : {
					required : 'Campo obrigatório.',
					email : 'Por favor, informe um email válido.'
				},
				esenha : {
					required : 'Campo obrigatório.',
					minlength : 'Senha muito curta.'
				},
				ecsenha : {
					required : 'Campo obrigatório.',
					equalTo : 'As duas senhas devem ser iguais.'
				},
				representantName : {
					required : 'Campo obrigatório.',
					minlength : 'O nome deve conter ao menos {0} caracteres.'
				},
				ecpf : {
					verificaCPF : 'CPF inválido.',
					required : 'Informe seu CPF.'
				},
				etermo : {
					required : 'Campo obrigatório.'
				}
			}
		});
	}-*/;

	/**
	 * Verifica se o formulário é valido.
	 * 
	 * @return true se válido
	 */
	public native boolean isValid() /*-{
		return $wnd.jQuery('#form_representante').valid();
	}-*/;

	/**
	 * Adiciona regra para email inválido.
	 * 
	 * @param email
	 */

	public native void addInvalidEmail(String invalidEmail) /*-{
		$wnd.jQuery('#eemail').rules('add', {
			notEqual : invalidEmail,
			messages : {
				notEqual : "Este email já esta cadastrado no Mapa"
			}
		});
	}-*/;
	
	/**
	 * Adiciona regra para cpf inválido.
	 * 
	 * @param email
	 */
	public native void addInvalidCpf(String invalidCpf) /*-{
		$wnd.jQuery('#ecpf').rules('add', {
			notEqualCPF : invalidCpf,
			messages : {
				notEqualCPF : "Este CPF já esta cadastrado no Mapa"
			}
		});
	}-*/;
}