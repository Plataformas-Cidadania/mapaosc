package gov.sgpr.fgv.osc.portalosc.map.shared.interfaces;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

/**
 * @author Eric Ferreira
 * 
 */
@RemoteServiceRelativePath("configService")
public interface ConfigService extends RemoteService {

	Boolean isClusterCreated() throws RemoteException;

}
