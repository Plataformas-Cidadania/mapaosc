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
    void addTokenPassword( java.lang.Integer idUser, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void deleteToken( java.lang.Integer idUser, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void getPassword( java.lang.Integer idUser, AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void getEmail( java.lang.Integer idUser, AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void usuarioAtivo( java.lang.Integer idUser, AsyncCallback<java.lang.Integer> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void getToken( long cpf, AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void getIdToken( java.lang.String token, AsyncCallback<java.lang.Integer> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void setPassword( java.lang.Integer idUser, java.lang.String password, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void enableUser( java.lang.Integer idUser, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void getStatus( java.lang.String email, AsyncCallback<java.lang.Boolean> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void getUser( java.lang.String email, AsyncCallback<gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void getUser( long cpf, AsyncCallback<gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void updateUser( gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser user, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void getEncryptKey( AsyncCallback<java.lang.Byte[]> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void getRepresentantUser( java.lang.String email, AsyncCallback<gov.sgpr.fgv.osc.portalosc.user.shared.model.RepresentantUser> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.representantlocality.shared.interfaces.RepresentantLocalityService
     */
    void addRepresentantUser( gov.sgpr.fgv.osc.portalosc.user.shared.model.RepresentantUser user, AsyncCallback<Void> callback );


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
