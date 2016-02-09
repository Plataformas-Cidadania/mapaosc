package gov.sgpr.fgv.osc.portalosc.staticcontent.shared.model;

import java.io.Serializable;

/**
 * @author Gabriel
 * Content class used by the XML parser to write data to be passed in the RPC response to the client.
 * The data is composed of a Title, an Id and a URL.
 */
public class Content implements Serializable {
	private String title;
	private String page;
	private String url;
	private String cssClass;
	private static final long serialVersionUID = 8064681518753528040L;
	
	/**
	 * @return the Title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the Title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the Page
	 */
	public String getPage() {
		return page;
	}
	/**
	 * @param page the Page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}
	/**
	 * @return the Url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the Url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}
	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Content [title=" + title + ", id=" + page + ", url=" + url + "]";
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((page == null) ? 0 : page.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Content other = (Content) obj;
		if (page == null) {
			if (other.page != null)
				return false;
		} else if (!page.equals(other.page))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	
	
}
