package gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RepresentantLocalityServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void addRepresentantLocality( gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model.RepresentantLocalityModel user, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void getCounty( java.lang.Integer idState, AsyncCallback<java.util.List<gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model.CountyModel>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void getEncryptKey( AsyncCallback<java.lang.Byte[]> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static RepresentantLocalityServiceAsync instance;

        public static final RepresentantLocalityServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (RepresentantLocalityServiceAsync) GWT.create( RepresentantLocalityService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
