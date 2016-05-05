package gov.sgpr.fgv.osc.portalosc.map.shared.interfaces;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PlaceServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceService
     */
    void getPlaces( int placeId, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.Place[]> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceService
     */
    void getPlaces( gov.sgpr.fgv.osc.portalosc.map.shared.model.PlaceType type, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.Place[]> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceService
     */
    void getOsc( int placeId, boolean all, AsyncCallback<java.util.SortedMap<java.lang.String,java.lang.Integer>> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceService
     */
    void getPlaceInfo( int placeId, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.Place> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.PlaceService
     */
    void getPlaceAncestorsInfo( int placeId, AsyncCallback<gov.sgpr.fgv.osc.portalosc.map.shared.model.Place[]> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static PlaceServiceAsync instance;

        public static final PlaceServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (PlaceServiceAsync) GWT.create( PlaceService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
