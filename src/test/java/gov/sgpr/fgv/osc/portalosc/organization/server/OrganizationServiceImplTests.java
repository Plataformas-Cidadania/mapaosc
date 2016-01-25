package gov.sgpr.fgv.osc.portalosc.organization.server;

import junit.framework.TestCase;

/**
 * @author vagnerpraia
 * 
 */
public class OrganizationServiceImplTests extends TestCase {
	private OrganizationServiceImpl service;
	
	protected void setUp() throws Exception {
		service = new OrganizationServiceImpl();
	}
	
	public void testGetOrganizationByID() {
		Long cnpj = service.getOrganizationByID(699098).getCnpj();
		assertEquals(new Long("5923642000186"), cnpj);
	}
}
