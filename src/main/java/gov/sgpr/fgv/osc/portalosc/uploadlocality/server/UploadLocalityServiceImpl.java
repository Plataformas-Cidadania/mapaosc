package gov.sgpr.fgv.osc.portalosc.uploadlocality.server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import gov.sgpr.fgv.osc.portalosc.configuration.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.model.AgreementLocalityModel;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

//public class UploadLocalityServiceImpl extends RemoteServiceImpl implements UploadLocalityService {
public class UploadLocalityServiceImpl extends RemoteServiceImpl {
	private static final long serialVersionUID = 6487133703022070014L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Byte[] desKey;
	
	public void init(ServletConfig config) {
		super.init(config);
		ServletContext context = getServletContext();
		String key[] = context.getInitParameter("CHAVE_CRIPTOGRAFIA").split(",");
		desKey = new Byte[key.length];
		for (int i = 0; i < key.length; i++){
			String valueString=key[i].trim();
			int intElem = Integer.valueOf(valueString);
			desKey[i] = (byte) intElem;
		}
	}
	
	public void uploadFile(ArrayList<AgreementLocalityModel> convenioList) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "";
		
		try {
			for(AgreementLocalityModel convenio : convenioList){
				Integer idOSC = 0;
				Integer idParceriaTernaria = 0;
				
				sql = "SELECT bosc_sq_osc "
					+ "FROM data.tb_osc "
					+ "WHERE bosc_nr_identificacao = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, convenio.getCnpjProponente());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					idOSC = rs.getInt("bosc_sq_osc");
				}
				rs.close();
				pstmt.close();
				
				if(idOSC > 0){
					sql = "INSERT INTO data.tb_osc(bosc_nr_identificacao, dcti_nr_identificacao, bosc_nm_osc, bosc_nm_fantasia_osc) "
						+ "VALUES(?, 2, ?, ?)";
						
					pstmt = conn.prepareStatement(sql);
					pstmt.setLong(1, convenio.getCnpjProponente());
					pstmt.setString(2, SafeHtmlUtils.htmlEscape(convenio.getRazaoSocialProponente()));
					pstmt.setString(3, SafeHtmlUtils.htmlEscape(convenio.getNomeFantasiaProponente()));
					pstmt.execute();
					pstmt.close();
					
					sql = "SELECT bosc_sq_osc "
							+ "FROM data.tb_osc "
							+ "WHERE bosc_nr_identificacao = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setLong(1, convenio.getCnpjProponente());
					rs = pstmt.executeQuery();
					if (rs.next()) {
						idOSC = rs.getInt("bosc_sq_osc");
					}
					rs.close();
					pstmt.close();
				}
				
				sql = "INSERT INTO data.tb_ternaria_localidade(bosc_sq_osc, tusu_sq_usuario, parloc_nr_parceria) "
					+ "VALUES(?, ?, ?)";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, idOSC);
				pstmt.setInt(2, convenio.getIdRepresentante());
				pstmt.setInt(3, convenio.getNumeroConvenio());
				pstmt.execute();
				pstmt.close();
				
				sql = "SELECT parloc_sq_parceria "
					+ "FROM data.tb_ternaria_localidade "
					+ "WHERE bosc_sq_osc = ? "
					+ "AND tusu_sq_usuario = ? "
					+ "AND parloc_nr_parceria = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, idOSC);
				pstmt.setInt(2, convenio.getIdRepresentante());
				pstmt.setInt(3, convenio.getNumeroConvenio());
				rs = pstmt.executeQuery();
				if (rs.next()) {
					idParceriaTernaria = rs.getInt("parloc_sq_parceria");
				}
				rs.close();
				pstmt.close();
				
				sql = "INSERT INTO portal.tb_usuario(parloc_sq_parceria, parloc_dt_inicio, parloc_dt_conclusao, "
					+ 								"parloc_sit_parceria, parloc_tipo_parceria, parloc_vl_total, parloc_vl_pago, "
					+ 								"parloc_contrapartida, parloc_orgao_conced, parloc_municipio_prop, "
					+ 								"parloc_endereco_prop, parloc_objeto_parceria) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, idParceriaTernaria);
				pstmt.setDate(2, new Date(convenio.getDataInicio().getTime()));
				pstmt.setDate(3, new Date(convenio.getDataConclusao().getTime()));
				pstmt.setString(4, SafeHtmlUtils.htmlEscape(convenio.getSituacaoParceria()));
				pstmt.setString(5, SafeHtmlUtils.htmlEscape(convenio.getTipoParceria()));
				pstmt.setDouble(6, convenio.getValorTotal());
				pstmt.setDouble(7, convenio.getValorPago());
				pstmt.setDouble(8, convenio.getValorContrapartidaFinanceira());
				pstmt.setString(9, SafeHtmlUtils.htmlEscape(convenio.getOrgaoConcedente()));
				pstmt.setString(10, SafeHtmlUtils.htmlEscape(convenio.getMunicipioProponente()));
				pstmt.setString(11, SafeHtmlUtils.htmlEscape(convenio.getEnderecoProponente()));
				pstmt.setString(12, SafeHtmlUtils.htmlEscape(convenio.getObjetoParceria()));
				pstmt.execute();
				pstmt.close();
			}
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
	}
}