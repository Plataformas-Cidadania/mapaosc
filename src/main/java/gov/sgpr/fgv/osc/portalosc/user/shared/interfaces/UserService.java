/**
 * 
 */
package gov.sgpr.fgv.osc.portalosc.user.shared.interfaces;

import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.RepresentantUser;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author victor
 * 
 *         Interface de acesso ao serviço de cadastro de usuários
 * 
 */
@RemoteServiceRelativePath("userService")
public interface UserService extends RemoteService {

	/**
	 * Adiciona um usuário no banco de dados
	 * 
	 * @param user
	 *            usuário a ser adicionado
	 */
	public void addUser(DefaultUser user) throws RemoteException;
	
	/**
	 * Adiciona o token no banco de dados
	 * 
	 * @param cpf
	 *            
	 */
	public void addToken(long cpf) throws RemoteException;
	
	/**
	 * Adiciona o token no banco de dados
	 * 
	 * @param idUsser
	 *            
	 */
	public void addTokenPassword(Integer idUser) throws RemoteException;
	
	/**
	 * Exclui o token no banco de dados
	 * 
	 * @param idUser
	 *            
	 */
	public void deleteToken(Integer idUser) throws RemoteException;
	
	/**
	 * @param idUser
	 *            Id do usuário
	 * @return Password do usuário.
	 *         
	 */
	public String getPassword(Integer idUser) throws RemoteException;
	
	/**
	 * @param idUser
	 *            Id do usuário
	 * @return Email do usuário.
	 *         
	 */
	public String getEmail(Integer idUser) throws RemoteException;
	
	/**
	 * Ativa Usuario
	 * 
	 * @param idUser
	 *            
	 */
	public Integer usuarioAtivo(Integer idUser) throws RemoteException;
	
	/**
	 * @param cpf
	 *            Cpf do usuário
	 * @return token do usuário.
	 *         
	 */
	public String getToken(long cpf) throws RemoteException;
	
	/**
	 * @param token
	 *            
	 * @return Id do usuário.
	 *         
	 */
	public Integer getIdToken(String token) throws RemoteException;
	
	/**
	 * Altera senha
	 * 
	 * @param idUser, password
	 *            
	 */
	public void setPassword(Integer idUser, String password) throws RemoteException;
	
	/**
	 * Ativa usuário
	 * 
	 * @param idUser
	 *            
	 */
	public void enableUser(Integer idUser) throws RemoteException;
	
	/**
	 * @param email
	 *            Email do usuário
	 * @return Status do usuário.
	 *         
	 */
	public Boolean getStatus(String email) throws RemoteException;

	/**
	 * @param email
	 *            Endereço de email do usuário
	 * @return usuário cadastrado no portal. NULL se não existir usuário com
	 *         este email.
	 */
	public DefaultUser getUser(String email) throws RemoteException;

	/**
	 * @param cpf
	 *            CPF do usuário
	 * @return usuário cadastrado no portal. NULL se não existir usuário com
	 *         este email.
	 */
	public DefaultUser getUser(long cpf) throws RemoteException;

	/**
	 * Atualiza as informações de um usuário no banco de dados
	 * 
	 * @param user
	 *            usuário a ser atualizado
	 */
	public void updateUser(DefaultUser user) throws RemoteException;

	/**
	 * @return Chave de criptografia
	 * @throws RemoteException
	 */
	public Byte[] getEncryptKey() throws RemoteException;
	
	public  RepresentantUser getRepresentantUser(String email) throws RemoteException;

	/**
	 * Adiciona um usuário no banco de dados
	 * 
	 * @param user
	 *           representante OSC a ser cadastrado
	 */
	public void addRepresentantUser(RepresentantUser user) throws RemoteException;
	
	public Boolean searchUserReccomend(Integer idUser, Integer idOsc) throws RemoteException;
	public void insertRecommendation(Integer idOSC, Integer idUser) throws RemoteException;
	public void deleteRecommendation(Integer idOSC, Integer idUser) throws RemoteException;
	public Integer getRecommendations(Integer idOSC) throws RemoteException;
}
