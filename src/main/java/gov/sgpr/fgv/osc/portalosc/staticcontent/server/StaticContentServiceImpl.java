package gov.sgpr.fgv.osc.portalosc.staticcontent.server;

import java.io.IOException;
import java.util.HashMap;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.xml.parsers.*;

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
		String configurationFile = context.getInitParameter("STATIC_CONFIG"); 
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(configurationFile);
			Element root = document.getDocumentElement();
			Array<Element> nodes = root.getChildNodes();
			for(Element element in )
			{
				
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
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
