package gov.sgpr.fgv.osc.portalosc.user.server;

import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.ValidationException;
import gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SocialNetworkService;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.FacebookUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.User;

public class SocialNetworkServiceImpl extends RemoteServiceImpl implements
		SocialNetworkService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1015206477337250290L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private String facebookAppId;
	private String facebookAppSecret;
	private String facebookAppToken;
	private FacebookClient facebookClient;

	@Override
	public void init(ServletConfig config) {
		try {
			super.init(config);
			ServletContext context = getServletContext();
			logger.log(Level.INFO, context.getServerInfo());
			facebookAppId = context.getInitParameter("FACEBOOK_APP_ID");
			facebookAppSecret = context.getInitParameter("FACEBOOK_APP_SECRET");
			facebookAppToken = context.getInitParameter("FACEBOOK_APP_TOKEN");
			facebookClient = new DefaultFacebookClient(facebookAppToken,
					facebookAppSecret);

		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	public void addUser(FacebookUser user) throws RemoteException {
		validate(user);
		java.sql.Date sqlDate = new java.sql.Date(new Date().getTime());
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;

		String sqlTableUser = "INSERT INTO portal.tb_usuario(tpus_cd_tipo_usuario, tusu_ee_email, tusu_nm_usuario, tusu_cd_senha, "
				+ "tusu_in_lista_email, tusu_in_ativo, tusu_dt_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?);";
		String sqlTableNetUser = "INSERT INTO portal.tb_usuario_rede_social(ures_nm_login, reso_cd_rede_social, ures_cd_token, tusu_sq_usuario) "
				+ "VALUES (?, ?, ?, (SELECT tusu_sq_usuario FROM portal.tb_usuario WHERE tusu_ee_email = ?))";

		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sqlTableUser);
			pstmt.setInt(1, user.getType().id());
			pstmt.setString(2, SafeHtmlUtils.htmlEscape(user.getEmail()));
			pstmt.setString(3, SafeHtmlUtils.htmlEscape(user.getName()));
			pstmt.setString(4, SafeHtmlUtils.htmlEscape(user.getPassword()));
			//pstmt.setLong(5, user.getCpf());
			pstmt.setBoolean(5, user.isMailingListMember());
			pstmt.setBoolean(6, true);
			pstmt.setDate(7, sqlDate);
			pstmt.execute();

			pstmt2 = conn.prepareStatement(sqlTableNetUser);
			pstmt2.setString(1, SafeHtmlUtils.htmlEscape(user.getUid()));
			pstmt2.setInt(2, 2);
			pstmt2.setString(3, SafeHtmlUtils.htmlEscape(user.getAccessToken()));
			pstmt2.setString(4, SafeHtmlUtils.htmlEscape(user.getEmail()));
			pstmt2.execute();

			conn.commit();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			// conn.rollback();
			throw new RemoteException(e);

		} finally {
			releaseConnection(conn, pstmt, pstmt2);
		}
	}

	private void validate(FacebookUser user) throws RemoteException {
//		try {
//			facebookClient.fetchObject(user.getUid(), User.class);
//		} catch (FacebookException e) {
//			throw new ValidationException("Usuário de facebook inválido.");
//		}
		if (hasUser(user)) {
			throw new ValidationException(
					"Usuário já existe na base de dados do Mapa.");
		}
	}

	
	public boolean hasUser(FacebookUser user) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT ures_nm_login FROM portal.tb_usuario_rede_social WHERE ures_nm_login = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, SafeHtmlUtils.htmlEscape(user.getUid()));
			rs = pstmt.executeQuery();

			if (rs.next()) {
//				logger.info("user login: " + rs.getString("ures_nm_login"));
				return true;
			}
			// String email = uid + "@facebook.com";
			return hasDefaultUser(user.getEmail());
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	private boolean hasDefaultUser(String email) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT tusu_sq_usuario FROM portal.tb_usuario  WHERE tusu_ee_email = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, SafeHtmlUtils.htmlEscape(email));
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}

	
	public FacebookUser getAppAccessTokenAndUserInfo(String facebookUserCode,
			String urlUser) throws RemoteException {
		String appId = facebookAppId;
		String appSecret = facebookAppSecret;
		StringBuffer Url = new StringBuffer(
				"https://graph.facebook.com/oauth/access_token?client_id=");
		Url.append(appId);
		Url.append("&redirect_uri=");
		Url.append(urlUser);
		Url.append("&client_secret=");
		Url.append(appSecret);
		Url.append("&code=");
		Url.append(facebookUserCode);

		try {

			URL spUrl = new URL(Url.toString());
			URLConnection conn = spUrl.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine = br.readLine();
			String inputline[] = inputLine.split("&");
			String accessToken = inputline[0];
			String expires = inputline[1];
			String at[] = accessToken.split("=");
			String accesstoken = at[1];

			FacebookUser fbUser = new FacebookUser();
			FacebookClient facebookClient = new DefaultFacebookClient(
					accesstoken);
			//User user = facebookClient.fetchObject("me", User.class);
			
			User user = facebookClient.fetchObject("me", User.class,
				     Parameter.with("fields", "id,name,email"));

			fbUser.setEmail(user.getEmail());
			fbUser.setName(user.getName());
			fbUser.setUid(user.getId());
			URL photoUrl = new URL("http://graph.facebook.com/" + user.getId()
					+ "/picture?type=small");
			URLConnection connPhoto = photoUrl.openConnection();
			fbUser.setSmallPictureUrl(connPhoto.getURL().toExternalForm());
			fbUser.setAccessToken(accesstoken);

			return fbUser;

		} catch (MalformedURLException e) {
			logger.severe(e.getMessage());

			throw new RemoteException(e.getCause());
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
			throw new RemoteException(e.getCause());

		}

	}
	
	public String getFacebookAppId(){
		String appId = facebookAppId;
		return appId;
	}
	
	public FacebookUser getFacebookuser(String email) throws RemoteException {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT tusu_sq_usuario, tpus_cd_tipo_usuario, tusu_ee_email, tusu_nm_usuario, tusu_cd_senha, "
				+ "tusu_nr_cpf, tusu_in_lista_email  "
				+ "FROM portal.tb_usuario  WHERE tusu_ee_email = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, SafeHtmlUtils.htmlEscape(email));
			rs = pstmt.executeQuery();
			FacebookUser user = null;
			if (rs.next()) {
				UserType type = UserType.get(rs.getInt("tpus_cd_tipo_usuario"));
				int userId = rs.getInt("tusu_sq_usuario"); 
				if(type.equals(UserType.FACEBOOK)){
					FacebookUser userF = new FacebookUser();
					String userPhoto = getFacebookPhotoUrl(userId);
					userF.setSmallPictureUrl(userPhoto);
					user = userF;
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
}
