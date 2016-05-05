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
    void addToken( long cpf, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void addTokenPassword( java.lang.Integer idUser, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void deleteToken( java.lang.Integer idUser, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void getPassword( java.lang.Integer idUser, AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void getEmail( java.lang.Integer idUser, AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void usuarioAtivo( java.lang.Integer idUser, AsyncCallback<java.lang.Integer> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void getToken( long cpf, AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void getIdToken( java.lang.String token, AsyncCallback<java.lang.Integer> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void setPassword( java.lang.Integer idUser, java.lang.String password, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void enableUser( java.lang.Integer idUser, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void getStatus( java.lang.String email, AsyncCallback<java.lang.Boolean> callback );


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
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void searchUserReccomend( java.lang.Integer idUser, java.lang.Integer idOsc, AsyncCallback<java.lang.Boolean> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void insertRecommendation( java.lang.Integer idOSC, java.lang.Integer idUser, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void deleteRecommendation( java.lang.Integer idOSC, java.lang.Integer idUser, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.user.shared.interfaces.UserService
     */
    void getRecommendations( java.lang.Integer idOSC, AsyncCallback<java.lang.Integer> callback );


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
