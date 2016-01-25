package gov.sgpr.fgv.osc.portalosc.configuration.server;

import junit.framework.TestCase;
import gov.sgpr.fgv.osc.portalosc.configuration.shared.model.ConfigurationModel;
import gov.sgpr.fgv.osc.portalosc.user.server.UserServiceImpl;

/**
 * @author vagnerpraia
 * 
 */
public class ConfigurationServiceImplTests extends TestCase {
	private ConfigurationServiceImpl service;
	private UserServiceImpl userService;
	private ConfigurationModel config;
	
	protected void setUp() throws Exception {
		service = new ConfigurationServiceImpl();
		userService = new UserServiceImpl();
		
		config = new ConfigurationModel();
		config.setCPF(76268162188L);
		config.setTipoUsuario(2);
		config.setEmail("user2@test.com");
		config.setNome("User 1");
		config.setSenha("e10adc3949ba59abbe56e057f20f883e");
		config.setListaEmail(true);
	}
	
	public void testUpdateConfiguration() {
		service.updateConfiguration(config, false);
		String email = userService.getUser(config.getCPF()).getEmail();
		assertEquals(config.getEmail(), email);
	}
	
	public void testReadConfigurationByID() {
		Integer id = userService.getUser(config.getCPF()).getId();
		ConfigurationModel configTest = service.readConfigurationByID(id);
		assertEquals(config.getEmail(), configTest.getEmail());
	}
	
	public void testReadConfigurationByCPF() {
		Integer id = userService.getUser(config.getCPF()).getId();
		ConfigurationModel configTest = service.readConfigurationByCPF(config.getCPF(), id);
		assertEquals(config.getEmail(), configTest.getEmail());
	}
	
	public void testReadConfigurationByEmail() {
		Integer id = userService.getUser(config.getCPF()).getId();
		ConfigurationModel configTest = service.readConfigurationByEmail(config.getEmail(), id);
		assertEquals(config.getEmail(), configTest.getEmail());
	}
	
	public void testReadNameOSC() {
		String oscTest = service.readNameOSC(699098);
		assertEquals("INSTITUTO FAZER ACONTECER", oscTest);
	}
}
