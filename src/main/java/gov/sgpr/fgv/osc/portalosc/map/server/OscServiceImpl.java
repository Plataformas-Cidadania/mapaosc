package gov.sgpr.fgv.osc.portalosc.map.server;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.Certifications;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.Committees;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.DataSource;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.DocumentType;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscCoordinate;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscDetail;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscMain;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscSummary;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.PublicResources;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.WorkRelationship;
import gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

/**
 * Implementação do Serviço que busca informações sobre as OSCs
 * 
 * @author victor
 * 
 */
public class OscServiceImpl extends RemoteServiceImpl implements OscService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6874676191762153756L;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private String documentPath;
	private String byLawPath;
	private String directorsBoardPath;
	private String treatyPath;
	private String accountabilityPath;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.server.RemoteServiceImpl#init(javax.servlet
	 * .ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) {
		super.init(config);
		// logger.info("OscServiceImpl.init()");
		ServletContext context = getServletContext();
		documentPath = context.getInitParameter("PastaDocumentos");
		byLawPath = context.getInitParameter("PastaEstatutos");
		directorsBoardPath = context.getInitParameter("PastaQuadroDirigentes");
		treatyPath = context.getInitParameter("PastaConvenios");
		accountabilityPath = context.getInitParameter("PastaPrestacaoContas");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OSCService#getSummary
	 * (int)
	 */
	/*public OscSummary getSummary(int oscId) throws RemoteException {
		
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT a.bosc_nm_osc, a.bosc_nm_fantasia_osc, e.suas_dt_ano_fundacao, "
				+ "count(f.cons_cd_conselho) qt_conselhos , h.dcte_ds_tamanho_estabelecimento, i.dcnj_nm_natureza_juridica, "
				+ "(d.siconv_qt_parceria_finalizada + d.siconv_qt_parceria_execucao)  qt_parceria, d.siconv_vl_global, "
				+ "g.lic_vl_aprovado "
				+ "FROM data.tb_osc a "
				+ "LEFT JOIN data.tb_osc_rais b ON (a.bosc_sq_osc = b.bosc_sq_osc) "
				+ "LEFT JOIN portal.tb_osc_interacao c ON (a.bosc_sq_osc = c.bosc_sq_osc) "
				+ "LEFT JOIN data.tb_osc_siconv d ON (a.bosc_sq_osc = d.bosc_sq_osc) "
				+ "LEFT JOIN data.tb_osc_censo_suas e ON (a.bosc_sq_osc = e.bosc_sq_osc) "
				+ "LEFT JOIN data.nm_osc_conselho f ON (a.bosc_sq_osc = f.bosc_sq_osc) "
				+ "LEFT JOIN data.tb_osc_lic g ON (a.bosc_sq_osc = g.bosc_sq_osc) "
				+ "LEFT JOIN data.dc_rais_tamanho_estabelecimento h ON (b.dcte_cd_tamanho_estabelecimento = h.dcte_cd_tamanho_estabelecimento) "
				+ "LEFT JOIN data.dc_natureza_juridica i ON (b.dcnj_cd_natureza_juridica = i.dcnj_cd_natureza_juridica) "
				+ "WHERE a.bosc_sq_osc = ? "
				+ "GROUP BY a.bosc_nm_osc, a.bosc_nm_fantasia_osc, e.suas_dt_ano_fundacao, "
				+ "h.dcte_ds_tamanho_estabelecimento, i.dcnj_nm_natureza_juridica, qt_parceria, d.siconv_vl_global, "
				+ "g.lic_vl_aprovado ";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				OscSummary summary = new OscSummary();
				summary.setId(oscId);
				String name = rs.getString("bosc_nm_fantasia_osc");
				if (name == null || name.isEmpty())
					name = rs.getString("bosc_nm_osc");
				summary.setName(name);
				summary.setRecomendations(getRecommendations(oscId));
				summary.setFoundationYear(rs.getInt("suas_dt_ano_fundacao"));
				boolean committeeParticipant = rs.getInt("qt_conselhos") > 0;
				summary.setCommitteeParticipant(committeeParticipant);
				summary.setLength(rs
						.getString("dcte_ds_tamanho_estabelecimento"));
				summary.setLegalTypeDescription(rs
						.getString("dcnj_nm_natureza_juridica"));
				summary.setPartnerships(rs.getInt("qt_parceria"));
				summary.setPartnershipGlobalValue(rs
						.getDouble("siconv_vl_global"));
				summary.setEncourageLawValue(rs.getDouble("lic_vl_aprovado"));
				Certifications certifications = getCertifications(oscId);
				summary.setCertifications(certifications);
				int[] dsCodes = { 1, 2, 3, 4, 5, 7, 8, 11, 13, 14, 15, 16 };
				DataSource[] dataSources = getDataSources(dsCodes, oscId);
				summary.setDataSources(dataSources);

				return summary;
			}
			return null;

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}*/

	private String getDocumentPath(long cnpj, DocumentType type)
			throws RemoteException {
		DecimalFormat df = new DecimalFormat("00000000000000");
		String cnpjStr = df.format(cnpj);
		String path = "/" + documentPath + "/" + cnpjStr.substring(0, 1) + "/"
				+ cnpj + "/<doc_path>/";
		switch (type) {
		case BY_LAW:
			path = path.replace("<doc_path>", byLawPath);
			break;
		case DIRECTORS_BOARD:
			path = path.replace("<doc_path>", directorsBoardPath);
			break;
		case TREATY:
			path = path.replace("<doc_path>", treatyPath);
			break;
		case ACCOUNTABILITY:
			path = path.replace("<doc_path>", accountabilityPath);
			break;

		default:
			String message = "Tipo de documento indefinido.";
			logger.log(Level.SEVERE, message);
			throw new RemoteException(message);
		}
		File folder = new File(getServletContext().getRealPath(path));
		if (folder != null && folder.listFiles() != null
				&& folder.listFiles().length > 0) {
			return path;
		} else {
			return null;
		}
	}
	
	public OscSummary getSummary(int oscId) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT bosc_nr_identificacao, dcti_cd_tipo, bosc_nm_osc, bosc_nm_fantasia_osc, ospr_ds_endereco, "
				+ "ospr_ds_endereco_complemento, ospr_nm_bairro, ospr_nm_municipio, ospr_sg_uf, ospr_nm_cep, "
				+ "dcsc_cd_alpha_subclasse, dcsc_nm_subclasse, ST_AsText(ospr_geometry) wkt, dcnj_cd_alpha_natureza_juridica, "
				+ "dcnj_nm_natureza_juridica, ospr_dt_ano_fundacao, ospr_ee_site, ospr_cd_municipio, "
				+ "rais_qt_vinculo_ativo, siconv_vl_global, siconv_qt_parceria_execucao "				
				+ "FROM portal.vm_osc_principal a "
				+ "LEFT JOIN data.tb_osc_rais b ON (a.bosc_sq_osc = b.bosc_sq_osc) "
				+ "LEFT JOIN data.tb_osc_siconv c ON (a.bosc_sq_osc = c.bosc_sq_osc) "
				//+ "a JOIN portal.tb_osc_contato b ON a.bosc_sq_osc = b.bosc_sq_osc "
				+ "WHERE a.bosc_sq_osc = ? " ;
		

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				OscSummary summary = new OscSummary();
				summary.setId(oscId);
				long code = rs.getLong("bosc_nr_identificacao");
				summary.setCode(code);
				int type = rs.getInt("dcti_cd_tipo");
				if (type == 1) {
					summary.setFormattedCNPJ(String.valueOf(code));
				} else if (type == 2) {
					summary.setFormattedCEI(String.valueOf(code));
				} else {
					summary.setFormattedCode(String.valueOf(code));
				}
				String name = rs.getString("bosc_nm_fantasia_osc");
				if (name == null || name.isEmpty())
					name = rs.getString("bosc_nm_osc");
				summary.setName(name);
				summary.setRecomendations(getRecommendations(oscId));
				String address = rs.getString("ospr_ds_endereco");
				String neighborhood = rs.getString("ospr_nm_bairro");
				String county = rs.getString("ospr_nm_municipio");
				String state = rs.getString("ospr_sg_uf");
				String cep = rs.getString("ospr_nm_cep");
				String length = rs.getString("rais_qt_vinculo_ativo");
				Double vlglobal = rs.getDouble("siconv_vl_global");
				String complement = rs
						.getString("ospr_ds_endereco_complemento");

				StringBuilder fullAddress = new StringBuilder(address);
				if (neighborhood != null && !neighborhood.isEmpty()) {
					fullAddress.append(", " + neighborhood);
				}
				fullAddress.append(", " + county);
				fullAddress.append(", " + state);
				if (cep != null && !cep.isEmpty()) {
					fullAddress.append(", " + cep);
				}
				if (complement != null && !complement.isEmpty()) {
					fullAddress.append(", " + complement);
				}
				summary.setAddress(fullAddress.toString());
				
				summary.setCountyId(rs.getInt("ospr_cd_municipio"));
				summary.setCnaeCode(rs.getString("dcsc_cd_alpha_subclasse"));
				summary.setCnaeDescription(rs.getString("dcsc_nm_subclasse"));
				summary.setLegalTypeCode(rs
						.getString("dcnj_cd_alpha_natureza_juridica"));
				summary.setLegalTypeDescription(rs
						.getString("dcnj_nm_natureza_juridica"));
				summary.setFoundationYear(rs.getInt("ospr_dt_ano_fundacao"));
				summary.setSite(rs.getString("ospr_ee_site"));
				summary.setLength(length);
				summary.setGlobalValue(vlglobal);
				summary.setPartnerships(rs.getInt("siconv_qt_parceria_execucao"));
				summary.setState(rs.getString("ospr_sg_uf"));
				summary.setCounty(rs.getString("ospr_nm_municipio"));
				String wkt = rs.getString("wkt");
				if (wkt != null && !wkt.isEmpty()) {
					WKTReader reader = new WKTReader();
					try {
						Point point = (Point) reader.read(wkt);
						OscCoordinate coord = new OscCoordinate();
						coord.setId(oscId);
						coord.setX(point.getX());
						coord.setY(point.getY());
						summary.setCoordinate(coord);
					} catch (ParseException e) {
						logger.severe(e.getMessage());
					}
				}
				summary.setContacts(getContacts(oscId));
				int[] dsCodes = { 1, 13 };
				DataSource[] dataSources = getDataSources(dsCodes, oscId);
				summary.setDataSources(dataSources);

				return summary;
			}
			return null;

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
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService#getDetail
	 * (int)
	 */
	public OscDetail getDetail(int oscId, String email) throws RemoteException {
		OscDetail detail = new OscDetail();
		OscMain main = getMainData(oscId);
		detail.setMain(main);
		detail.setWorkRelationship(getWorkRelationship(oscId));
		detail.setCertifications(getCertifications(oscId));
		detail.setPublicResources(getPublicResources(oscId));
		detail.setCommittees(getCommittees(oscId));
		detail.setByLawPath(getDocumentPath(main.getCode(), DocumentType.BY_LAW));
		detail.setDirectorsBoardPath(getDocumentPath(main.getCode(),
				DocumentType.DIRECTORS_BOARD));
		detail.setTreatyPath(getDocumentPath(main.getCode(),
				DocumentType.TREATY));
		detail.setAccountabilityPath(getDocumentPath(main.getCode(),
				DocumentType.ACCOUNTABILITY));
		detail.setRecommended(getRecommendation(oscId, email));
		detail.setRecommendations(getRecommendations(oscId));
		return detail;
	}

	public boolean getRecommendation(int oscId, String email)
			throws RemoteException {

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT osus_in_recomendacao "
				+ "FROM portal.nm_osc_usuario, portal.tb_usuario "
				+ "WHERE nm_osc_usuario.tusu_sq_usuario = tb_usuario.tusu_sq_usuario "
				+ "AND nm_osc_usuario.bosc_sq_osc = ? "
				+ "AND tb_usuario.tusu_ee_email = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();
			boolean recommendation;

			if (rs.next()) {

				recommendation = rs.getBoolean("osus_in_recomendacao");
				return recommendation;
			}
			return false;

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}

	}

	/**
	 * @param oscId
	 *            Código da OSC no banco de dados grava a recomendação da OSC
	 * @throws RemoteException
	 */

	private void insertRecommendation(int oscId, String email, boolean value)
			throws RemoteException {

		Connection conn = getConnection();
		PreparedStatement pstmt = null;

		String sql = "INSERT INTO portal.nm_osc_usuario(bosc_sq_osc, tusu_sq_usuario, osus_in_recomendacao) "
				+ "values (?,(SELECT tusu_sq_usuario FROM portal.tb_usuario "
				+ "WHERE tusu_ee_email = ?),?)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			pstmt.setString(2, email);
			pstmt.setBoolean(3, value);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}

	}

	public void setRecommendation(int oscId, String email, boolean value)
			throws RemoteException {

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT a.bosc_sq_osc,a.tusu_sq_usuario "
				+ "FROM portal.nm_osc_usuario a "
				+ "JOIN portal.tb_usuario b ON (b.tusu_sq_usuario = a.tusu_sq_usuario) "
				+ "WHERE a.bosc_sq_osc= ? " + "AND b.tusu_ee_email = ?";

		try {

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();

			if (!rs.next()) {
				insertRecommendation(oscId, email, value);
			} else
				updateRecommendation(oscId, email, value);

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private void updateRecommendation(int oscId, String email, boolean value)
			throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;

		String sql = "UPDATE portal.nm_osc_usuario "
				+ "SET osus_in_recomendacao= ? "
				+ "WHERE bosc_sq_osc=? AND tusu_sq_usuario=(SELECT tusu_sq_usuario FROM portal.tb_usuario "
				+ "WHERE tusu_ee_email = ?)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBoolean(1, value);
			pstmt.setInt(2, oscId);
			pstmt.setString(3, email);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
	}

	private Integer getRecommendations(int oscId) throws RemoteException {

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "SELECT count(*) qt_recomendation "
				+ "FROM portal.nm_osc_usuario "
				+ "WHERE bosc_sq_osc = ? AND osus_in_recomendacao=true";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			rs = pstmt.executeQuery();

			int qt_recomendation;

			if (rs.next()) {
				qt_recomendation = rs.getInt("qt_recomendation");

				return qt_recomendation;
			}

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService#getMainData
	 * (int)
	 */

	public OscMain getMainData(int oscId) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT bosc_nr_identificacao, dcti_cd_tipo, bosc_nm_osc, bosc_nm_fantasia_osc, ospr_ds_endereco, "
				+ "ospr_ds_endereco_complemento, ospr_nm_bairro, ospr_nm_municipio, ospr_sg_uf, ospr_nm_cep, "
				+ "dcsc_cd_alpha_subclasse, dcsc_nm_subclasse, ST_AsText(ospr_geometry) wkt, dcnj_cd_alpha_natureza_juridica, "
				+ "dcnj_nm_natureza_juridica, ospr_dt_ano_fundacao, ospr_ee_site, ospr_cd_municipio, ospr_tx_descricao "
				+ "FROM portal.vm_osc_principal WHERE bosc_sq_osc = ?";

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				OscMain main = new OscMain();
				main.setId(oscId);
				long code = rs.getLong("bosc_nr_identificacao");
				main.setCode(code);
				int type = rs.getInt("dcti_cd_tipo");
				if (type == 1) {
					main.setFormattedCNPJ(String.valueOf(code));
				} else if (type == 2) {
					main.setFormattedCEI(String.valueOf(code));
				} else {
					main.setFormattedCode(String.valueOf(code));
				}
				String name = rs.getString("bosc_nm_fantasia_osc");
				if (name == null || name.isEmpty())
					name = rs.getString("bosc_nm_osc");
				main.setName(name);
				String address = rs.getString("ospr_ds_endereco");
				String neighborhood = rs.getString("ospr_nm_bairro");
				String county = rs.getString("ospr_nm_municipio");
				String state = rs.getString("ospr_sg_uf");
				String cep = rs.getString("ospr_nm_cep");
				String complement = rs
						.getString("ospr_ds_endereco_complemento");

				StringBuilder fullAddress = new StringBuilder(address);
				if (neighborhood != null && !neighborhood.isEmpty()) {
					fullAddress.append(", " + neighborhood);
				}
				fullAddress.append(", " + county);
				fullAddress.append(", " + state);
				if (cep != null && !cep.isEmpty()) {
					fullAddress.append(", " + cep);
				}
				if (complement != null && !complement.isEmpty()) {
					fullAddress.append(", " + complement);
				}
				main.setAddress(fullAddress.toString());
				main.setCountyId(rs.getInt("ospr_cd_municipio"));
				main.setCnaeCode(rs.getString("dcsc_cd_alpha_subclasse"));
				main.setCnaeDescription(rs.getString("dcsc_nm_subclasse"));
				main.setLegalTypeCode(rs
						.getString("dcnj_cd_alpha_natureza_juridica"));
				main.setLegalTypeDescription(rs
						.getString("dcnj_nm_natureza_juridica"));
				main.setFoundationYear(rs.getInt("ospr_dt_ano_fundacao"));
				main.setSite(rs.getString("ospr_ee_site"));
				main.setState(rs.getString("ospr_sg_uf"));
				main.setCounty(rs.getString("ospr_nm_municipio"));
				String wkt = rs.getString("wkt");
				if (wkt != null && !wkt.isEmpty()) {
					WKTReader reader = new WKTReader();
					try {
						Point point = (Point) reader.read(wkt);
						OscCoordinate coord = new OscCoordinate();
						coord.setId(oscId);
						coord.setX(point.getX());
						coord.setY(point.getY());
						main.setCoordinate(coord);
					} catch (ParseException e) {
						logger.severe(e.getMessage());
					}
				}
				main.setContacts(getContacts(oscId));
				int[] dsCodes = { 1, 2, 5, 6, 8, 12, 13 };
				DataSource[] dataSources = getDataSources(dsCodes, oscId);
				main.setDataSources(dataSources);
				
				main.setDescription(rs.getString("ospr_tx_descricao"));

				return main;
			}
			return null;

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	/**
	 * @param oscId
	 *            Código da OSC no banco de dados
	 * @return Relação de contatos da OSC
	 * @throws RemoteException
	 */
	private Map<String, String> getContacts(int oscId) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT cont_ds_tipo_contato, cont_ds_contato  FROM portal.tb_osc_contato WHERE bosc_sq_osc = ?";

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			rs = pstmt.executeQuery();
			Map<String, String> contacts = new HashMap<String, String>();
			while (rs.next()) {
				contacts.put(rs.getString("cont_ds_tipo_contato"),
						rs.getString("cont_ds_contato"));
			}
			return contacts;

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
	 * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService#
	 * getWorkRelationship(int)
	 */
	public WorkRelationship getWorkRelationship(int oscId)
			throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT dcte_ds_tamanho_estabelecimento, rais_qt_vinculo_ativo, "
				+ "COALESCE(rais_qt_vinculo_clt, sus_qt_vinculo_clt, (suas_qt_contratados_fundamental + suas_qt_contratados_medio + "
				+ "suas_qt_contratados_superior)) qt_vinculo_clt, "
				+ "COALESCE(rais_qt_vinculo_estatutario,sus_qt_vinculo_emprego_publico ) qt_vinculo_emprego_publico, "
				+ "sus_qt_vinculo_autonomo,  sus_qt_vinculo_cooperativa, sus_qt_vinculo_estagio, sus_qt_vinculo_residencia, "
				+ "sus_qt_vinculo_temporario, sus_qt_vinculo_outros, "
				+ "(suas_qt_cedidos_fundamental + suas_qt_cedidos_medio +  suas_qt_cedidos_superior) suas_qt_cedidos, "
				+ "(suas_qt_estagiario_fundamental + suas_qt_estagiario_medio + suas_qt_estagiario_superior) suas_qt_estagiario,  "
				+ "(suas_qt_voluntario_fundamental + suas_qt_voluntario_medio + suas_qt_voluntario_superior) suas_qt_voluntario "
				+ "FROM  data.tb_osc LEFT JOIN data.tb_osc_rais ON (tb_osc.bosc_sq_osc = tb_osc_rais.bosc_sq_osc) "
				+ "LEFT JOIN data.tb_osc_censo_suas ON (tb_osc.bosc_sq_osc = tb_osc_censo_suas.bosc_sq_osc) "
				+ "LEFT JOIN data.tb_osc_sus ON (tb_osc.bosc_sq_osc = tb_osc_sus.bosc_sq_osc) "
				+ "JOIN data.dc_rais_tamanho_estabelecimento ON (tb_osc_rais.dcte_cd_tamanho_estabelecimento = dc_rais_tamanho_estabelecimento.dcte_cd_tamanho_estabelecimento) "
				+ "WHERE tb_osc.bosc_sq_osc = ?";

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			rs = pstmt.executeQuery();
			WorkRelationship workers = new WorkRelationship();
			if (rs.next()) {
				workers.setOscLength(rs
						.getString("dcte_ds_tamanho_estabelecimento"));
				workers.setActiveWorkers(rs.getInt("rais_qt_vinculo_ativo"));
				workers.setLegalWorkers(rs.getInt("qt_vinculo_clt"));
				workers.setPublicSectorWorkers(rs
						.getInt("qt_vinculo_emprego_publico"));
				workers.setFreelances(rs.getInt("sus_qt_vinculo_autonomo"));
				workers.setCoop(rs.getInt("sus_qt_vinculo_cooperativa"));
				workers.setTrainees(rs.getInt("sus_qt_vinculo_estagio"));
				workers.setMedicalResidency(rs
						.getInt("sus_qt_vinculo_residencia"));
				workers.setTemporaryWorkers(rs
						.getInt("sus_qt_vinculo_temporario"));
				workers.setOthers(rs.getInt("sus_qt_vinculo_outros"));
				workers.setAssignedWorkers(rs.getInt("suas_qt_cedidos"));
				workers.setTrainees(rs.getInt("suas_qt_estagiario"));
				workers.setVolunteers(rs.getInt("suas_qt_voluntario"));

			}
			int[] dsCodes = { 1, 5, 10 };
			DataSource[] dataSources = getDataSources(dsCodes, oscId);
			workers.setDataSources(dataSources);
			return workers;

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
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService#getCertifications
	 * (int)
	 */
	public Certifications getCertifications(int oscId) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT cnea_dt_publicacao, cebas_mec_dt_inicio_validade, cebas_mec_dt_fim_validade, "
				+ "cebas_saude_dt_inicio_validade, cebas_saude_dt_fim_validade, cnes_oscip_dt_publicacao, cnes_upf_dt_declaracao "
				+ "FROM data.tb_osc_certificacao WHERE bosc_sq_osc = ?";

		Certifications cert = new Certifications();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				cert.setCneaPublication(rs.getDate("cnea_dt_publicacao"));
				cert.setCebasMecBeginning(rs
						.getDate("cebas_mec_dt_inicio_validade"));
				cert.setCebasMecEnd(rs.getDate("cebas_mec_dt_fim_validade"));
				cert.setCebasSusBeginning(rs
						.getDate("cebas_saude_dt_inicio_validade"));
				cert.setCebasSusEnd(rs.getDate("cebas_saude_dt_fim_validade"));
				cert.setOscipPublication(rs.getDate("cnes_oscip_dt_publicacao"));
				cert.setUpfDeclaration(rs.getDate("cnes_upf_dt_declaracao"));
			}
			int[] dsCodes = { 2, 3, 4, 7, 11, 14 };
			DataSource[] dataSources = getDataSources(dsCodes, oscId);
			cert.setDataSources(dataSources);
			return cert;

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
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService#getCommittees
	 * (int)
	 */
	public Committees getCommittees(int oscId) throws RemoteException {

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT cons_nm_conselho, tpar_nm_tipo_participacao "
				+ "FROM data.nm_osc_conselho a "
				+ "LEFT JOIN data.tb_conselhos b ON (a.cons_cd_conselho = b.cons_cd_conselho) "
				+ "LEFT JOIN data.dc_tipo_participacao c ON (a.tpar_cd_tipo_participacao = c.tpar_cd_tipo_participacao) "
				+ "WHERE bosc_sq_osc = ?";
		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			rs = pstmt.executeQuery();

			Committees committees = new Committees();

			Map<String, String> committeesMap = new HashMap<String, String>();

			while (rs.next()) {
				committeesMap.put(rs.getString("cons_nm_conselho"),
						rs.getString("tpar_nm_tipo_participacao"));
			}

			committees.setCommittees(committeesMap);

			int[] dsCodes = { 15 };
			DataSource[] dataSources = getDataSources(dsCodes, oscId);
			committees.setDataSources(dataSources);
			return committees;

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	public PublicResources getPublicResources(int oscId) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT siconv_qt_parceria_finalizada, siconv_qt_parceria_execucao, siconv_vl_global, siconv_vl_repasse, "
				+ "siconv_vl_contrapartida_financeira, siconv_vl_contrapartida_outras, siconv_vl_empenhado, siconv_vl_desembolsado, "
				+ "finep_qt_projetos_proponente, finep_qt_projetos_executor, finep_qt_projetos_coexecutor, "
				+ "finep_qt_projetos_interveniente, lic_vl_solicitado, lic_vl_aprovado, lic_vl_captado "
				+ "FROM  data.tb_osc LEFT JOIN data.tb_osc_siconv ON (tb_osc.bosc_sq_osc = tb_osc_siconv.bosc_sq_osc) "
				+ "LEFT JOIN data.tb_osc_finep ON (tb_osc.bosc_sq_osc = tb_osc_finep.bosc_sq_osc) "
				+ "LEFT JOIN data.tb_osc_lic ON (tb_osc.bosc_sq_osc = tb_osc_lic.bosc_sq_osc) "
				+ "WHERE tb_osc.bosc_sq_osc = ?";

		// logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, oscId);
			rs = pstmt.executeQuery();

			PublicResources resources = new PublicResources();
			if (rs.next()) {
				resources.setPartnershipsEnded(rs
						.getInt("siconv_qt_parceria_finalizada"));
				resources.setInExecutionPartnership(rs
						.getInt("siconv_qt_parceria_execucao"));
				resources.setGlobalValue(rs.getDouble("siconv_vl_global"));
				resources.setTransferValue(rs.getDouble("siconv_vl_repasse"));
				resources.setFinancialCounterpartValue(rs
						.getDouble("siconv_vl_contrapartida_financeira"));
				resources.setOthersCounterpartValue(rs
						.getDouble("siconv_vl_contrapartida_outras"));
				resources
						.setCommittedValue(rs.getDouble("siconv_vl_empenhado"));
				resources.setDisbursedValue(rs
						.getDouble("siconv_vl_desembolsado"));
				resources.setTechnologicalAsProposer(rs
						.getInt("finep_qt_projetos_proponente"));
				resources.setTechnologicalAsExecutor(rs
						.getInt("finep_qt_projetos_executor"));
				resources.setTechnologicalAsCoExecutor(rs
						.getInt("finep_qt_projetos_coexecutor"));
				resources.setTechnologicalAsIntervenient(rs
						.getInt("finep_qt_projetos_interveniente"));
				resources.setCulturalRequestedValue(rs
						.getDouble("lic_vl_solicitado"));
				resources.setCulturalApprovedValue(rs
						.getDouble("lic_vl_aprovado"));
				resources
						.setCulturalRaisedValue(rs.getDouble("lic_vl_captado"));

			}
			int[] dsCodes = { 8, 12, 13 };
			DataSource[] dataSources = getDataSources(dsCodes, oscId);
			resources.setDataSources(dataSources);
			return resources;

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	public DataSource[] getFonteIndicadores(int[] id) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT mdfd_cd_fonte_dados, mdfd_sg_fonte_dados, mdfd_nm_fonte_dados, mdfd_dt_aquisicao, mdfd_ee_referencia "
				+ "FROM data.md_fonte_dados WHERE mdfd_cd_fonte_dados in (?";
		for (int i = 1; i < id.length; i++) {
			sql += ", ?";
		}
		sql += ")";
	
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < id.length; i++) {
				pstmt.setInt(i + 1, id[i]);
			}
			rs = pstmt.executeQuery();

			Set<DataSource> dsCol = new HashSet<DataSource>();
			while (rs.next()) {
				DataSource ds = new DataSource();
				ds.setId(rs.getInt("mdfd_cd_fonte_dados"));
				ds.setAcronym(rs.getString("mdfd_sg_fonte_dados"));
				ds.setName(rs.getString("mdfd_nm_fonte_dados"));
				ds.setAcquisitionDate(rs.getDate("mdfd_dt_aquisicao"));
				ds.setSiteURL(rs.getString("mdfd_ee_referencia"));
				dsCol.add(ds);
			}

			return dsCol.toArray(new DataSource[dsCol.size()]);

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
		
	}
	

	private DataSource[] getDataSources(int[] dataSourceId, int oscId)
			throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
