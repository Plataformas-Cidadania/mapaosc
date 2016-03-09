package gov.sgpr.fgv.osc.portalosc.representantlocality.server;

import gov.sgpr.fgv.osc.portalosc.user.client.components.Email;
import gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.ValidationException;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.FacebookUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.RepresentantUser;
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
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class RepresentantLocalityServiceImpl extends RemoteServiceImpl implements UserService {
	private static final long serialVersionUID = -7836597805845364122L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Byte[] desKey;
	private Email email;
	
	@Override
	public void init(ServletConfig config) {
		super.init(config);
		// logger.info("OscServiceImpl.init()");
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
	
	public void addUser(DefaultUser user) throws RemoteException {
		validate(user);
		java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO portal.tb_usuario(tpus_cd_tipo_usuario, tusu_ee_email, tusu_nm_usuario, tusu_cd_senha, "
				+ "tusu_nr_cpf, tusu_in_lista_email, tusu_in_ativo, tusu_dt_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user.getType().id());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getPassword());
			pstmt.setLong(5, user.getCpf());
			pstmt.setBoolean(6, user.isMailingListMember());
			pstmt.setBoolean(7, false);
			pstmt.setDate(8, sqlDate);
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
			addToken(user.getCpf());
		}
		email.send(user.getEmail(), "Confirmação de Cadastro Mapa das Organizações da Sociedade Civil", email.confirmation(user.getName(), getToken(user.getCpf())));
	}
	
	public void addToken(long cpf) throws RemoteException {
		Connection conn = getConnection();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); 
		c.add(Calendar.DATE, 30); 
		java.sql.Date sqlDate = new java.sql.Date(c.getTime().getTime());   
		String token = md5(new Date().toString() + cpf);
		PreparedStatement pstmt = null;

		String sql = "INSERT INTO portal.tb_token (tusu_sq_usuario, tokn_cd_token, tokn_data_token) VALUES ((SELECT tusu_sq_usuario FROM portal.tb_usuario WHERE tusu_nr_cpf = ?), ?, ?);";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, cpf);
			pstmt.setString(2, token);
			pstmt.setDate(3, sqlDate);
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
	}
	
	public void addTokenPassword(Integer idUser) throws RemoteException {
		Connection conn = getConnection();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); 
		c.add(Calendar.DATE, 1); 
		java.sql.Date sqlDate = new java.sql.Date(c.getTime().getTime());   
		String token = md5(new Date().toString() + idUser);
		PreparedStatement pstmt = null;

		String sql = "INSERT INTO portal.tb_token (tusu_sq_usuario, tokn_cd_token, tokn_data_token) VALUES (?, ?, ?);";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idUser);
			pstmt.setString(2, token);
			pstmt.setDate(3, sqlDate);
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
		email.send(getEmail(idUser), "Alterar Senha", email.changePassword(getName(idUser), token));
	}
	
	public void deleteToken(Integer idUser) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM portal.tb_token where tusu_sq_usuario=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idUser);
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
	}
	
	public String getToken(long cpf) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String token = null;
		String sql = "SELECT tokn_cd_token FROM portal.tb_token WHERE tusu_sq_usuario = (SELECT tusu_sq_usuario FROM portal.tb_usuario WHERE tusu_nr_cpf = ?)";
		
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
	
	public Integer getIdToken(String token) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Integer idUsuario = null;
		String sql = "SELECT tusu_sq_usuario FROM portal.tb_token WHERE tokn_cd_token = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, token);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				idUsuario = rs.getInt("tusu_sq_usuario");
			}
			return idUsuario;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	public String getName(Integer idUser) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String nmUser = null;
		String sql = "SELECT tusu_nm_usuario FROM portal.tb_usuario WHERE tusu_sq_usuario = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idUser);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				nmUser = rs.getString("tusu_nm_usuario");
			}
			return nmUser;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	public String getEmail(Integer idUser) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String nmUser = null;
		String sql = "SELECT tusu_ee_email FROM portal.tb_usuario WHERE tusu_sq_usuario = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idUser);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				nmUser = rs.getString("tusu_ee_email");
			}
			return nmUser;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	public String getPassword(Integer idUser) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String nmUser = null;
		String sql = "SELECT tusu_cd_senha FROM portal.tb_usuario WHERE tusu_sq_usuario = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idUser);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				nmUser = rs.getString("tusu_cd_senha");
			}
			return nmUser;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	public void setPassword(Integer idUser, String password) throws RemoteException {
		java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "UPDATE portal.tb_usuario SET tusu_cd_senha=?, tusu_dt_atualizacao=? WHERE tusu_sq_usuario=?"; 
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, password);
			pstmt.setDate(2, sqlDate);
			pstmt.setInt(3, idUser);
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
	}
	
	public void enableUser(Integer idUser) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "UPDATE portal.tb_usuario SET tusu_in_ativo=? WHERE tusu_sq_usuario=?"; 
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBoolean(1, true);
			pstmt.setInt(2, idUser);
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
		email.send(getEmail(idUser), "Cadastro Confirmado!" , email.welcome(getName(idUser)));
	}
	
	public Integer usuarioAtivo(Integer idUser) throws RemoteException {
		enableUser(idUser);
		return idUser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.UserService#getUser(
	 * java.lang.String)
	 */
	public DefaultUser getUser(String email) throws RemoteException {
		logger.info("Buscando usuário na base pelo email");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT tusu_sq_usuario, tpus_cd_tipo_usuario, tusu_ee_email, tusu_nm_usuario, tusu_cd_senha, "
				   + 		"tusu_nr_cpf, tusu_in_lista_email "
				   + "FROM portal.tb_usuario  WHERE tusu_ee_email = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			DefaultUser user = null;
			if (rs.next()) {
				UserType type = UserType.get(rs.getInt("tpus_cd_tipo_usuario"));
				int userId = rs.getInt("tusu_sq_usuario"); 
				if(type.equals(UserType.FACEBOOK)){
					FacebookUser userF = new FacebookUser();
					logger.info("Buscando foto no facebook");
					String userPhoto = getFacebookPhotoUrl(userId);
					userF.setSmallPictureUrl(userPhoto);
					user = userF;
				}else{
					user = new DefaultUser();
				}
				user.setId(userId);
				user.setType(type);
				user.setEmail(email);
				user.setName(rs.getString("tusu_nm_usuario"));
				user.setPassword(rs.getString("tusu_cd_senha"));
				user.setCpf(rs.getLong("tusu_nr_cpf"));
				user.setMailingListMember(rs.getBoolean("tusu_in_lista_email"));
			}
			return user;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	public Boolean getStatus(String email) throws RemoteException {
		logger.info("Buscando status na base pelo email");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Boolean status = null;
		String sql = "SELECT tusu_in_ativo FROM portal.tb_usuario WHERE tusu_ee_email = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				status = rs.getBoolean("tusu_in_ativo");
			}
			return status;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	public DefaultUser getUser(long cpf) throws RemoteException {
		logger.info("Buscando usuário por cpf");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT tusu_sq_usuario, tpus_cd_tipo_usuario, tusu_ee_email, tusu_nm_usuario, tusu_cd_senha, "
				+ "tusu_nr_cpf, tusu_in_lista_email  "
				+ "FROM portal.tb_usuario  WHERE tusu_nr_cpf = ? ";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, cpf);
			rs = pstmt.executeQuery();
			DefaultUser user = null;
			if (rs.next()) {
				user = new DefaultUser();
				user.setId(rs.getInt("tusu_sq_usuario"));
				UserType type = UserType.get(rs.getInt("tpus_cd_tipo_usuario"));
				user.setType(type);
				user.setEmail(rs.getString("tusu_ee_email"));
				user.setName(rs.getString("tusu_nm_usuario"));
				user.setPassword(rs.getString("tusu_cd_senha"));
				user.setCpf(cpf);
				user.setMailingListMember(rs.getBoolean("tusu_in_lista_email"));
			}
			return user;
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
	 * gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.UserService#updateUser
	 * (gov.sgpr.fgv.osc.portalosc.map.shared.model.User)
	 */
	public void updateUser(DefaultUser user) throws RemoteException {
		validate(user);
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "UPDATE portal.tb_usuario SET tpus_cd_tipo_usuario=?, tusu_ee_email=?, tusu_nm_usuario=?, tusu_cd_senha=?, tusu_nr_cpf=?, tusu_in_lista_email=? WHERE tusu_sq_usuario=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user.getType().id());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getPassword());
			pstmt.setLong(5, user.getCpf());
			pstmt.setBoolean(6, user.isMailingListMember());
			pstmt.setInt(7, user.getId());
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
	}
	
	private String md5(String s) {
	    try {
	        MessageDigest m = MessageDigest.getInstance("MD5");
	        m.update(s.getBytes(), 0, s.length());
	        String hash = new BigInteger(1,m.digest()).toString(16);
	        return hash;    
	        
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	private void validate(DefaultUser user) throws RemoteException {
		logger.info("Validando usuário padrão");
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
			if (user.getPassword() == null
					|| user.getPassword().isEmpty()) {
				throw new ValidationException("Senha do usuário não cadastrada");
			}
			if (user.getType() == null) {
				throw new ValidationException(
						"Tipo do usuário não especificado.");
			}
		} catch (Exception e) {
			throw new RemoteException(e);
		}
	}
	
	public Byte[] getEncryptKey() throws RemoteException {
		return desKey;
	}
	
	private String getFacebookPhotoUrl(int userId){ 
		Connection conn = getConnection();
		PreparedStatement pstatement = null;
		ResultSet resultSet = null;
		
		String sql = "SELECT ures_nm_login FROM portal.tb_usuario_rede_social WHERE tusu_sq_usuario = ? ";
		try{
			pstatement = conn.prepareStatement(sql);
			pstatement.setInt(1, userId);
			resultSet = pstatement.executeQuery();
			FacebookUser user = null;
			if(resultSet.next()){
				user = new FacebookUser();
				long userUid = resultSet.getLong("ures_nm_login");
				user.setUid(String.valueOf(userUid));
			}
			String photoUrl = "http://graph.facebook.com/" + user.getUid()+ "/picture?type=small";
			user.setSmallPictureUrl(photoUrl);
			
			return photoUrl;
		}catch (SQLException e){
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstatement, resultSet);
		}
	}
	
	public RepresentantUser getRepresentantUser(String email)
			throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT tusu_sq_usuario, tpus_cd_tipo_usuario, tusu_ee_email, tusu_nm_usuario, tusu_cd_senha, "
				   + 		"tusu_nr_cpf, bosc_sq_osc, tusu_in_lista_email "
				   + "FROM portal.tb_usuario WHERE tusu_ee_email = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			RepresentantUser user = null;
			if (rs.next()) {
				user = new RepresentantUser();
				user.setId(rs.getInt("tusu_sq_usuario"));
				user.setType(UserType.get(rs.getInt("tpus_cd_tipo_usuario")));
				user.setEmail(email);
				user.setName(rs.getString("tusu_nm_usuario"));
				user.setPassword(rs.getString("tusu_cd_senha"));
				user.setCpf(rs.getLong("tusu_nr_cpf"));
				user.setOscId(rs.getInt("bosc_sq_osc"));
				user.setMailingListMember(rs.getBoolean("tusu_in_lista_email"));
			}
			return user;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	public void addRepresentantUser(RepresentantUser user) throws RemoteException {
		validateRepresentant(user);
		java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		
		String sql = "INSERT INTO portal.tb_usuario "
				   + 			"(tpus_cd_tipo_usuario, tusu_ee_email, tusu_nm_usuario, tusu_cd_senha, "
				   + 			"tusu_nr_cpf, bosc_sq_osc, tusu_in_lista_email, tusu_in_ativo, tusu_dt_cadastro) "
				   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user.getType().id());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getPassword());
			pstmt.setLong(5, user.getCpf());
			pstmt.setLong(6, user.getOscId());
			pstmt.setBoolean(7, user.isMailingListMember());
			pstmt.setBoolean(8, false);
			pstmt.setDate(9, sqlDate);
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
			addToken(user.getCpf());
		}
		email.send(user.getEmail(), "Confirmação de Cadastro Mapa das Organizações da Sociedade Civil", email.confirmation(user.getName(), getToken(user.getCpf())));
		
		String emailOSC = searchEmailById(user.getOscId());
		logger.info("Enviando e-mail para " + emailOSC + ".");
		email.send(emailOSC, "Informação de Cadastro Mapa das Organizações da Sociedade Civil", email.informationOSC(user, searchNameOSCByOSC(user.getOscId())));
	}
	
	private void validateRepresentant(RepresentantUser user) throws RemoteException {
		try {
			EmailValidator emailValidator = new EmailValidator();
			if (!emailValidator.validate(user.getEmail())) {
				throw new ValidationException("Email inválido");
			}
			if (user.getName() == null || user.getName().isEmpty()) {
				throw new ValidationException("Nome do usuário não preenchido");
			}
			if (user.getPassword() == null
					|| user.getPassword().isEmpty()) {
				throw new ValidationException("Senha do usuário não cadastrada");
			}
			if (user.getType() == null) {
				throw new ValidationException(
						"Tipo do usuário não especificado.");
			}
		} catch (Exception e) {
			throw new RemoteException(e);
		}
	}
	
	private String searchEmailById(Integer id) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "SELECT cont_ds_contato "
				   + "FROM portal.tb_osc_contato "
				   + "WHERE bosc_sq_osc = ? AND cont_ds_tipo_contato = 'Email'";
		
		logger.info(sql);
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			String result = "mapaosc@ipea.gov.br";
			while (rs.next()) {
				if(result != null){
					result = rs.getString("cont_ds_contato");
				}
			}
			return result;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	private String searchNameOSCByOSC(Integer idOSC) throws RemoteException {
		logger.info("Buscando informações da OSC");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT bosc_nm_osc "
				   + "FROM data.tb_osc "
				   + "WHERE bosc_sq_osc = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, idOSC);
			rs = pstmt.executeQuery();
			String result = "Nome ou CNPJ da entidade";
			if (rs.next()) {
				result = rs.getString("bosc_nm_osc");
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