package gov.sgpr.fgv.osc.portalosc.user.client.components;

import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.FacebookUser;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class LogonWidget extends Composite {
	private final Logger logger = Logger.getLogger(this.getClass().getName());

	/**
	 * Construtor
	 */
	public LogonWidget() {
		initWidget(getHtml());
	}

	public LogonWidget(DefaultUser user) {
		initWidget(getLoggedHtml(user.getName()));
	}
	public LogonWidget(FacebookUser user) {
		initWidget(getLoggedHtml(user.getName()));
	}

	private HTML getHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<form id='acesso' name='Acesso'>");
		htmlBuilder
				.append("	<label for='email' class='esconder'>Email</label>");
		htmlBuilder
				.append("	<input type='text' name='email' id='email' placeholder='Email' />");
		htmlBuilder
				.append("	<label for='senha' class='esconder'>Senha</label>");
		htmlBuilder
				.append("	<input type='password' name='senha' id='senha' placeholder='Senha' />");
		htmlBuilder
				.append("	<div id='floatingLoginErrorList'></div>");
		htmlBuilder.append("	<div>");
		htmlBuilder.append("		<a href='#'>Esqueci a senha</a>");
		htmlBuilder.append("		<a href='");
		htmlBuilder.append(GWT.getHostPageBaseURL() + "User.html");
		htmlBuilder.append("		'>Registrar-se</a>");
		htmlBuilder.append("	</div>");
		htmlBuilder
				.append("	<input type='button' name='entrar' id='entrar' value='Entrar' />");
		htmlBuilder.append("</form>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;

	}

	private HTML getLoggedHtml(String name) {
		StringBuilder htmlBuilder = new StringBuilder();
		
		htmlBuilder.append("<div class='clearfix'>");
		htmlBuilder.append("   <img id=\"netUserImage\" src=\"\" align= \"right\"/>");
		htmlBuilder.append("	<ul>");
		htmlBuilder.append("		<li><h2>");
		htmlBuilder.append(name);
		htmlBuilder.append("		</h2></li>");
		htmlBuilder.append("		<li><a href='#'>  Configurações  </a></li>");
		htmlBuilder.append("	</ul>");
		htmlBuilder.append("</div>");
		htmlBuilder
				.append("<input type='button' name='sair' id='sair' value='Sair'	/>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;

	}

	/**
	 * @param listener
	 *            Controla o evento de click do botão de logon.
	 */
	public void addLogonListener(EventListener listener) {
		Element btnEnter = DOM.getElementById("entrar");
		Event.sinkEvents(btnEnter, Event.ONCLICK);
		Event.setEventListener(btnEnter, listener);
		validate();
	}

	/**
	 * @param listener
	 *            Controla o evento de click do botão de logout.
	 */
	public void addLogoutListener(EventListener listener) {
		Element btnLogout = DOM.getElementById("sair");
		Event.sinkEvents(btnLogout, Event.ONCLICK);
		Event.setEventListener(btnLogout, listener);
	}

	private native void validate() /*-{
		$wnd.jQuery.validator.addMethod("passwdEqual", function(value, element,
				param) {
			return value == param;
		}, "Senha Inválida");

		$wnd.jQuery.validator.addMethod("notEqual", function(value, element,
				param) {
			return value != param;
		}, "Por favor, informe um email cadastrado");

		$wnd.jQuery('#acesso').validate({
			ignore : [],
			rules : {
				email : {
					required : true,
					email : true
				},
				senha : {
					required : true,
					minlength : 6
				}
			},
			messages : {
				senha : {
					required : 'Campo obrigatório.',
					minlength : 'Senha muito curta'
				},
				email : {
					required : 'Campo obrigatório.',
					email : 'Por favor, informe um email válido'
				}
			},
			
			errorPlacement: function(error, element) {
				error.appendTo($wnd.jQuery('#floatingLoginErrorList'));
			},
			
			showErrors: function(errorMap, errorList) {
				if(errorList.length > 0) {
					$wnd.jQuery(errorList[0].element).closest('form').addClass('formError');
				}
				this.defaultShowErrors();
			},
			
			success: function(label, element) {
				$wnd.jQuery(element).removeClass('error');
				$wnd.jQuery(label).remove();
				if($wnd.jQuery(element).closest('form').find('.error').length == 0) {
					$wnd.jQuery(element).closest('form').removeClass('formError');
				}
			},
		});
	}-*/;

	/**
	 * Adiciona regra para email inválido.
	 * 
	 * @param invalidEmail
	 */
	public native void addInvalidEmail(String invalidEmail) /*-{
		$wnd.jQuery('#email').rules('add', {
			notEqual : invalidEmail,
			messages : {
				notEqual : "Email não cadastrado no Mapa"
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
		$wnd.jQuery('#senha').rules('remove');
		$wnd.jQuery('#senha').rules('add', {
			notEqual : invalidPasswd,
			messages : {
				notEqual : "Senha inválida"
			}
		});
	}-*/;

	/**
	 * Verifica se o formulário é valido.
	 * 
	 * @return true se válido
	 */
	public native boolean isValid() /*-{
		return $wnd.jQuery('#acesso').valid();
	}-*/;

	/**
	 * @return email.
	 */
	public String getEmail() {
		InputElement email = InputElement.as(DOM.getElementById("email"));
		return email.getValue();
	}

	/**
	 * @return senha.
	 */
	public String getPassword() {
		InputElement passwd = InputElement.as(DOM.getElementById("senha"));
		return passwd.getValue();
	}
}
