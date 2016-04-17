package gov.sgpr.fgv.osc.portalosc.map.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.ConfigService;
import gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

/**
 * @author Eric Ferreira
 */

public class ConfigServiceImpl extends RemoteServiceImpl implements ConfigService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1171796036824255570L;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	

	public Boolean isClusterCreated() throws RemoteException {
		logger.info("MapServiceImpl.createdClusters()");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sqlConsult = "SELECT configuration, value FROM syst.tb_mapaosc_config WHERE configuration = 'Created_Clusters';";
		boolean createdClusters = false;
		try {
			pstmt = conn.prepareStatement(sqlConsult);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				createdClusters = Boolean.parseBoolean(rs.getString("value"));
			}
			return createdClusters;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);

		}
	}

}
