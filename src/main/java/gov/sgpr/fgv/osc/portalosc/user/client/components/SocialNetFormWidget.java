package gov.sgpr.fgv.osc.portalosc.user.client.components;

import gov.sgpr.fgv.osc.portalosc.user.client.controller.UserController;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.FacebookUser;
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

/**
 * @author diego
 *
 */
/**
 * @author diego
 * 
 */
public class SocialNetFormWidget extends Composite {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private Button okButton;
	private Anchor cancelButton;
	private TextBox email;
	private PasswordTextBox passwd;
	private PasswordTextBox passConfirm;
	private TextBox name;
	private TextBox cpf;
	private CheckBox mailing;
	private FacebookUser user = new FacebookUser();
	public boolean isDefaultUser = true;

	public SocialNetFormWidget() {
		initWidget(getHtml());
	}

	/**
	 * @param listener
	 *            Controla o evento de click do botão de facebook.
	 */
	public void enterFacebookUser(EventListener listener) {
		Element btnFbIn = DOM.getElementById("b-facebook");
		Event.sinkEvents(btnFbIn, Event.ONCLICK);
		Event.setEventListener(btnFbIn, listener);
	}
	
	/**
	 * Função que traz as informações do usuário, depois que 
	 * o mesmo realizar o login no facebook, de volta na fancybox
	 */
	public void showSocialUser(FacebookUser user){
		this.user = user;
		Element userImage = DOM.getElementById("ruserImage");
		userImage.setAttribute("src", user.getSmallPictureUrl());
		Element ruserName = DOM.getElementById("ruserName");
		ruserName.setInnerText(user.getName());
		Element ruserMail = DOM.getElementById("ruserMail");
		ruserMail.setInnerText(user.getEmail());
	
		openWidget();
	}
	public native void openWidget() /*-{
		$wnd.jQuery('#bRedes').click(); 
	}-*/;

	
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

	/**
	 * @param listener
	 *            Controla o evento de click do botão de cancelar.
	 */
	public void addCancelListener(EventListener listener) {
		Element btnCancel = DOM.getElementById("btnCancelar");
		Event.sinkEvents(btnCancel, Event.ONCLICK);
		Event.setEventListener(btnCancel, listener);
	}

	/**
	 * @return Usuário a ser cadastrado.
	 */
	public FacebookUser getUser() {
		Element userName = DOM.getElementById("ruserName");	
		Element userMail = DOM.getElementById("ruserMail");	
		user.getName();
		InputElement passwd = InputElement.as(DOM.getElementById("rsenha"));
		String password = UserController.encrypt(passwd.getValue());
		user.setName(userName.getInnerHTML());
		user.setEmail(userMail.getInnerHTML());
		user.setPassword(password);
		
		if (isDefaultUser) {
			InputElement cpf = InputElement.as(DOM.getElementById("rcpf"));
			long ncpf = Long.valueOf(cpf.getValue().replaceAll("\\D", ""));
			user.setCpf(ncpf);
			user.setType(UserType.DEFAULT);
		} else{
			user.setCpf(-1);
			user.setType(UserType.FACEBOOK);
		}
		return user;
	}

	

	public void clearUser() {
		Element userImage = DOM.getElementById("ruserImage");
		Element userName = DOM.getElementById("ruserName");
		userImage.setAttribute("src", "");
		userName.setInnerText("");
	}
	
