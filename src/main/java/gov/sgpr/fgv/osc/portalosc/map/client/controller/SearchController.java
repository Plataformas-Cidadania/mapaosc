package gov.sgpr.fgv.osc.portalosc.map.client.controller;

import gov.sgpr.fgv.osc.portalosc.map.client.components.SearchWidget;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.SearchService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.SearchServiceAsync;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.SearchResult;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.SearchResultType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
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
	private SearchResult searchResult = new SearchResult();
	private Integer limitResult = 5;
	private Integer quantLetter = 0;
	
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
		
		searchWidget.addSearchClickListener(new EventListener() {
			@Override
			public void onBrowserEvent(Event event) {
				String criteria = searchWidget.getValue();
				AsyncCallback<List<SearchResult>> callbackSearch = new AsyncCallback<List<SearchResult>>() {
					
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
					}
					
					public void onSuccess(List<SearchResult> result) {
						if (!result.isEmpty()) {
							if (result.get(0).getType().equals(SearchResultType.STATE)){
								History.newItem("P" + result.get(0).getId());
							}
							if (result.get(0).getType().equals(SearchResultType.COUNTY)){
								History.newItem("P" + result.get(0).getId());
							}
							if (result.get(0).getType().equals(SearchResultType.OSC)){
								History.newItem("O" + result.get(0).getId());
							}
							if (result.get(0).getType().equals(SearchResultType.ADDRESS)){
								History.newItem("A" + result.get(0).getId());
							}
							
							searchWidget.close();
						}
					}
				};
				searchService.search(criteria, LIMIT, callbackSearch);
			}
		});
	}
	
	private void search() {
		String criteria = searchWidget.getValue();
		
		AsyncCallback<List<SearchResult>> callbackSearch = new AsyncCallback<List<SearchResult>>() {
			
			public void onFailure(Throwable caught) {
				logger.log(Level.SEVERE, caught.getMessage());
			}
			
			public void onSuccess(final List<SearchResult> result) {				
				if(result.size() < 5 && searchWidget.getValue().length() > 3 && quantLetter != searchWidget.getValue().length()){
					String address = searchWidget.getValue();
					String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address.replace(" ", "+") + ",brasil";
					
					quantLetter = searchWidget.getValue().length();
					limitResult = 5 - result.size();
					RequestBuilder req = new RequestBuilder(RequestBuilder.GET, url);
					try{
						req.sendRequest(null, new RequestCallback() {
							
							@Override
							public void onResponseReceived(Request request, Response response) {
								List<SearchResult> listResult = new ArrayList<SearchResult>();
								Boolean flag = false;
								for(String s : response.getText().split("\n")){
									if(searchResult.getLongetude() != null) searchResult.setId(listResult.size() + 1);
									searchResult.setType(SearchResultType.ADDRESS);
									if(s.contains("formatted_address")) searchResult.setValue(s.split(": ")[1].split("\",")[0].replace("\"", ""));
									if(s.contains("\"location\" :")) flag = true;
									if(flag){
										if(s.contains("lat")) searchResult.setLatitude(s.split(": ")[1].split(",")[0].replace("\"", ""));
										else if(s.contains("lng")) searchResult.setLongetude(s.split(": ")[1].replace("\"", ""));
										if(searchResult.getLatitude() != null && searchResult.getLongetude() != null) flag = false;
									}
									if(searchResult.getLongetude() != null){
										if(!result.contains(searchResult)) listResult.add(searchResult);
										searchResult = new SearchResult();
									}
									if(listResult.size() > limitResult) break;
								}
								result.addAll(listResult);
								onSuccess(result);
							}
							
							@Override
							public void onError(Request request, Throwable exception) {
								logger.info("Erro na busca de endereço pelo Google");
							}
						});
					}catch(Exception e){
						logger.info("Erro na busca de endereço pelo Google\n" + e);
					}
				}
				
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