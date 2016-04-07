package gov.sgpr.fgv.osc.portalosc.user.server;

import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SearchService;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.SearchResult;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.SearchResultType;
import gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

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
	public List<SearchResult> search(String criteria, Boolean oscConfig, int limit) throws RemoteException {
		List<SearchResult> result = new ArrayList<SearchResult>();
		String criteria1 = criteria;
		
		// criteria1 = criteria1.replaceAll("\\D","");
		criteria1 = criteria1.replace(".", "");
		criteria1 = criteria1.replace("-", "");
		criteria1 = criteria1.replace("/", "");
		
		int newLimit = limit - result.size();
		List<SearchResult> ret = searchRegionByName(criteria, newLimit);
		if(oscConfig){
			// Busca por região
			result.addAll(ret);
			if (result.size() == limit) {
				return result;
			}
			
			// Busca por UF
			newLimit = limit - result.size();
			ret = searchStateByName(criteria, newLimit);
			result.addAll(ret);
			if (result.size() == limit) {
				return result;
			}
			
			// Busca por município
			newLimit = limit - result.size();
			ret = searchCountyByName(criteria, newLimit);
			result.addAll(ret);
			if (result.size() == limit) {
				return result;
			}
		}else{

			// Busca por OSC com lexema
			newLimit = limit - result.size();
			ret = searchOscTsquery(criteria, newLimit);
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
	
	private List<SearchResult> searchRegionByName(String name, int limit) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT edre_cd_regiao, edre_nm_regiao "
				   + "FROM spat.ed_regiao "
				   + "WHERE similarity(edre_nm_regiao, ?) > 0.5 "
				   + "ORDER BY edre_nm_regiao <-> ?"
				   + "LIMIT ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			String normalized = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			pstmt.setString(1, normalized);
			pstmt.setString(2, normalized);
			pstmt.setInt(3, limit);
			rs = pstmt.executeQuery();
			List<SearchResult> result = new ArrayList<SearchResult>();
			while (rs.next()) {
				SearchResult sr = new SearchResult();
				sr.setId(rs.getInt("edre_cd_regiao"));
				sr.setValue(rs.getString("edre_nm_regiao"));
				sr.setType(SearchResultType.REGION);
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
	
	private List<SearchResult> searchStateByName(String name, int limit) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT eduf_cd_uf, eduf_nm_uf "
				   + "FROM spat.ed_uf "
				   + "WHERE similarity(eduf_nm_uf, ?) > 0.5 "
				   + "OR UPPER(eduf_sg_uf) = ? "
				   + "ORDER BY eduf_nm_uf <-> ?"
				   + "LIMIT ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			String normalized = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			pstmt.setString(1, normalized);
			pstmt.setString(2, normalized);
			pstmt.setString(3, normalized);
			pstmt.setInt(4, limit);
			rs = pstmt.executeQuery();
			List<SearchResult> result = new ArrayList<SearchResult>();
			while (rs.next()) {
				SearchResult sr = new SearchResult();
				sr.setId(rs.getInt("eduf_cd_uf"));
				sr.setValue(rs.getString("eduf_nm_uf"));
				sr.setType(SearchResultType.STATE);
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
	
	private List<SearchResult> searchCountyByName(String name, int limit) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT edmu_cd_municipio, edmu_nm_municipio "
				   + "FROM spat.ed_municipio "
				   + "WHERE similarity(edmu_nm_municipio, ?) > 0.5 "
				   + "ORDER BY edmu_nm_municipio <-> ?"
				   + "LIMIT ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			String normalized = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			pstmt.setString(1, normalized);
			pstmt.setString(2, normalized);
			pstmt.setInt(3, limit);
			rs = pstmt.executeQuery();
			List<SearchResult> result = new ArrayList<SearchResult>();
			while (rs.next()) {
				SearchResult sr = new SearchResult();
				sr.setId(rs.getInt("edmu_cd_municipio"));
				sr.setValue(rs.getString("edmu_nm_municipio"));
				sr.setType(SearchResultType.COUNTY);
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
