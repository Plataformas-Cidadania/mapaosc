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

/**
 * @author Gabriel
 *
 */
public class StaticContentServiceImpl extends RemoteServiceImpl implements StaticContentService {
	
	private String staticUrl;
	private HashMap<String, Content> map;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	/**
	 * 
	 */

	/* (non-Javadoc)
	 * @see gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl#init(javax.servlet.ServletConfig)
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
				String page = site.getElementsByTagName("page").item(0).getFirstChild().getNodeValue();
				String title = site.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
				String key = site.getElementsByTagName("key").item(0).getFirstChild().getNodeValue();
				String cssClass = site.getElementsByTagName("class").item(0).getFirstChild().getNodeValue();
				Content content = new Content();
				content.setPage(page);
				content.setTitle(title);
				content.setCssClass(cssClass);
				content.setUrl(staticUrl+page);
				map.put(key, content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see gov.sgpr.fgv.osc.portalosc.staticcontent.shared.interfaces.StaticContentService#getContentFromHash(java.lang.String)
	 */
	@Override
	public Content getContentFromParam(String param) throws RemoteException 
	{
		Content content;
		if(map.containsKey(param))
		{
			content = map.get(param);
		}
		else
		{
			content = new Content();
			content.setUrl("");
			content.setTitle("Unknown Page");
			content.setCssClass("");
		}
		return content;
	}
}
