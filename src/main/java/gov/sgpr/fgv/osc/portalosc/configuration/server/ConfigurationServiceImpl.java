package gov.sgpr.fgv.osc.portalosc.configuration.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import gov.sgpr.fgv.osc.portalosc.configuration.shared.model.ConfigurationModel;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.exception.ValidationException;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.validate.CpfValidator;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.validate.EmailValidator;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.exception.RemoteException;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.ConfigurationService;
import gov.sgpr.fgv.osc.portalosc.configuration.server.Email;

/**
 * @author vagnerpraia
 * 
 */
public class ConfigurationServiceImpl extends RemoteServiceImpl implements ConfigurationService {
	private static final long serialVersionUID = -5965003754375086786L;
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
	
	public void updateConfiguration(ConfigurationModel configuration, Boolean flagEmail) throws RemoteException {
		logger.info("Atualizando informações do usuário no banco de dados");
		validate(configuration);
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "UPDATE portal.tb_usuario "
				   + "SET tpus_cd_tipo_usuario = ?, tusu_ee_email = ?, tusu_nm_usuario = ?, "
				   + 	 "tusu_cd_senha = ?, tusu_nr_cpf = ?, bosc_sq_osc = ?, tusu_in_lista_email = ? "
				   + "WHERE tusu_sq_usuario = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, configuration.getTipoUsuario());
			pstmt.setString(2, configuration.getEmail());
			pstmt.setString(3, configuration.getNome());
			pstmt.setString(4, configuration.getSenha());
			pstmt.setLong(5, configuration.getCPF());
			pstmt.setInt(6, configuration.getIdOsc());
			pstmt.setBoolean(7, configuration.getListaEmail());
			pstmt.setLong(8, configuration.getId());
			pstmt.execute();
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt);
		}
		
		if(flagEmail){
			String emailOSC = searchEmailById(configuration.getIdOsc());
			logger.info("Enviando e-mail para " + emailOSC + ".");
//			email.send(emailOSC, "Informação de Cadastro Mapa das Organizações da Sociedade Civil", email.informationOSC(configuration, searchNameOSCByOSC(configuration.getIdOsc())));
			email.send("vagnerpraia@gmail.com", "Informação de Cadastro Mapa das Organizações da Sociedade Civil", email.informationOSC(configuration, searchNameOSCByOSC(configuration.getIdOsc())));
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
	
	public ConfigurationModel readConfigurationByID(Integer id) throws RemoteException {
		logger.info("Buscando usuario no banco de dados por pelo ID");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT tusu_sq_usuario, tpus_cd_tipo_usuario, tusu_ee_email, "
				   + 		"tusu_nm_usuario, tusu_cd_senha, tusu_nr_cpf, bosc_sq_osc, "
				   + 		"tusu_in_lista_email "
				   + "FROM portal.tb_usuario "
				   + "WHERE tusu_sq_usuario = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			ConfigurationModel configuration = new ConfigurationModel();
			if (rs.next()) {
				configuration.setId(rs.getInt("tusu_sq_usuario"));
				configuration.setTipoUsuario(rs.getInt("tpus_cd_tipo_usuario"));
				configuration.setEmail(rs.getString("tusu_ee_email"));
				configuration.setNome(rs.getString("tusu_nm_usuario"));
				configuration.setSenha(rs.getString("tusu_cd_senha"));
				configuration.setCPF(rs.getLong("tusu_nr_cpf"));
				configuration.setIdOsc(rs.getInt("bosc_sq_osc"));
				configuration.setListaEmail(rs.getBoolean("tusu_in_lista_email"));
			}
			return configuration;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	public ConfigurationModel readConfigurationByCPF(Long cpf, Integer id) throws RemoteException {
		logger.info("Buscando usuario no banco de dados por pelo CPF");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT tusu_sq_usuario, tpus_cd_tipo_usuario, tusu_ee_email, "
				   + 		"tusu_nm_usuario, tusu_cd_senha, tusu_nr_cpf, bosc_sq_osc, "
				   + 		"tusu_in_lista_email "
				   + "FROM portal.tb_usuario "
				   + "WHERE tusu_nr_cpf = ? "
				   + "AND tusu_sq_usuario != ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, cpf);
			pstmt.setInt(2, id);
			rs = pstmt.executeQuery();
			ConfigurationModel configuration = null;
			if (rs.next()) {
				configuration = new ConfigurationModel();
				configuration.setId(rs.getInt("tusu_sq_usuario"));
				configuration.setTipoUsuario(rs.getInt("tpus_cd_tipo_usuario"));
				configuration.setEmail(rs.getString("tusu_ee_email"));
				configuration.setNome(rs.getString("tusu_nm_usuario"));
				configuration.setSenha(rs.getString("tusu_cd_senha"));
				configuration.setCPF(rs.getLong("tusu_nr_cpf"));
				configuration.setIdOsc(rs.getInt("bosc_sq_osc"));
				configuration.setListaEmail(rs.getBoolean("tusu_in_lista_email"));
			}
			return configuration;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	public ConfigurationModel readConfigurationByEmail(String email, Integer id) throws RemoteException {
		logger.info("Buscando usuario no banco de dados por pelo e-mail");
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT tusu_sq_usuario, tpus_cd_tipo_usuario, tusu_ee_email, "
				   + 		"tusu_nm_usuario, tusu_cd_senha, tusu_nr_cpf, bosc_sq_osc, "
				   + 		"tusu_in_lista_email "
				   + "FROM portal.tb_usuario "
				   + "WHERE tusu_ee_email = ? "
				   + "AND tusu_sq_usuario != ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setInt(2, id);
			rs = pstmt.executeQuery();
			ConfigurationModel configuration = null;
			if (rs.next()) {
				configuration = new ConfigurationModel();
				configuration.setId(rs.getInt("tusu_sq_usuario"));
				configuration.setTipoUsuario(rs.getInt("tpus_cd_tipo_usuario"));
				configuration.setEmail(rs.getString("tusu_ee_email"));
				configuration.setNome(rs.getString("tusu_nm_usuario"));
				configuration.setSenha(rs.getString("tusu_cd_senha"));
				configuration.setCPF(rs.getLong("tusu_nr_cpf"));
				configuration.setIdOsc(rs.getInt("bosc_sq_osc"));
				configuration.setListaEmail(rs.getBoolean("tusu_in_lista_email"));
			}
			return configuration;
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RemoteException(e);
		} finally {
			releaseConnection(conn, pstmt, rs);
		}
	}
	
	public String readNameOSC(Integer idOSC) throws RemoteException {
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
	
	public Byte[] getEncryptKey() throws RemoteException {
		return desKey;
	}
	
	private void validate(ConfigurationModel configuration) throws RemoteException {
		logger.info("Validando configuracao");
		try {
			CpfValidator cpfValidator = new CpfValidator();
			if (!cpfValidator.validate(configuration.getCPF())) {
				throw new ValidationException("CPF inválido");
			}
			EmailValidator emailValidator = new EmailValidator();
			if (!emailValidator.validate(configuration.getEmail())) {
				throw new ValidationException("Email inválido");
			}
			if (configuration.getNome() == null || configuration.getNome().isEmpty()) {
				throw new ValidationException("Nome do usuário não preenchido");
			}
			if (configuration.getSenha() == null
					|| configuration.getSenha().isEmpty()) {
				throw new ValidationException("Senha do usuário não cadastrada");
			}
			if (configuration.getTipoUsuario() == null) {
				throw new ValidationException("Tipo do usuário não especificado.");
			}
		} catch (Exception e) {
			throw new RemoteException(e);
		}
	}
}
