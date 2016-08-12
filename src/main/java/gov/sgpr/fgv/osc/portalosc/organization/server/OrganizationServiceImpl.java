package gov.sgpr.fgv.osc.portalosc.organization.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import gov.sgpr.fgv.osc.portalosc.organization.shared.model.ConvenioModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.DiretorModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.LocalizacaoModel;
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
			sql = "SELECT bosc_nm_osc, bosc_nm_fantasia_osc, dcti_cd_tipo, bosc_nr_identificacao, ospr_ds_endereco, dcnj_nm_natureza_juridica, "
				+ 		 "dcsc_nm_subclasse, ospr_tx_descricao, ospr_dt_ano_fundacao, ospr_ee_site, "
				+ 		 "vl_valor_parcerias_federal, vl_valor_parcerias_estadual, vl_valor_parcerias_municipal, ee_facebook, "
				+ 		 "ee_google, ee_linkedin, ee_twitter, im_imagem, ee_como_participar "
				+ "FROM portal.vm_osc_principal "
				+ "WHERE bosc_sq_osc = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();	
			if (rs.next()) {
				organization.setRazaoSocial(rs.getString("bosc_nm_osc"));
				organization.setNomeFantasia(rs.getString("bosc_nm_fantasia_osc"));
				organization.setTipoIdentificacao(rs.getInt("dcti_cd_tipo"));
				organization.setNumeroIdentificacao(rs.getLong("bosc_nr_identificacao"));
				organization.setEndereco(rs.getString("ospr_ds_endereco"));
				organization.setNaturezaJuridica(rs.getString("dcnj_nm_natureza_juridica"));
				organization.setCnae(rs.getString("dcsc_nm_subclasse"));
				organization.setDescricaoProjeto(rs.getString("ospr_tx_descricao"));
				organization.setAnoFundacao(rs.getInt("ospr_dt_ano_fundacao"));
				organization.setSite(rs.getString("ospr_ee_site"));
				Double parceriasFederal = rs.getDouble("vl_valor_parcerias_federal");
				Double parceriasEstadual = rs.getDouble("vl_valor_parcerias_estadual");
				Double parceriasMunicipal = rs.getDouble("vl_valor_parcerias_municipal");
				organization.setValorRecursosTotal(parceriasFederal + parceriasEstadual + parceriasMunicipal);
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
			
			sql = "SELECT a.loca_ds_endereco || ', '  || b.edmu_nm_municipio || ', '  || c.eduf_sg_uf AS endereco "
				+ "FROM data.tb_localizacao a "
				+ "LEFT JOIN spat.ed_municipio b "
				+ "ON a.edmu_cd_municipio = b.edmu_cd_municipio "
				+ "LEFT JOIN spat.ed_uf c "
				+ "ON b.eduf_cd_uf = c.eduf_cd_uf "
				+ "WHERE a.bosc_sq_osc = ? "
				+ "AND a.mdfd_cd_fonte_dados = 1";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();	
			if (rs.next()) {
				organization.setEndereco(rs.getString("endereco"));
			}
			rs.close();
			pstmt.close();
			
			sql = "SELECT tdir_sq_diretor,cargo,nome "
				+ "FROM data.tb_osc_diretor "
				+ "WHERE bosc_sq_osc = ?";
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
				+ "WHERE bosc_sq_osc = ? "
				+ "AND osus_in_recomendacao = true";
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
				+ "WHERE bosc_sq_osc = ? "
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
			
			sql = "SELECT proj_cd_projeto, titulo, status, data_inicio, data_fim, valor_total, fonte_recurso, link, publico_alvo, "
				+ 		 "abrangencia, financiadores, descricao "
				+ "FROM data.tb_osc_projeto "
				+ "WHERE proj_cd_projeto IN (SELECT proj_cd_projeto FROM data.tb_ternaria_projeto WHERE bosc_sq_osc = ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			ArrayList<ProjetoModel> projetoList = new ArrayList<ProjetoModel>();
			while (rs.next()) {
				ProjetoModel projeto = new ProjetoModel();
				projeto.setId(rs.getInt("proj_cd_projeto"));
				projeto.setTitulo(rs.getString("titulo"));
				projeto.setStatus(rs.getString("status"));
				projeto.setDataInicio(rs.getDate("data_inicio"));
				projeto.setDataFim(rs.getDate("data_fim"));
				projeto.setValorTotal(rs.getDouble("valor_total"));
				projeto.setFonteRecursos(rs.getString("fonte_recurso"));
				projeto.setLink(rs.getString("link"));
				projeto.setPublicoAlvo(rs.getString("publico_alvo"));
				projeto.setAbrangencia(rs.getString("abrangencia"));
				projeto.setFinanciadores(rs.getString("financiadores"));
				projeto.setDescricao(rs.getString("descricao"));
				
				projetoList.add(projeto);
			}
			organization.setProjetos(projetoList);
			rs.close();
			pstmt.close();
			
			sql = "SELECT proj_sq_loc, ed_municipio.edmu_cd_municipio, edmu_nm_municipio, ed_regiao.edre_cd_regiao, edre_nm_regiao, ed_uf.eduf_cd_uf,  eduf_nm_uf "
					+ "FROM data.tb_osc_projeto_loc LEFT JOIN spat.ed_municipio ON (tb_osc_projeto_loc.edmu_cd_municipio = ed_municipio.edmu_cd_municipio) "
					+ "LEFT JOIN spat.ed_regiao ON (tb_osc_projeto_loc.edre_cd_regiao = ed_regiao.edre_cd_regiao) "
					+ "LEFT JOIN spat.ed_uf ON (tb_osc_projeto_loc.eduf_cd_uf = ed_uf.eduf_cd_uf) "
					+ "WHERE tb_osc_projeto_loc.proj_cd_projeto = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			for(int i = 0; i< organization.getProjetos().size(); i++){
				pstmt.setInt(1, organization.getProjetos().get(i).getId());
				rs = pstmt.executeQuery();
				ArrayList<LocalizacaoModel> localizacaoList = new ArrayList<LocalizacaoModel>();
				while (rs.next()) {
					
					LocalizacaoModel localizacao = new LocalizacaoModel();
					localizacao.setIdLocal(rs.getInt("proj_sq_loc"));
					localizacao.setIdMunicipio(rs.getInt("edmu_cd_municipio"));
					localizacao.setMunicipio(rs.getString("edmu_nm_municipio"));
					localizacao.setIdRegiao(rs.getInt("edre_cd_regiao"));
					localizacao.setRegiao(rs.getString("edre_nm_regiao"));
					localizacao.setIdUf(rs.getInt("eduf_cd_uf"));
					localizacao.setUf(rs.getString("eduf_nm_uf"));
					localizacaoList.add(localizacao);
					
				}
				organization.getProjetos().get(i).setLocalizacao(localizacaoList);
			}
			rs.close();
			pstmt.close();
				
			sql = "SELECT nr_convenio, tx_objeto_convenio, tx_situacao, dt_inicio_vigencia, dt_fim_vigencia, vl_global, nm_orgao_concedente, "
				+ 		 "tx_objeto_convenio, conv_publico_alvo, abrangencia "
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
				convenio.setAbrangencia(rs.getString("abrangencia"));
				
				conveniosList.add(convenio);
			}
			organization.setConvenios(conveniosList);
			rs.close();
			pstmt.close();
			
			sql = "SELECT conv_sq_loc, ed_municipio.edmu_cd_municipio, edmu_nm_municipio, ed_regiao.edre_cd_regiao, edre_nm_regiao, ed_uf.eduf_cd_uf,  eduf_nm_uf "
					+ "FROM data.tb_osc_convenios_loc LEFT JOIN spat.ed_municipio ON (tb_osc_convenios_loc.edmu_cd_municipio = ed_municipio.edmu_cd_municipio) "
					+ "LEFT JOIN spat.ed_regiao ON (tb_osc_convenios_loc.edre_cd_regiao = ed_regiao.edre_cd_regiao) "
					+ "LEFT JOIN spat.ed_uf ON (tb_osc_convenios_loc.eduf_cd_uf = ed_uf.eduf_cd_uf) "
					+ "WHERE tb_osc_convenios_loc.nr_convenio = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			for(int i = 0; i< organization.getConvenios().size(); i++){
				pstmt.setInt(1, organization.getConvenios().get(i).getNConv());
				rs = pstmt.executeQuery();
				ArrayList<LocalizacaoModel> localizacaoList = new ArrayList<LocalizacaoModel>();
				while (rs.next()) {
					LocalizacaoModel localizacao = new LocalizacaoModel();
					localizacao.setIdLocal(rs.getInt("conv_sq_loc"));
					localizacao.setIdMunicipio(rs.getInt("edmu_cd_municipio"));
					localizacao.setMunicipio(rs.getString("edmu_nm_municipio"));
					localizacao.setIdRegiao(rs.getInt("edre_cd_regiao"));
					localizacao.setRegiao(rs.getString("edre_nm_regiao"));
					localizacao.setIdUf(rs.getInt("eduf_cd_uf"));
					localizacao.setUf(rs.getString("eduf_nm_uf"));
					localizacaoList.add(localizacao);
				}
				organization.getConvenios().get(i).setLocalizacao(localizacaoList);;
			}
			rs.close();
			pstmt.close();
			
			sql = "SELECT cnea_dt_publicacao, cebas_mec_dt_inicio_validade, cebas_mec_dt_fim_validade, cebas_saude_dt_inicio_validade, "
				+ 		 "cebas_saude_dt_fim_validade, cnes_oscip_dt_publicacao, /*cnes_upf_dt_declaracao,*/ cebas_mds_dt_inicio_validade, "
				+ 		 "cebas_mds_dt_fim_validade "
				+ "FROM data.tb_osc_certificacao "
				+ "WHERE bosc_sq_osc = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			HashMap<String, String> certificacao = new HashMap<String, String>();			
			while (rs.next()) {
				Date now = new Date(new java.util.Date().getTime());
				
				String strDateCNAE = rs.getString("cnea_dt_publicacao");
				if(strDateCNAE != null){
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDateCNAE);
					if(now.after(date)){
						certificacao.put("Entidade Ambientalista", "CNEA/MMA");
					}
				}
				String strDateInicioCebasMec = rs.getString("cebas_mec_dt_inicio_validade");
				String strDateFimCebasMec = rs.getString("cebas_mec_dt_fim_validade");
				if(strDateInicioCebasMec != null && strDateFimCebasMec != null){
					Date dateInicioCebasMec = new SimpleDateFormat("yyyy-MM-dd").parse(strDateInicioCebasMec);
					Date dateFimCebasMec = new SimpleDateFormat("yyyy-MM-dd").parse(strDateFimCebasMec);
					if(now.after(dateInicioCebasMec) && now.before(dateFimCebasMec)){
						certificacao.put("CEBAS - Educação", "CEBAS/MEC");
					}
				}
				String strDateInicioCebasSaude = rs.getString("cebas_saude_dt_inicio_validade");
				String strDateFimCebasSaude = rs.getString("cebas_saude_dt_fim_validade");
				if(strDateInicioCebasSaude != null && strDateFimCebasSaude != null){
					Date dateInicioCebasSaude = new SimpleDateFormat("yyyy-MM-dd").parse(strDateInicioCebasSaude);
					Date dateFimCebasSaude = new SimpleDateFormat("yyyy-MM-dd").parse(strDateFimCebasSaude);
					if(now.after(dateInicioCebasSaude) && now.before(dateFimCebasSaude)){
						certificacao.put("CEBAS - Saúde", "CEBAS/MS");
					}
				}
				String strDateCnesOscip = rs.getString("cnes_oscip_dt_publicacao");
				if(strDateCnesOscip != null){
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDateCnesOscip);
					if(now.after(date)){
						certificacao.put("OSCIP", "CNES/MJ");
					}
				}
				/*String strDateCnesUpf = rs.getString("cnes_upf_dt_declaracao");
				if(strDateCnesUpf != null){
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDateCnesUpf);
					if(now.after(date)){
						certificacao.put("Utilidade Pública Federal", "CNES/MJ");
					}
				}*/
				String strDateInicioCebasMds = rs.getString("cebas_mds_dt_inicio_validade");
				String strDateFimCebasMds = rs.getString("cebas_mds_dt_fim_validade");
				if(strDateInicioCebasMds != null && strDateFimCebasMds != null){
					Date dateInicioCebasMds = new SimpleDateFormat("yyyy-MM-dd").parse(strDateInicioCebasMds);
					Date dateFimCebasMds = new SimpleDateFormat("yyyy-MM-dd").parse(strDateFimCebasMds);
					if(now.after(dateInicioCebasMds) && now.before(dateFimCebasMds)){
						certificacao.put("CEBAS - Assistência Social", "CEBAS/MDS");
					}
				}
			}
			organization.setCertificacao(certificacao);
			
						
			
			for(ConvenioModel c : conveniosList){
				if(organization.getValorParceriasFederal() < 0) organization.setValorParceriasFederal(0.0);
				organization.setValorParceriasFederal(organization.getValorParceriasFederal() + c.getValorTotal());
			}
			if(organization.getValorParceriasFederal() < 0) organization.setValorParceriasFederal(0.0);
			
