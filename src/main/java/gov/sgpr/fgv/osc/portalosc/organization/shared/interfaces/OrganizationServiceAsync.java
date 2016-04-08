package gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface OrganizationServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationService
     */
    void getOrganizationByID( java.lang.Integer id, AsyncCallback<gov.sgpr.fgv.osc.portalosc.organization.shared.model.OrganizationModel> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationService
     */
    void setOrganization( gov.sgpr.fgv.osc.portalosc.organization.shared.model.OrganizationModel organization, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationService
     */
    void removeDiretor( java.lang.Integer id, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationService
     */
    void removeLocalProj( java.lang.Integer id, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationService
     */
    void removeLocalConv( java.lang.Integer id, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationService
     */
    void searchOSCbyUser( java.lang.Integer idUser, java.lang.Integer idOsc, AsyncCallback<java.lang.Boolean> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.organization.shared.interfaces.OrganizationService
     */
    void getEncryptKey( AsyncCallback<java.lang.Byte[]> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static OrganizationServiceAsync instance;

        public static final OrganizationServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (OrganizationServiceAsync) GWT.create( OrganizationService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
