package gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConfigurationServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.ConfigurationService
     */
    void updateConfiguration( gov.sgpr.fgv.osc.portalosc.configuration.shared.model.ConfigurationModel configuration, java.lang.Boolean flagEmail, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.ConfigurationService
     */
    void readConfigurationByID( java.lang.Integer id, AsyncCallback<gov.sgpr.fgv.osc.portalosc.configuration.shared.model.ConfigurationModel> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.ConfigurationService
     */
    void readConfigurationByCPF( java.lang.Long cpf, java.lang.Integer id, AsyncCallback<gov.sgpr.fgv.osc.portalosc.configuration.shared.model.ConfigurationModel> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.ConfigurationService
     */
    void readConfigurationByEmail( java.lang.String email, java.lang.Integer id, AsyncCallback<gov.sgpr.fgv.osc.portalosc.configuration.shared.model.ConfigurationModel> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.ConfigurationService
     */
    void getEncryptKey( AsyncCallback<java.lang.Byte[]> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.configuration.shared.interfaces.ConfigurationService
     */
    void readNameOSC( java.lang.Integer idOSC, AsyncCallback<java.lang.String> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static ConfigurationServiceAsync instance;

        public static final ConfigurationServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (ConfigurationServiceAsync) GWT.create( ConfigurationService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
