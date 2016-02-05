package gov.sgpr.fgv.osc.portalosc.organization.server;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import gov.sgpr.fgv.osc.portalosc.organization.shared.model.OrganizationModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.ProjetoModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.exception.RemoteException;
import gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationService;

/**
 * @author vagnerpraia
 * 
 */
public class OrganizationServiceImpl extends RemoteServiceImpl implements OrganizationService {
	private static final long serialVersionUID = -5965003754375086786L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Byte[] desKey;
	
	@Override
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
	
	public OrganizationModel getOrganizationByID(Integer id) throws RemoteException {
		logger.info("Buscando organização no banco de dados pelo ID " + id.toString());
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		OrganizationModel organization = new OrganizationModel();
		organization.setId(id);
		
		String sql = "";
		
		try {
			sql = "SELECT bosc_nm_osc, bosc_nm_fantasia_osc, bosc_nr_identificacao, ospr_ds_endereco, dcnj_nm_natureza_juridica, "
				+ "		  dcsc_nm_subclasse, ospr_tx_descricao, ospr_dt_ano_fundacao, ospr_ee_site, vl_valor_parcerias_total,  "
				+ "		  vl_valor_parcerias_federal, vl_valor_parcerias_estadual, vl_valor_parcerias_municipal, ee_facebook, "
				+ "		  ee_google, ee_linkedin, ee_twitter, im_imagem, ee_como_participar "
				+ "FROM portal.vm_osc_principal "
				+ "WHERE bosc_sq_osc = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();	
			if (rs.next()) {
				organization.setRazaoSocial(rs.getString("bosc_nm_osc"));
				organization.setNomeFantasia(rs.getString("bosc_nm_fantasia_osc"));
				organization.setCnpj(rs.getLong("bosc_nr_identificacao"));
				organization.setEndereco(rs.getString("ospr_ds_endereco"));
				organization.setNaturezaJuridica(rs.getString("dcnj_nm_natureza_juridica"));
				organization.setCnae(rs.getString("dcsc_nm_subclasse"));
				organization.setDescricaoProjeto(rs.getString("ospr_tx_descricao"));
				organization.setAnoFundacao(rs.getInt("ospr_dt_ano_fundacao"));
				organization.setSite(rs.getString("ospr_ee_site"));
				organization.setValorParceriasTotal(rs.getDouble("vl_valor_parcerias_total"));
				organization.setValorParceriasFederal(rs.getDouble("vl_valor_parcerias_federal"));
				organization.setValorParceriasEstadual(rs.getDouble("vl_valor_parcerias_estadual"));
				organization.setFacebook(rs.getString("ee_facebook"));
				organization.setGoogle(rs.getString("ee_google"));
				organization.setLinkedin(rs.getString("ee_linkedin"));
				organization.setTwitter(rs.getString("ee_twitter"));
				organization.setImagem(rs.getString("im_imagem"));
				organization.setComoParticipar(rs.getString("ee_como_participar"));
			}
			rs.close();
			pstmt.close();
			
			sql = "SELECT count(bosc_sq_osc) AS sum "
				+ "FROM portal.nm_osc_usuario "
				+ "WHERE bosc_sq_osc = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				organization.setRecomendacoes(rs.getInt("sum"));
			}
			organization.setTotalColaboradores(organization.getTrabalhadores() + organization.getVoluntarios() + organization.getPortadoresDeficiencia());
			rs.close();
			pstmt.close();
			
			sql = "SELECT cont_ds_contato "
				+ "FROM portal.tb_osc_contato "
				+ "WHERE bosc_sq_osc = ?"
				+ "AND cont_ds_tipo_contato = 'Email'";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();	
			if (rs.next()) {
				organization.setEmail(rs.getString("cont_ds_contato"));
			}
			rs.close();
			pstmt.close();
			
			sql = "SELECT cons_nm_conselho "
				+ "FROM data.nm_osc_conselho AS a "
				+ "INNER JOIN data.tb_conselhos AS b "
				+ "ON a.cons_cd_conselho = b.cons_cd_conselho "
				+ "WHERE bosc_sq_osc = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			ArrayList<String> participacaoSocial = new ArrayList<String>();
			while (rs.next()) {
				participacaoSocial.add(rs.getString("cons_nm_conselho"));
			}
			organization.setParticipacaoSocial(participacaoSocial);
			rs.close();
			pstmt.close();
			
			sql = "SELECT rais_qt_vinculo_clt, rais_qt_vinculo_voluntario, rais_qt_vinculo_deficiente "
				+ "FROM data.tb_osc_rais "
				+ "WHERE bosc_sq_osc = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				organization.setTrabalhadores(rs.getInt("rais_qt_vinculo_clt"));
				organization.setVoluntarios(rs.getInt("rais_qt_vinculo_voluntario"));
				organization.setPortadoresDeficiencia(rs.getInt("rais_qt_vinculo_deficiente"));
			}
			organization.setTotalColaboradores(organization.getTrabalhadores() + organization.getVoluntarios() + organization.getPortadoresDeficiencia());
			rs.close();
			pstmt.close();
			
