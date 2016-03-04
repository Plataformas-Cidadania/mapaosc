package gov.sgpr.fgv.osc.portalosc.user.client;

import gov.sgpr.fgv.osc.portalosc.user.client.controller.UserController;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;

public class User implements EntryPoint {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private UserController user = new UserController();

	public void onModuleLoad() {
		logger.info("Iniciando carregamento de cadastro de Usuário");
		user.init();
		user.carousel();
	}

}
