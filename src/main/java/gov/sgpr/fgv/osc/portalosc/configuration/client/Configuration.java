package gov.sgpr.fgv.osc.portalosc.configuration.client;

import gov.sgpr.fgv.osc.portalosc.configuration.client.controller.ConfigurationController;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class Configuration implements EntryPoint {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private ConfigurationController configuration = new ConfigurationController();
	
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
