package gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces;

import gov.sgpr.fgv.osc.portalosc.configuration.shared.model.ConfigurationModel;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.exception.RemoteException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("configurationService")
public interface ConfigurationService extends RemoteService {

	public void updateConfiguration(ConfigurationModel configuration, Boolean flagEmail) throws RemoteException;
	public ConfigurationModel readConfigurationByID(Integer id) throws RemoteException;
	public ConfigurationModel readConfigurationByCPF(Long cpf, Integer id) throws RemoteException;
	public ConfigurationModel readConfigurationByEmail(String email, Integer id) throws RemoteException;
	public Byte[] getEncryptKey();
	public String readNameOSC(Integer idOSC) throws RemoteException;
}
