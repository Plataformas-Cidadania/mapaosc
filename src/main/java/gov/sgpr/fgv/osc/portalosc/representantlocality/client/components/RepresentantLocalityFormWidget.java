package gov.sgpr.fgv.osc.portalosc.representantlocality.client.components;

import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model.RepresentantLocalityUser;
import gov.sgpr.fgv.osc.portalosc.representantlocality.client.controller.RepresentantLocalityController;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;

import java.util.logging.Logger;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class RepresentantLocalityFormWidget extends Composite {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	
	public RepresentantLocalityFormWidget() {
		initWidget(getHtml());
	}
	
	public RepresentantLocalityFormWidget(RepresentantLocalityUser user) {
		initWidget(getHtml());
		setUser(user);
	}
	
	public void addSubmitListener(EventListener listener) {
		Element btnFillIn = DOM.getElementById("btnCadastro");
		Event.sinkEvents(btnFillIn, Event.ONCLICK);
		Event.setEventListener(btnFillIn, listener);
		validate();
	}
	
	public void addSubmitform(EventListener listener) {
		Element cemail = DOM.getElementById("cemail");
		Element csenha = DOM.getElementById("csenha");
		Element ccsenha = DOM.getElementById("ccsenha");
		Element cnome = DOM.getElementById("cnome");
		Element ccpf = DOM.getElementById("ccpf");
		Element cest = DOM.getElementById("cest");
		Element cmun = DOM.getElementById("cmun");
		Element corgao = DOM.getElementById("corgao");
		Element ccargo = DOM.getElementById("ccargo");
		Element ctel = DOM.getElementById("ctel");
		
		Event.sinkEvents(cemail, Event.ONKEYPRESS);
		Event.sinkEvents(csenha, Event.ONKEYPRESS);
		Event.sinkEvents(ccsenha, Event.ONKEYPRESS);
		Event.sinkEvents(cnome, Event.ONKEYPRESS);
		Event.sinkEvents(ccpf, Event.ONKEYPRESS);
		Event.sinkEvents(cest, Event.ONKEYPRESS);
		Event.sinkEvents(cmun, Event.ONKEYPRESS);
		Event.sinkEvents(corgao, Event.ONKEYPRESS);
		Event.sinkEvents(ccargo, Event.ONKEYPRESS);
		Event.sinkEvents(ctel, Event.ONKEYPRESS);
		
		Event.setEventListener(cemail, listener);
		Event.setEventListener(csenha, listener);
		Event.setEventListener(ccsenha, listener);
		Event.setEventListener(cnome, listener);
		Event.setEventListener(ccpf, listener);
		Event.setEventListener(cest, listener);
		Event.setEventListener(cmun, listener);
		Event.setEventListener(corgao, listener);
		Event.setEventListener(ccargo, listener);
		Event.setEventListener(ctel, listener);
		
		validate();
	}
	
	public void addCancelListener(EventListener listener) {
		Element btnCancel = DOM.getElementById("btnCancelar");
		Event.sinkEvents(btnCancel, Event.ONCLICK);
		Event.setEventListener(btnCancel, listener);
	}
	
	public RepresentantLocalityUser getUser() {
		InputElement email = InputElement.as(DOM.getElementById("cemail"));
		InputElement passwd = InputElement.as(DOM.getElementById("csenha"));
		InputElement name = InputElement.as(DOM.getElementById("cnome"));
		InputElement cpf = InputElement.as(DOM.getElementById("ccpf"));
		SelectElement state = SelectElement.as(DOM.getElementById("cest"));
		SelectElement county = SelectElement.as(DOM.getElementById("cmun"));
		InputElement organ = InputElement.as(DOM.getElementById("corgao"));
		InputElement function = InputElement.as(DOM.getElementById("ccargo"));
		InputElement phone = InputElement.as(DOM.getElementById("ctel"));
		InputElement mailingListMember = InputElement.as(DOM.getElementById("cinscrever"));
		
		RepresentantLocalityUser user = new RepresentantLocalityUser();
		
		user.setEmail(email.getValue());
		user.setPassword(RepresentantLocalityController.encrypt(passwd.getValue()));
		user.setName(name.getValue());
		user.setCpf(Long.valueOf(cpf.getValue().replaceAll("\\D", "")));
		user.setState(state.getValue());
		user.setCounty(county.getValue());
		
		user.setOrgan(organ.getValue());
		user.setFunction(function.getValue());
		user.setPhone(phone.getValue());
		user.setMailingListMember(mailingListMember.getPropertyBoolean("checked"));
		user.setType(UserType.LOCALITY_AGENT);
		
		return user;
	}
	
	public void setUser(RepresentantLocalityUser user) {
		if (DOM.getElementById("ctermo").getPropertyBoolean("checked")){
			InputElement email = InputElement.as(DOM.getElementById("cemail"));
			InputElement passwd = InputElement.as(DOM.getElementById("csenha"));
			InputElement cpasswd = InputElement.as(DOM.getElementById("ccsenha"));
			InputElement name = InputElement.as(DOM.getElementById("cnome"));
			InputElement cpf = InputElement.as(DOM.getElementById("ccpf"));
			SelectElement state = SelectElement.as(DOM.getElementById("cest"));
			SelectElement county = SelectElement.as(DOM.getElementById("cmun"));
			InputElement organ = InputElement.as(DOM.getElementById("corgao"));
			InputElement function = InputElement.as(DOM.getElementById("ccargo"));
			InputElement phone = InputElement.as(DOM.getElementById("ctel"));
			InputElement mailingListMember = InputElement.as(DOM.getElementById("cinscrever"));
			
			email.setValue(user.getEmail());
			passwd.setValue(user.getPassword());
			cpasswd.setValue(user.getPassword());
			name.setValue(user.getName());
			cpf.setValue(String.valueOf(user.getCpf()));
			state.setValue(user.getState());
			county.setValue(user.getCounty());
			organ.setValue(user.getOrgan());
			function.setValue(user.getFunction());
			phone.setValue(user.getPhone());
			mailingListMember.setPropertyBoolean("checked", user.isMailingListMember());
		}
	}
	
	private HTML getHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		
		htmlBuilder.append("<form id='form_cadastro' name='form_cadastro' method='post' style='display: block; clear:both;'>");
		htmlBuilder.append("	<div style='float:left'>");
		htmlBuilder.append("		<label for='cemail'>Email:</label><input type='text' name='cemail' id='cemail'  placeholder='E-mail' class='email' required='required' />");
		htmlBuilder.append("		<label for='csenha'>Senha:</label><input type='password' name='csenha' id='csenha' placeholder='Senha' class='senha' required='required' />");
		htmlBuilder.append("		<label for='ccsenha'>Confirmar Senha:</label><input type='password' name='ccsenha' id='ccsenha' placeholder='Confirmar Senha' required='required' class='senha' />");
		htmlBuilder.append("		<label for='cnome'>Nome:</label><input type='text' name='cnome' id='cnome' placeholder='Nome' class='nome' required='required' />");
		htmlBuilder.append("		<label for='ccpf'>CPF</label><input type='text' name='ccpf' id='ccpf' placeholder='CPF' class='cpf' required='required' />");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("	<div style='float:left; margin-left: 15px;'>");
		htmlBuilder.append("		<label for='cest'>UF:</label><select id='cest' name='cest' required='required'>");
		htmlBuilder.append("											<option disabled selected></option>");
		htmlBuilder.append("											<option value='12'>AC</option>");
		htmlBuilder.append("											<option value='27'>AL</option>");
		htmlBuilder.append("											<option value='13'>AM</option>");
		htmlBuilder.append("											<option value='16'>AP</option>");
		htmlBuilder.append("											<option value='29'>BA</option>");
		htmlBuilder.append("											<option value='23'>CE</option>");
		htmlBuilder.append("											<option value='53'>DF</option>");
		htmlBuilder.append("											<option value='32'>ES</option>");
		htmlBuilder.append("											<option value='52'>GO</option>");
		htmlBuilder.append("											<option value='21'>MA</option>");
		htmlBuilder.append("											<option value='31'>MG</option>");
		htmlBuilder.append("											<option value='50'>MS</option>");
		htmlBuilder.append("											<option value='51'>MT</option>");
		htmlBuilder.append("											<option value='15'>PA</option>");
		htmlBuilder.append("											<option value='25'>PB</option>");
		htmlBuilder.append("											<option value='26'>PE</option>");
		htmlBuilder.append("											<option value='22'>PI</option>");
		htmlBuilder.append("											<option value='41'>PR</option>");
		htmlBuilder.append("											<option value='33'>RJ</option>");
		htmlBuilder.append("											<option value='24'>RN</option>");
		htmlBuilder.append("											<option value='11'>RO</option>");
		htmlBuilder.append("											<option value='14'>RR</option>");
		htmlBuilder.append("											<option value='43'>RS</option>");
		htmlBuilder.append("											<option value='42'>SC</option>");
		htmlBuilder.append("											<option value='28'>SE</option>");
		htmlBuilder.append("											<option value='35'>SP</option>");
		htmlBuilder.append("											<option value='17'>TO</option>");
		htmlBuilder.append("										</select>");
		htmlBuilder.append("		<label for='cmun'>Município:</label><select id='cmun' name='cmun' required='required'>");
			htmlBuilder.append("												<option>Rio de Janeiro</option>");
		htmlBuilder.append("											</select>");
		htmlBuilder.append("		<label for='corgao'>Orgão:</label><input type='text' name='corgao' id='corgao' placeholder='Orgão' class='nome' required='required' />");
		htmlBuilder.append("		<label for='ccargo'>Função:</label><input type='text' name='ccargo' id='ccargo' placeholder='Função' class='nome' required='required' />");
		htmlBuilder.append("		<label for='ctel'>Telefone (com DDD):</label><input type='text' name='ctel' id='ctel' placeholder='(xx) yyyy-yyyy' class='nome' required='required' />");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("	<div class='clearfix' style='clear:both; float: none; width: auto; text-align: center; margin:10px'>");
		htmlBuilder.append("		<span>Concordo com os <a href='termos.html'>termos de uso</a></span><input type='checkbox' name='cinscrever' id='cinscrever' /> ");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("	<div class='botoes' style='clear:both; float: none; width: auto; text-align: center; margin:10px'>");
		htmlBuilder.append("		<div style='display: inline-block; width:auto; float: none; '>");
		htmlBuilder.append("			<a href='#' class='cancelar'>Cancelar</a> ou <input style='background-position: -2px -57px;' type='submit' id='btnCadastro' value='Finalizar cadastro' />");
		htmlBuilder.append("		</div>");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("</form>");
		
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
				},
				ctermo : {
					required : true
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
				},
				ctermo : {
					required : 'Campo obrigatório.'
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
