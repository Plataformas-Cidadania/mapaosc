package gov.sgpr.fgv.osc.portalosc.map.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService
     * @deprecated
     */
    @Deprecated
    void getOSCCoordinates( int minValue, int maxValue, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.OscCoordinate[]> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService
     * @deprecated
     */
    @Deprecated
    void getOSCCoordinatesSize( AsyncCallback<java.lang.Integer> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService
     * @deprecated
     */
    @Deprecated
    void getOSCCoordinates( int locationCode, int minValue, int maxValue, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.OscCoordinate[]> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService
     * @deprecated
     */
    @Deprecated
    void getOSCCoordinatesSize( int locationCode, AsyncCallback<java.lang.Integer> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService
     */
    void getOSCCoordinates( gov.sgpr.fgv.osc.portalosc.map.shared.model.BoundingBox bbox, int width, int height, boolean all, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.Coordinate[]> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService
     */
    void getOSCCoordinates( gov.sgpr.fgv.osc.portalosc.map.shared.model.BoundingBox bbox, int zoomLevel, boolean all, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.Coordinate[]> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService
     */
    void getOSCCoordinates( gov.sgpr.fgv.osc.portalosc.map.shared.model.BoundingBox bbox, boolean all, AsyncCallback<java.util.Set<gov.sgpr.fgv.osc.portalosc.map.shared.model.OscCoordinate>> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static MapServiceAsync instance;

        public static final MapServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (MapServiceAsync) GWT.create( MapService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
