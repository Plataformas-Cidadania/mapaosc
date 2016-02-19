package gov.sgpr.fgv.osc.portalosc.map.shared;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.MapServiceAsync;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscCoordinate;
import vhmeirelles.gwtGeocluster.model.BoundingBox;
import vhmeirelles.gwtGeocluster.model.Coordinate;

/**
 * GWT JUnit <b>integration</b> tests must extend GWTTestCase. Using
 * <code>"GwtTest*"</code> naming pattern exclude them from running with
 * surefire during the test phase.
 * 
 * If you run the tests using the Maven command line, you will have to navigate
 * with your browser to a specific url given by Maven. See
 * http://mojo.codehaus.org/gwt-maven-plugin/user-guide/testing.html for
 * details.
 */
public class GwtTestMapService extends GWTTestCase {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private Integer size;
	private int delayPeriod;
	private int schedulePeriod;
	private int sp;
	private Integer sizeSP;
	private int ac;
	private Integer sizeAC;
	private int no;
	private Integer sizeNO;
	private int codOscSP;
	private BoundingBox bbox;
	private BoundingBox bbox1;
	private BoundingBox bbox2;
	private BoundingBox bbox3;
	private int width1;
	private int height1;
	private int width2;
	private int height2;

	/**
	 * Must refer to a valid module that sources this class.
	 */
	public String getModuleName() {
		return "gov.sgpr.fgv.osc.portalosc.map.MapJUnit";
	}

	@Override
	protected void gwtSetUp() throws Exception {
		super.gwtSetUp();
		delayPeriod = 500;
		schedulePeriod = 100;
		size = 3126;
		sizeSP = 25106;
		sp = 3550308;
		ac = 12;
		no = 1;
		sizeSP = 25106;
		sizeAC = 154;
		sizeNO = 154;
		codOscSP = 355573;
		// BoundingBox [minX=-53.068447, minY=-25.2381088655661, maxX=-39.7002,
		// maxY=-14.2263654451788]
		bbox = new BoundingBox();
		bbox.setBounds(-53.068447, -25.2381088655661, -39.7002,
				-14.2263654451788);
		// BoundingBox [minX=-42.4487882136263, minY=-22.9395041603147,
		// maxX=-42.132675878025, maxY=-22.5509320888413]
		// Width: 1903
		// Height: 870
		bbox1 = new BoundingBox();
		bbox1.setBounds(-42.4487882136263, -22.9395041603147, -42.132675878025,
				-22.5509320888413);
		width1 = 1903;
		height1 = 870;

		// INFO: ((-23.012678048360662, -42.729693365076), (-22.461723439994113,
		// -41.423006963708815))
		bbox2 = new BoundingBox();
		bbox2.setBounds(-42.729693365076, -23.012678048360662,
				-41.423006963708815, -22.461723439994113);
		// INFO: ((-23.057708077904937, -43.372682850883166),
		// (-22.782599735083657, -42.71933965019957))
		bbox3 = new BoundingBox();
		bbox3.setBounds(-43.372682850883166, -23.057708077904937,
				-42.71933965019957, -22.782599735083657);
	}

	@Override
	protected void gwtTearDown() throws Exception {
		super.gwtTearDown();
		size = null;
	}

	public void testGetOSCCoordinatesSize() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				MapServiceAsync mapService = GWT.create(MapService.class);

				AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(Integer result) {
						assertEquals(size, result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				mapService.getOSCCoordinatesSize(callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetOSCCoordinates() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				final int minValue = 1;
				final int maxValue = size / 100;
				// do some validation logic
				MapServiceAsync mapService = GWT.create(MapService.class);

				AsyncCallback<OscCoordinate[]> callback = new AsyncCallback<OscCoordinate[]>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(OscCoordinate[] result) {
						assertNotNull(result);
						assertEquals(maxValue - minValue, result.length);
						// tell the test system the test is now done
						finishTest();
					}
				};
				mapService.getOSCCoordinates(minValue, maxValue, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testOSCLocationSize() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				MapServiceAsync mapService = GWT.create(MapService.class);

				AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(Integer result) {
						assertEquals(sizeAC, result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				mapService.getOSCCoordinatesSize(no, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetOSCCoordinatesLocation() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				final int minValue = 1;
				final int maxValue = sizeNO / 100;

				// do some validation logic
				MapServiceAsync mapService = GWT.create(MapService.class);

				AsyncCallback<OscCoordinate[]> callback = new AsyncCallback<OscCoordinate[]>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(OscCoordinate[] result) {
						assertNotNull(result);
						assertEquals(maxValue - minValue, result.length);
						// tell the test system the test is now done
						finishTest();
					}
				};
				mapService.getOSCCoordinates(no, minValue, maxValue, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetOSCCoordinatesBbox() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				MapServiceAsync mapService = GWT.create(MapService.class);

				AsyncCallback<Set<OscCoordinate>> callback = new AsyncCallback<Set<OscCoordinate>>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(Set<OscCoordinate> result) {
						assertNotNull(result);
						assertEquals(206, result.size());
						// tell the test system the test is now done
						finishTest();
					}
				};
				mapService.getOSCCoordinates(bbox3, false, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetOSCCoordinatesBboxMap1() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				MapServiceAsync mapService = GWT.create(MapService.class);

				AsyncCallback<Coordinate[]> callback = new AsyncCallback<Coordinate[]>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(Coordinate[] result) {
						assertNotNull(result);
						assertEquals(1, result.length);
						// tell the test system the test is now done
						finishTest();
					}
				};
				mapService.getOSCCoordinates(bbox1, width1, height1, false,
						callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetOSCCoordinatesBboxMap2() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				MapServiceAsync mapService = GWT.create(MapService.class);

				AsyncCallback<Coordinate[]> callback = new AsyncCallback<Coordinate[]>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(Coordinate[] result) {
						assertNotNull(result);
						assertEquals(6, result.length);
						// tell the test system the test is now done
						finishTest();
					}
				};
				mapService.getOSCCoordinates(bbox2, width1, height1, false,
						callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetOSCCoordinatesBboxMap3() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				MapServiceAsync mapService = GWT.create(MapService.class);

				AsyncCallback<Coordinate[]> callback = new AsyncCallback<Coordinate[]>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(Coordinate[] result) {
						assertNotNull(result);
						assertEquals(23, result.length);
						// tell the test system the test is now done
						finishTest();
					}
				};
				mapService.getOSCCoordinates(bbox3, width1, height1, false,
						callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}
}
