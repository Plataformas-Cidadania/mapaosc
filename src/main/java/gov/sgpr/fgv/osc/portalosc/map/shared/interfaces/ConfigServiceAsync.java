package gov.sgpr.fgv.osc.portalosc.map.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConfigServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.ConfigService
     */
    void isClusterCreated( AsyncCallback<java.lang.Boolean> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static ConfigServiceAsync instance;

        public static final ConfigServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (ConfigServiceAsync) GWT.create( ConfigService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
