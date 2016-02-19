package gov.sgpr.fgv.osc.portalosc.map.shared.model;

import vhmeirelles.gwtGeocluster.model.Coordinate;

/**
 * @author victor
 * Coordenadas de uma OSC
 *
 */
public class OscCoordinate extends Coordinate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7717417398356897560L;
	private int id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof OscCoordinate))
			return false;
		OscCoordinate other = (OscCoordinate) obj;
		if (id != other.id)
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OscCoordinate [id=" + id + ", x=" + super.getX() + ", y=" + super.getY() + "]";
	}


}