	private HTML getHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder
				.append("<h3 class=\"b-redes\">Use sua conta em redes sociais</h3>");
		htmlBuilder.append("<div>");
		htmlBuilder
				.append("Utilizando a conta de uma rede social, você poderá:");
		htmlBuilder.append("<ul>");
		htmlBuilder
				.append("<li>Compartilhar informações com seus amigos.</li>");
		htmlBuilder.append("</ul>");
		htmlBuilder
				.append("<form id=\"form_redes\" name=\"form_redes\" method=\"post\">");
		htmlBuilder.append("<div class=\"b-redes clearfix\">");
		htmlBuilder.append("<span class=\"esconder\">Facebook</span> ");
		htmlBuilder
				.append("<input type=\"radio\" id=\"b-facebook\" name=\"redes\" value=\"false\" />");
		htmlBuilder
				.append("<label for=\"b-facebook\" title=\"Facebook\" class=\"tooltip\"></label> ");
		 htmlBuilder.append("<span class=\"esconder\">Linkedin</span> ");
		 htmlBuilder.append("<input type=\"radio\" id=\"b-linkedin\" name=\"redes\" value=\"false\" /> ");
		 htmlBuilder.append("<label for=\"b-linkedin\" title=\"Linkedin\" class=\"tooltip\"></label> ");
		 htmlBuilder.append("<span class=\"esconder\">Google</span> ");
		 htmlBuilder.append("<input type=\"radio\" id=\"b-google\" name=\"redes\" value=\"false\" /> ");
		 htmlBuilder.append("<label for=\"b-google\" title=\"Google\" class=\"tooltip\"></label> ");
		 htmlBuilder.append("<span class=\"esconder\">Twitter</span>");
		 htmlBuilder.append("<input type=\"radio\" id=\"b-twitter\" name=\"redes\" value=\"false\" />");
		 htmlBuilder.append("<label for=\"b-twitter\" title=\"Twitter\" class=\"tooltip\"></label> ");
		htmlBuilder.append("</div> ");
		htmlBuilder.append("<div id=\"infoUser\" name=\"infoUser\" class=\"clearfix \" > ");
		htmlBuilder.append("<span id=\"userAlreadyExistis\" ;></span> ");
		htmlBuilder.append("<table>");
		htmlBuilder.append("<tr>");
		htmlBuilder.append("<td>");
		htmlBuilder.append("<img id=\"ruserImage\" src=\"\">");
		htmlBuilder.append("</td>");
		htmlBuilder.append("<td>");
		htmlBuilder.append("<label class=\"netUser \" for=\"rusuario\">   Usuário:   <strong><span  id=\"ruserName\" name=\"ruserName\"></span></strong></label> ");
		htmlBuilder.append("<label class=\"netUser \" for=\"ruserEmail\">   Email:   <strong><span id=\"ruserMail\" name=\"ruserMail\"></span></strong></label> ");
		htmlBuilder.append("</td>");
		htmlBuilder.append("</tr>");
		htmlBuilder.append("</table>");
		htmlBuilder.append("<label  for=\"rsenha\">Minha senha é:</label> ");
		htmlBuilder
				.append("<input type=\"password\" name=\"rsenha\" id=\"rsenha\" placeholder=\"Senha\" class=\"senha\" />");
		htmlBuilder.append("<label for=\"ecsenha\">Confirmar senha:</label>");
		htmlBuilder
				.append("<input type=\"password\" required=\"required\" name=\"ecsenha\" id=\"ecsenha\" placeholder=\"Confirmar Senha\" class=\"senha\"/>");
		htmlBuilder.append("</div> ");
		
		htmlBuilder.append("<h4>Gostaria de se cadastrar no Mapa?</h4>");
		htmlBuilder
				.append("Informando seu CPF você será cadastrado e poderá avaliar as ");
		htmlBuilder
				.append("organizações e definir suas preferências no mapa.");

		htmlBuilder.append("<div class=\"cadastro_cpf clearfix\">");
		htmlBuilder.append("<div>");
		htmlBuilder
				.append("<input type=\"radio\" name=\"rcadastro_cpf\" id=\"rcpf_sim\"");
		htmlBuilder
				.append("value=\"sim\" required=\"required\" class=\"cpf_sim\" checked=\"checked\"/> <label");
		htmlBuilder
				.append("for=\"rcpf_sim\">Sim, meu CPF é:</label> <input type=\"text\"");
		htmlBuilder
				.append("name=\"rcpf\" id=\"rcpf\" placeholder=\"CPF\" maxlength=\"14\"");
		htmlBuilder.append("class=\"cpf\" />");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div>");
		htmlBuilder
				.append("<input type=\"radio\" name=\"rcadastro_cpf\" id=\"rcpf_nao\" ");
		htmlBuilder
				.append("value=\"nao\" required=\"required\" /> <label for=\"rcpf_nao\">Não,");
		htmlBuilder.append("quero apenas efetuar o login</label>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");

