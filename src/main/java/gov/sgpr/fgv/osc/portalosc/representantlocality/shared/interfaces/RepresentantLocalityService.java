package gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces;

import java.util.List;

import gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model.CountyModel;
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
	
	public List<CountyModel> getCounty(Integer idState) throws RemoteException;
	
	/**
	 * @return Chave de criptografia
	 * @throws RemoteException
	 */
	public Byte[] getEncryptKey() throws RemoteException;
}
