/**
 * 
 */
package gov.sgpr.fgv.osc.portalosc.user.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.dbcp.BasicDataSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author victor
 * 
 */
public abstract class RemoteServiceImpl extends RemoteServiceServlet {

	private static final long serialVersionUID = 356121544812474118L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static BasicDataSource datasource = new BasicDataSource();
	private boolean testMode = false;

	@Override
	public void init(ServletConfig config) {
		try {
			super.init(config);
			ServletContext context = getServletContext();
			logger.log(Level.INFO, context.getServerInfo());
			try {
				testMode = Boolean.valueOf(context.getInitParameter("TestMode"));
				if (testMode)
					logger.log(Level.INFO, "############# SERVIDOR EM MODO DE TESTE ##########################");
			} catch (Exception ex) {
				String message = "Não foi possível setar o modo de teste. Verifique o WEB.XML.";
				logger.log(Level.SEVERE, message);
			}
			logger.log(Level.INFO, "Inicializando base de dados");
			datasource.setDriverClassName(context.getInitParameter("DriverName"));
			datasource.setUsername(context.getInitParameter("UserName"));
			datasource.setPassword(context.getInitParameter("Password"));
			String url = "jdbc:" + context.getInitParameter("DBMS") + "://" + context.getInitParameter("ServerName")
					+ ":" + context.getInitParameter("Port") + "/" + context.getInitParameter("DatabaseName");
			datasource.setUrl(url);
			int max = Integer.valueOf(context.getInitParameter("MaxActiveConnections"));
			int maxIdle = Integer.valueOf(context.getInitParameter("MaxIdleConnections"));
			int minIdle = Integer.valueOf(context.getInitParameter("MinIdleConnections"));
			int maxWait = Integer.valueOf(context.getInitParameter("MaxWaitConnections"));
			int initialSize = Integer.valueOf(context.getInitParameter("InitialPoolSize"));
			datasource.setMaxActive(max);
			datasource.setMaxIdle(maxIdle);
			datasource.setMinIdle(minIdle);
			datasource.setMaxWait(maxWait);
			datasource.setInitialSize(initialSize);
			datasource.setTestWhileIdle(true);
			datasource.setTestOnBorrow(true);
			datasource.setTestOnReturn(false);
			datasource.setValidationQuery("SELECT 1");
			datasource.setTimeBetweenEvictionRunsMillis(5000);
			datasource.setRemoveAbandoned(true);
			datasource.setRemoveAbandonedTimeout(60);
			datasource.setLogAbandoned(true);
			datasource.setMinEvictableIdleTimeMillis(30000);
			// datasource.setValidationQueryTimeout(30000);

			try {
				datasource.getConnection();
				logger.log(Level.INFO, "Conexão " + url + " iniciada com sucesso.");
			} catch (SQLException e) {
				String message = "Could not find our DataSource in DBManager. We're about to have problems.";
				logger.log(Level.SEVERE, message);
				logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: init(ServletConfig config)");
				logger.log(Level.SEVERE, e.getMessage());
				e.printStackTrace();

			}
			copyFile("src/main/webapp/WEB-INF/web.xml", "src/main/webapp/WEB-INF/web-sample.xml");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: init(ServletConfig config)");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
	}

	protected Connection getConnection() {
		try {
			return datasource.getConnection();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: getConnection()");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	protected void releaseConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: releaseConnection(Connection conn)");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
	}

	protected void releaseConnection(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			conn.close();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: releaseConnection(Connection conn, Statement stmt, ResultSet rs)");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
	}

	protected void releaseConnection(Connection conn, Statement stmt) {
		try {
			stmt.close();
			stmt = null;
			conn.close();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: releaseConnection(Connection conn, Statement stmt)");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
	}

	protected void releaseConnection(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			conn.close();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: releaseConnection(Connection conn, PreparedStatement pstmt, ResultSet rs)");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
	}

	protected void releaseConnection(Connection conn, PreparedStatement pstmt) {
		try {
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			conn.close();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: releaseConnection(Connection conn, PreparedStatement pstmt) ");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
	}

	protected void releaseConnection(Connection conn, PreparedStatement pstmt, PreparedStatement pstmt2) {
		try {
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if (pstmt2 != null) {
				pstmt2.close();
				pstmt2 = null;
			}
			conn.close();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: releaseConnection(Connection conn, PreparedStatement pstmt, PreparedStatement pstmt2");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @return the testMode
	 */
	public boolean isTestMode() {
		return testMode;
	}

	/**
	 * @return web-sample.xml
	 */
	@SuppressWarnings("resource")
	private void copyFile(final String source1, final String destination1)
			throws IOException, ParserConfigurationException, SAXException {
		File source = new File(source1);
		File destination = new File(destination1);
		if (destination.exists())
			destination.delete();
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;
		try {
			sourceChannel = new FileInputStream(source).getChannel();
			destinationChannel = new FileOutputStream(destination).getChannel();
			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		} finally {
			if (sourceChannel != null && sourceChannel.isOpen())
				sourceChannel.close();
			if (destinationChannel != null && destinationChannel.isOpen())
				destinationChannel.close();
		}
		alterFile();
	}

	/**
	 * @return web-sample.xml without some configurations
	 */

	private void alterFile() throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(new File("src/main/webapp/WEB-INF/web-sample.xml"));

		try {
			Element root = doc.getDocumentElement();

			// Return the NodeList on a given tag name
			NodeList childNodes = root.getElementsByTagName("context-param");

			for (int index = 0; index < childNodes.getLength(); index++) {
				NodeList subChildNodes = childNodes.item(index).getChildNodes();
				// logger.info("Item 1 - " +
				// subChildNodes.item(1).getTextContent());
				// logger.info("Item 3 - " +
				// subChildNodes.item(3).getTextContent());

				if (subChildNodes.item(1).getTextContent().equals("ServerName"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("Port"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("DatabaseName"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("UserName"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("Password"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("FACEBOOK_APP_ID"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("FACEBOOK_APP_SECRET"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("FACEBOOK_APP_TOKEN"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("CHAVE_CRIPTOGRAFIA"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("SERVER_SMTP"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("SMTP_AUTH"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("SMTP_PORT"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("FROM_ADDRESS"))
					subChildNodes.item(3).setTextContent(" ");
				else if (subChildNodes.item(1).getTextContent().equals("NAME_ADDRESS"))
					subChildNodes.item(3).setTextContent(" ");

			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("src/main/webapp/WEB-INF/web-sample.xml"));
			transformer.transform(source, result);

			logger.info("web-sample.xml was created.");

		} catch (TransformerException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: alterFile()");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
		}
	}

}