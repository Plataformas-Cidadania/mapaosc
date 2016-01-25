package gov.sgpr.fgv.osc.portalosc.user.server;

import junit.framework.TestCase;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.UserType;

public class UserServiceImplTests extends TestCase {
	private UserServiceImpl service;
	private DefaultUser user1;
	
	protected void setUp() throws Exception {
		service = new UserServiceImpl();
		
		user1 = new DefaultUser();
		user1.setCpf(76268162188L);
		user1.setType(UserType.DEFAULT);
		user1.setEmail("user1@test.com");
		user1.setName("User 1");
		user1.setPassword("e10adc3949ba59abbe56e057f20f883e");
		user1.setMailingListMember(true);
	}
	
	public void testAddUser() {
		service.addUser(user1);
		DefaultUser userTest = service.getUser(user1.getEmail());
		assertEquals(user1.getCpf(), userTest.getCpf());
	}
	
	public void testAddToken() {
		service.addToken(user1.getCpf());
		String token = service.getToken(user1.getCpf());
		assertNotNull(token);
		DefaultUser userTest = service.getUser(user1.getEmail());
		service.deleteToken(userTest.getId());
	}
	
	public void testAddTokenPassword() {
		DefaultUser userTest = service.getUser(user1.getCpf());
		service.addTokenPassword(userTest.getId());
		String token = service.getToken(user1.getCpf());
		assertNotNull(token);
		service.deleteToken(userTest.getId());
	}
	
	public void testDeleteToken() {
		service.addToken(user1.getCpf());
		DefaultUser userTest = service.getUser(user1.getEmail());
		service.deleteToken(userTest.getId());
		String token = service.getToken(user1.getCpf());
		assertNotNull(token);
	}
	
	public void testGetToken() {
		service.addToken(user1.getCpf());
		String token = service.getToken(user1.getCpf());
		assertNotNull(token);
		DefaultUser userTest = service.getUser(user1.getEmail());
		service.deleteToken(userTest.getId());
	}
	
	public void testGetIdToken() {
		service.addToken(user1.getCpf());
		String token = service.getToken(user1.getCpf());
		Integer idToken = service.getIdToken(token);
		assertNotNull(idToken);
		DefaultUser userTest = service.getUser(user1.getEmail());
		service.deleteToken(userTest.getId());
	}
	
	public void testGetName() {
		DefaultUser userTest = service.getUser(user1.getEmail());
		String name = service.getName(userTest.getId());
		assertNotNull(name);
	}
	
	public void testGetEmail() {
		DefaultUser userTest = service.getUser(user1.getEmail());
		String email = service.getName(userTest.getId());
		assertNotNull(email);
	}
	
	public void testGetPassword(Integer idUser) {
		DefaultUser userTest = service.getUser(user1.getEmail());
		String password = service.getName(userTest.getId());
		assertNotNull(password);
	}
	
	public void testSetPassword() {
		DefaultUser userTest = service.getUser(user1.getEmail());
		service.setPassword(userTest.getId(), "c33367701511b4f6020ec61ded352059");
		
		service.setPassword(userTest.getId(), "e10adc3949ba59abbe56e057f20f883e");
	}
	
	public void testEnableUser() {
		DefaultUser userTest = service.getUser(user1.getEmail());
		service.enableUser(userTest.getId());
	}
	
	public void testUsuarioAtivo(){
		DefaultUser userTest = service.getUser(user1.getEmail());
		service.enableUser(userTest.getId());
	}
	
	public void testGetUser() {
		service.addUser(user1);
		DefaultUser userTest = service.getUser(user1.getEmail());
		assertEquals(user1.getCpf(), userTest.getCpf());
	}
	
	public void testGetStatus() {
		service.addUser(user1);
		Boolean result = service.getStatus(user1.getEmail());
		assertNotNull(result);
	}
}
