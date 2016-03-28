package gov.sgpr.fgv.osc.portalosc.user.client.components;

import gov.sgpr.fgv.osc.portalosc.user.client.controller.UserController;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class PopupChangePassword implements EntryPoint {
	
	private MyPopup popup;
	
	private static class MyPopup extends PopupPanel {
		
	    public MyPopup() {
	      
	      super(true);    
	    
	      setWidget(getHtml());
	      setStyleName("overlay");
	    }
	    
	}
	
	public void onModuleLoad() {
	   popup = new MyPopup();
	   popup.show();
	}
	
	public void addSubmitListener(EventListener listener) {
		Element btnFillIn = DOM.getElementById("esqueci_senha");
		Event.sinkEvents(btnFillIn, Event.ONCLICK);
		Event.setEventListener(btnFillIn, listener);
		validate();
	}
	
	public void addSubmitcsenha(EventListener listener) {
		Element csenha = DOM.getElementById("csenha");
		Element ccsenha = DOM.getElementById("ccsenha");
		Event.sinkEvents(csenha, Event.ONKEYPRESS);
		Event.sinkEvents(ccsenha, Event.ONKEYPRESS);
		Event.setEventListener(csenha, listener);
		Event.setEventListener(ccsenha, listener);
		validate();
	}
	
	public void addStopPropagation(EventListener listener) {
		Element pop = DOM.getElementById("popup");
		Event.sinkEvents(pop, Event.ONCLICK);
		Event.setEventListener(pop, listener);
	}
	
	public void addCancelListener(EventListener listener) {
		Element Overlay = popup.getElement();
		Event.sinkEvents(Overlay, Event.ONCLICK);
		Event.setEventListener(Overlay, listener);
	}

	/**
	 * @return Usuário a ser cadastrado.
	 */
	public String getPassword() {
		InputElement passwd = InputElement.as(DOM.getElementById("csenha"));
		String changePassword = UserController.encrypt(passwd.getValue());
		return changePassword;
	}

	/**
	 * @return Usuário a ser cadastrado.
	 */
	
	private static HTML getHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		
		htmlBuilder.append("<div id='popup' class='alterar_senha pop_up_alert clearfix'>");
		htmlBuilder.append("<h2> Alterar Senha</h2>");
		htmlBuilder.append("<div>");
		htmlBuilder.append("<p>Olá, <br/> por favor preencha os campos para que sua senha seja redefinida.</p>");
		htmlBuilder.append("<form id='form_alterar_senha' name='form_alterar_senha' method='post'>");
		htmlBuilder.append("<div class='small_form'>");
		htmlBuilder.append("<div class='coluna_1'>");
		htmlBuilder.append("<div><label for='csenha'>Nova Senha:</label>");
		htmlBuilder.append("<input type='password' required='required' name='csenha' id='csenha' placeholder='Senha' class='senha'/></div>");
		htmlBuilder.append("<div><label for='ccsenha'>Confirmar Nova Senha:</label>");
		htmlBuilder.append("<input type='password' required='required' name='ccsenha' id='ccsenha' placeholder='Confirmar Senha' class='senha' /></div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div class='coluna_2'><input type='button' id='esqueci_senha' value='Redefinir Senha' style='float:right;' /><div class='clearfix'></div></div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</form>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
		
		HTML html = new HTML(htmlBuilder.toString());
		return html;

	}
	
	public void close(){
		popup.hide();
	}
	
	private native void validate() /*-{

	var validate = $wnd.jQuery('#form_alterar_senha').validate({
		ignore : [],
		rules : {
			csenha : {
				required : true,
				minlength : 6
			},
			ccsenha : {
				required : true,
				equalTo : '#csenha'
			}
		},
		messages : {
			csenha : {
				required : 'Campo obrigatório.',
				minlength : 'Senha muito curta.'
			},
			ccsenha : {
				required : 'Campo obrigatório.',
				equalTo : 'As duas senhas devem ser iguais.'
			}
		},
		
		errorPlacement: function(error, element) {
			var parent = $wnd.jQuery(element).parent();
			console.log(parent); 
			error.appendTo(parent);
			},
		});
	}-*/;
	
	public native boolean isValid() /*-{
		return $wnd.jQuery('#form_alterar_senha').valid();
	}-*/;

}

