package gov.sgpr.fgv.osc.portalosc.map.client.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
//import java.util.logging.Logger;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.AbstractHasData;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RowCountChangeEvent;

import gov.sgpr.fgv.osc.portalosc.map.shared.model.Place;

/**
 * @author victor Componente gráfico que apresenta os Infográficos na tela.
 */
public class MatrixWidget extends Composite {
	//private Logger logger = Logger.getLogger(this.getClass().getName());
	private Place[] places;
	//private boolean ignoreClick;
	private ListBox indicatorListBox;
	private Grid grid;

	/**
	 * Construtor
	 * @param breadcrumbIndicadores 
	 */
	public MatrixWidget(Place[] places, HTMLPanel breadcrumbIndicadores) {
		this.places = places;
		indicatorListBox = new ListBox();
		initWidget(getHtml(breadcrumbIndicadores));
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		final Element div = DOM.getElementById("matrixBody");
		grid = new Grid(1, 3);
		Set<String> indicators = places[0].getIndicators().keySet();
		// List 
		for (String ind : indicators) {
			indicatorListBox.addItem(ind);
		}
		/*indicatorListBox.addItem("Quantidade de organizações");
		indicatorListBox.addItem("Valor dos Recursos");
		indicatorListBox.addItem("Quantidade de parcerias");
		indicatorListBox.addItem("Quantidade de vínculos");*/
		int count = places.length > 50 ? 50 : places.length;
		indicatorListBox.setVisibleItemCount(count+1);
		String listHeight = (indicators.size() * 30) + "px";
		//String listHeight = (3 * 30) + "px";
		indicatorListBox.setHeight(listHeight);
		grid.setWidget(0, 0, indicatorListBox.asWidget());

		// Table
		String indicator = (String) places[0].getIndicators().keySet()
				.toArray()[0];
		ScrollPanel scrollPanel = new ScrollPanel(getTable(indicator));
		int height = calculateHeight();
		scrollPanel.setHeight(height + "px");
		scrollPanel.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		grid.setWidget(0, 2, scrollPanel);

		HTMLTable.CellFormatter formatter = grid.getCellFormatter();
		formatter.setHorizontalAlignment(0, 2,
				HasHorizontalAlignment.ALIGN_CENTER);
		formatter.setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);

