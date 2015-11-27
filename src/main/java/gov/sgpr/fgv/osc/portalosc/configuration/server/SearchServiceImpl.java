package gov.sgpr.fgv.osc.portalosc.configuration.server;

import gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.SearchService;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.model.SearchResult;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.model.SearchResultType;
import gov.sgpr.fgv.osc.portalosc.configuration.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.exception.RemoteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

public class SearchServiceImpl extends RemoteServiceImpl implements SearchService {
	private static final long serialVersionUID = 7303151394220679280L;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.SearchService#search
	 * (java.lang.String)
	 */
	public List<SearchResult> search(String criteria, int limit) throws RemoteException {
		List<SearchResult> result = new ArrayList<SearchResult>();

		// Busca por cnpj
		String criteria1 = criteria;
		// criteria1 = criteria1.replaceAll("\\D","");
		criteria1 = criteria1.replace(".", "");
		criteria1 = criteria1.replace("-", "");
		criteria1 = criteria1.replace("/", "");

		if (StringUtils.isNumeric(criteria1.trim())) {
			List<SearchResult> ret = searchOscByCode(Long.valueOf(criteria1),
					limit);
			result.addAll(ret);
			if (result.size() == limit) {
				return result;
			}
		}
		
		// Busca por nome Completo da OSC
		int newLimit = limit - result.size();
		List<SearchResult> ret = searchOscByFullName(criteria, newLimit);
		result.addAll(ret);
		if (result.size() == limit) {
			return result;
		}

		// Busca por nome fantasia da OSC
		newLimit = limit - result.size();
		ret = searchOscByFantasyName(criteria, newLimit);
		result.addAll(ret);
		if (result.size() == limit) {
			return result;
		}

		// Busca por nome da OSC
		newLimit = limit - result.size();
		ret = searchOscByName(criteria, newLimit);
		result.addAll(ret);

		return result;
	}

	private List<SearchResult> searchOscByCode(long code, int limit)
			throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT a.bosc_sq_osc, COALESCE(a.bosc_nm_fantasia_osc, a.bosc_nm_osc) nome  "
				+ "FROM portal.vm_osc_principal a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc)"
				+ "WHERE b.inte_in_ativa = true AND b.inte_in_osc = true AND "
				+ "CAST(a.bosc_nr_identificacao as character varying) like ? ORDER BY a.bosc_nr_identificacao LIMIT ? ";

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			String value = "%" + code + "%";
			pstmt.setString(1, value);
			pstmt.setInt(2, limit);
			rs = pstmt.executeQuery();
			List<SearchResult> result = new ArrayList<SearchResult>();
			while (rs.next()) {
				SearchResult sr = new SearchResult();
				sr.setId(rs.getInt("bosc_sq_osc"));
				sr.setValue(rs.getString("nome"));
				sr.setType(SearchResultType.OSC);
				result.add(sr);
			}
			return result;

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private List<SearchResult> searchOscByFullName(String name, int limit)
			throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT a.bosc_sq_osc, a.bosc_nm_osc  "
				+ "FROM portal.vm_osc_principal a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc)"
				+ "WHERE b.inte_in_ativa = true AND b.inte_in_osc = true AND "
				+ "UPPER(unaccent(a.bosc_nm_osc)) = (unaccent(?)) ORDER BY a.bosc_nm_osc LIMIT ? ";

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
					.replaceAll("[^\\p{ASCII}]", "");
			String value = normalized.toUpperCase();
			pstmt.setString(1, value);
			pstmt.setInt(2, limit);
			rs = pstmt.executeQuery();
			List<SearchResult> result = new ArrayList<SearchResult>();
			while (rs.next()) {
				SearchResult sr = new SearchResult();
				sr.setId(rs.getInt("bosc_sq_osc"));
				sr.setValue(rs.getString("bosc_nm_osc"));
				sr.setType(SearchResultType.OSC);
				result.add(sr);
			}
			return result;

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private List<SearchResult> searchOscByFantasyName(String name, int limit)
			throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT a.bosc_sq_osc, a.bosc_nm_fantasia_osc  "
				+ "FROM portal.vm_osc_principal a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc)"
				+ "WHERE b.inte_in_ativa = true AND b.inte_in_osc = true AND "
				+ "UPPER(unaccent(a.bosc_nm_fantasia_osc)) like ? ORDER BY a.bosc_nm_fantasia_osc LIMIT ? ";

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
					.replaceAll("[^\\p{ASCII}]", "");
			String value = "%" + normalized.toUpperCase() + "%";
			pstmt.setString(1, value);
			pstmt.setInt(2, limit);
			rs = pstmt.executeQuery();
			List<SearchResult> result = new ArrayList<SearchResult>();
			while (rs.next()) {
				SearchResult sr = new SearchResult();
				sr.setId(rs.getInt("bosc_sq_osc"));
				sr.setValue(rs.getString("bosc_nm_fantasia_osc"));
				sr.setType(SearchResultType.OSC);
				result.add(sr);
			}
			return result;

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private List<SearchResult> searchOscByName(String name, int limit)
			throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT a.bosc_sq_osc, a.bosc_nm_osc  "
				+ "FROM portal.vm_osc_principal a JOIN portal.tb_osc_interacao b ON (a.bosc_sq_osc = b.bosc_sq_osc)"
				+ "WHERE b.inte_in_ativa = true AND b.inte_in_osc = true AND "
				+ "UPPER(unaccent(a.bosc_nm_osc)) like (unaccent(?)) ORDER BY a.bosc_nm_osc LIMIT ? ";

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
					.replaceAll("[^\\p{ASCII}]", "");
			String value = "%" + normalized.toUpperCase() + "%";
			pstmt.setString(1, value);
			pstmt.setInt(2, limit);
			rs = pstmt.executeQuery();
			List<SearchResult> result = new ArrayList<SearchResult>();
			while (rs.next()) {
				SearchResult sr = new SearchResult();
				sr.setId(rs.getInt("bosc_sq_osc"));
				sr.setValue(rs.getString("bosc_nm_osc"));
				sr.setType(SearchResultType.OSC);
				result.add(sr);
			}
			return result;

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	

}
