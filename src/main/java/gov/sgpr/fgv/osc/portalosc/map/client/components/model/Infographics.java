package gov.sgpr.fgv.osc.portalosc.map.client.components.model;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author victor
 * 
 *         Classe que define a visão dos infográficos de forma matricial
 */
public class Infographics implements GraphRenderer {
	private String title;
	private GraphRenderer[][] graphs;
	private int rowCount;
	private int columnCount;

	/**
	 * Construtor
	 * 
	 * @param rowCount
	 *            quantidade de linhas
	 * @param columnCount
	 *            quantidade de colunas
	 */
	public Infographics(int rowCount, int columnCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		graphs = new GraphRenderer[rowCount][columnCount];
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void addGraph(int row, int col, GraphRenderer graph) {
		try{
			graphs[row][col] = graph;
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	public GraphRenderer getGraph(int row, int col) {
		return graphs[row][col];
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	
	public Widget getWidget() {
		Grid grid = new Grid(getRowCount(), getColumnCount());
		for (int i = 0; i < getRowCount(); i++) {
			for (int j = 0; j < getColumnCount(); j++) {
				GraphRenderer g = getGraph(i, j);
				if (g != null) {
					grid.setWidget(i, j, g.getWidget());
				}
			}
		}
		return grid;
	}

	
}
