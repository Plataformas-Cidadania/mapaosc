package gov.sgpr.fgv.osc.portalosc.map.client.controller;

import gov.sgpr.fgv.osc.portalosc.map.client.components.SearchWidget;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.SearchService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.SearchServiceAsync;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.SearchResult;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class SearchController {
	private static final int DELAY = 500;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private SearchWidget searchWidget = new SearchWidget();
	private final RootPanel searchDiv = RootPanel.get("busca");
	private SearchServiceAsync searchService = GWT.create(SearchService.class);
	private static int LIMIT = 5;
	private Date changeDate;
	
	public void init() {
		searchDiv.add(searchWidget);
		
		searchWidget.addFocusListener(new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				searchWidget.setValue("");
			}
		});
		
		searchWidget.addChangeListener(new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				if(event.getKeyCode() == KeyCodes.KEY_DOWN || event.getKeyCode() == KeyCodes.KEY_UP){
					if(DOM.getElementById("list1") != null) {
						DOM.getElementById("list1").focus();
					}
				}
				else{
					changeDate = new Date();
					final Date thisDate = changeDate;
					Timer t = new Timer() {
						@Override
						public void run() {
							if (changeDate.equals(thisDate)) {
								search();
							}
						}
					};
					t.schedule(DELAY);
				}
			}
		});
		
//		searchWidget.addSearchClickListener(new EventListener() {
//			@Override
//			public void onBrowserEvent(Event event) {
//				String criteria = searchWidget.getValue();
//				AsyncCallback<List<SearchResult>> callbackSearch = new AsyncCallback<List<SearchResult>>() {
//					
//					public void onFailure(Throwable caught) {
//						logger.log(Level.SEVERE, caught.getMessage());
//					}
//					
//					public void onSuccess(List<SearchResult> result) {
//						if (!result.isEmpty()) {
//							if (result.get(0).getType().equals(SearchResultType.STATE)){
//								History.newItem("P" + result.get(0).getId());
//							}
//							if (result.get(0).getType().equals(SearchResultType.COUNTY)){
//								History.newItem("P" + result.get(0).getId());
//							}
//							if (result.get(0).getType().equals(SearchResultType.OSC)){
//								History.newItem("O" + result.get(0).getId());
//							}
//							
//							searchWidget.close();
//						}
//					}
//				};
//				searchService.search(criteria, LIMIT, callbackSearch);
//			}
//		});
	}
	
	private void search() {
		String criteria = searchWidget.getValue();
		AsyncCallback<List<SearchResult>> callbackSearch = new AsyncCallback<List<SearchResult>>() {
			
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(List<SearchResult> result) {
				searchWidget.setItems(result);
			
				searchWidget.addFocus(new EventListener() {
					@Override
					public void onBrowserEvent(Event event) {
						if (event.getKeyCode() == KeyCodes.KEY_DOWN){
							event.preventDefault();
							searchWidget.count++;
							if(searchWidget.count>searchWidget.result){
								searchWidget.count = 5;
							}
							DOM.getElementById("list"+ searchWidget.count).focus();
						}else{
							if (event.getKeyCode() == KeyCodes.KEY_UP){
								event.preventDefault();
								searchWidget.count--;
								if(searchWidget.count<1){
									searchWidget.count = 1;
								}
								DOM.getElementById("list"+ searchWidget.count).focus();
							}
						}
					}
				});
				searchWidget.addresultBusca(new EventListener() {
					@Override
					public void onBrowserEvent(Event event) {
						searchWidget.close();
					}
				});
			}
		};
		if (!criteria.trim().isEmpty()) {
			searchService.search(criteria, LIMIT, callbackSearch);
		}
	}
	
	/**
	 * @return Instância da busca
	 */
	public SearchController getInstance() {
		return this;
	}
	
	/**
	 * @param isVisible
	 *            indica se a busca deve estar visível ou não.
	 */
	protected void setVisible(boolean isVisible) {
		Visibility visibility = isVisible ? Visibility.VISIBLE : Visibility.HIDDEN;
		searchDiv.getElement().getStyle().setVisibility(visibility);
	}
}