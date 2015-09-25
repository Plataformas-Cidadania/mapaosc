package gov.sgpr.fgv.osc.portalosc.map.client.components;

import gov.sgpr.fgv.osc.portalosc.map.client.components.model.BreadcrumbItem;

import java.util.LinkedList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class BreadcrumbWidget extends Composite implements ClickHandler {

	private LinkedList<BreadcrumbItem> breadCrumbContent = new LinkedList<BreadcrumbItem>();

	public void addItem(BreadcrumbItem item) {
		if (breadCrumbContent.isEmpty()) {
			BreadcrumbItem root = new BreadcrumbItem();
			root.setItemText("Brasil");
			root.setItemId("0");
			this.breadCrumbContent.add(root);
		}
		this.breadCrumbContent.add(item);
	}

	public void removeItem(BreadcrumbItem item) {
		if (!this.breadCrumbContent.isEmpty()) {
			this.breadCrumbContent.remove(item);
		}
	}

	public void clearBreadcrumb() {
		while (!this.breadCrumbContent.isEmpty())
			this.breadCrumbContent.removeLast();
	}

	public void removeLastItemUntil(BreadcrumbItem item) {
		if (!this.breadCrumbContent.isEmpty()) {
			while (this.breadCrumbContent.lastIndexOf(findItemOnBreadcrumb(item
					.getItemId())) != this.breadCrumbContent.size() - 1)
				this.breadCrumbContent.removeLast();
		}
	}

	public boolean isItemOnBreadcrumb(String id) {
		for (BreadcrumbItem item : this.breadCrumbContent) {
			if (item.getItemId().equals(id))
				return true;
		}
		return false;
	}

	public BreadcrumbItem findItemOnBreadcrumb(String id) {
		for (BreadcrumbItem item : this.breadCrumbContent) {
			if (item.getItemId().equals(id))
				return item;
		}
		return null;
	}

	public void rebuildBreadcrumb(BreadcrumbItem item) {
		if (this.breadCrumbContent.contains(item)) {
			this.breadCrumbContent.remove(item);
		} else {
			this.breadCrumbContent.add(item);
		}

	}

	public HTML getBreadcrumbHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<ul class=\"breadcrumb\">");
		for (BreadcrumbItem entry : this.breadCrumbContent) {
			if (this.breadCrumbContent.lastIndexOf(entry) != this.breadCrumbContent
					.size() - 1)
				htmlBuilder.append("<li><a href=\"#" + entry.getItemId()
						+ "\">" + entry.getItemText()
						+ "</a>&nbsp;&gt;&nbsp;</li>");
			else
				htmlBuilder.append("<li><b><a href=\"#" + entry.getItemId()
						+ "\">" + entry.getItemText() + "</a></b></li>");
		}
		htmlBuilder.append("</ul>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}

	public void onClick(ClickEvent event) {

	}

}
