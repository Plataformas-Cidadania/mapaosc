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

public class SearchServiceImpl extends RemoteServiceImpl implements SearchService {
	private static final long serialVersionUID = 2944399891133442097L;
	
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
		String criteria1 = criteria;
		
		// criteria1 = criteria1.replaceAll("\\D","");
		criteria1 = criteria1.replace(".", "");
		criteria1 = criteria1.replace("-", "");
		criteria1 = criteria1.replace("/", "");
		
		// Busca por OSC com lexema
		int newLimit = limit - result.size();
		List<SearchResult> ret = searchOscTsquery(criteria, newLimit);
		result.addAll(ret);
		if (result.size() == limit) {
			return result;
		}
		
		// Busca por OSC normal
		newLimit = limit - result.size();
		ret = searchOscNormal(criteria, newLimit);
		result.addAll(ret);
		if (result.size() == limit) {
			return result;
		}
		
		return result;
	}
	
	private List<SearchResult> searchOscTsquery(String name, int limit) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT bosc_sq_osc, COALESCE(bosc_nm_fantasia_osc, bosc_nm_osc) nome "
				   + "FROM portal.search_index "
				   + "WHERE document @@ to_tsquery('portuguese_unaccent', ?) "
				   + "AND ("
				   + "    similarity(bosc_nm_osc, ?) > 0.2 "
				   + "    OR similarity(bosc_nm_fantasia_osc, ?) > 0.2 "
				   + "    OR bosc_nr_identificacao::TEXT = ? "
				   + "    OR bosc_sq_osc::TEXT = ? "
				   + ") " 
				   + "ORDER BY ts_rank(document, to_tsquery('portuguese_unaccent', ?)) DESC "
				   + "LIMIT ?";
		
		try {	
			pstmt = conn.prepareStatement(sql);
			
			String normalized = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll("[^A-Za-z0-9 ]", "");
			while(normalized.startsWith("0")) normalized = normalized.substring(1, normalized.length());
			
			String normalized_to_tsquery = normalized.trim().toLowerCase().replace(" ", "&");
			
			pstmt.setString(1, normalized_to_tsquery);
			pstmt.setString(2, normalized);
			pstmt.setString(3, normalized);
			pstmt.setString(4, normalized);
			pstmt.setString(5, normalized);
			pstmt.setString(6, normalized_to_tsquery);
			pstmt.setInt(7, limit);
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
	
	private List<SearchResult> searchOscNormal(String name, int limit) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT bosc_sq_osc, COALESCE(bosc_nm_fantasia_osc, bosc_nm_osc) nome "
				   + "FROM portal.search_index "
				   + "WHERE UPPER(unaccent(bosc_nm_osc)) ILIKE ? "
				   + "OR UPPER(unaccent(bosc_nm_fantasia_osc)) ILIKE ? "
				   + "ORDER BY GREATEST(similarity(bosc_nm_osc, ?), similarity(bosc_nm_fantasia_osc, ?)) DESC "
				   + "LIMIT ?";
		
		try {	
			pstmt = conn.prepareStatement(sql);
			
			String normalized = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			String value = "%" + normalized.toUpperCase() + "%";
			
			pstmt.setString(1, value);
			pstmt.setString(2, value);
			pstmt.setString(3, normalized);
			pstmt.setString(4, normalized);
			pstmt.setInt(5, limit);
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
}
