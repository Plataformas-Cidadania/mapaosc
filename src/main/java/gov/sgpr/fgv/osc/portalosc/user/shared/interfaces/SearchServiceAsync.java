package gov.sgpr.fgv.osc.portalosc.user.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SearchServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SearchService
     */
    void search( java.lang.String criteria, java.lang.Boolean oscConfig, int resultLimit, AsyncCallback<java.util.List<gov.sgpr.fgv.osc.portalosc.user.shared.model.SearchResult>> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static SearchServiceAsync instance;

        public static final SearchServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (SearchServiceAsync) GWT.create( SearchService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
