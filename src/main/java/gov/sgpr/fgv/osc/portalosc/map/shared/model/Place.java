package gov.sgpr.fgv.osc.portalosc.map.shared.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import vhmeirelles.gwtGeocluster.model.BoundingBox;
import vhmeirelles.gwtGeocluster.model.Coordinate;

/**
 * Classe que representa um lugar.
 * 
 * @author victor
 * Eric Ferreira
 * Modified Date: 26/01/2016
 * Change cluster and boundingbox classes to library GeoCluster
 */
public class Place implements Serializable, Comparable<Place> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2663573816831680686L;

	int id;
	String name;
	Coordinate centroid;
	BoundingBox boundingBox;
	
	Map<String, Double> indicators = new HashMap<String, Double>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Coordinate getCentroid() {
		return centroid;
	}

	public void setCentroid(Coordinate centroid) {
		this.centroid = centroid;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	public Map<String, Double> getIndicators() {
		return indicators;
	}

	public void addIndicator(String key, double value) {
		this.indicators.put(key, value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((boundingBox == null) ? 0 : boundingBox.hashCode());
		result = prime * result
				+ ((centroid == null) ? 0 : centroid.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((indicators == null) ? 0 : indicators.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Place other = (Place) obj;
		if (boundingBox == null) {
			if (other.boundingBox != null)
				return false;
		} else if (!boundingBox.equals(other.boundingBox))
			return false;
		if (centroid == null) {
			if (other.centroid != null)
				return false;
		} else if (!centroid.equals(other.centroid))
			return false;
		if (id != other.id)
			return false;
		if (indicators == null) {
			if (other.indicators != null)
				return false;
		} else if (!indicators.equals(other.indicators))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Place [id=" + id + ", name=" + name + ", centroid=" + centroid
				+ ", boundingBox=" + boundingBox + ", indicators=" + indicators
				+ "]";
	}

	public int compareTo(Place o) {
		return this.getName().compareTo(o.getName());
	}

}
