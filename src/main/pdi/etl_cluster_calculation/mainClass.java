package gov.ipea.mapaosc.clusterCalculation;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import vhmeirelles.gwtGeocluster.model.BoundingBox;
import vhmeirelles.gwtGeocluster.model.Coordinate;
import vhmeirelles.gwtGeocluster.model.GeoCluster;
import vhmeirelles.gwtGeocluster.model.SimpleCluster;

public class main {
	private static ConcurrentNavigableMap<Integer, OscCoordinate> allOscCoordinates = new ConcurrentSkipListMap<Integer, OscCoordinate>();
	private static ConcurrentNavigableMap<Integer, OscCoordinate> activeOscCoordinates = new ConcurrentSkipListMap<Integer, OscCoordinate>();
	private static GeoCluster geoCluster = new GeoCluster();
	private static BoundingBox bbox;
	private static GregorianCalendar date;
	private static WKTReader reader;
	private static String driverName;
	private static String DBMS;
	private static String serverName;
	private static String port;
	private static String databaseName;
	private static String userName;
	private static String password;
	private static int initialClusterZoomLevel;
	private static int clusterGridSize;
	private static int minClusterZoomLevel;
	private static int maxClusterZoomLevel;
	private static int maxClusterZoomLevelCalc;

	public static void main(final String[] args)
			throws ParserConfigurationException, SAXException, IOException, SQLException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		File xml_file = new File("web.xml");

