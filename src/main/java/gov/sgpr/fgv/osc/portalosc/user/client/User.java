package gov.sgpr.fgv.osc.portalosc.user.client;

import gov.sgpr.fgv.osc.portalosc.user.client.controller.UserController;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

public class User implements EntryPoint {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private UserController user = new UserController();

	public void onModuleLoad() {
		logger.info("Iniciando carregamento de cadastro de Usuário");
		try{
			user.init();
			Element div = DOM.getElementById("divcarousel");
			if(div != null)
				user.carousel();
		}catch(Exception e){
			logger.info("Ocoreu um erro na classe " + this.getClass().getName() + ": " + e.getMessage());
			Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
		}
	}

}
