package gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RepresentantLocalityServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void addUser( gov.sgpr.fgv.osc.portalosc.representantlocality.shared.model.RepresentantLocalityUser user, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void addToken( long cpf, AsyncCallback<Void> callback );


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
