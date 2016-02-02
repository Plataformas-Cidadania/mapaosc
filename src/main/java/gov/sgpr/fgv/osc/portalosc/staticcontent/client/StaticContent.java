package gov.sgpr.fgv.osc.portalosc.staticcontent.client;

import com.google.gwt.core.client.EntryPoint;

import gov.sgpr.fgv.osc.portalosc.staticcontent.client.controller.StaticContentController;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StaticContent implements EntryPoint {
	private StaticContentController staticController = new StaticContentController();

	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad() {
		staticController.init();
	}


}
