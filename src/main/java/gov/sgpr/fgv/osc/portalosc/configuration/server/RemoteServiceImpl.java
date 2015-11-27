package gov.sgpr.fgv.osc.portalosc.configuration.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.dbcp.BasicDataSource;

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
				testMode = Boolean
						.valueOf(context.getInitParameter("TestMode"));
				if (testMode)
					logger.log(Level.INFO, "############# SERVIDOR EM MODO DE TESTE ##########################");
			} catch (Exception ex) {
				String message = "Não foi possível setar o modo de teste. Verifique o WEB.XML.";
				logger.log(Level.SEVERE, message);
			}
			logger.log(Level.INFO, "Inicializando base de dados");
			datasource.setDriverClassName(context
					.getInitParameter("DriverName"));
			datasource.setUsername(context.getInitParameter("UserName"));
			datasource.setPassword(context.getInitParameter("Password"));
			String url = "jdbc:" + context.getInitParameter("DBMS") + "://"
					+ context.getInitParameter("ServerName") + ":"
					+ context.getInitParameter("Port") + "/"
					+ context.getInitParameter("DatabaseName");
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
			//datasource.setValidationQueryTimeout(30000);

			try {
				datasource.getConnection();
				logger.log(Level.INFO, "Conexão " + url
						+ " iniciada com sucesso.");
			} catch (SQLException ex) {
				String message = "Could not find our DataSource in DBManager. We're about to have problems.";
				logger.log(Level.SEVERE, message);

			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	protected Connection getConnection() {
		try {
			return datasource.getConnection();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	protected void releaseConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	protected void releaseConnection(Connection conn, Statement stmt,
			ResultSet rs) {
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
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	protected void releaseConnection(Connection conn, Statement stmt) {
		try {
			stmt.close();
			stmt = null;
			conn.close();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	protected void releaseConnection(Connection conn, PreparedStatement pstmt,
			ResultSet rs) {
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
			logger.log(Level.SEVERE, e.getMessage());
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
			logger.log(Level.SEVERE, e.getMessage());
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
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * @return the testMode
	 */
	public boolean isTestMode() {
		return testMode;
	}

}
