package gov.sgpr.fgv.osc.portalosc.staticcontent.shared.interfaces;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import gov.sgpr.fgv.osc.portalosc.staticcontent.shared.model.Content;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

@RemoteServiceRelativePath("staticContentService")
public interface StaticContentService extends  RemoteService{
	
	public Content getContentFromHash(String hash) throws RemoteException;
	
}
