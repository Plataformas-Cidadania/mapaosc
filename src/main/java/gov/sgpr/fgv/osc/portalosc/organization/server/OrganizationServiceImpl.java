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

import gov.sgpr.fgv.osc.portalosc.organization.shared.model.ConvenioModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.DiretorModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.OrganizationModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.ProjetoModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.exception.RemoteException;
import gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationService;
import gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl;

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
				+ "		  dcsc_nm_subclasse, ospr_tx_descricao, ospr_dt_ano_fundacao, ospr_ee_site, "
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
				Double parceriasFederal = rs.getDouble("vl_valor_parcerias_federal");
				Double parceriasEstadual = rs.getDouble("vl_valor_parcerias_estadual");
				Double parceriasMunicipal = rs.getDouble("vl_valor_parcerias_municipal");
				organization.setValorParceriasTotal(parceriasFederal + parceriasEstadual + parceriasMunicipal);
				organization.setValorParceriasFederal(parceriasFederal);
				organization.setValorParceriasEstadual(parceriasEstadual);
				organization.setValorParceriasMunicipal(parceriasMunicipal);
				organization.setFacebook(rs.getString("ee_facebook"));
				organization.setGoogle(rs.getString("ee_google"));
				organization.setLinkedin(rs.getString("ee_linkedin"));
				organization.setTwitter(rs.getString("ee_twitter"));
				organization.setImagem(rs.getString("im_imagem"));
				organization.setComoParticipar(rs.getString("ee_como_participar"));
			}
			rs.close();
			pstmt.close();
			
			sql = "SELECT tdir_sq_diretor,cargo,nome FROM data.tb_osc_diretor WHERE bosc_sq_osc = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			ArrayList<DiretorModel> diretorList = new ArrayList<DiretorModel>();
			while (rs.next()) {
				DiretorModel diretor = new DiretorModel();
				diretor.setId(rs.getInt("tdir_sq_diretor"));
				diretor.setCargo(rs.getString("cargo"));
				diretor.setNome(rs.getString("nome"));
				
				diretorList.add(diretor);
			}
			organization.setDiretores(diretorList);
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
			
			sql = "SELECT proj_cd_projetos, titulo, status, data_inicio, data_fim, valor_total, fonte_recurso, link, publico_alvo, "
					+ "		  abrangencia, localizacao, financiadores, descricao "
					+ "FROM data.tb_osc_projeto "
					+ "WHERE bosc_sq_osc = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			ArrayList<ProjetoModel> projetoList = new ArrayList<ProjetoModel>();
			while (rs.next()) {
				ProjetoModel projeto = new ProjetoModel();
				projeto.setId(rs.getInt("proj_cd_projetos"));
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
				
			sql = "SELECT nr_convenio, tx_objeto_convenio, tx_situacao, dt_inicio_vigencia, dt_fim_vigencia, vl_global, nm_orgao_concedente, tx_objeto_convenio, conv_publico_alvo "
					+ "FROM data.tb_osc_convenios "
					+ "WHERE bosc_sq_osc = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			ArrayList<ConvenioModel> conveniosList = new ArrayList<ConvenioModel>();
			while (rs.next()) {
				ConvenioModel convenio = new ConvenioModel();
				convenio.setNConv(rs.getInt("nr_convenio"));
				convenio.setTitulo(rs.getString("tx_objeto_convenio"));
				convenio.setStatus(rs.getString("tx_situacao"));
				convenio.setDataInicio(rs.getDate("dt_inicio_vigencia"));
				convenio.setDataFim(rs.getDate("dt_fim_vigencia"));
				convenio.setValorTotal(rs.getDouble("vl_global"));
				convenio.setFinanciadores(rs.getString("nm_orgao_concedente"));
				convenio.setDescricao(rs.getString("tx_objeto_convenio"));
				convenio.setPublicoAlvo(rs.getString("conv_publico_alvo"));
				
				conveniosList.add(convenio);
			}
			organization.setConvenios(conveniosList);
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
		logger.info("Atualizando informações da organização"+ organization.getId().toString() +"no banco de dados");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		
		String sql = "";
		
		try {
						
			sql = "UPDATE portal.vm_osc_principal " 
				  + "SET bosc_nm_fantasia_osc = ?, ospr_tx_descricao = ?, ospr_dt_ano_fundacao = ?, ospr_ee_site = ? "
				  + "WHERE bosc_sq_osc = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, organization.getNomeFantasia());
			pstmt.setString(2, organization.getDescricaoProjeto());
			pstmt.setInt(3, organization.getAnoFundacao());
			pstmt.setString(4, organization.getSite());
			pstmt.setLong(5, organization.getId());
			pstmt.execute();
			pstmt.close();
			
			sql = "UPDATE data.tb_osc_rais SET rais_qt_vinculo_voluntario = ? WHERE bosc_sq_osc = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, organization.getVoluntarios());
			pstmt.setInt(2, organization.getId());
			pstmt.execute();
			pstmt.close();
			
			sql = "UPDATE portal.tb_osc_contato SET cont_ds_contato = ?, cont_ds_tipo_contato = 'Email' "
					   + "WHERE bosc_sq_osc = ?";
						
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, organization.getEmail());
				pstmt.setInt(2, organization.getId());
				pstmt.execute();
				pstmt.close();
			
			
			sql = "INSERT INTO data.tb_osc_diretor (bosc_sq_osc,cargo, nome) VALUES (?, ?, ?);";
						
			pstmt = conn.prepareStatement(sql);
			
			for (int i = 0; i < organization.getDiretores().size(); i++ ) {
				if(organization.getDiretores().get(i).getId() == -1){
					pstmt.setInt(1,  organization.getId());
					pstmt.setString(2,organization.getDiretores().get(i).getCargo());
					pstmt.setString(3,organization.getDiretores().get(i).getNome());
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			
			
			sql = "UPDATE data.tb_osc_diretor SET bosc_sq_osc = ?, cargo = ?, nome = ? WHERE tdir_sq_diretor = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			for (int i = 0; i < organization.getDiretores().size(); i++ ) {
				if(organization.getDiretores().get(i).getId() != -1){
					pstmt.setInt(1,  organization.getId());
					pstmt.setString(2,organization.getDiretores().get(i).getCargo());
					pstmt.setString(3,organization.getDiretores().get(i).getNome());
					pstmt.setInt(4,organization.getDiretores().get(i).getId());
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			
			
			sql = "INSERT INTO data.tb_osc_projeto (bosc_sq_osc, titulo, status, data_inicio, data_fim, valor_total, fonte_recurso, link, publico_alvo, "
					+ "financiadores, descricao) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			
			for (int i = 0; i < organization.getProjetos().size(); i++ ) {
				if(organization.getProjetos().get(i).getId() == -1){
					pstmt.setInt(1, organization.getId()  );
					pstmt.setString(2,organization.getProjetos().get(i).getTitulo());
					pstmt.setString(3,organization.getProjetos().get(i).getStatus());
					Date dtInicio = organization.getProjetos().get(i).getDataInicio();
					java.sql.Date sqlDateInicio = new java.sql.Date(dtInicio.getTime());
					pstmt.setDate(4,sqlDateInicio);
					Date dtFinal = organization.getProjetos().get(i).getDataFim();
					java.sql.Date sqlDateFinal = new java.sql.Date(dtFinal.getTime());
					pstmt.setDate(5,sqlDateFinal);
					pstmt.setDouble(6, organization.getProjetos().get(i).getValorTotal());
					pstmt.setString(7,organization.getProjetos().get(i).getFonteRecursos());
					pstmt.setString(8,organization.getProjetos().get(i).getLink());
					pstmt.setString(9,organization.getProjetos().get(i).getPublicoAlvo());
					pstmt.setString(10,organization.getProjetos().get(i).getFinanciadores());
					pstmt.setString(11,organization.getProjetos().get(i).getDescricao());
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			
			
			sql = "UPDATE data.tb_osc_projeto SET bosc_sq_osc = ?, titulo = ?, status = ?, data_inicio = ?, data_fim = ?, valor_total = ?, fonte_recurso = ?,"
					+ " link = ?, publico_alvo = ?, financiadores = ?, descricao = ? WHERE proj_cd_projetos = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			for (int i = 0; i < organization.getProjetos().size(); i++ ) {
				if(organization.getProjetos().get(i).getId() != -1){			
					pstmt.setInt(1, organization.getId()  );
					pstmt.setString(2,organization.getProjetos().get(i).getTitulo());
					pstmt.setString(3,organization.getProjetos().get(i).getStatus());
					Date dtInicio = organization.getProjetos().get(i).getDataInicio();
					java.sql.Date sqlDateInicio = new java.sql.Date(dtInicio.getTime());
					pstmt.setDate(4,sqlDateInicio);
					Date dtFinal = organization.getProjetos().get(i).getDataFim();
					java.sql.Date sqlDateFinal = new java.sql.Date(dtFinal.getTime());
					pstmt.setDate(5,sqlDateFinal);
					pstmt.setDouble(6, organization.getProjetos().get(i).getValorTotal());
					pstmt.setString(7,organization.getProjetos().get(i).getFonteRecursos());
					pstmt.setString(8,organization.getProjetos().get(i).getLink());
					pstmt.setString(9,organization.getProjetos().get(i).getPublicoAlvo());
					pstmt.setString(10,organization.getProjetos().get(i).getFinanciadores());
					pstmt.setString(11,organization.getProjetos().get(i).getDescricao());
					pstmt.setInt(12,organization.getProjetos().get(i).getId());
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			
			sql = "UPDATE data.tb_osc_convenios SET conv_publico_alvo = ? WHERE nr_convenio = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			for (int i = 0; i < organization.getConvenios().size(); i++ ) {
				if(organization.getConvenios().get(i).getNConv() != -1){			
					pstmt.setString(1,organization.getConvenios().get(i).getPublicoAlvo());
					pstmt.setInt(2,organization.getConvenios().get(i).getNConv());
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
	}
	
	public void removeDiretor(Integer id) throws RemoteException {
		logger.info("Removendo Diretor");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM data.tb_osc_diretor where tdir_sq_diretor=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
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
		logger.info("searchOSCbyUser");
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
