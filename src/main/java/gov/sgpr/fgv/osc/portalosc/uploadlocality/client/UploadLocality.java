package gov.sgpr.fgv.osc.portalosc.uploadlocality.client;

import gov.sgpr.fgv.osc.portalosc.uploadlocality.client.controller.UploadLocalityController;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;

public class UploadLocality implements EntryPoint {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private UploadLocalityController ctrl = new UploadLocalityController();

	public void onModuleLoad() {
		logger.info("Iniciando módulo de upload de convênios de estados ou munícipios");
		ctrl.init();
	}
}
