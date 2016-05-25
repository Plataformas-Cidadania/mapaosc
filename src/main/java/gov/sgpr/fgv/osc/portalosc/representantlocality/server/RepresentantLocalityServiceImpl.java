package gov.sgpr.fgv.osc.portalosc.representantlocality.server;

import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService;
import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model.CountyModel;
import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model.RepresentantLocalityModel;
import gov.sgpr.fgv.osc.portalosc.user.client.components.Email;
import gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.ValidationException;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;
import gov.sgpr.fgv.osc.portalosc.user.shared.validate.CpfValidator;
import gov.sgpr.fgv.osc.portalosc.user.shared.validate.EmailValidator;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class RepresentantLocalityServiceImpl extends RemoteServiceImpl implements RepresentantLocalityService {
	private static final long serialVersionUID = -7836597805845364122L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Byte[] desKey;
	private Email email;
	
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
		String serverSMTP = context.getInitParameter("SERVER_SMTP");
		String authSMTP = context.getInitParameter("SMTP_AUTH");
		String portSMTP = context.getInitParameter("SMTP_PORT");
		String fromAddress = context.getInitParameter("FROM_ADDRESS");
		String nameAddress = context.getInitParameter("NAME_ADDRESS");
		email = new Email(serverSMTP, authSMTP, portSMTP, fromAddress, nameAddress);
	}
	
	public void addRepresentantLocality(RepresentantLocalityModel user) throws RemoteException {
		logger.info("Adicionando representante de localidades");
		validate(user);
		java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "";
		try {
			sql = "INSERT INTO portal.tb_usuario(tpus_cd_tipo_usuario, tusu_ee_email, tusu_nm_usuario, tusu_cd_senha, "
					   + 					    "tusu_nr_cpf, tusu_in_lista_email, tusu_in_ativo, tusu_dt_cadastro) "
					   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user.getType().id());
			pstmt.setString(2, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(user.getEmail())));
			pstmt.setString(3, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(user.getName())));
			pstmt.setString(4, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(user.getPassword())));
			pstmt.setLong(5, user.getCpf());
			pstmt.setBoolean(6, false);
			pstmt.setBoolean(7, false);
			pstmt.setDate(8, sqlDate);
			pstmt.execute();
			pstmt.close();
			
			if(user.getType() == UserType.LOCALITY_AGENT_STATE){
				sql = "INSERT INTO portal.tb_representante_localidade (tusu_sq_usuario, eduf_cd_uf, trlo_nm_orgao, trlo_nm_funcao, trlo_tx_telefone) "
					+ "VALUES ((SELECT tusu_sq_usuario FROM portal.tb_usuario WHERE tusu_nr_cpf = ?), ?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, user.getCpf());
				pstmt.setInt(2, user.getState());
				pstmt.setString(3, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(user.getOrgan())));
				pstmt.setString(4, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(user.getFunction())));
				pstmt.setLong(5, user.getPhone());
				pstmt.execute();
				pstmt.close();
			}else{
				sql = "INSERT INTO portal.tb_representante_localidade (tusu_sq_usuario, eduf_cd_uf, edmu_cd_municipio, trlo_nm_orgao, trlo_nm_funcao, trlo_tx_telefone) "
					+ "VALUES ((SELECT tusu_sq_usuario FROM portal.tb_usuario WHERE tusu_nr_cpf = ?), ?, ?, ?, ?, ?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setLong(1, user.getCpf());
				pstmt.setInt(2, user.getState());
				pstmt.setInt(3, user.getCounty());
				pstmt.setString(4, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(user.getOrgan())));
				pstmt.setString(5, SafeHtmlUtils.htmlEscape(StringUtils.defaultString(user.getFunction())));
				pstmt.setLong(6, user.getPhone());
				pstmt.execute();
				pstmt.close();
			}
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE, "Class: " + this.getClass().getName() + " / Method: addRepresentantLocality(RepresentantLocalityModel user)");
			logger.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
			addToken(user.getCpf());
		}
		email.send(user.getEmail(), "Confirmação de Cadastro Mapa das Organizações da Sociedade Civil", email.confirmation(user.getName(), getToken(user.getCpf())));
	}
	
	public List<CountyModel> getCounty(Integer idState){
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CountyModel> result = new ArrayList<CountyModel>();
		String sql = "SELECT edmu_cd_municipio, edmu_nm_municipio "
				   + "FROM spat.ed_municipio "
				   + "WHERE eduf_cd_uf = ?"
				   + "ORDER BY edmu_nm_municipio";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idState);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				CountyModel place = new CountyModel();
				place.setId(rs.getInt("edmu_cd_municipio"));
				place.setName(rs.getString("edmu_nm_municipio"));
				result.add(place);
			}
			return result;
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	private String getToken(long cpf) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String token = null;
		String sql = "SELECT tokn_cd_token FROM portal.tb_token "
				   + "WHERE tusu_sq_usuario = (SELECT tusu_sq_usuario FROM portal.tb_usuario WHERE tusu_nr_cpf = ?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, cpf);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				token = rs.getString("tokn_cd_token");
			}
			return token;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	private void addToken(long cpf) throws RemoteException {
		logger.info("Adicionando token");
		Connection conn = getConnection();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); 
		c.add(Calendar.DATE, 30); 
		java.sql.Date sqlDate = new java.sql.Date(c.getTime().getTime());   
		String token = md5(new Date().toString() + cpf);
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO portal.tb_token (tusu_sq_usuario, tokn_cd_token, tokn_data_token) "
				   + "VALUES ((SELECT tusu_sq_usuario FROM portal.tb_usuario WHERE tusu_nr_cpf = ?), ?, ?);";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, cpf);
			pstmt.setString(2, token);
			pstmt.setDate(3, sqlDate);
			pstmt.execute();
			pstmt.close();
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
	
	private void validate(DefaultUser user) throws RemoteException {
		logger.info("Validando representantes de localidades");
		try {
			CpfValidator cpfValidator = new CpfValidator();
			if (!cpfValidator.validate(user.getCpf())) {
				throw new ValidationException("CPF inválido");
			}
			EmailValidator emailValidator = new EmailValidator();
			if (!emailValidator.validate(user.getEmail())) {
				throw new ValidationException("Email inválido");
			}
			if (user.getName() == null || user.getName().isEmpty()) {
				throw new ValidationException("Nome do usuário não preenchido");
			}
			if (user.getPassword() == null || user.getPassword().isEmpty()) {
				throw new ValidationException("Senha do usuário não cadastrada");
			}
			if (user.getType() == null) {
				throw new ValidationException("Tipo do usuário não especificado.");
			}
		} catch (Exception e) {
			throw new RemoteException(e);
		}
	}
	
	private String md5(String s) {
	    try {
	        MessageDigest m = MessageDigest.getInstance("MD5");
	        m.update(s.getBytes(), 0, s.length());
	        String hash = new BigInteger(1, m.digest()).toString(16);
	        return hash;
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}