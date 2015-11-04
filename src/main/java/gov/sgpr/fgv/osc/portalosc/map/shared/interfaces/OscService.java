package gov.sgpr.fgv.osc.portalosc.map.shared.interfaces;

import java.util.Map;

import gov.sgpr.fgv.osc.portalosc.map.shared.model.Certifications;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.Committees;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.DataSource;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscDetail;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscMain;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscSummary;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.PublicResources;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.WorkRelationship;
import gov.sgpr.fgv.osc.portalosc.user.shared.exception.RemoteException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Interface do Serviço que busca informações sobre as OSCs
 * 
 * @author victor
 * 
 */
@RemoteServiceRelativePath("oscService")
public interface OscService extends RemoteService {

	/**
	 * @param oscId
	 *            Código da OSC no banco de dados
	 * @return O resumo da OSC.
	 * @throws RemoteException
	 */
	OscSummary getSummary(int oscId) throws RemoteException;

	/**
	 * @param oscId Código da OSC no banco de dados
	 * @return Dados gerais da OSC 
	 * @throws RemoteException
	 */
	OscMain getMainData(int oscId) throws RemoteException;

	/**
	 * @param oscId Código da OSC no banco de dados
	 * @return Dados de relações de trabalhos da OSC
	 * @throws RemoteException
	 */
	WorkRelationship getWorkRelationship(int oscId) throws RemoteException;

	/**
	 * @param oscId Código da OSC no banco de dados
	 * @return Certificações Federais obtidas pela OSC
	 * @throws RemoteException
	 */
	Certifications getCertifications(int oscId) throws RemoteException;

	/**
	 * @param oscId Código da OSC no banco de dados
	 * @return Recursos públicos obtidos pela OSC
	 * @throws RemoteException
	 */
	PublicResources getPublicResources(int oscId) throws RemoteException;
	/**
	 * @param oscId
	 *            Código da OSC no banco de dados
	 * @return O resumo da OSC.
	 * @throws RemoteException
	 */
	OscDetail getDetail(int oscId, String email) throws RemoteException;
	
	
	
	/**
	 * @param oscId Código da OSC no banco de dados
	 * @return Conselhos e Participações
	 * @throws RemoteException
	 */
	Committees getCommittees (int oscId) throws RemoteException;
	
	/**
	 * @param oscId Código da OSC no banco de dados
	 * @param email Email do cliente no banco
	 * @param value Valor da recomendacao true ou false
	 * @ Envia para o banco a recomendação
	 * @throws RemoteException
	 */
	void setRecommendation(int oscId, String email, boolean value) throws RemoteException;
	
	DataSource[] getFonteIndicadores(int[] id) throws RemoteException;
	
	
}
