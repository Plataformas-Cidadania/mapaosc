package gov.sgpr.fgv.osc.portalosc.map.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OscServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService
     */
    void getSummary( int oscId, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.OscSummary> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService
     */
    void getMainData( int oscId, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.OscMain> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService
     */
    void getWorkRelationship( int oscId, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.WorkRelationship> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService
     */
    void getCertifications( int oscId, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.Certifications> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService
     */
    void getPublicResources( int oscId, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.PublicResources> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService
     */
    void getDetail( int oscId, java.lang.String email, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.OscDetail> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService
     */
    void getCommittees( int oscId, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.Committees> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService
     */
    void setRecommendation( int oscId, java.lang.String email, boolean value, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService
     */
    void getFonteIndicadores( int[] id, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.DataSource[]> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static OscServiceAsync instance;

        public static final OscServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (OscServiceAsync) GWT.create( OscService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
