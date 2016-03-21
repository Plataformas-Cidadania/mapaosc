package gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces;

import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model.RepresentantLocalityModel;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("representantLocalityService")
public interface RepresentantLocalityService extends RemoteService {

	/**
	 * Adiciona um usuário no banco de dados
	 * 
	 * @param user
	 *            usuário a ser adicionado
	 */
	public void addRepresentantLocality(RepresentantLocalityModel user) throws RemoteException;
	
	/**
	 * Adiciona o token no banco de dados
	 * 
	 * @param cpf
	 *            
	 */
	public void addToken(long cpf) throws RemoteException;
	
	/**
	 * @return Chave de criptografia
	 * @throws RemoteException
	 */
	public Byte[] getEncryptKey() throws RemoteException;
}
