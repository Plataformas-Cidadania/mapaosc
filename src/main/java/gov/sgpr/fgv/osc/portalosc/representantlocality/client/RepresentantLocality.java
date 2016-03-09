package gov.sgpr.fgv.osc.portalosc.representantlocality.client;

import gov.sgpr.fgv.osc.portalosc.representantlocality.client.controller.RepresentantLocalityController;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;

public class RepresentantLocality implements EntryPoint {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private RepresentantLocalityController ctrl = new RepresentantLocalityController();

	public void onModuleLoad() {
		logger.info("Iniciando carregamento de cadastro de representante de estados ou munícipios");
		ctrl.init();
	}
}
