package gov.sgpr.fgv.osc.portalosc.user.client.components;

import gov.sgpr.fgv.osc.portalosc.user.client.controller.UserController;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;

import java.util.logging.Logger;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class UserFormWidget extends Composite {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Button okButton;
	private Anchor cancelButton;
	private TextBox email;
	private PasswordTextBox passwd;
	private PasswordTextBox passConfirm;
	private TextBox name;
	private TextBox cpf;
	private CheckBox mailing;

	public UserFormWidget() {
		initWidget(getHtml());
	}

	public UserFormWidget(DefaultUser user) {
		initWidget(getHtml());
		setUser(user);
	}

	/**
	 * @param listener
	 *            Controla o evento de click do botão de cadastrar usuário.
	 */
	public void addSubmitListener(EventListener listener) {
		Element btnFillIn = DOM.getElementById("btnCadastro");
		Event.sinkEvents(btnFillIn, Event.ONCLICK);
		Event.setEventListener(btnFillIn, listener);
		validate();
	}
	
	public void addSubmitcemail(EventListener listener) {
		Element cemail = DOM.getElementById("cemail");
		Event.sinkEvents(cemail, Event.ONKEYPRESS);
		Event.setEventListener(cemail, listener);
		validate();
	}
	
	public void addSubmitcsenha(EventListener listener) {
		Element csenha = DOM.getElementById("csenha");
		Event.sinkEvents(csenha, Event.ONKEYPRESS);
		Event.setEventListener(csenha, listener);
		validate();
	}
	
	public void addSubmitccsenha(EventListener listener) {
		Element ccsenha = DOM.getElementById("ccsenha");
		Event.sinkEvents(ccsenha, Event.ONKEYPRESS);
		Event.setEventListener(ccsenha, listener);
		validate();
	}
	
	public void addSubmitcnome(EventListener listener) {
		Element cnome = DOM.getElementById("cnome");
		Event.sinkEvents(cnome, Event.ONKEYPRESS);
		Event.setEventListener(cnome, listener);
		validate();
	}
	
	public void addSubmitccpf(EventListener listener) {
		Element ccpf = DOM.getElementById("ccpf");
		Event.sinkEvents(ccpf, Event.ONKEYPRESS);
		Event.setEventListener(ccpf, listener);
		validate();
	}

	/**
	 * @param listener
	 *            Controla o evento de click do botão de cadastrar usuário.
	 */
	public void addCancelListener(EventListener listener) {
		Element btnCancel = DOM.getElementById("btnCancelar");
		Event.sinkEvents(btnCancel, Event.ONCLICK);
		Event.setEventListener(btnCancel, listener);
	}

	/**
	 * @return Usuário a ser cadastrado.
	 */
	public DefaultUser getUser() {
		InputElement email = InputElement.as(DOM.getElementById("cemail"));
		InputElement passwd = InputElement.as(DOM.getElementById("csenha"));
		InputElement name = InputElement.as(DOM.getElementById("cnome"));
		InputElement cpf = InputElement.as(DOM.getElementById("ccpf"));
		boolean mailingListMember = DOM.getElementById("cinscrever")
				.getPropertyBoolean("checked");
		DefaultUser user = new DefaultUser();
		user.setEmail(email.getValue());
		String password = UserController.encrypt(passwd.getValue());
		user.setPassword(password);
		user.setName(name.getValue());
		long ncpf = Long.valueOf(cpf.getValue().replaceAll("\\D", ""));
		user.setCpf(ncpf);
		user.setMailingListMember(mailingListMember);
		user.setType(UserType.DEFAULT);
		return user;
	}

	/**
	 * @return Usuário a ser cadastrado.
	 */
	public void setUser(DefaultUser user) {
		InputElement email = InputElement.as(DOM.getElementById("cemail"));
		InputElement passwd = InputElement.as(DOM.getElementById("csenha"));
		InputElement cpasswd = InputElement.as(DOM.getElementById("ccsenha"));
		InputElement name = InputElement.as(DOM.getElementById("cnome"));
		InputElement cpf = InputElement.as(DOM.getElementById("ccpf"));
		email.setValue(user.getEmail());
		passwd.setValue(user.getPassword());
		cpasswd.setValue(user.getPassword());
		name.setValue(user.getName());
		cpf.setValue(String.valueOf(user.getCpf()));
		DOM.getElementById("cinscrever").setPropertyBoolean("checked",
				user.isMailingListMember());
	}

	private HTML getHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<h3 class='b-cadastro'>Cadastre-se no Mapa</h3>");
		htmlBuilder.append("<div>");
		htmlBuilder.append("Cadastrando-se no Mapa, você poderá:");
		htmlBuilder.append("<ul>");
		htmlBuilder.append("<li>Avaliar Organizações</li>");
		htmlBuilder.append("<li>Compartilhar informações com seus amigos</li>");
		htmlBuilder.append("<li>Definir suas preferências no mapa</li>");
		htmlBuilder.append("</ul>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div>");
		htmlBuilder
				.append("<form id='form_cadastro' name='form_cadastro' method='post'>");
		htmlBuilder.append("<div class='clearfix'>");
		htmlBuilder.append("<label for='cemail'>Email:</label> ");
		htmlBuilder
				.append("<input type='text' name='cemail' id='cemail' placeholder='E-mail' class='email' required='required' />");
		htmlBuilder.append("<label for='csenha'>Senha:</label> ");
		htmlBuilder
				.append("<input type='password' name='csenha' id='csenha' placeholder='Senha' class='senha' required='required' />");
		htmlBuilder.append("<label for='ccsenha'>Confirmar Senha:</label> ");
		htmlBuilder
				.append("<input type='password' name='ccsenha' id='ccsenha' placeholder='Confirmar Senha' required='required' class='senha' />");
		htmlBuilder.append("<label for='cnome'>Nome:</label> ");
		htmlBuilder
				.append("<input type='text' name='cnome' id='cnome' placeholder='Nome' class='nome' required='required' />");
		htmlBuilder.append("<label for='ccpf'>CPF</label> ");
		htmlBuilder
				.append("<input type='text' name='ccpf' id='ccpf' placeholder='CPF' class='cpf' required='required' />");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div>");
		htmlBuilder
				.append("<input type='checkbox' id='cinscrever' name='cinscrever'/>");
		htmlBuilder
				.append("<div>Desejo receber e-mail sobre as novidades do Mapa das Organizações da Sociedade Civil.</div>");
		htmlBuilder.append("</div>");
		htmlBuilder
				.append("<div class='botoes' id='botoes_cadastro'><a href='#' class='cancelar' id='btnCancelar'>Cancelar</a> ou ");
		htmlBuilder
				.append("<input type='button' id='btnCadastro'  value='Finalizar cadastro' /></div>");
		htmlBuilder.append("</form>");
		htmlBuilder.append("</div>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;

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

		var validate = $wnd.jQuery('#form_cadastro').validate({
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
			}
		});
	}-*/;

	/**
	 * Verifica se o formulário é valido.
	 * 
	 * @return true se válido
	 */
	public native boolean isValid() /*-{
		return $wnd.jQuery('#form_cadastro').valid();
	}-*/;

	/**
	 * Adiciona regra para email inválido.
	 * 
	 * @param email
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
	 * Adiciona regra para cpf inválido.
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

}
