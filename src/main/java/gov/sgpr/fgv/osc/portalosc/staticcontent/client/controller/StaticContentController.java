package gov.sgpr.fgv.osc.portalosc.staticcontent.client.controller;

import java.util.logging.Logger;

import com.google.gwt.dom.client.*;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

public class StaticContentController 
{
	private static Logger logger = Logger.getLogger(StaticContentController.class.getName());
	private static String staticUrl = "Endereço para o Joomla";
	
	public void init() 
	{
		String hash = Window.Location.getHash();
		loadStaticContent(parseHash(hash), "static_content");
	}
	
	public void loadStaticContent(String page, String element_id) 
	{
		final Element d = DOM.getElementById(element_id);
		if(d != null)
		{
			final Element s = DOM.createSpan();
			s.setInnerText("Ocorreu um erro de comunicação com o servidor");
			String url = staticUrl+page;
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));
			try {
				Request request = builder.sendRequest(null, new RequestCallback() {
					public void onError(Request request, Throwable exception) {
						logger.info("Falha no carregamento do conteúdo estático");
						d.appendChild(s);
					}

					public void onResponseReceived(Request request, Response response) {
						if (200 == response.getStatusCode()) {
							logger.info("Carregando conteúdo estático do Joomla");
							d.setInnerHTML(response.getText());
						} else {
							logger.info("Falha no carregamento do conteúdo estático");
							d.appendChild(s);
						}
					}
				});
			} catch (RequestException e) {
				logger.info("Falha no carregamento do conteúdo estático");
				d.appendChild(s);
			}
		}
	}
	
	public String parseHash(String hash)
	{
		return "2";
	}
}
