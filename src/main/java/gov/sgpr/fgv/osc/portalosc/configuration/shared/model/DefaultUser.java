package gov.sgpr.fgv.osc.portalosc.configuration.shared.model;

import java.io.Serializable;

/**
 * @author victor
 * 
 *         Representa um usuário do portal
 * 
 */
public class DefaultUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4196896517937267335L;
	
	private int id;
	private UserType type;
	private String email;
	private String name;
	private String password;
	private long cpf;
	private boolean mailingListMember;

	/**
	 * @return Identificador númerico gerado automaticamente pelo banco de dados
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            {@link #getId()}
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return Tipo de usuário
	 */
	public UserType getType() {
		return type;
	}

	/**
	 * @param type
	 *            {@link #getType()}
	 */
	public void setType(UserType type) {
		this.type = type;
	}

	/**
	 * @return Endereço de email válido do usuário. Utilizado como login de
	 *         acesso ao Mapa.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            {@link #getEmail()}
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return Nome do usuário
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            {@link #getName()}
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Senha de acesso ao Mapa criptografada.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            {@link #getPassword()}
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return CPF do usuário
	 */
	public long getCpf() {
		return cpf;
	}

	/**
	 * @param cpf
	 *            {@link #getCpf()}
	 */
	public void setCpf(long cpf) {
		this.cpf = cpf;
	}

	/**
	 * @return indicador de participação na lista de email do portal
	 */
	public boolean isMailingListMember() {
		return mailingListMember;
	}

	/**
	 * @param mailingListMember
	 *            {@link #isMailingListMember()}
	 */
	public void setMailingListMember(boolean mailingListMember) {
		this.mailingListMember = mailingListMember;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (cpf ^ (cpf >>> 32));
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + id;
		result = prime * result + (mailingListMember ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultUser other = (DefaultUser) obj;
		if (cpf != other.cpf)
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id != other.id)
			return false;
		if (mailingListMember != other.mailingListMember)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (type != other.type)
			return false;
		return true;
	}


	
	@Override
	public String toString() {
		return "User [id=" + id + ", type=" + type + ", email=" + email
				+ ", name=" + name + ", passwordMD5=" + password + ", cpf="
				+ cpf + ", mailingListMember=" + mailingListMember + "]";
	}

	
}
