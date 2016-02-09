package gov.sgpr.fgv.osc.portalosc.staticcontent.shared.interfaces;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import gov.sgpr.fgv.osc.portalosc.staticcontent.shared.model.Content;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

/**
 * @author Gabriel
 *
 */
@RemoteServiceRelativePath("staticContentService")
public interface StaticContentService extends  RemoteService{
	
	/**
	 * @param hash
	 * @return Content
	 * @throws RemoteException
	 */
	public Content getContentFromParam(String param) throws RemoteException;
	
}
