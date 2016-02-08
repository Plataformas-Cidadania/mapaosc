package gov.sgpr.fgv.osc.portalosc.map.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import gov.sgpr.fgv.osc.portalosc.map.client.components.ClusterMarker;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscCoordinate;
import gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;
import vhmeirelles.gwtGeocluster.model.BoundingBox;
import vhmeirelles.gwtGeocluster.model.Cluster;
import vhmeirelles.gwtGeocluster.model.Coordinate;
import vhmeirelles.gwtGeocluster.model.GeoCluster;
import vhmeirelles.gwtGeocluster.model.SimpleCluster;

/**
 * @author victor
 * 
 *         Eric Ferreira Modified Date: 26/01/2016 Change cluster, boundingbox
 *         and coordinate classes to library GeoCluster
 */

public class MapServiceImpl extends RemoteServiceImpl implements MapService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1171796036824255570L;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private ConcurrentNavigableMap<Integer, OscCoordinate> allOscCoordinates = new ConcurrentSkipListMap<Integer, OscCoordinate>();
	private ConcurrentNavigableMap<Integer, OscCoordinate> activeOscCoordinates = new ConcurrentSkipListMap<Integer, OscCoordinate>();
	private ConcurrentNavigableMap<Integer, ConcurrentNavigableMap<Integer, OscCoordinate>> countyCoordinates = new ConcurrentSkipListMap<Integer, ConcurrentNavigableMap<Integer, OscCoordinate>>();
	private ConcurrentNavigableMap<Integer, ConcurrentNavigableMap<Integer, OscCoordinate>> stateCoordinates = new ConcurrentSkipListMap<Integer, ConcurrentNavigableMap<Integer, OscCoordinate>>();
	private ConcurrentNavigableMap<Integer, ConcurrentNavigableMap<Integer, OscCoordinate>> regionCoordinates = new ConcurrentSkipListMap<Integer, ConcurrentNavigableMap<Integer, OscCoordinate>>();
	private boolean running = false;
	private int initialClusterZoomLevel = 7;
	private int clusterGridSize = 100;
	private int minClusterZoomLevel = 4;
	private int maxClusterZoomLevel = 18;
	private boolean flagRefreshClusters = false;
	private int maxClusterZoomLevelCalc = 9;
	GeoCluster geocluster = new GeoCluster();

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.sgpr.fgv.osc.portalosc.map.server.RemoteServiceImpl#init(javax.
	 * servlet .ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) {
		super.init(config);
		running = true;
		createCache();
		running = false;
		ServletContext context = getServletContext();
		initialClusterZoomLevel = Integer.valueOf(context.getInitParameter("InitialClusterZoomLevel"));
		clusterGridSize = Integer.valueOf(context.getInitParameter("ClusterGridSize"));
		minClusterZoomLevel = Integer.valueOf(context.getInitParameter("MinClusterZoomLevel"));
		maxClusterZoomLevel = Integer.valueOf(context.getInitParameter("MaxClusterZoomLevel"));
		flagRefreshClusters = Boolean.valueOf(context.getInitParameter("FlagRefreshClusters"));
		maxClusterZoomLevelCalc = Integer.valueOf(context.getInitParameter("MaxClusterZoomLevelCalc"));
		// clusterCalc();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService#
	 * getOscLocation (int, int)
	 */
	public OscCoordinate[] getOSCCoordinates(int minValue, int maxValue) throws RemoteException {
		// logger.info("MapServiceImpl.getOSCLocations()");
		if (running || allOscCoordinates.isEmpty()) {
			return getOscCoordinatesByRange(minValue, maxValue);
		} else {
			Collection<OscCoordinate> oscLocations = allOscCoordinates.subMap(minValue, maxValue).values();
			return oscLocations.toArray(new OscCoordinate[oscLocations.size()]);
		}
	}

	/**
	 * @param minValue
	 *            limite mínimo
	 * @param maxValue
	 *            limite máximo
	 * @return coleção de oscs
	 * @throws RemoteException
	 */
	private OscCoordinate[] getOscCoordinatesByRange(int minValue, int maxValue) throws RemoteException {
		// logger.info("MapServiceImpl.getOscLocationsByRange()");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.bosc_sq_osc, ST_AsText(a.bosc_geometry) wkt FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc) "
				+ "WHERE b.inte_in_osc = true AND b.inte_in_ativa = true AND a.bosc_geometry is not null LIMIT ? OFFSET ?";
		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, maxValue - minValue);
			pstmt.setInt(2, minValue);
			rs = pstmt.executeQuery();
			Set<OscCoordinate> oscLocations = new HashSet<OscCoordinate>();
			while (rs.next()) {
				int id = rs.getInt("bosc_sq_osc");
				String wkt = rs.getString("wkt");
				if (wkt != null && !wkt.isEmpty()) {
					WKTReader reader = new WKTReader();
					try {
						Point point = (Point) reader.read(wkt);
						OscCoordinate coord = new OscCoordinate();
						coord.setId(id);
						coord.setX(point.getX());
						coord.setY(point.getY());
						oscLocations.add(coord);
					} catch (ParseException e) {
						logger.severe(e.getMessage());
					}
				}
			}
			return oscLocations.toArray(new OscCoordinate[oscLocations.size()]);
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private OscCoordinate[] getCountyCoordinates(int countyCode, int minValue, int maxValue) throws RemoteException {

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.bosc_sq_osc, ST_AsText(a.bosc_geometry) wkt FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON "
				+ "(a.bosc_sq_osc = b.bosc_sq_osc) WHERE b.inte_in_osc = true AND b.inte_in_ativa = true AND a.bosc_geometry is not null "
				+ "AND ST_Contains((SELECT edmu_geometry FROM spat.ed_municipio WHERE edmu_cd_municipio = ?),a.bosc_geometry) = true LIMIT ? OFFSET ?";

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, countyCode);
			pstmt.setInt(2, maxValue - minValue);
			pstmt.setInt(3, minValue);
			rs = pstmt.executeQuery();
			Set<OscCoordinate> oscLocations = new HashSet<OscCoordinate>();
			while (rs.next()) {
				int id = rs.getInt("bosc_sq_osc");
				String wkt = rs.getString("wkt");
				if (wkt != null && !wkt.isEmpty()) {
					WKTReader reader = new WKTReader();
					try {
						Point point = (Point) reader.read(wkt);
						OscCoordinate coord = new OscCoordinate();
						coord.setId(id);
						coord.setX(point.getX());
						coord.setY(point.getY());
						oscLocations.add(coord);
					} catch (ParseException e) {
						logger.severe(e.getMessage());
					}
				}
			}
			return oscLocations.toArray(new OscCoordinate[oscLocations.size()]);
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private OscCoordinate[] getStateCoordinates(int stateCode, int minValue, int maxValue) throws RemoteException {

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.bosc_sq_osc, ST_AsText(a.bosc_geometry) wkt FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON "
				+ "(a.bosc_sq_osc = b.bosc_sq_osc) WHERE b.inte_in_osc = true AND b.inte_in_ativa = true AND a.bosc_geometry is not null "
				+ "AND ST_Contains((SELECT eduf_geometry FROM spat.ed_uf WHERE eduf_cd_uf = ?),a.bosc_geometry) = true LIMIT ? OFFSET ?";

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, stateCode);
			pstmt.setInt(2, maxValue - minValue);
			pstmt.setInt(3, minValue);
			rs = pstmt.executeQuery();
			Set<OscCoordinate> oscLocations = new HashSet<OscCoordinate>();
			while (rs.next()) {
				int id = rs.getInt("bosc_sq_osc");
				String wkt = rs.getString("wkt");
				if (wkt != null && !wkt.isEmpty()) {
					WKTReader reader = new WKTReader();
					try {
						Point point = (Point) reader.read(wkt);
						OscCoordinate coord = new OscCoordinate();
						coord.setId(id);
						coord.setX(point.getX());
						coord.setY(point.getY());
						oscLocations.add(coord);
					} catch (ParseException e) {
						logger.severe(e.getMessage());
					}
				}
			}
			return oscLocations.toArray(new OscCoordinate[oscLocations.size()]);
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private OscCoordinate[] getRegionCoordinates(int regionCode, int minValue, int maxValue) throws RemoteException {

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.bosc_sq_osc, ST_AsText(a.bosc_geometry) wkt FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON "
				+ "(a.bosc_sq_osc = b.bosc_sq_osc) WHERE b.inte_in_osc = true AND b.inte_in_ativa = true AND a.bosc_geometry is not null "
				+ "AND ST_Contains((SELECT eduf_geometry FROM spat.ed_uf WHERE eduf_cd_uf = ?),a.bosc_geometry) = true LIMIT ? OFFSET ?";

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, regionCode);
			pstmt.setInt(2, maxValue - minValue);
			pstmt.setInt(3, minValue);
			rs = pstmt.executeQuery();
			Set<OscCoordinate> oscLocations = new HashSet<OscCoordinate>();
			while (rs.next()) {
				int id = rs.getInt("bosc_sq_osc");
				String wkt = rs.getString("wkt");
				if (wkt != null && !wkt.isEmpty()) {
					WKTReader reader = new WKTReader();
					try {
						Point point = (Point) reader.read(wkt);
						OscCoordinate coord = new OscCoordinate();
						coord.setId(id);
						coord.setX(point.getX());
						coord.setY(point.getY());
						oscLocations.add(coord);
					} catch (ParseException e) {
						logger.severe(e.getMessage());
					}
				}
			}
			return oscLocations.toArray(new OscCoordinate[oscLocations.size()]);
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	/**
	 * @return Quantidade de OSCs na base de dados
	 * @throws RemoteException
	 */
	private int getSize() throws RemoteException {
		// logger.info("MapServiceImpl.getSize()");
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT count(*) size FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc) "
				+ "WHERE b.inte_in_osc = true AND b.inte_in_ativa = true AND a.bosc_geometry is not null";
		// logger.info(sql);
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			int size = 0;
			if (rs.next()) {
				size = rs.getInt("size");
			}
			return size;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, stmt, rs);
		}
	}

	private int getCountySize(int countyCode) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT count(*) size FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc) "
				+ "WHERE b.inte_in_osc = true AND b.inte_in_ativa = true AND a.bosc_geometry is not null "
				+ "AND ST_Contains((SELECT edmu_geometry FROM spat.ed_municipio WHERE edmu_cd_municipio = ?),a.bosc_geometry) = true";
		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, countyCode);
			rs = pstmt.executeQuery();
			int size = 0;
			if (rs.next()) {
				size = rs.getInt("size");
			}
			return size;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private int getStateSize(int stateCode) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT count(*) size FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc) "
				+ "WHERE b.inte_in_osc = true AND b.inte_in_ativa = true AND a.bosc_geometry is not null "
				+ "AND ST_Contains((SELECT eduf_geometry FROM spat.ed_uf WHERE eduf_cd_uf = ?),a.bosc_geometry) = true";
		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, stateCode);
			rs = pstmt.executeQuery();
			int size = 0;
			if (rs.next()) {
				size = rs.getInt("size");
			}
			return size;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private int getRegionSize(int regionCode) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT count(*) size FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc) "
				+ "WHERE b.inte_in_osc = true AND b.inte_in_ativa = true AND a.bosc_geometry is not null "
				+ "AND ST_Contains((SELECT edre_geometry FROM spat.ed_regiao WHERE edre_cd_regiao = ?),a.bosc_geometry) = true";
		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, regionCode);
			rs = pstmt.executeQuery();
			int size = 0;
			if (rs.next()) {
				size = rs.getInt("size");
			}
			return size;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private int getLocationSize(int locationCode) throws RemoteException {
		if (String.valueOf(locationCode).length() == 7)
			return getCountySize(locationCode);
		if (String.valueOf(locationCode).length() == 2)
			return getStateSize(locationCode);
		if (String.valueOf(locationCode).length() == 1)
			return getRegionSize(locationCode);

		// Dispara exceção no caso de código inválido.
		throw new RemoteException("Código de localização inválido");
	}

	private void createCache() throws RemoteException {
		logger.info("MapServiceImpl.createCache()");
		createAllCoordinatesCache();
		logger.info("Cache created....");
	}

	private void createActiveCoordinatesCache() throws RemoteException {
		// logger.info("MapServiceImpl.createAllCoordinatesCache()");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.bosc_sq_osc, ST_AsText(a.bosc_geometry) wkt FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc) "
				+ "WHERE b.inte_in_osc = true AND b.inte_in_ativa = true AND a.bosc_geometry is not null";
		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int idx = 1;
			while (rs.next()) {
				Integer id = rs.getInt("bosc_sq_osc");
				String wkt = rs.getString("wkt");
				if (wkt != null && !wkt.isEmpty()) {
					WKTReader reader = new WKTReader();
					try {
						Point point = (Point) reader.read(wkt);
						OscCoordinate coord = new OscCoordinate();
						coord.setId(id);
						coord.setX(point.getX());
						coord.setY(point.getY());
						activeOscCoordinates.put(idx, coord);
						idx++;
					} catch (ParseException e) {
						logger.severe(e.getMessage());
					}
				}
			}

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private void createAllCoordinatesCache() throws RemoteException {
		// logger.info("MapServiceImpl.createAllCoordinatesCache()");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.bosc_sq_osc, ST_AsText(a.bosc_geometry) wkt, b.inte_in_ativa FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc) "
				+ "WHERE b.inte_in_osc = true AND a.bosc_geometry is not null";
		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int idx = 1;
			while (rs.next()) {
				Integer id = rs.getInt("bosc_sq_osc");
				Boolean active = rs.getBoolean("inte_in_ativa");
				String wkt = rs.getString("wkt");
				if (wkt != null && !wkt.isEmpty()) {
					WKTReader reader = new WKTReader();
					try {
						Point point = (Point) reader.read(wkt);
						OscCoordinate coord = new OscCoordinate();
						coord.setId(id);
						coord.setX(point.getX());
						coord.setY(point.getY());
						allOscCoordinates.put(idx, coord);
						if (active) {
							activeOscCoordinates.put(idx, coord);
						}
						idx++;
					} catch (ParseException e) {
						logger.severe(e.getMessage());
					}
				}
			}

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private void createCountyCoordinatesCache() throws RemoteException {
		// logger.info("MapServiceImpl.createCountyCoordinatesCache()");
		Connection conn = getConnection();
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT edmu_cd_municipio FROM spat.ed_municipio ORDER BY edmu_cd_municipio";
		// logger.info(sql);
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int countyCode = rs.getInt("edmu_cd_municipio");
				createCountyCoordinatesCache(countyCode);
			}
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, stmt, rs);
		}

	}

	private void createStateCoordinatesCache(int stateCode) throws RemoteException {
		// logger.info("MapServiceImpl.createStateCoordinatesCache()");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT edmu_cd_municipio FROM spat.ed_municipio WHERE eduf_cd_uf = ? ORDER BY edmu_cd_municipio";
		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, stateCode);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int countyCode = rs.getInt("edmu_cd_municipio");
				createCountyCoordinatesCache(countyCode);
			}
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}

	}

	private void createCoordinatesCache(int locationCode) throws RemoteException {
		// logger.info("MapServiceImpl.createCoordinatesCache()");
		if (String.valueOf(locationCode).length() == 7) {
			createCountyCoordinatesCache(locationCode);
			return;
		}
		if (String.valueOf(locationCode).length() == 2) {
			createStateCoordinatesCache(locationCode);
			return;
		}
		// Dispara exceção no caso de código inválido.
		throw new RemoteException("Código de localização inválido");
	}

	private void createCountyCoordinatesCache(int countyCode) throws RemoteException {
		// logger.info("MapServiceImpl.createCountyCoordinatesCache(" +
		// countyCode + ")");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.bosc_sq_osc, ST_AsText(a.bosc_geometry) wkt FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON "
				+ "(a.bosc_sq_osc = b.bosc_sq_osc) WHERE b.inte_in_osc = true AND b.inte_in_ativa = true AND a.bosc_geometry is not null "
				+ "AND ST_Contains((SELECT edmu_geometry FROM spat.ed_municipio WHERE edmu_cd_municipio = ?),a.bosc_geometry) = true ";
		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, countyCode);
			rs = pstmt.executeQuery();
			int idx = 1;
			ConcurrentNavigableMap<Integer, OscCoordinate> coordinates = new ConcurrentSkipListMap<Integer, OscCoordinate>();
			while (rs.next()) {
				Integer id = rs.getInt("bosc_sq_osc");
				String wkt = rs.getString("wkt");
				if (wkt != null && !wkt.isEmpty()) {
					WKTReader reader = new WKTReader();
					try {
						Point point = (Point) reader.read(wkt);
						OscCoordinate coord = new OscCoordinate();
						coord.setId(id);
						coord.setX(point.getX());
						coord.setY(point.getY());
						coordinates.put(idx, coord);
						idx++;
					} catch (ParseException e) {
						logger.severe(e.getMessage());
					}
				}
			}
			countyCoordinates.put(countyCode, coordinates);
			int stateCode = countyCode / 100000;
			int regionCode = stateCode / 10;
			ConcurrentNavigableMap<Integer, OscCoordinate> stateCoord = stateCoordinates.get(stateCode);
			if (stateCoord == null) {
				stateCoord = new ConcurrentSkipListMap<Integer, OscCoordinate>();
			}
			stateCoord.putAll(coordinates);
			stateCoordinates.put(stateCode, stateCoord);
			ConcurrentNavigableMap<Integer, OscCoordinate> regionCoord = regionCoordinates.get(regionCode);
			if (regionCoord == null) {
				regionCoord = new ConcurrentSkipListMap<Integer, OscCoordinate>();
			}
			regionCoord.putAll(coordinates);
			regionCoordinates.put(regionCode, regionCoord);

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService#
	 * getOSCLocationSize()
	 */
	public int getOSCCoordinatesSize() throws RemoteException {
		// logger.info("MapServiceImpl.getOSCCoordinatesSize()");
		if (running || allOscCoordinates.isEmpty()) {
			return getSize();
		} else {
			return allOscCoordinates.size();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService#
	 * getOSCCoordinates (int, int, int)
	 */
	public OscCoordinate[] getOSCCoordinates(int locationCode, int minValue, int maxValue) throws RemoteException {
		// logger.info("MapServiceImpl.getOSCCoordinates(locationCode, minValue,
		// maxValue)");
		if (running || countyCoordinates.isEmpty()) {
			if (String.valueOf(locationCode).length() == 7)
				return getCountyCoordinates(locationCode, minValue, maxValue);
			if (String.valueOf(locationCode).length() == 2)
				return getStateCoordinates(locationCode, minValue, maxValue);
			if (String.valueOf(locationCode).length() == 1)
				return getRegionCoordinates(locationCode, minValue, maxValue);

			// Dispara exceção no caso de código inválido.
			throw new RemoteException("Código de localização inválido");
		}
		if (String.valueOf(locationCode).length() == 7) {
			Collection<OscCoordinate> coords = countyCoordinates.get(locationCode).subMap(minValue, maxValue).values();
			return coords.toArray(new OscCoordinate[coords.size()]);
		}
		if (String.valueOf(locationCode).length() == 2) {
			Collection<OscCoordinate> coords = stateCoordinates.get(locationCode).subMap(minValue, maxValue).values();
			return coords.toArray(new OscCoordinate[coords.size()]);
		}

		if (String.valueOf(locationCode).length() == 1) {
			Collection<OscCoordinate> coords = regionCoordinates.get(locationCode).subMap(minValue, maxValue).values();
			return coords.toArray(new OscCoordinate[coords.size()]);
		}

		// Dispara exceção no caso de código inválido.
		throw new RemoteException("Código de localização inválido");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService#
	 * getOSCCoordinatesSize(int)
	 */
	public int getOSCCoordinatesSize(int locationCode) throws RemoteException {
		// logger.info("MapServiceImpl.getOSCCoordinatesSize(locationCode)");
		if (running || countyCoordinates.isEmpty()) {
			return getLocationSize(locationCode);
		}
		if (String.valueOf(locationCode).length() == 7)
			return countyCoordinates.get(locationCode).size();
		if (String.valueOf(locationCode).length() == 2) {
			int size = 0;
			for (int code : countyCoordinates.keySet()) {
				if (String.valueOf(code).substring(0, 2).equals(String.valueOf(locationCode))) {
					size = size + countyCoordinates.get(code).size();
				}
			}
			return size;
		}
		if (String.valueOf(locationCode).length() == 1) {
			int size = 0;
			for (int code : countyCoordinates.keySet()) {
				if (String.valueOf(code).substring(0, 1).equals(String.valueOf(locationCode))) {
					size = size + countyCoordinates.get(code).size();
				}
			}
			return size;
		}
		// Dispara exceção no caso de código inválido.
		throw new RemoteException("Código de localização inválido");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService#
	 * getOSCCoordinates
	 * (gov.sgpr.fgv.osc.portalosc.map.shared.model.BoundingBox, int, int)
	 */
	public Coordinate[] getOSCCoordinates(BoundingBox bbox, int width, int height, boolean all) throws RemoteException {
		// int zoomLevel = getBoundsZoomLevel(bbox, width, height);
		int zoomLevel = geocluster.getBoundsZoomLevel(bbox, width, height);
		return getOSCCoordinates(bbox, zoomLevel, all);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService#
	 * getOSCCoordinates
	 * (gov.sgpr.fgv.osc.portalosc.map.shared.model.BoundingBox, int)
	 */
	public Coordinate[] getOSCCoordinates(BoundingBox bbox, int zoomLevel, boolean all) throws RemoteException {
		Set<Coordinate> elements = new HashSet<Coordinate>();

		if (zoomLevel < initialClusterZoomLevel)
			return elements.toArray(new Coordinate[elements.size()]);

		int gridSize = clusterGridSize;
		ConcurrentNavigableMap<Integer, OscCoordinate> col = all ? allOscCoordinates : activeOscCoordinates;
		if (zoomLevel < minClusterZoomLevel) {
			Cluster cluster = new Cluster();
			cluster.addAll(col.values());
			elements.add(cluster);
		} else {
			Set<Coordinate> coords = new HashSet<Coordinate>();
			coords.addAll(getOSCCoordinates(bbox, all));
			if (zoomLevel > maxClusterZoomLevel)
				return coords.toArray(new Coordinate[coords.size()]);

			// elements = cluster(coords, gridSize, zoomLevel);
			elements = geocluster.cluster(coords, gridSize, zoomLevel);
			clusterCalc(all);
		}

		return elements.toArray(new Coordinate[elements.size()]);
	}

	public Set<OscCoordinate> getOSCCoordinates(BoundingBox bbox, boolean all) throws RemoteException {
		Set<OscCoordinate> coords = new HashSet<OscCoordinate>();
		ConcurrentNavigableMap<Integer, OscCoordinate> col = all ? allOscCoordinates : activeOscCoordinates;
		for (OscCoordinate coord : col.values()) {
			if (coord.getX() >= bbox.getMinX() && coord.getX() <= bbox.getMaxX() && coord.getY() >= bbox.getMinY()
					&& coord.getY() <= bbox.getMaxY()) {
				coords.add(coord);
			}

		}

		return coords;

	}

	public void clusterCalc(boolean all) {
		if (flagRefreshClusters) {

			Connection conn = getConnection();
			PreparedStatement pstmt = null;
			String sqlDrop = "DROP TABLE IF EXISTS portal.tb_osc_cluster;";
			String sqlCreate = " CREATE TABLE portal.tb_osc_cluster("
					+ "cluster_geometry geometry(Point,4674) NOT NULL," + "cluster_quantity INT NOT NULL,"
					+ "zoom_level INT NOT NULL);";
			try {
				pstmt = conn.prepareStatement(sqlDrop + sqlCreate);
				pstmt.execute();
			} catch (SQLException e) {
				logger.severe(e.getMessage());
				throw new RemoteException(e);
			} finally {
				releaseConnection(conn, pstmt);

			}

			Set<Coordinate> elements = new HashSet<Coordinate>();

			for (int i = minClusterZoomLevel; i <= maxClusterZoomLevelCalc; i++) {
				// for (int i = 4; i < 5; i++) {
				BoundingBox bbox = new BoundingBox();
				bbox.setBounds(-135.2343766875, -41.83682747857742, 32.0214826875, 19.062118368308703);
				Set<Coordinate> coords = new HashSet<Coordinate>();
				coords.addAll(getOSCCoordinates(bbox, all));
				elements = geocluster.cluster(coords, clusterGridSize, i);

				for (Coordinate c : elements) {

					if (c instanceof SimpleCluster) {
						SimpleCluster cluster = (SimpleCluster) c;
						logger.info(cluster.toWKT() + " - " + cluster.getQuantity() + " - " + i);

						conn = getConnection();
						pstmt = null;
						String sqlInsert = "INSERT INTO portal.tb_osc_cluster(cluster_geometry,cluster_quantity,zoom_level)"
								+ "VALUES(ST_GeomFromText(?,4674),?,?)";
						try {
							pstmt = conn.prepareStatement(sqlInsert);
							pstmt.setString(1, cluster.toWKT());
							pstmt.setInt(2, cluster.getQuantity());
							pstmt.setInt(3, i);
							pstmt.execute();
						} catch (SQLException e) {
							logger.severe(e.getMessage());
							throw new RemoteException(e);
						} finally {
							releaseConnection(conn, pstmt);
						}

					}

				}

			}

		}

	}
}