		if (xml_file.isFile() && xml_file.canRead()) {
			Document doc = builder.parse(xml_file);
			Element root = doc.getDocumentElement();
			NodeList nodel = root.getChildNodes();

			for (int a = 0; a < nodel.getLength(); a++) {
				Node node = nodel.item(a);

				if (node instanceof Element) {

					if (((Element) node).getTagName() == "DriverName")
						driverName = ((Element) node).getTextContent();
					else if (((Element) node).getTagName() == "DBMS")
						DBMS = ((Element) node).getTextContent();
					else if (((Element) node).getTagName() == "ServerName")
						serverName = ((Element) node).getTextContent();
					else if (((Element) node).getTagName() == "Port")
						port = ((Element) node).getTextContent();
					else if (((Element) node).getTagName() == "DatabaseName")
						databaseName = ((Element) node).getTextContent();
					else if (((Element) node).getTagName() == "UserName")
						userName = ((Element) node).getTextContent();
					else if (((Element) node).getTagName() == "Password")
						password = ((Element) node).getTextContent();
					else if (((Element) node).getTagName() == "InitialClusterZoomLevel")
						initialClusterZoomLevel = Integer.parseInt(((Element) node).getTextContent());
					else if (((Element) node).getTagName() == "ClusterGridSize")
						clusterGridSize = Integer.parseInt(((Element) node).getTextContent());
					else if (((Element) node).getTagName() == "MinClusterZoomLevel")
						minClusterZoomLevel = Integer.parseInt(((Element) node).getTextContent());
					else if (((Element) node).getTagName() == "MaxClusterZoomLevel")
						maxClusterZoomLevel = Integer.parseInt(((Element) node).getTextContent());
					else if (((Element) node).getTagName() == "MaxClusterZoomLevelCalc")
						maxClusterZoomLevelCalc = Integer.parseInt(((Element) node).getTextContent());
				}

			}
		}
		Connection con = connection(driverName, DBMS, serverName, port, databaseName, userName, password);
		clusterCalculation(minClusterZoomLevel, maxClusterZoomLevelCalc, clusterGridSize, con);
	}

	private static Connection connection(String driverName, String DBMS, String serverName, String port,
			String databaseName, String userName, String password) {
		System.out.println("-------- PostgreSQL " + "JDBC Connection Testing ------------");

		try {

			Class.forName(driverName);

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
			e.printStackTrace();
			return null;

		}

		System.out.println("PostgreSQL JDBC Driver Registered!");

		Connection connection = null;

		try {

			connection = DriverManager.getConnection(
					"jdbc:" + DBMS + "://" + serverName + ":" + port + "/" + databaseName, userName, password);
			return connection;

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;

		}
	}

	private static void clusterCalculation(int minClusterZoomLevel, int maxClusterZoomLevelCalc, int clusterGridSize,
			Connection connection) throws RemoteException, SQLException {

		if (connection != null) {
			System.out.println("Successful connection");

			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sqlConsult = "SELECT configuration, value FROM syst.tb_mapaosc_config WHERE configuration = 'Created_Clusters';";
			boolean createdClusters = false;
			try {
				pstmt = connection.prepareStatement(sqlConsult);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					createdClusters = Boolean.parseBoolean(rs.getString("value"));
				}
			} catch (

			SQLException e)

			{
				System.out.println(e.getMessage());
				throw new RemoteException();
			}

			if (!createdClusters)

			{
				date = new GregorianCalendar();
				System.out.println(date.getTime() + " - Calculation Clusters ...");

				pstmt = null;
				String sqlDrop = "DELETE FROM portal.tb_osc_cluster;";
				String sqlCreate = " CREATE TABLE IF NOT EXISTS portal.tb_osc_cluster(cluster_geometry geometry(Point,4674) NOT NULL, cluster_quantity INT NOT NULL,zoom_level INT NOT NULL, cluster_boundingbox geometry(Polygon,4674) NOT NULL);";
				try {
					pstmt = connection.prepareStatement(sqlDrop + sqlCreate);
					pstmt.execute();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
					throw new RemoteException();
				}

				date = new GregorianCalendar();
				System.out.println(date.getTime() + " - Prepared DB.");

				System.out.println(date.getTime() + " - Add all Coords...");

				pstmt = null;
				rs = null;
				String sql = "SELECT a.bosc_sq_osc, ST_AsText(a.bosc_geometry) wkt, b.inte_in_ativa FROM data.tb_osc a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc) "
						+ "WHERE b.inte_in_osc = true AND a.bosc_geometry is not null";
				try {
					pstmt = connection.prepareStatement(sql);
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
								System.out.println(e.getMessage());
							}
						}
					}

				} catch (SQLException e) {
					System.out.println(e.getMessage());
					throw new RemoteException();
				}

				Set<Coordinate> elements = new HashSet<Coordinate>();

				BoundingBox bbox = new BoundingBox();
				bbox.setBounds(-121.1718766875, -45.64476785916705, 17.958982687499997, 23.966176339963845);
				Set<Coordinate> coords = new HashSet<Coordinate>();

				date = new GregorianCalendar();
				System.out.println(date.getTime() + " - Add all Coords...");

				// ConcurrentNavigableMap<Integer, OscCoordinate> col = true ?
				// allOscCoordinates : activeOscCoordinates;
				ConcurrentNavigableMap<Integer, OscCoordinate> col = activeOscCoordinates;
				for (OscCoordinate coord : col.values()) {
					if (coord.getX() >= bbox.getMinX() && coord.getX() <= bbox.getMaxX()
							&& coord.getY() >= bbox.getMinY() && coord.getY() <= bbox.getMaxY()) {
						coords.add(coord);
					}

				}

				for (int i = minClusterZoomLevel; i <= maxClusterZoomLevelCalc; i++) {
					// for (int i = 4; i < 5; i++) {
					date = new GregorianCalendar();
					System.out.println(date.getTime() + " - Clusters calculation...");

					elements = geoCluster.cluster(coords, clusterGridSize, i);

					date = new GregorianCalendar();
					System.out.println(date.getTime() + " - Insert Clusters in DB...");
					for (Coordinate c : elements) {

						if (c instanceof SimpleCluster) {
							SimpleCluster cluster = (SimpleCluster) c;

							pstmt = null;
							double minX, minY, maxX, maxY = 0;
							String sqlInsert = "INSERT INTO portal.tb_osc_cluster(cluster_geometry,cluster_quantity,zoom_level,cluster_boundingbox)"
									+ "VALUES(ST_GeomFromText(?,4674),?,?,ST_GeomFromText(?,4674))";
							try {
								pstmt = connection.prepareStatement(sqlInsert);
								pstmt.setString(1, cluster.toWKT());
								pstmt.setInt(2, cluster.getQuantity());
								pstmt.setInt(3, i);
								minX = cluster.getBbox().getMinX();
								minY = cluster.getBbox().getMinY();
								maxX = cluster.getBbox().getMaxX();
								maxY = cluster.getBbox().getMaxY();
								pstmt.setString(4, "POLYGON((" + minX + " " + minY + "," + minX + " " + maxY + ","
										+ maxX + " " + maxY + "," + maxX + " " + minY + "," + minX + " " + minY + "))");
								pstmt.execute();
							} catch (SQLException e) {
								System.out.println(e.getMessage());
								throw new RemoteException();
							}

						}

					}

				}
				date = new GregorianCalendar();
				System.out.println(date.getTime() + " - Clusters created in DB.");

				pstmt = null;
				String sql2 = "UPDATE syst.tb_mapaosc_config SET value='true' WHERE configuration = 'Created_Clusters';";

				try {
					pstmt = connection.prepareStatement(sql2);
					pstmt.execute();
				} catch (SQLException e2) {
					System.out.println(e2.getMessage());
					throw new RemoteException();
				} finally {
					pstmt.close();
					pstmt = null;
					connection.close();
				}
				date = new GregorianCalendar();
				System.out.println(date.getTime() + " - Configuration table updated.");

			} else {
				date = new GregorianCalendar();
				System.out.println(date.getTime() + " - Configuration table already updated.");
			}
		}

		else

		{
			date = new GregorianCalendar();
			System.out.println(date.getTime() + " - Failed to make connection!");
		}

	}

}