			sql = "SELECT titulo, status, data_inicio, data_fim, valor_total, fonte_recurso, link, publico_alvo, "
				+ "		  abrangencia, localizacao, financiadores, descricao "
				+ "FROM data.tb_osc_projeto "
				+ "WHERE bosc_sq_osc = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			ArrayList<ProjetoModel> projetoList = new ArrayList<ProjetoModel>();
			while (rs.next()) {
				ProjetoModel projeto = new ProjetoModel();
				projeto.setTitulo(rs.getString("titulo"));
				projeto.setStatus(rs.getString("status"));
				projeto.setDataInicio(rs.getDate("data_inicio"));
				projeto.setDataFim(rs.getDate("data_fim"));
				projeto.setValorTotal(rs.getDouble("valor_total"));
				projeto.setFonteRecursos(rs.getString("fonte_recurso"));
				projeto.setLink(rs.getString("link"));
				projeto.setPublicoAlvo(rs.getString("publico_alvo"));
				projeto.setAbrangencia(rs.getString("abrangencia"));
				projeto.setLocalizacao(rs.getString("localizacao"));
				projeto.setFinanciadores(rs.getString("financiadores"));
				projeto.setDescricao(rs.getString("descricao"));
				
				projetoList.add(projeto);
			}
			organization.setProjetos(projetoList);
			rs.close();
			pstmt.close();
			
