package gov.sgpr.fgv.osc.portalosc.staticcontent.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

import gov.sgpr.fgv.osc.portalosc.staticcontent.client.controller.StaticContentController;

/**
 * @author Gabriel
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class StaticContent implements EntryPoint {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private StaticContentController staticController = new StaticContentController();

	/* (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		try{
			staticController.init();
		}catch(Exception e){
			logger.info("Ocoreu um erro na classe " + this.getClass().getName() + ": " + e.getMessage());
			Window.Location.assign(GWT.getHostPageBaseURL() + "error.html");
		}
	}


}
