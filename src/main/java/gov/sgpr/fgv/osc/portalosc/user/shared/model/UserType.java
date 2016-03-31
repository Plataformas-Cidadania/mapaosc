package gov.sgpr.fgv.osc.portalosc.user.shared.model;

/**
 * @author victor
 * 
 * Tipo de Usuário
 *
 */
public enum UserType {
	MASTER(1),
	DEFAULT(2),
	FACEBOOK(3),
	OSC_AGENT(4),
	LOCALITY_AGENT_STATE(5),
	LOCALITY_AGENT_COUNTY(6);
	
	private final int id;
	UserType(int id) {
		this.id = id;
	}
	
	public int id() {
		return id;
	}
	
	public static UserType get(int id) {
		for (UserType type: values()){
			if (type.id == id){
				return type;
			}
		}
		return null;
	}
}