//			sql = "SELECT lic_vl_captado "
//				+ "FROM data.tb_osc_lic "
//				+ "WHERE bosc_sq_osc = ?";
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setInt(1, id);
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				organization.setValorParceriasFederal(organization.getValorParceriasFederal() + rs.getInt("lic_vl_captado"));
//			}
//			rs.close();
//			pstmt.close();
			
			
			for(ProjetoModel p : projetoList){
				if(organization.getValorRecursosPrivados() < 0) organization.setValorRecursosPrivados(0.0);
				if(organization.getValorParceriasFederal() < 0) organization.setValorParceriasFederal(0.0);
				if(organization.getValorParceriasEstadual() < 0) organization.setValorParceriasEstadual(0.0);
				if(organization.getValorParceriasMunicipal() < 0) organization.setValorParceriasMunicipal(0.0);
				
				if(p.getValorTotal() >= 0.0){
					if(p.getFonteRecursos().contains("Federal")){
						organization.setValorParceriasFederal(organization.getValorParceriasFederal() + p.getValorTotal());
					}else if(p.getFonteRecursos().contains("Estadual")){
						organization.setValorParceriasEstadual(organization.getValorParceriasEstadual() + p.getValorTotal());
					}else if(p.getFonteRecursos().contains("Municipal")){
						organization.setValorParceriasMunicipal(organization.getValorParceriasMunicipal() + p.getValorTotal());
					}else{
						organization.setValorRecursosPrivados(organization.getValorRecursosPrivados() + p.getValorTotal());
					}
				}
			}
			if(organization.getValorRecursosPrivados() < 0) organization.setValorRecursosPrivados(0.0);
			if(organization.getValorParceriasFederal() < 0) organization.setValorParceriasFederal(0.0);
			if(organization.getValorParceriasEstadual() < 0) organization.setValorParceriasEstadual(0.0);
			if(organization.getValorParceriasMunicipal() < 0) organization.setValorParceriasMunicipal(0.0);
			
			organization.setValorRecursosTotal(organization.getValorParceriasFederal() + organization.getValorParceriasEstadual() + organization.getValorParceriasMunicipal() + organization.getValorRecursosPrivados());
			
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: getOrganizationByID(Integer id)");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e);
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: getOrganizationByID(Integer id)");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
		return organization;
	}
	
	public void setOrganization(OrganizationModel organization) throws RemoteException {
		logger.info("Atualizando informações da organização " + organization.getId().toString() + " no banco de dados");
		ArrayList<Integer> projetoList = new ArrayList<Integer>();
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		Connection conn2 = getConnection();
		PreparedStatement pstmt2 = null;
		
		String sql = "";
		String sql2 = "";
		
		try {
			sql = "UPDATE portal.vm_osc_principal " 
				+ "SET bosc_nm_fantasia_osc = ?, ospr_tx_descricao = ?, ospr_dt_ano_fundacao = ?, ospr_ee_site = ?, "
				+ 	  "ee_google = ?, ee_facebook = ?, ee_linkedin = ?, ee_twitter = ? "
				+ "WHERE bosc_sq_osc = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getNomeFantasia())));
			pstmt.setString(2, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getDescricaoProjeto())));
			pstmt.setInt(3, organization.getAnoFundacao());
			pstmt.setString(4, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getSite())));
			pstmt.setString(5, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getGoogle())));
			pstmt.setString(6, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getFacebook())));
			pstmt.setString(7, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getLinkedin())));
			pstmt.setString(8, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getTwitter())));
			pstmt.setLong(9, organization.getId());
			pstmt.execute();
			pstmt.close();
			
			sql = "UPDATE data.tb_osc_rais SET rais_qt_vinculo_voluntario = ? "
				+ "WHERE bosc_sq_osc = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, organization.getVoluntarios());
			pstmt.setInt(2, organization.getId());
			pstmt.execute();
			pstmt.close();
			
			sql = "UPDATE portal.tb_osc_contato SET cont_ds_contato = ?, cont_ds_tipo_contato = 'Email' "
				+ "WHERE bosc_sq_osc = ?";
						
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getEmail())));
			pstmt.setInt(2, organization.getId());
			pstmt.execute();
			pstmt.close();
			
			sql = "INSERT INTO data.tb_osc_diretor (bosc_sq_osc,cargo, nome) "
				+ "VALUES (?, ?, ?)";
						
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < organization.getDiretores().size(); i++ ) {
				if(organization.getDiretores().get(i).getId() == -1){
					pstmt.setInt(1,  organization.getId());
					pstmt.setString(2, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getDiretores().get(i).getCargo())));
					pstmt.setString(3, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getDiretores().get(i).getNome())));
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			
			sql = "UPDATE data.tb_osc_diretor SET bosc_sq_osc = ?, cargo = ?, nome = ? "
				+ "WHERE tdir_sq_diretor = ?";
			
			pstmt = conn.prepareStatement(sql);
			
			for (int i = 0; i < organization.getDiretores().size(); i++ ) {
				if(organization.getDiretores().get(i).getId() != -1){
					pstmt.setInt(1,  organization.getId());
					pstmt.setString(2, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getDiretores().get(i).getCargo())));
					pstmt.setString(3, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getDiretores().get(i).getNome())));
					pstmt.setInt(4, organization.getDiretores().get(i).getId());
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			
			sql = "INSERT INTO data.tb_osc_projeto (titulo, status, data_inicio, data_fim, valor_total, "
					+ 		 	  					   "fonte_recurso, link, publico_alvo, abrangencia, financiadores, descricao) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				
			sql2 = "INSERT INTO data.tb_osc_projeto_loc (proj_cd_projeto, edmu_cd_municipio, edre_cd_regiao, eduf_cd_uf) "
					+ "VALUES (?, ?, ?, ?);";
			
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt2 = conn2.prepareStatement(sql2);
			
			for(int i = 0; i < organization.getProjetos().size(); i++){
				if(organization.getProjetos().get(i).getId() == -1){
					pstmt.setString(1, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getTitulo())));
					pstmt.setString(2, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getStatus())));
					
					Date dtInicio = organization.getProjetos().get(i).getDataInicio();
					if(dtInicio == null){
						pstmt.setDate(3, null);
					}else{
						java.sql.Date sqlDateInicio = new java.sql.Date(dtInicio.getTime());
						pstmt.setDate(3, sqlDateInicio);
					}
					
					Date dtFinal = organization.getProjetos().get(i).getDataFim();
					if(dtFinal == null){
						pstmt.setDate(4, null);
					}else{
						java.sql.Date sqlDateFinal = new java.sql.Date(dtFinal.getTime());
						pstmt.setDate(4, sqlDateFinal);
					}
					
					pstmt.setDouble(5, organization.getProjetos().get(i).getValorTotal());
					pstmt.setString(6, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getFonteRecursos())));
					pstmt.setString(7, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getLink())));
					pstmt.setString(8, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getPublicoAlvo())));
					pstmt.setString(9, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getAbrangencia())));
					pstmt.setString(10, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getFinanciadores())));
					pstmt.setString(11, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getDescricao())));
					pstmt.executeUpdate();
					
					ResultSet rs = pstmt.getGeneratedKeys();
					int id = -1;
					while (rs.next()) {
						id = rs.getInt(1);
						projetoList.add(id);
		            } 
		            rs.close();
					
					for (int j = 0; j < organization.getProjetos().get(i).getLocalizacao().size(); j++ ) {
						pstmt2.setInt(1, id);
						if(organization.getProjetos().get(i).getLocalizacao().get(j).getIdMunicipio() == -1)
							pstmt2.setNull(2, Types.INTEGER);
						else
							pstmt2.setInt(2,organization.getProjetos().get(i).getLocalizacao().get(j).getIdMunicipio());
						if(organization.getProjetos().get(i).getLocalizacao().get(j).getIdRegiao() == -1)
							pstmt2.setNull(3, Types.INTEGER);
						else
							pstmt2.setInt(3,organization.getProjetos().get(i).getLocalizacao().get(j).getIdRegiao());
						if(organization.getProjetos().get(i).getLocalizacao().get(j).getIdUf() == -1)
							pstmt2.setNull(4, Types.INTEGER);
						else
							pstmt2.setInt(4,organization.getProjetos().get(i).getLocalizacao().get(j).getIdUf());
						pstmt2.executeUpdate();
					}
				}
			}
			pstmt.close();
			pstmt2.close();
			
			sql = "UPDATE data.tb_osc_projeto SET titulo = ?, status = ?, data_inicio = ?, data_fim = ?, "
					+ 		 "valor_total = ?, fonte_recurso = ?, "
					+ 		 "link = ?, publico_alvo = ?, abrangencia = ?, financiadores = ?, descricao = ? "
					+ "WHERE proj_cd_projeto = ?";
				
			sql2 = "INSERT INTO data.tb_osc_projeto_loc (proj_cd_projeto, edmu_cd_municipio, edre_cd_regiao, eduf_cd_uf) "
					+ "VALUES (?, ?, ?, ?);";
			
			pstmt = conn.prepareStatement(sql);
			pstmt2 = conn2.prepareStatement(sql2);
				
			for (int i = 0; i < organization.getProjetos().size(); i++ ) {
				if(organization.getProjetos().get(i).getId() != -1){
					pstmt.setString(1, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getTitulo())));
					pstmt.setString(2, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getStatus())));
					
					Date dtInicio = organization.getProjetos().get(i).getDataInicio();
					if(dtInicio == null){
						pstmt.setDate(3, null);
					}else{
						java.sql.Date sqlDateInicio = new java.sql.Date(dtInicio.getTime());
						pstmt.setDate(3, sqlDateInicio);
					}
					
					Date dtFinal = organization.getProjetos().get(i).getDataFim();
					if(dtFinal == null){
						pstmt.setDate(4, null);
					}else{
						java.sql.Date sqlDateFinal = new java.sql.Date(dtFinal.getTime());
						pstmt.setDate(4, sqlDateFinal);
					}
					
					pstmt.setDouble(5, organization.getProjetos().get(i).getValorTotal());
					pstmt.setString(6, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getFonteRecursos())));
					pstmt.setString(7, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getLink())));
					pstmt.setString(8, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getPublicoAlvo())));
					pstmt.setString(9, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getAbrangencia())));
					
					for (int j = 0; j < organization.getProjetos().get(i).getLocalizacao().size(); j++ ) {
						pstmt2.setInt(1, organization.getProjetos().get(i).getId());
						if(organization.getProjetos().get(i).getLocalizacao().get(j).getIdMunicipio() == -1)
							pstmt2.setNull(2, Types.INTEGER);
						else
							pstmt2.setInt(2,organization.getProjetos().get(i).getLocalizacao().get(j).getIdMunicipio());
						if(organization.getProjetos().get(i).getLocalizacao().get(j).getIdRegiao() == -1)
							pstmt2.setNull(3, Types.INTEGER);
						else
							pstmt2.setInt(3,organization.getProjetos().get(i).getLocalizacao().get(j).getIdRegiao());
						if(organization.getProjetos().get(i).getLocalizacao().get(j).getIdUf() == -1)
							pstmt2.setNull(4, Types.INTEGER);
						else
							pstmt2.setInt(4,organization.getProjetos().get(i).getLocalizacao().get(j).getIdUf());
						pstmt2.executeUpdate();
					}
					pstmt.setString(10, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getFinanciadores())));
					pstmt.setString(11, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getProjetos().get(i).getDescricao())));
					pstmt.setInt(12, organization.getProjetos().get(i).getId());
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			pstmt2.close();
			
			
			
			sql = "INSERT INTO data.tb_ternaria_projeto (bosc_sq_osc, proj_cd_projeto) "
				+ "VALUES (?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			for(Integer id : projetoList){			
				pstmt.setInt(1,organization.getId());
				pstmt.setInt(2, id);
				pstmt.executeUpdate();
			}
			pstmt.close();
			
			
			
			sql = "UPDATE data.tb_osc_convenios "
					+ "SET conv_publico_alvo = ?, abrangencia = ? "
					+ "WHERE nr_convenio = ?";
			
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < organization.getConvenios().size(); i++ ) {
				if(organization.getConvenios().get(i).getNConv() != -1){			
					pstmt.setString(1, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getConvenios().get(i).getPublicoAlvo())));
					pstmt.setString(2, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(organization.getConvenios().get(i).getAbrangencia())));
					pstmt.setInt(3,organization.getConvenios().get(i).getNConv());
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			
			sql = "INSERT INTO data.tb_osc_convenios_loc (nr_convenio, edmu_cd_municipio, edre_cd_regiao, eduf_cd_uf) "
					+ "VALUES (?, ?, ?, ?);";
							
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < organization.getConvenios().size(); i++ ) {
				for (int j = 0; j < organization.getConvenios().get(i).getLocalizacao().size(); j++ ) {					
					pstmt.setInt(1, organization.getConvenios().get(i).getNConv());
					if(organization.getConvenios().get(i).getLocalizacao().get(j).getIdMunicipio() == -1)
						pstmt.setNull(2, Types.INTEGER);
					else
						pstmt.setInt(2,organization.getConvenios().get(i).getLocalizacao().get(j).getIdMunicipio());
					if(organization.getConvenios().get(i).getLocalizacao().get(j).getIdRegiao() == -1)
						pstmt.setNull(3, Types.INTEGER);
					else
						pstmt.setInt(3,organization.getConvenios().get(i).getLocalizacao().get(j).getIdRegiao());
					if(organization.getConvenios().get(i).getLocalizacao().get(j).getIdUf() == -1)
						pstmt.setNull(4, Types.INTEGER);
					else
						pstmt.setInt(4,organization.getConvenios().get(i).getLocalizacao().get(j).getIdUf());
					pstmt.executeUpdate();
				}
			}
			pstmt.close();
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: setOrganization(OrganizationModel organization)");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
	}
	
	public void removeDiretor(Integer id) throws RemoteException {
		logger.info("Removendo Diretor");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM data.tb_osc_diretor "
				   + "WHERE tdir_sq_diretor=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.execute();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: removeDiretor(Integer id)");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
	}
	
	public void removeLocalProj(Integer id) throws RemoteException {
		logger.info("Removendo localização");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM data.tb_osc_projeto_loc "
				   + "WHERE proj_sq_loc = ?";
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
	
	public void removeLocalConv(Integer id) throws RemoteException {
		logger.info("Removendo localização");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM data.tb_osc_convenios_loc "
				   + "WHERE conv_sq_loc = ?";
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
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean result = false;
		String sql = "SELECT bosc_sq_osc "
				   + "FROM portal.tb_usuario "
				   + "WHERE tusu_sq_usuario = ? "
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
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: searchOSCbyUser(Integer idUser, Integer idOsc)");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
}
