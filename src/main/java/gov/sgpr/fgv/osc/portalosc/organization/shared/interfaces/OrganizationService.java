package gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces;

import gov.sgpr.fgv.osc.portalosc.organization.shared.model.OrganizationModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.exception.RemoteException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("organizationService")
public interface OrganizationService extends RemoteService {
	public OrganizationModel getOrganizationByID(Integer id) throws RemoteException;
	public void setOrganization(OrganizationModel organization) throws RemoteException;
	public Byte[] getEncryptKey();
}
