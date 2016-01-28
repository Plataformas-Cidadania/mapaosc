package gov.sgpr.fgv.osc.portalosc.staticcontent.server;

import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import gov.sgpr.fgv.osc.portalosc.staticcontent.shared.interfaces.StaticContentService;
import gov.sgpr.fgv.osc.portalosc.staticcontent.shared.model.Content;
import gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

public class StaticContentServiceImpl extends RemoteServiceImpl implements StaticContentService {
	
	private String staticUrl;
	private HashMap<String, Content> map;
	/**
	 * 
	 */

	@Override
	public void init(ServletConfig config)
	{
		super.init(config);
		ServletContext context = getServletContext();
		staticUrl = context.getInitParameter("STATIC_URL");
	}

	@Override
	public Content getContentFromHash(String hash) throws RemoteException 
	{
		Content content = new Content();
		content.setId("2");
		content.setTitle("Acessibilidade");
		content.setUrl(staticUrl+"?option=com_content&view=article&id="+content.getId());
		return content;
	}
}
