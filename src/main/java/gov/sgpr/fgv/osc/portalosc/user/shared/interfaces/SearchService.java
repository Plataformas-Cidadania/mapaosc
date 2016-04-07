package gov.sgpr.fgv.osc.portalosc.user.shared.interfaces;

import gov.sgpr.fgv.osc.portalosc.user.shared.model.SearchResult;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface do Serviço de busca do mapa
 * 
 * @author victor
 * 
 */
@RemoteServiceRelativePath("searchOSCService")
public interface SearchService extends RemoteService {
	/**
	 * @param criteria
	 *            Critério de busca
	 * @param resultLimit
	 *            quantidade de resultados
	 * @return
	 * @throws RemoteException
	 */
	List<SearchResult> search(String criteria, Boolean oscConfig, int resultLimit) throws RemoteException;
}