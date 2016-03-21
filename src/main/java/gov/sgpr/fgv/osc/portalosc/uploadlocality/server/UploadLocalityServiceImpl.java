package gov.sgpr.fgv.osc.portalosc.uploadlocality.server;

import gov.sgpr.fgv.osc.portalosc.configuration.server.RemoteServiceImpl;
import gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.interfaces.UploadLocalityService;
import gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.model.AgreementLocalityModel;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

public class UploadLocalityServiceImpl extends RemoteServiceImpl implements UploadLocalityService {
	private static final long serialVersionUID = 6487133703022070014L;

	@Override
	public void uploadFile(AgreementLocalityModel agreement) throws RemoteException {
		
	}
}