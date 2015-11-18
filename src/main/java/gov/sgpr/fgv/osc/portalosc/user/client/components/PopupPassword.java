package gov.sgpr.fgv.osc.portalosc.user.client.components;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class PopupPassword implements EntryPoint {
	
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
		Element btnFillIn = DOM.getElementById("enviar");
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
	
	public void addStopPropagation(EventListener listener) {
		Element pop = DOM.getElementById("popup");
		Event.sinkEvents(pop, Event.ONCLICK);
		Event.setEventListener(pop, listener);
	}
	
	public void addCancelListener(EventListener listener) {
		Element btnCancel = DOM.getElementById("cancelar");
		Element Overlay = popup.getElement();
		Event.sinkEvents(btnCancel, Event.ONCLICK);
		Event.sinkEvents(Overlay, Event.ONCLICK);
		Event.setEventListener(btnCancel, listener);
		Event.setEventListener(Overlay, listener);
	}
	
	public String getEmail() {
		InputElement emailElement = InputElement.as(DOM.getElementById("cemail"));
		String email = emailElement.getValue();
		return email;
	}
	
	  
	private static HTML getHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<div  id='popup' class='pop_up_alert clearfix'>");
		htmlBuilder.append("<h2> Esqueceu sua senha?</h2>");
		htmlBuilder.append("<div>");
		htmlBuilder.append("<p>Informe o e-mail cadastrado e lhe enviaremos instruções para redefinir sua senha.</p>");
		htmlBuilder.append("<form id='form_esqueci_senha' method='post'>");
		htmlBuilder.append("<fieldset><label id='labelerror' for='cemail'>E-mail:</label>");
		htmlBuilder.append("<input type='text' name='cemail' id='cemail' placeholder='E-mail' class='email' required='required'/></fieldset>");
		htmlBuilder.append("<div class='botoes'><a id='cancelar' href='#'>Cancelar</a> ou ");
		htmlBuilder.append("<input type='button' name='enviar' id='enviar'  value='Enviar' /></div>");
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
	
	public void addInvalidEmail(){
		
		Element html = DOM.createLabel();
		html.addClassName("error");
		html.setInnerText("Este email não esta cadastrado no Mapa!");
		Element div = DOM.getElementById("labelerror");
		div.getParentElement().addClassName("formError");
		div.appendChild(html);
	}
	
	public void addConfirme(){
		
		Element html = DOM.createLabel();
		html.addClassName("error");
		html.setInnerText("Confirme seu Cadastro!");
		Element div = DOM.getElementById("labelerror");
		div.getParentElement().addClassName("formError");
		div.appendChild(html);
	}

	
	private native void validate() /*-{

	var validate = $wnd.jQuery('#form_esqueci_senha').validate({
		ignore : [],
		rules : {
			cemail : {
					required : true,
					email : true
				}
		},
		messages : {
			cemail : {
					required : 'Campo obrigatório.',
					email : 'Por favor, informe um email válido.'
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
		return $wnd.jQuery('#form_esqueci_senha').valid();
	}-*/;
	
}
