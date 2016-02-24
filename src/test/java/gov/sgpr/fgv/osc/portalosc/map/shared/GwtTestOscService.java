package gov.sgpr.fgv.osc.portalosc.map.shared;

import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscService;
import gov.sgpr.fgv.osc.portalosc.map.shared.interfaces.OscServiceAsync;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.Certifications;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.Committees;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.DataSource;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscDetail;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscMain;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.OscSummary;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.PublicResources;
import gov.sgpr.fgv.osc.portalosc.map.shared.model.WorkRelationship;

import java.sql.Date;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
public class GwtTestOscService extends GWTTestCase {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private int delayPeriod;
	private int schedulePeriod;
	private String email;
	private Boolean recommendation;
	private int oscCode1;
	private int oscCode2;
	private int oscCode3;
	private OscSummary summary1;
	private OscSummary summary2;
	private OscSummary summary3;

	/**
	 * Must refer to a valid module that sources this class.
	 */
	public String getModuleName() {
		return "gov.sgpr.fgv.osc.portalosc.map.MapJUnit";
	}

	@Override
	protected void gwtSetUp() throws Exception {
		super.gwtSetUp();
		delayPeriod = 1000;
		schedulePeriod = 200;
		// 192964;19719103000134;1;"ASSOCIACAO BENEFICENTE SAO CAMILO";"";"De 20 a 49";37;1;83572.54
		oscCode1 = 192964;
		summary1 = new OscSummary();
		summary1.setId(192964);
		summary1.setName("ASSOCIACAO BENEFICENTE SAO CAMILO");
		summary1.setRecomendations(1);
		summary1.setLength("De 20 a 49");
		summary1.setPartnerships(1);
		summary1.setGlobalValue(83572.54);
		summary1.setLegalTypeDescription("Associação Privada");
		summary1.setFoundationYear(0);
		//summary1.setCommitteeParticipant(false);
		//summary1.setEncourageLawValue(0.0);

		Certifications certification1 = new Certifications();
		certification1.setCneaPublication(null);
		certification1.setCebasMecBeginning(null);
		certification1.setCebasMecEnd(null);
		certification1.setCebasSusBeginning(null);
		certification1.setCebasSusEnd(null);
		certification1.setCebasSusBeginning(null);
		certification1.setCebasMdsEnd(null);
		certification1.setOscipPublication(null);
		certification1.setUpfDeclaration(new Date(97, 02, 25));

		DataSource datasource1 = new DataSource();
		DataSource[] dataSources1 = new DataSource[1];
		datasource1.setId(2);
		datasource1.setAcronym("MJ/CNES/UPF");
		datasource1
				.setName("Cadastro Nacional de Entidades de Utilidade Pública do Ministério da Justiça - Utilidade Pública Federal");
		datasource1.setAcquisitionDate(new Date(113, 0, 01));
		datasource1.setSiteURL("http://portal.mj.gov.br");
		dataSources1[0] = datasource1;
		certification1.setDataSources(dataSources1);
		summary1.setCertifications(certification1);

		DataSource[] Sources1 = new DataSource[3];
		DataSource source1 = new DataSource();
		source1.setId(1);
		source1.setAcronym("MTE/RAIS");
		source1.setName("Relação Anual de Informações Sociais do Ministério do trabalho e Emprego");
		source1.setAcquisitionDate(new Date(113, 00, 01));
		source1.setSiteURL("http://www.rais.gov.br");
		Sources1[0] = source1;

		source1 = new DataSource();
		source1.setId(2);
		source1.setAcronym("MJ/CNES/UPF");
		source1.setName("Cadastro Nacional de Entidades de Utilidade Pública do Ministério da Justiça - Utilidade Pública Federal");
		source1.setAcquisitionDate(new Date(113, 00, 01));
		source1.setSiteURL("http://portal.mj.gov.br");
		Sources1[1] = source1;

		source1 = new DataSource();
		source1.setId(13);
		source1.setAcronym("MPOG/SICONV");
		source1.setName("Sistema de Convênios do Ministério do Planejamento, Orçamento e Gestão ");
		source1.setAcquisitionDate(new Date(113, 02, 26));
		source1.setSiteURL("https://convenios.gov.br/siconv/");
		Sources1[2] = source1;
		summary1.setDataSources(Sources1);

		// 197350;22628044000101;1;"SANTA CASA DE MISERICORDIA DE MTE SANTO";"";"De 20 a 49";43;2;307050
		oscCode2 = 197350;
		summary2 = new OscSummary();
		summary2.setId(197350);
		summary2.setName("SANTA CASA DE MISERICORDIA DE MTE SANTO");
		summary2.setRecomendations(0);
		summary2.setLength("De 20 a 49");
		summary2.setPartnerships(2);
		summary2.setGlobalValue(307050.0);
		summary2.setLegalTypeDescription("Associação Privada");
		summary2.setFoundationYear(0);
		//summary2.setCommitteeParticipant(false);
		//summary2.setEncourageLawValue(0.0);

		Certifications certification2 = new Certifications();
		DataSource datasource2 = new DataSource();
		DataSource[] dataSources2 = new DataSource[1];

		certification2.setCneaPublication(null);
		certification2.setCebasMecBeginning(null);
		certification2.setCebasMecEnd(null);
		certification2.setCebasSusBeginning(null);
		certification2.setCebasSusEnd(null);
		certification2.setCebasSusBeginning(null);
		certification2.setCebasMdsEnd(null);
		certification2.setOscipPublication(null);
		certification2.setUpfDeclaration(new Date(91, 06, 03));

		datasource2.setId(2);
		datasource2.setAcronym("MJ/CNES/UPF");
		datasource2
				.setName("Cadastro Nacional de Entidades de Utilidade Pública do Ministério da Justiça - Utilidade Pública Federal");
		datasource2.setAcquisitionDate(new Date(113, 0, 01));
		datasource2.setSiteURL("http://portal.mj.gov.br");
		dataSources2[0] = datasource2;
		certification2.setDataSources(dataSources2);
		summary2.setCertifications(certification2);

		DataSource[] Sources2 = new DataSource[3];
		DataSource source2 = new DataSource();
		source2.setId(1);
		source2.setAcronym("MTE/RAIS");
		source2.setName("Relação Anual de Informações Sociais do Ministério do trabalho e Emprego");
		source2.setAcquisitionDate(new Date(113, 00, 01));
		source2.setSiteURL("http://www.rais.gov.br");
		Sources2[0] = source2;

		source2 = new DataSource();
		source2.setId(2);
		source2.setAcronym("MJ/CNES/UPF");
		source2.setName("Cadastro Nacional de Entidades de Utilidade Pública do Ministério da Justiça - Utilidade Pública Federal");
		source2.setAcquisitionDate(new Date(113, 00, 01));
		source2.setSiteURL("http://portal.mj.gov.br");
		Sources2[1] = source2;

		source2 = new DataSource();
		source2.setId(13);
		source2.setAcronym("MPOG/SICONV");
		source2.setName("Sistema de Convênios do Ministério do Planejamento, Orçamento e Gestão ");
		source2.setAcquisitionDate(new Date(113, 02, 26));
		source2.setSiteURL("https://convenios.gov.br/siconv/");
		Sources2[2] = source2;
		summary2.setDataSources(Sources2);

		// 194854;20041620000186;1;"ASSOCIACAO BRAS. CRIADORES DE GIROLANDO";"";"De 20 a 49";48;3;451630
		oscCode3 = 194854;
		summary3 = new OscSummary();
		summary3.setId(194854);
		summary3.setName("ASSOCIACAO BRAS. CRIADORES DE GIROLANDO");
		summary3.setRecomendations(0);
		summary3.setLength("De 20 a 49");
		summary3.setPartnerships(3);
		summary3.setGlobalValue(451630.0);
		summary3.setLegalTypeDescription("Associação Privada");
		summary3.setFoundationYear(0);
		//summary3.setCommitteeParticipant(false);
		//summary3.setEncourageLawValue(0.0);

		Certifications certification3 = new Certifications();
		certification3.setCneaPublication(null);
		certification3.setCebasMecBeginning(null);
		certification3.setCebasMecEnd(null);
		certification3.setCebasSusBeginning(null);
		certification3.setCebasSusEnd(null);
		certification3.setCebasSusBeginning(null);
		certification3.setCebasMdsEnd(null);
		certification3.setOscipPublication(null);
		certification3.setUpfDeclaration(null);

		DataSource[] dataSources3 = {};
		certification3.setDataSources(dataSources3);
		summary3.setCertifications(certification3);

		DataSource[] Sources3 = new DataSource[2];
		DataSource source3 = new DataSource();
		source3.setId(1);
		source3.setAcronym("MTE/RAIS");
		source3.setName("Relação Anual de Informações Sociais do Ministério do trabalho e Emprego");
		source3.setAcquisitionDate(new Date(113, 00, 01));
		source3.setSiteURL("http://www.rais.gov.br");
		Sources3[0] = source3;

		source3 = new DataSource();
		source3.setId(13);
		source3.setAcronym("MPOG/SICONV");
		source3.setName("Sistema de Convênios do Ministério do Planejamento, Orçamento e Gestão ");
		source3.setAcquisitionDate(new Date(113, 02, 26));
		source3.setSiteURL("https://convenios.gov.br/siconv/");
		Sources3[1] = source3;
		summary3.setDataSources(Sources3);

		email = "augustoccb@ig.com.br";
		recommendation = true;
	}

