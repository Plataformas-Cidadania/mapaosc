package gov.sgpr.fgv.osc.portalosc.user.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void addUser( gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser user, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void getUser( java.lang.String email, AsyncCallback<gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void getUser( long cpf, AsyncCallback<gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void updateUser( gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser user, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void getEncryptKey( AsyncCallback<java.lang.Byte[]> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void getRepresentantUser( java.lang.String email, AsyncCallback<gov.sgpr.fgv.osc.portalosc.user.shared.model.RepresentantUser> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void addRepresentantUser( gov.sgpr.fgv.osc.portalosc.user.shared.model.RepresentantUser user, AsyncCallback<Void> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static UserServiceAsync instance;

        public static final UserServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (UserServiceAsync) GWT.create( UserService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
