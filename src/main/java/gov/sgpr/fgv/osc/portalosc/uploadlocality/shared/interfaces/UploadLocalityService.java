package gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.interfaces;

import gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.model.AgreementLocalityModel;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("uploadLocalityService")
public interface UploadLocalityService extends RemoteService {
	public void uploadFile(AgreementLocalityModel agreement) throws RemoteException;
}