	@Override
	protected void gwtTearDown() throws Exception {
		super.gwtTearDown();
	}

	public void testGetSummary1() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				OscServiceAsync oscService = GWT.create(OscService.class);

				AsyncCallback<OscSummary> callback = new AsyncCallback<OscSummary>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(OscSummary result) {
						assertEquals(summary1, result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				oscService.getSummary(oscCode1, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetSummary2() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				OscServiceAsync oscService = GWT.create(OscService.class);

				AsyncCallback<OscSummary> callback = new AsyncCallback<OscSummary>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(OscSummary result) {
						assertEquals(summary2, result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				oscService.getSummary(oscCode2, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetSummary3() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				OscServiceAsync oscService = GWT.create(OscService.class);

				AsyncCallback<OscSummary> callback = new AsyncCallback<OscSummary>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(OscSummary result) {
						assertEquals(summary3, result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				oscService.getSummary(oscCode3, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetDetail() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				OscServiceAsync oscService = GWT.create(OscService.class);

				AsyncCallback<OscDetail> callback = new AsyncCallback<OscDetail>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(OscDetail result) {
						assertNotNull(result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				oscService.getDetail(oscCode1, email, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetCommittees() {

		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				OscServiceAsync oscService = GWT.create(OscService.class);

				AsyncCallback<Committees> callback = new AsyncCallback<Committees>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(Committees result) {
						assertNotNull(result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				oscService.getCommittees(oscCode1, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetMain() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				OscServiceAsync oscService = GWT.create(OscService.class);

				AsyncCallback<OscMain> callback = new AsyncCallback<OscMain>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(OscMain result) {
						assertNotNull(result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				oscService.getMainData(oscCode1, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetWorkRelationship() {

		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				OscServiceAsync oscService = GWT.create(OscService.class);

				AsyncCallback<WorkRelationship> callback = new AsyncCallback<WorkRelationship>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(WorkRelationship result) {
						assertNotNull(result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				oscService.getWorkRelationship(oscCode1, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetCertifications() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				OscServiceAsync oscService = GWT.create(OscService.class);

				AsyncCallback<Certifications> callback = new AsyncCallback<Certifications>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(Certifications result) {
						assertNotNull(result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				oscService.getCertifications(oscCode1, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testGetPublicResources() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				OscServiceAsync oscService = GWT.create(OscService.class);

				AsyncCallback<PublicResources> callback = new AsyncCallback<PublicResources>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(PublicResources result) {
						assertNotNull(result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				oscService.getPublicResources(oscCode1, callback);
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(delayPeriod);

		// Schedule the event and return control to the test system.
		timer.schedule(schedulePeriod);
	}

	public void testSetRecommendation() {
		// Setup an asynchronous event handler.
		Timer timer = new Timer() {
			public void run() {
				// do some validation logic
				OscServiceAsync oscService = GWT.create(OscService.class);

				AsyncCallback<Void> callback = new AsyncCallback<Void>() {
					public void onFailure(Throwable caught) {
						logger.log(Level.SEVERE, caught.getMessage());
						caught.printStackTrace();
					}

					public void onSuccess(Void result) {
						assertNull(result);
						// tell the test system the test is now done
						finishTest();
					}
				};
				oscService.setRecommendation(oscCode1, email, recommendation,
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
