package gov.sgpr.fgv.osc.portalosc.organization.client;

import gov.sgpr.fgv.osc.portalosc.organization.client.controller.OrganizationController;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class Organization implements EntryPoint {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private OrganizationController configuration = new OrganizationController();
	
	public void onModuleLoad() {
		logger.info("Iniciando carregamento da página da configuração de usuário");
		try{
			configuration.init();
		}catch(Exception e){
			logger.info("Ocoreu um erro na classe " + this.getClass().getName() + ": " + e.getMessage());
			Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
		}
	}
}
