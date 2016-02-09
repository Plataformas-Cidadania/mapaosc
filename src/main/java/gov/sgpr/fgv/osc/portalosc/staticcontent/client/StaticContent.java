package gov.sgpr.fgv.osc.portalosc.staticcontent.client;

import com.google.gwt.core.client.EntryPoint;

import gov.sgpr.fgv.osc.portalosc.staticcontent.client.controller.StaticContentController;

/**
 * @author Gabriel
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StaticContent implements EntryPoint {
	private StaticContentController staticController = new StaticContentController();

	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		staticController.init();
	}


}