			sql = "SELECT cnea_dt_publicacao, cebas_mec_dt_inicio_validade, cebas_mec_dt_fim_validade, cebas_saude_dt_inicio_validade, "
				+ 		 "cebas_saude_dt_fim_validade, cnes_oscip_dt_publicacao, cnes_upf_dt_declaracao, cebas_mds_dt_inicio_validade, "
				+ 		 "cebas_mds_dt_fim_validade "
				+ "FROM data.tb_osc_certificacao "
				+ "WHERE bosc_sq_osc = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			ArrayList<String> certificacao = new ArrayList<String>();			
			while (rs.next()) {
				Date now = new Date(new java.util.Date().getTime());
				
				String strDateCNAE = rs.getString("cnea_dt_publicacao");
				if(strDateCNAE != null){
					Date date = new SimpleDateFormat("yyyy-mm-dd").parse(strDateCNAE);
					if(now.after(date)){
						certificacao.add("CNAE");
					}
				}
				String strDateInicioCebasMec = rs.getString("cebas_mec_dt_inicio_validade");
				String strDateFimCebasMec = rs.getString("cebas_mec_dt_fim_validade");
				if(strDateInicioCebasMec != null && strDateFimCebasMec != null){
					Date dateInicioCebasMec = new SimpleDateFormat("yyyy-mm-dd").parse(strDateInicioCebasMec);
					Date dateFimCebasMec = new SimpleDateFormat("yyyy-mm-dd").parse(strDateFimCebasMec);
					if(now.after(dateInicioCebasMec) && now.before(dateFimCebasMec)){
						certificacao.add("CEBAS/MEC");
					}
				}
				String strDateInicioCebasSaude = rs.getString("cebas_saude_dt_inicio_validade");
				String strDateFimCebasSaude = rs.getString("cebas_saude_dt_fim_validade");
				if(strDateInicioCebasSaude != null && strDateFimCebasSaude != null){
					Date dateInicioCebasSaude = new SimpleDateFormat("yyyy-mm-dd").parse(strDateInicioCebasSaude);
					Date dateFimCebasSaude = new SimpleDateFormat("yyyy-mm-dd").parse(strDateFimCebasSaude);
					if(now.after(dateInicioCebasSaude) && now.before(dateFimCebasSaude)){
						certificacao.add("CEBAS/Saúde");
					}
				}
				String strDateCnesOscip = rs.getString("cnes_oscip_dt_publicacao");
				if(strDateCnesOscip != null){
					Date date = new SimpleDateFormat("yyyy-mm-dd").parse(strDateCnesOscip);
					if(now.after(date)){
						certificacao.add("CNES/OSCIP");
					}
				}
				String strDateCnesUfp = rs.getString("cnes_upf_dt_declaracao");
				if(strDateCnesUfp != null){
					Date date = new SimpleDateFormat("yyyy-mm-dd").parse(strDateCnesUfp);
					if(now.after(date)){
						certificacao.add("CNES/UFP");
					}
				}
				String strDateInicioCebasMds = rs.getString("cebas_mds_dt_inicio_validade");
				String strDateFimCebasMds = rs.getString("cebas_mds_dt_fim_validade");
				if(strDateInicioCebasMds != null && strDateFimCebasMds != null){
					Date dateInicioCebasMds = new SimpleDateFormat("yyyy-mm-dd").parse(strDateInicioCebasMds);
					Date dateFimCebasMds = new SimpleDateFormat("yyyy-mm-dd").parse(strDateFimCebasMds);
					if(now.after(dateInicioCebasMds) && now.before(dateFimCebasMds)){
						certificacao.add("CEBAS/MDS");
					}
				}
			}
			organization.setCertificacao(certificacao);
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} catch (ParseException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
		
		return organization;
	}
	
	public void setOrganization(OrganizationModel organization) throws RemoteException {
		logger.info("Atualizando informações da organização no banco de dados");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		
		try {
			String sql = "UPDATE portal.tb_usuario "
					   + "SET tpus_cd_tipo_usuario = ?, tusu_ee_email = ?, tusu_nm_usuario = ?, "
					   + "tusu_cd_senha = ?, tusu_nr_cpf = ?, bosc_sq_osc = null, tusu_in_lista_email = ?, tusu_dt_atualizacao = ? "
					   + "WHERE tusu_sq_usuario = ?";
			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, organization.getTipoUsuario());
//			pstmt.setString(2, organization.getEmail());
//			pstmt.setString(3, organization.getNome());
//			pstmt.setString(4, organization.getSenha());
//			pstmt.setLong(5, organization.getCPF());
//			pstmt.setBoolean(6, organization.getListaEmail());
//			pstmt.setDate(7, sqlDate);
//			pstmt.setLong(8, organization.getId());
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
	}
	
	public Byte[] getEncryptKey() throws RemoteException {
		return desKey;
	}
	
	public Boolean searchOSCbyUser(Integer idUser, Integer idOsc) throws RemoteException{
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean result = false;
		String sql = "SELECT bosc_sq_osc "
				   + "FROM portal.tb_usuario "
				   + "WHERE tusu_sq_usuario = ?"
				   + "AND bosc_sq_osc = ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idUser);
			pstmt.setInt(2, idOsc);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = true;
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
