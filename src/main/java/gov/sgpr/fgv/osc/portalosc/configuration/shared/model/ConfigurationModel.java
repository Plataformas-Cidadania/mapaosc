package gov.sgpr.fgv.osc.portalosc.configuration.shared.model;

import java.io.Serializable;

/**
 * @author vagnerpraia
 * 
 *         Modelo de uma userpage
 * 
 */
public class ConfigurationModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -436498762969230574L;
	
	private Integer id = 0;
	private Long cpf = 0L;
	private String nome = "";
	private String email = "";
	private String senha = "";
	private Boolean listaEmail = false;
	private Integer tipoUsuario = 0;
	private Integer idOsc = 0;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getCPF() {
		return cpf;
	}
	public void setCPF(Long cpf) {
		this.cpf = cpf;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public Boolean getListaEmail() {
		return listaEmail;
	}
	public void setListaEmail(Boolean listaEmail) {
		this.listaEmail = listaEmail;
	}
	public Integer getTipoUsuario() {
		return tipoUsuario;
	}
	public void setTipoUsuario(Integer tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	public Integer getIdOsc() {
		return idOsc;
	}
	public void setIdOsc(Integer idOsc) {
		this.idOsc = idOsc;
	}
}