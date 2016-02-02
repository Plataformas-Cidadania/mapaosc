package gov.sgpr.fgv.osc.portalosc.staticcontent.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.*;

import gov.sgpr.fgv.osc.portalosc.staticcontent.shared.interfaces.StaticContentService;
import gov.sgpr.fgv.osc.portalosc.staticcontent.shared.model.Content;
import gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

public class StaticContentServiceImpl extends RemoteServiceImpl implements StaticContentService {
	
	private String staticUrl;
	private HashMap<String, Content> map;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	/**
	 * 
	 */

	@Override
	public void init(ServletConfig config)
	{
		super.init(config);
		ServletContext context = getServletContext();
		staticUrl = context.getInitParameter("STATIC_URL");
		String configurationFile = context.getRealPath("/"+context.getInitParameter("STATIC_CONFIG")); 
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		map = new HashMap<String, Content>();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(configurationFile);
			Element root = document.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("site");
			for(int i = 0; i < nodes.getLength(); i++)
			{
				Element site = (Element)nodes.item(i);
				String id = site.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
				String title = site.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
				String hash = site.getElementsByTagName("hash").item(0).getFirstChild().getNodeValue();
				Content content = new Content();
				content.setId(id);
				content.setTitle(title);
				content.setUrl(staticUrl+"?option=com_content&view=article&id="+id);
				map.put(hash, content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Content getContentFromHash(String hash) throws RemoteException 
	{
		Content content;
		if(map.containsKey(hash))
		{
			content = map.get(hash);
		}
		else
		{
			content = new Content();
			content.setUrl("");
			content.setTitle("Unknown Page");
		}
		return content;
	}
}
