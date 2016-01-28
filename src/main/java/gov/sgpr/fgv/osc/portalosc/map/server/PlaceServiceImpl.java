package gov.sgpr.fgv.osc.portalosc.map.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceService;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.Place;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.PlaceType;
import gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;
import vhmeirelles.gwtGeocluster.model.BoundingBox;
import vhmeirelles.gwtGeocluster.model.Coordinate;

/**
 * Implementação do Serviço que busca informações sobre as divisões geopoliticas
 * 
 * @author victor
 * Eric Ferreira
 * Modified Date: 26/01/2016
 * Change cluster and boundingbox classes to library GeoCluster
 */
public class PlaceServiceImpl extends RemoteServiceImpl implements PlaceService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8016262116480184375L;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private ConcurrentNavigableMap<Integer, Place> counties = new ConcurrentSkipListMap<Integer, Place>();
	private ConcurrentNavigableMap<Integer, Place> states = new ConcurrentSkipListMap<Integer, Place>();
	private ConcurrentNavigableMap<Integer, Place> regions = new ConcurrentSkipListMap<Integer, Place>();
	private boolean running = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.server.RemoteServiceImpl#init(javax.servlet
	 * .ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) {
		logger.info("PlaceServiceImpl.init()");
		super.init(config);
		running = true;
		createCache();
		running = false;
		logger.info("Cache finalizado.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceService#getPlaces
	 * (int)
	 */
	public Place[] getPlaces(int placeId) throws RemoteException {
		logger.info("PlaceServiceImpl.getPlaces()");
		while (running) {
			// do nothing
		}
		if (String.valueOf(placeId).length() == 7) {
			Place[] place = new Place[1];
			place[0] = counties.get(placeId);
			return place;
		}
		if (String.valueOf(placeId).length() == 2) {
			return getCounties(placeId);
		}

		if (String.valueOf(placeId).length() == 1) {
			return getStates(placeId);
		}
		// Dispara exceção no caso de código inválido.
		throw new RemoteException("Código de localização inválido");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceService#getPlaces
	 * (gov.sgpr.fgv.osc.portalosc.map.shared.model.PlaceType)
	 */
	public Place[] getPlaces(PlaceType type) throws RemoteException {
		while (running) {
			// do nothing
		}
		SortedSet<Place> places = new TreeSet<Place>();
		switch (type) {
		case REGION:
			places.addAll(regions.values());
			return places.toArray(new Place[places.size()]);
		case STATE:
			places.addAll(states.values());
			return places.toArray(new Place[places.size()]);
		case COUNTY:
			places.addAll(counties.values());
			return places.toArray(new Place[places.size()]);
		default:
			String message = "Tipo de lugar inválido ou não implementado.";
			logger.log(Level.SEVERE, message);
			throw new RemoteException(message);
		}

	}

	public SortedMap<String, Integer> getOsc(int placeId, boolean all)
			throws RemoteException {
		if (String.valueOf(placeId).length() == 7) {
			return getCountyOsc(placeId, all);
		}
		if (String.valueOf(placeId).length() == 2) {
			throw new RemoteException(
					"Método de busca por UF ainda não implementado");
		}

		if (String.valueOf(placeId).length() == 1) {
			throw new RemoteException(
					"Método de busca por Região ainda não implementado");
		}
		// Dispara exceção no caso de código inválido.
		throw new RemoteException("Código de localização inválido");
	}

	private Place[] getCounties(int stateId) {
		SortedSet<Place> places = new TreeSet<Place>();
		for (int id : counties.keySet()) {
			int code = id / 100000;
			if (code == stateId) {
				places.add(counties.get(id));
			}
		}
		return places.toArray(new Place[places.size()]);
	}

	private Place[] getStates(int regionId) {
		SortedSet<Place> places = new TreeSet<Place>();
		for (int id : states.keySet()) {
			int code = id / 10;
			if (code == regionId) {
				places.add(states.get(id));
			}
		}
		return places.toArray(new Place[places.size()]);
	}

	private void createCache() {
		cacheRegions();
		cacheStates();
		cacheCounties();

	}

	private void cacheCounties() {
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.edmu_cd_municipio, a.edmu_nm_municipio, ST_AsText(a.edmu_centroid) centroid, "
				+ "ST_AsText(a.edmu_bounding_box) bounding_box, c.indi_nm_indicador, b.ftmu_vl_valor "
				+ "FROM spat.ed_municipio a JOIN olap.ft_municipio b ON (a.edmu_cd_municipio = b.edmu_cd_municipio) "
				+ "JOIN olap.tb_indicador c ON (c.indi_cd_indicadores = b.indi_cd_indicadores) "
				+ "ORDER BY a.edmu_cd_municipio ";
		// logger.info(sql);
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			WKTReader reader = new WKTReader();
			while (rs.next()) {
				int id = rs.getInt("edmu_cd_municipio");
				String indicatorName = rs.getString("indi_nm_indicador");
				double indicatorValue = rs.getDouble("ftmu_vl_valor");
				if (counties.containsKey(id)) {
					Place place = counties.get(id);
					place.addIndicator(indicatorName, indicatorValue);
					counties.put(id, place);
					continue;
				}
				Place place = new Place();
				place.setId(id);
				place.setName(rs.getString("edmu_nm_municipio"));
				String centroidWKT = rs.getString("centroid");
				String bboxWKT = rs.getString("bounding_box");
				try {
					Point point = (Point) reader.read(centroidWKT);
					Coordinate centroid = new Coordinate();
					centroid.setX(point.getX());
					centroid.setY(point.getY());
					place.setCentroid(centroid);
					Polygon rectangle = (Polygon) reader.read(bboxWKT);
					Envelope envelope = rectangle.getEnvelopeInternal();
					BoundingBox bbox = new BoundingBox();
					bbox.setBounds(envelope.getMinX(), envelope.getMinY(),
							envelope.getMaxX(), envelope.getMaxY());
					place.setBoundingBox(bbox);
				} catch (ParseException e) {
					logger.severe(e.getMessage());
				}
				place.addIndicator(indicatorName, indicatorValue);
				counties.put(id, place);
			}
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, stmt, rs);
		}

	}

	private void cacheStates() {
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.eduf_cd_uf, a.eduf_nm_uf, ST_AsText(a.eduf_centroid) centroid, "
				+ "ST_AsText(a.eduf_bounding_box) bounding_box, c.indi_nm_indicador, b.ftuf_vl_valor  "
				+ "FROM spat.ed_uf a JOIN olap.ft_uf b ON (a.eduf_cd_uf = b.eduf_cd_uf) "
				+ "JOIN olap.tb_indicador c ON (c.indi_cd_indicadores = b.indi_cd_indicadores) "
				+ "ORDER BY a.eduf_cd_uf";
		// logger.info(sql);
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			WKTReader reader = new WKTReader();
			while (rs.next()) {
				int id = rs.getInt("eduf_cd_uf");
				String indicatorName = rs.getString("indi_nm_indicador");
				double indicatorValue = rs.getDouble("ftuf_vl_valor");
				if (states.containsKey(id)) {
					Place place = states.get(id);
					place.addIndicator(indicatorName, indicatorValue);
					states.put(id, place);
					continue;
				}
				Place place = new Place();
				place.setId(id);
				place.setName(rs.getString("eduf_nm_uf"));
				String centroidWKT = rs.getString("centroid");
				String bboxWKT = rs.getString("bounding_box");
				try {
					Point point = (Point) reader.read(centroidWKT);
					Coordinate centroid = new Coordinate();
					centroid.setX(point.getX());
					centroid.setY(point.getY());
					place.setCentroid(centroid);
					Polygon rectangle = (Polygon) reader.read(bboxWKT);
					Envelope envelope = rectangle.getEnvelopeInternal();
					BoundingBox bbox = new BoundingBox();
					bbox.setBounds(envelope.getMinX(), envelope.getMinY(),
							envelope.getMaxX(), envelope.getMaxY());
					place.setBoundingBox(bbox);
				} catch (ParseException e) {
					logger.severe(e.getMessage());
				}
				place.addIndicator(indicatorName, indicatorValue);
				states.put(id, place);
			}
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, stmt, rs);
		}

	}

	private void cacheRegions() {
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.edre_cd_regiao, a.edre_nm_regiao, ST_AsText(a.edre_centroid) centroid, "
				+ "ST_AsText(a.edre_bounding_box) bounding_box,  c.indi_nm_indicador, b.ftre_vl_valor  "
				+ "FROM spat.ed_regiao a JOIN olap.ft_regiao b ON (a.edre_cd_regiao = b.edre_cd_regiao) "
				+ "JOIN olap.tb_indicador c ON (c.indi_cd_indicadores = b.indi_cd_indicadores) ORDER BY a.edre_cd_regiao";
		// logger.info(sql);
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			WKTReader reader = new WKTReader();
			while (rs.next()) {
				int id = rs.getInt("edre_cd_regiao");
				String indicatorName = rs.getString("indi_nm_indicador");
				double indicatorValue = rs.getDouble("ftre_vl_valor");
				if (regions.containsKey(id)) {
					Place place = regions.get(id);
					place.addIndicator(indicatorName, indicatorValue);
					regions.put(id, place);
					continue;
				}
				Place place = new Place();
				place.setId(id);
				place.setName(rs.getString("edre_nm_regiao"));
				String centroidWKT = rs.getString("centroid");
				String bboxWKT = rs.getString("bounding_box");
				try {
					Point point = (Point) reader.read(centroidWKT);
					Coordinate centroid = new Coordinate();
					centroid.setX(point.getX());
					centroid.setY(point.getY());
					place.setCentroid(centroid);
					Polygon rectangle = (Polygon) reader.read(bboxWKT);
					Envelope envelope = rectangle.getEnvelopeInternal();
					BoundingBox bbox = new BoundingBox();
					bbox.setBounds(envelope.getMinX(), envelope.getMinY(),
							envelope.getMaxX(), envelope.getMaxY());
					place.setBoundingBox(bbox);
				} catch (ParseException e) {
					logger.severe(e.getMessage());
				}
				place.addIndicator(indicatorName, indicatorValue);
				regions.put(id, place);
			}
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, stmt, rs);
		}
	}

	private SortedMap<String, Integer> getCountyOsc(int countyCode, boolean all)
			throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder
				.append("SELECT a.bosc_sq_osc, a.bosc_nm_osc, a.bosc_nm_fantasia_osc ");
		htmlBuilder
				.append("FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc) ");
		htmlBuilder.append("WHERE b.inte_in_osc = true ");
		if (!all) {
			htmlBuilder.append("AND b.inte_in_ativa = true ");
		}
		htmlBuilder.append("AND a.bosc_geometry is not null ");
		htmlBuilder
				.append("AND ST_Contains((SELECT edmu_geometry FROM spat.ed_municipio WHERE edmu_cd_municipio = ?), a.bosc_geometry) = true ");

		String sql = htmlBuilder.toString();

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, countyCode);
			rs = pstmt.executeQuery();
			SortedMap<String, Integer> values = new TreeMap<String, Integer>();
			while (rs.next()) {
				Integer id = rs.getInt("bosc_sq_osc");
				String name = rs.getString("bosc_nm_fantasia_osc");
				if (name == null || name.isEmpty())
					name = rs.getString("bosc_nm_osc");
				values.put(name, id);
			}
			return values;

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	public Place getPlaceInfo(int placeId) throws RemoteException {
		while (running) {
			// do nothing
		}
		Place place = new Place();
		if (String.valueOf(placeId).length() == 7) {
			place = counties.get(placeId);
			return place;
		}
		if (String.valueOf(placeId).length() == 2) {
			place = states.get(placeId);
			return place;
		}
		if (String.valueOf(placeId).length() == 1) {
			place = regions.get(placeId);
			return place;
		}
		// Dispara exceção no caso de código inválido.
		throw new RemoteException("Código de localização inválido");
	}

	public Place[] getPlaceAncestorsInfo(int placeId) throws RemoteException {
		while (running) {
			// do nothing
		}
		List<Place> placeHistory = new LinkedList<Place>();
		if (String.valueOf(placeId).length() == 7) {
			placeHistory.add(regions.get(Integer.parseInt(String.valueOf(
					placeId).substring(0, 1))));
			placeHistory.add(states.get(Integer.parseInt(String
					.valueOf(placeId).substring(0, 2))));
			placeHistory.add(counties.get(placeId));

			return placeHistory.toArray(new Place[0]);
		}
		if (String.valueOf(placeId).length() == 2) {
			placeHistory.add(regions.get(Integer.parseInt(String.valueOf(
					placeId).substring(0, 1))));
			placeHistory.add(states.get(placeId));
			return placeHistory.toArray(new Place[0]);
		}
		if (String.valueOf(placeId).length() == 1) {
			placeHistory.add(regions.get(placeId));
			return placeHistory.toArray(new Place[0]);
		}
		// Dispara exceção no caso de código inválido.
		throw new RemoteException("Código de localização inválido");
	}

}
