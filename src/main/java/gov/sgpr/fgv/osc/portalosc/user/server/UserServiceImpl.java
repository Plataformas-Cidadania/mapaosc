/**
 * 
 */
package gov.sgpr.fgv.osc.portalosc.user.server;

import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.ValidationException;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.FacebookUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.RepresentantUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;
import gov.sgpr.fgv.osc.portalosc.user.shared.validate.CpfValidator;
import gov.sgpr.fgv.osc.portalosc.user.shared.validate.EmailValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * @author victor
 * 
 */
public class UserServiceImpl extends RemoteServiceImpl implements UserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7836597805845364122L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Byte[] desKey;

	/* (non-Javadoc)
	 * @see gov.sgpr.fgv.osc.portalosc.user.server.RemoteServiceImpl#init(javax.servlet.ServletConfig)
	 */
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
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.UserService#addUser(
	 * gov.sgpr.fgv.osc.portalosc.map.shared.model.User)
	 */
	public void addUser(DefaultUser user) throws RemoteException {
		validate(user);
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO portal.tb_usuario(tpus_cd_tipo_usuario, tusu_ee_email, tusu_nm_usuario, tusu_cd_senha, "
				+ "tusu_nr_cpf, tusu_in_lista_email) VALUES (?, ?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user.getType().id());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getPassword());
			pstmt.setLong(5, user.getCpf());
			pstmt.setBoolean(6, user.isMailingListMember());
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
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
				+ "tusu_nr_cpf, tusu_in_lista_email  "
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

	@Override
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
	
	@Override
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
				+ "tusu_in_lista_email, bosc_sq_osc  "
				+ "FROM portal.tb_usuario  WHERE tusu_ee_email = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			RepresentantUser user = null;
			if (rs.next()) {
				UserType type = UserType.get(rs.getInt("tpus_cd_tipo_usuario"));
				int userId = rs.getInt("tusu_sq_usuario");

				user = new RepresentantUser();
				user.setId(userId);
				user.setType(type);
				user.setEmail(email);
				user.setName(rs.getString("tusu_nm_usuario"));
				user.setPassword(rs.getString("tusu_cd_senha"));
				user.setMailingListMember(rs.getBoolean("tusu_in_lista_email"));
				user.setOscId(rs.getInt("bosc_sq_osc"));

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
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO portal.tb_usuario(tpus_cd_tipo_usuario, tusu_ee_email, tusu_nm_usuario, tusu_cd_senha, "
				+ "tusu_in_lista_email, bosc_sq_osc) VALUES (?, ?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user.getType().id());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getPassword());
			pstmt.setBoolean(5, user.isMailingListMember());
			pstmt.setLong(6, user.getOscId());
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		
	}
		
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

}
