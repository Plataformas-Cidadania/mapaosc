package gov.sgpr.fgv.osc.portalosc.map.shared;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

public class GwtTestMap extends GWTTestSuite {
	  public static Test suite() {
	    TestSuite suite = new TestSuite("Test for a Maps Application");
	    suite.addTestSuite(GwtTestMapService.class); 
	    //suite.addTestSuite(GwtTestOscService.class); 
	    suite.addTestSuite(GwtTestPlaceService.class); 
	    suite.addTestSuite(GwtTestSearchService.class); 
	    return suite;
	  }
	}
