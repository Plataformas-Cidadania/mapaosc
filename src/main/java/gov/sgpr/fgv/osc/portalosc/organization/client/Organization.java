package gov.sgpr.fgv.osc.portalosc.organization.client;

import gov.sgpr.fgv.osc.portalosc.organization.client.controller.OrganizationController;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;

public class Organization implements EntryPoint {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private OrganizationController configuration = new OrganizationController();
	
	public void onModuleLoad() {
		logger.info("Iniciando carregamento da página da configuração de usuário");
		configuration.init();
	}
}
