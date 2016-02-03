package gov.sgpr.fgv.osc.portalosc.staticcontent.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StaticContentServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.staticcontent.shared.interfaces.StaticContentService
     */
    void getContentFromHash( java.lang.String hash, AsyncCallback<gov.sgpr.fgv.osc.portalosc.staticcontent.shared.model.Content> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static StaticContentServiceAsync instance;

        public static final StaticContentServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (StaticContentServiceAsync) GWT.create( StaticContentService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