		grid.setHeight(height + "px");
		div.appendChild(grid.getElement());
		div.getStyle().setHeight(height, Unit.PX);
		Window.scrollTo(0, 0);
	}

	private HTML getHtml(HTMLPanel breadcrumbIndicadores) {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<h2> Matriz de Indicadores");
		htmlBuilder.append("</h2>");
		htmlBuilder.append("<div id=\"matrixBody\">");
		htmlBuilder.append(breadcrumbIndicadores);
		htmlBuilder.append("</div>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}

	public void addIndicatorChangeListener(EventListener listener) {
		Event.sinkEvents(indicatorListBox.getElement(), Event.ONCLICK);
		Event.setEventListener(indicatorListBox.getElement(), listener);
	}

	private CellTable<IndicatorTableValue> getTable(String indicator) {
		List<IndicatorTableValue> values = new ArrayList<IndicatorTableValue>();
		double total = 0;
		for (Place place : places) {
			double value = 0.0;
			if (place.getIndicators().containsKey(indicator))
				value = place.getIndicators().get(indicator);
			IndicatorTableValue line = new IndicatorTableValue(place.getId(),
					place.getName(), value);
			values.add(line);
			total += value;
		}
		// IndicatorTableValue line = new IndicatorTableValue(-1, "TOTAL",
		// total);
		// values.add(line);

		final CellTable<IndicatorTableValue> table = new CellTable<IndicatorTableValue>();
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		setupOnePageList(table);

		Column<IndicatorTableValue, SafeHtml> placeColumn = new Column<IndicatorTableValue, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(IndicatorTableValue obj) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String html;
				String url = Window.Location.getHref();
				Integer cod = Integer.parseInt(url.substring(url.indexOf("#") + 2));
				if (obj.placeCode == -1 || obj.placeCode == cod) {
					html = "<strong>" + obj.placeName + "<st)rong>";
				} else {
					html = "<a href=\"#M" + obj.placeCode + "\">"
							+ obj.placeName + "</a>";
				}
				sb.appendHtmlConstant(html);
				return sb.toSafeHtml();
			}
		};

		table.addColumn(placeColumn, "Localização", "TOTAL");

		TextColumn<IndicatorTableValue> valueColumn = new TextColumn<IndicatorTableValue>() {
			@Override
			public String getValue(IndicatorTableValue object) {
				return NumberFormat.getDecimalFormat().format(object.value);
			}
		};
		table.addColumn(valueColumn, indicator, NumberFormat.getDecimalFormat()
				.format(total));
		// Set the total row count. This isn't strictly necessary, but it
		// affects
		// paging calculations, so its good habit to keep the row count up to
		// date.
		table.setRowCount(values.size(), true);

		// Push the data into the widget.
		table.setRowData(0, values);

		return table;
	}

	public static void setupOnePageList(final AbstractHasData<?> cellTable) {
		cellTable.addRowCountChangeHandler(new RowCountChangeEvent.Handler() {
			
			public void onRowCountChange(RowCountChangeEvent event) {
				cellTable.setVisibleRange(new Range(0, event.getNewRowCount()));
			}
		});
	}

	public void updateTable(String indicator) {
		ScrollPanel scrollPanel = new ScrollPanel(getTable(indicator));
		scrollPanel.setHeight(grid.getOffsetHeight() + "px");
		scrollPanel.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		grid.setWidget(0, 2, scrollPanel);
	}

	public String getSelectedIndicator() {
		return indicatorListBox
				.getItemText(indicatorListBox.getSelectedIndex());
	}

	private int calculateHeight() {
		int headerHeight = DOM.getElementById("barra-brasil").getClientHeight()
				+ DOM.getElementById("topo").getClientHeight();
		int footerHeight = DOM.getElementById("rodape").getClientHeight();
		int height = Window.getClientHeight() - headerHeight - footerHeight
				- 100;
		return height;
	}

	private static class IndicatorTableValue {
		private final int placeCode;
		private final String placeName;
		private final double value;

		// private final String percent;
		// private final String rank;

		public IndicatorTableValue(int placeCode, String placeName, double value) {
			this.placeCode = placeCode;
			this.placeName = placeName;
			this.value = value;
			// this.percent = percent;
			// this.rank = rank;
		}
	}

	public static class AnchorCell extends AbstractCell<String> {

		/**
		 * The HTML templates used to render the cell.
		 */
		interface Templates extends SafeHtmlTemplates {
			/**
			 * The template for this Cell, which includes styles and a value.
			 * 
			 * @param styles
			 *            the styles to include in the style attribute of the
			 *            div
			 * @param value
			 *            the safe value. Since the value type is
			 *            {@link SafeHtml}, it will not be escaped before
			 *            including it in the template. Alternatively, you could
			 *            make the value type String, in which case the value
			 *            would be escaped.
			 * @return a {@link SafeHtml} instance
			 */
			@SafeHtmlTemplates.Template("<a href=\"{0}\">{1}</a>")
			SafeHtml messageWithLink(String url, SafeHtml message);
			//SafeHtml cell(SafeStyles styles, SafeHtml value);
		}

		/**
		 * Create a singleton instance of the templates used to render the cell.
		 */
		private static Templates templates = GWT.create(Templates.class);

		@Override
		public void render(Context context, String value, SafeHtmlBuilder sb) {
			/*
			 * Always do a null check on the value. Cell widgets can pass null
			 * to cells if the underlying data contains a null, or if the data
			 * arrives out of order.
			 */
			if (value == null) {
				return;
			}

			// If the value comes from the user, we escape it to avoid XSS
			// attacks.
			SafeHtml safeValue = SafeHtmlUtils.fromString(value);

			// Use the template to create the Cell's html.
			//SafeStyles styles = SafeStylesUtils.forTrustedColor(safeValue.asString());
			String url = safeValue.asString();
			SafeHtml rendered = templates.messageWithLink(url, safeValue);
			sb.append(rendered);
		}
	}

}
