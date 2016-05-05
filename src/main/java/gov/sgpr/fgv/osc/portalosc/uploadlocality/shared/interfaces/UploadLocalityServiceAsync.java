package gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UploadLocalityServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.interfaces.UploadLocalityService
     */
    void uploadFile( gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.model.AgreementLocalityModel agreement, AsyncCallback<Void> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static UploadLocalityServiceAsync instance;

        public static final UploadLocalityServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (UploadLocalityServiceAsync) GWT.create( UploadLocalityService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