		htmlBuilder.append("<div class=\"botoes\">");
		htmlBuilder
				.append("<a href=\"#\" class=\"cancelar\" id=\"btnCancelar\" name=\"btnCancelar\">Cancelar</a> ou ");
		htmlBuilder
				.append("<input type=\"button\" id='btnCadastro' name='btnCadastro' value=\"Entrar\" class=\"botao_entrar\" />");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</form>");
		htmlBuilder.append("</div>");

		HTML html = new HTML(htmlBuilder.toString());
		return html;

	}

	@Override
	protected void onAttach() {
		super.onAttach();
		final Element cpfYes = DOM.getElementById("rcpf_sim");
		final Element cpfNo = DOM.getElementById("rcpf_nao");

		Event.sinkEvents(cpfYes, Event.ONCLICK);
		Event.setEventListener(cpfYes, new EventListener() {
			public void onBrowserEvent(Event event) {
				addValidateCpf();
				isDefaultUser = true;
			}
		});
		Event.sinkEvents(cpfNo, Event.ONCLICK);
		Event.setEventListener(cpfNo, new EventListener() {
			public void onBrowserEvent(Event event) {
				removeValidateCpf();
				isDefaultUser = false;

			}
		});
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

		var validate = $wnd.jQuery('#form_redes').validate({
			ignore : [],
			rules : {
				rsenha : {
					required : true,
					minlength : 6
				},
				ecsenha:{
					required : true,
					equalTo : '#rsenha'
				},				
				rcpf : {
					verificaCPF : true,
					required : true
				}
			},
			messages : {
				rsenha : {
					required : 'Campo obrigatório.',
					minlength : 'Senha muito curta.'
				},
				ecsenha : {
					required : 'Campo obrigatório.',
					equalTo : 'As duas senhas devem ser iguais.'
				},
				rcpf : {
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
		return $wnd.jQuery('#form_redes').valid();
	}-*/;

	/**
	 * Adiciona regra para cpf inválido.
	 * 
	 * @param email
	 */
	public native void addValidateCpf() /*-{
		$wnd.jQuery('#rcpf').rules('add', {
			verificaCPF : true,
			required : true,
			messages : {
				verificaCPF : 'CPF inválido.',
				required : 'Informe seu CPF.'
			}
		});

	}-*/;

	/**
	 * Adiciona regra para cpf inválido.
	 * 
	 * @param email
	 */
	public native void removeValidateCpf() /*-{
		$wnd.jQuery('#rcpf').rules('remove');
	}-*/;

	/**
	 * Adiciona regra para usuário inválido.
	 * 
	 * @param usuario
	 */
	public native void addInvalidUser(String invalidUser) /*-{
		$wnd.jQuery('#rusuario').rules('add', {
			notEqual : invalidUser,
			messages : {
				notEqual : "Usuário não cadastrado no Facebook."
			}
		});
	}-*/;

	/**
	 * Adiciona regra para cpf já cadastrado no mapa.
	 * 
	 * @param email
	 */
	public native void addInvalidCpf(String invalidCpf) /*-{
														$wnd.jQuery('#rcpf').rules('add', {
														notEqualCPF : invalidCpf,
														messages : {
														notEqualCPF : "Este CPF já esta cadastrado no Mapa"
														}
														});
														}-*/;


	public native void closeWidget() /*-{
		$wnd.jQuery('#bRedes').close;
	}-*/;
	
	/**
	 * Adiciona regra para usuário já existente na base de dados.
	 * 
	 */
	public native void addUserAlreadyExists(String uEmail) /*-{
       $wnd.jQuery("#infoUser #userAlreadyExistis").text("Usuário já cadastrado!");
	}-*/;
	
}
