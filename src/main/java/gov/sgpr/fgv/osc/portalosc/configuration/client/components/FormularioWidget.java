package gov.sgpr.fgv.osc.portalosc.configuration.client.components;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

import gov.sgpr.fgv.osc.portalosc.configuration.client.controller.ConfigurationController;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.model.ConfigurationModel;
import gov.sgpr.fgv.osc.portalosc.configuration.client.components.SearchWidget;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.model.SearchResult;

public class FormularioWidget extends Composite {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private SearchWidget searchWidget = new SearchWidget();
	private Element searchTextField;
	
	public FormularioWidget(){
		initWidget(getHTML());
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		searchTextField = DOM.getElementById("enome");
	}
	
	private HTML getHTML(){
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<div class='configuracao clearfix'>");
		htmlBuilder.append("	<form id='form_cadastro_config' name='form_cadastro_config' method='post'>");
		htmlBuilder.append("		<div class='tabs clearfix'>");
		htmlBuilder.append("			<h2 >Identificação</h2>");
		htmlBuilder.append("			<div class='tabbody' id='c_identificacao'>");
		htmlBuilder.append("				<h3>Dados pessoais:</h3>");
		htmlBuilder.append("				<fieldset>");
		htmlBuilder.append("					<input type='hidden' name='ctipo' id='ctipo' class='nome' />");
		htmlBuilder.append("					<input type='hidden' name='cid' id='cid' class='nome' />");
		htmlBuilder.append("					<label for='cnome'>Nome:</label> <input type='text' required='required' name='cnome' id='cnome' placeholder='Nome' class='nome' />");
		htmlBuilder.append("					<label for='ccpf'>CPF</label> <input type='text' required='required' name='ccpf' id='ccpf' placeholder='CPF' class='cpf' />");
		htmlBuilder.append("					<label for='cemail'>Email:</label> <input type='text' required='required' name='cemail' id='cemail'  placeholder='E-mail' class='email' />");
		htmlBuilder.append("					<label for='csenha'>Senha:</label> <input required='required' type='password' name='csenha' id='csenha' placeholder='Senha' class='senha' />");
		htmlBuilder.append("					<label for='ccsenha'>Confirmar Senha:</label> <input type='password' required='required' name='ccsenha' id='ccsenha' placeholder='Confirmar Senha' class='senha' />");
		htmlBuilder.append("				</fieldset>");
		htmlBuilder.append("				<div>");
		htmlBuilder.append("					<h3>Para se tornar representante de uma organização ou mudar a organização da qual é representante, localize a organização.</h3>");
		htmlBuilder.append("					<fieldset>");
		htmlBuilder.append("						<input type='hidden' name='eid' id='eid' class='entidade' value='0' />");
		htmlBuilder.append("						<input type='text' name='enome' id='enome' placeholder='Nome ou CNPJ da entidade' class='entidade' editable='false' />");
		
		htmlBuilder.append("						<div id='entity_name' style='display:none'>");
		htmlBuilder.append("							<p></p><p>Representante da organização:</p>");
		htmlBuilder.append("							<label><strong><span id='oscName' name='oscName'></span></strong></label>");
		htmlBuilder.append("							<input type='hidden' name='oscCode' id='oscCode' value='0' />");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div id='botoes' style='display:none'>");
		htmlBuilder.append("							<input type='button' name='cancelarOSC' id='cancelarOSC' value='Cancelar' class='localizar' alt='Cancelar ser representante de OSC' />");
		htmlBuilder.append("						</div>");
		
		htmlBuilder.append("					</fieldset>");
		htmlBuilder.append("					<p>");
		htmlBuilder.append("						<input type='checkbox' name='inscrever' value='inscrever' id='inscrever' />");
		htmlBuilder.append("						<label for='inscrever'>Desejo receber e-mail sobre as novidades do Portal das Organizações da Sociedade Civil.</label>");
		htmlBuilder.append("					</p>");
		htmlBuilder.append("				</div>");
		htmlBuilder.append("			</div>");
		htmlBuilder.append("		</div>");
		htmlBuilder.append("		<div id='floatingLoginErrorList'></div>");
		htmlBuilder.append("		<div class='botoes'><a href='#' class='cancelar' id='btnCancelar'>Cancelar</a> ou <input type='button' id='btnSalvar' name='btnSalvar' value='Salvar alterações' class='salvar'/></div>");
		htmlBuilder.append("	</form>");
		htmlBuilder.append("</div>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;
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
	
//	/**
//	 * @param listener
//	 *            Controla o evento de click do botão "Localizar".
//	 */
//	public void addSearchClickListener(EventListener listener) {
//		final Element elem = DOM.getElementById("localizarOSC");
//		Event.sinkEvents(elem, Event.ONCLICK);
//		Event.setEventListener(elem, listener);
//	}
	
	/**
	 * @param listener
	 *            Controla o evento de click do botão "Localizar".
	 */
	public void addCancelOSCClickListener(EventListener listener) {
		final Element elem = DOM.getElementById("cancelarOSC");
		Event.sinkEvents(elem, Event.ONCLICK);
		Event.setEventListener(elem, listener);
	}
	
	/**
	 * @param listener
	 *            Controla o evento de click do botão salvar.
	 */
	public void addSalvarListener(EventListener listener) {
		Element btnSalvar = DOM.getElementById("btnSalvar");
		Event.sinkEvents(btnSalvar, Event.ONCLICK);
		Event.setEventListener(btnSalvar, listener);
		validate();
	}
	
	/**
	 * @param listener
	 *            Controla o evento de click do botão cancelar.
	 */
	public void addCancelarListener(EventListener listener) {
		Element btnCancelar = DOM.getElementById("btnCancelar");
		Event.sinkEvents(btnCancelar, Event.ONCLICK);
		Event.setEventListener(btnCancelar, listener);
	}
	
	public ConfigurationModel getUser() {
		ConfigurationModel user = new ConfigurationModel();
		String oscCode = InputElement.as(DOM.getElementById("oscCode")).getValue();
		if(oscCode == "0"){
			user.setTipoUsuario(2);
		}
		else{
			user.setTipoUsuario(Integer.valueOf(InputElement.as(DOM.getElementById("ctipo")).getValue()));
		}
		user.setId(Integer.valueOf(InputElement.as(DOM.getElementById("cid")).getValue()));
		user.setNome(InputElement.as(DOM.getElementById("cnome")).getValue());
		user.setCPF(Long.valueOf(InputElement.as(DOM.getElementById("ccpf")).getValue().replaceAll("\\D", "")));
		user.setEmail(InputElement.as(DOM.getElementById("cemail")).getValue());
		user.setSenha(ConfigurationController.encrypt(InputElement.as(DOM.getElementById("csenha")).getValue()));
		if(DOM.getElementById("inscrever").getPropertyBoolean("checked")){
			user.setListaEmail(true);
		}
		else{
			user.setListaEmail(false);
		}
		user.setIdOsc(Integer.valueOf(oscCode));
		return user;
	}
	
	public void setUser(ConfigurationModel user) {
		DOM.getElementById("ctipo").setAttribute("value", String.valueOf(user.getTipoUsuario()));
		DOM.getElementById("cid").setAttribute("value", String.valueOf(user.getId()));
		DOM.getElementById("cnome").setAttribute("value", user.getNome());
		DOM.getElementById("ccpf").setAttribute("value", String.valueOf(user.getCPF()));
		DOM.getElementById("cemail").setAttribute("value", user.getEmail());
		DOM.getElementById("csenha").setAttribute("value", ConfigurationController.decrypt(user.getSenha()));
		DOM.getElementById("ccsenha").setAttribute("value", ConfigurationController.decrypt(user.getSenha()));
		if(user.getListaEmail()){
			DOM.getElementById("inscrever").setAttribute("checked", "checked");
		}
		DOM.getElementById("eid").setAttribute("value", String.valueOf(user.getIdOsc()));
		if(user.getIdOsc() != 0){
			DOM.getElementById("entity_name").getStyle().setDisplay(Display.BLOCK);
			DOM.getElementById("botoes").getStyle().setDisplay(Display.BLOCK);
		}
		DOM.getElementById("oscCode").setAttribute("value", String.valueOf(user.getIdOsc()));
	}
	
	public void setValue(String value) {
		searchTextField.setAttribute("value", value);
	}
	
	public String getValue() {
		return searchTextField.getPropertyString("value");
	}
	
	public void showOrganization(String oscInfo, String oscId) {
		DOM.getElementById("resultado_busca").getStyle().setDisplay(Display.NONE);
		DOM.getElementById("entity_name").getStyle().setDisplay(Display.BLOCK);
		DOM.getElementById("botoes").getStyle().setDisplay(Display.BLOCK);
		DOM.getElementById("oscName").setInnerText(oscInfo);
		DOM.getElementById("oscCode").setAttribute("value", oscId);
		DOM.getElementById("ctipo").setAttribute("value", "4");
	}
	
	public void clearOSC(){
		DOM.getElementById("entity_name").getStyle().setDisplay(Display.NONE);
		DOM.getElementById("botoes").getStyle().setDisplay(Display.NONE);
		DOM.getElementById("oscName").setInnerText("");
		DOM.getElementById("oscCode").setAttribute("value", "0");
		DOM.getElementById("ctipo").setAttribute("value", "2");
	}
	
	public void addResultItems(List<SearchResult> items, EventListener listener) {
		searchWidget.setItems(items);
		searchWidget.addSearchListener(listener);
	}
	
	public Boolean getEmail(){
		Boolean result = false;
		if(InputElement.as(DOM.getElementById("oscCode")).getValue() != InputElement.as(DOM.getElementById("eid")).getValue() && 
				InputElement.as(DOM.getElementById("ctipo")).getValue() == "4"){
			result = true;
		}
		return result;
	}
	
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
	
		var validate = $wnd.jQuery('#form_cadastro_config').validate({
			ignore : [],
			rules : {
				cemail : {
					required : true,
					email : true
				},
				cnome : {
					required : true,
					minlength : 5
				},
				ccpf : {
					verificaCPF : true,
					required : true
				},
				csenha : {
					required : true,
					minlength : 6
				},
				ccsenha : {
					required : true,
					equalTo : '#csenha'
				},
				cinscrever : {
					required : false
				}
			},
			messages : {
				csenha : {
					required : 'Campo obrigatório.',
					minlength : 'Senha muito curta.'
				},
				cemail : {
					required : 'Campo obrigatório.',
					email : 'Por favor, informe um email válido.'
				},
				ccsenha : {
					required : 'Campo obrigatório.',
					equalTo : 'As duas senhas devem ser iguais.'
				},
				cnome : {
					required : 'Campo obrigatório.',
					minlength : 'O nome deve conter ao menos {0} caracteres.'
				},
				ccpf : {
					verificaCPF : 'CPF inválido.',
					required : 'Informe seu CPF.'
				}
			},
			
			errorPlacement: function(error, element) {
				var parent = $wnd.jQuery(element).parent();
				console.log(parent); 
				error.appendTo(parent);
			},
		});
	}-*/;
	
	/**
	 * Verifica se o formulário é valido.
	 * 
	 * @return true se válido
	 */
	public native boolean isValid() /*-{
		return $wnd.jQuery('#form_cadastro_config').valid();
	}-*/;
	
	/**
	 * Adiciona regra para email inválido.
	 * 
	 * @param invalidEmail
	 */
	public native void addInvalidEmail(String invalidEmail) /*-{
	$wnd.jQuery('#cemail').rules('add', {
		notEqual : invalidEmail,
		messages : {
			notEqual : "Este email já esta cadastrado no Mapa"
		}
	});
}-*/;
	
	/**
	 * Adiciona regra para cpf já cadastrado no mapa.
	 * 
	 * @param email
	 */
	public native void addInvalidCpf(String invalidCpf) /*-{
		$wnd.jQuery('#ccpf').rules('add', {
			notEqualCPF : invalidCpf,
			messages : {
				notEqualCPF : "Este CPF já esta cadastrado no Mapa"
			}
		});
	}-*/;
	
	/**
	 * Adiciona regra para email inválido.
	 * 
	 * @param passwd
	 *            senha inválida
	 */
	
	public native void addInvalidPassword(String invalidPasswd) /*-{
		$wnd.jQuery('#csenha').rules('remove');
		$wnd.jQuery('#ccsenha').rules('remove');
		$wnd.jQuery('#csenha').rules('add', {
			notEqual : invalidPasswd,
			messages : {
				notEqual : "Senha inválida"
			}
		});
	}-*/;
}