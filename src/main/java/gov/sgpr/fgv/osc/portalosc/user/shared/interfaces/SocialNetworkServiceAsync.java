package gov.sgpr.fgv.osc.portalosc.user.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SocialNetworkServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SocialNetworkService
     */
    void addUser( gov.sgpr.fgv.osc.portalosc.user.shared.model.FacebookUser user, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SocialNetworkService
     */
    void hasUser( gov.sgpr.fgv.osc.portalosc.user.shared.model.FacebookUser user, AsyncCallback<java.lang.Boolean> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SocialNetworkService
     */
    void getAppAccessTokenAndUserInfo( java.lang.String facebookUserCode, java.lang.String urlUser, AsyncCallback<gov.sgpr.fgv.osc.portalosc.user.shared.model.FacebookUser> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SocialNetworkService
     */
    void getFacebookAppId( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.SocialNetworkService
     */
    void getFacebookuser( java.lang.String email, AsyncCallback<gov.sgpr.fgv.osc.portalosc.user.shared.model.FacebookUser> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static SocialNetworkServiceAsync instance;

        public static final SocialNetworkServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (SocialNetworkServiceAsync) GWT.create( SocialNetworkService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