//		String sql = "SELECT b.mdfd_cd_fonte_dados, b.mdfd_sg_fonte_dados, b.mdfd_nm_fonte_dados, b.mdfd_dt_aquisicao, b.mdfd_ee_referencia "
//				+ "FROM syst.nm_osc_fonte_dados a "
//				+ "JOIN data.md_fonte_dados b on (a.mdfd_cd_fonte_dados = b.mdfd_cd_fonte_dados) "
//				+ "WHERE b.mdfd_cd_fonte_dados in (?";
//		for (int i = 1; i < dataSourceId.length; i++) {
//			sql += ", ?";
//		}
//		sql += ") and a.bosc_sq_osc = ? ";
		// logger.info(sql);
		
		String sql = "SELECT mdfd_sg_fonte_dados, mdfd_nm_fonte_dados, mdfd_dt_aquisicao, mdfd_ee_referencia "
				+ "FROM data.md_fonte_dados WHERE  mdfd_cd_fonte_dados in (?";
				
		for (int i = 1; i < dataSourceId.length; i++) {
			sql += ", ?";
		}
		sql += ")";
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < dataSourceId.length; i++) {
				pstmt.setInt(i + 1, dataSourceId[i]);
			}

			//pstmt.setInt(dataSourceId.length + 1, oscId);
			rs = pstmt.executeQuery();

			Set<DataSource> dsCol = new HashSet<DataSource>();
			// int i = 0;
			while (rs.next()) {
				DataSource ds = new DataSource();
				//ds.setId(rs.getInt("mdfd_cd_fonte_dados"));
				ds.setAcronym(rs.getString("mdfd_sg_fonte_dados"));
				ds.setName(rs.getString("mdfd_nm_fonte_dados"));
				ds.setAcquisitionDate(rs.getDate("mdfd_dt_aquisicao"));
				ds.setSiteURL(rs.getString("mdfd_ee_referencia"));
				// dsArray[i] = ds;
				// i++;
				dsCol.add(ds);
			}

			return dsCol.toArray(new DataSource[dsCol.size()]);

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
}
