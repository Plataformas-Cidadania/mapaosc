package gov.sgpr.fgv.osc.portalosc.staticcontent.shared.model;

import java.io.Serializable;

public class Content implements Serializable {
	/*
	 * 
	 */

	private static final long serialVersionUID = 8064681518753528040L;
	private String title;
	private String id;
	private String url;
	
	public String getId() 
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getTitle() 
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getUrl()
	{
		return this.url;
	}
	
	public void setUrl(String url)
	{
		this.url = url;
	}
}
