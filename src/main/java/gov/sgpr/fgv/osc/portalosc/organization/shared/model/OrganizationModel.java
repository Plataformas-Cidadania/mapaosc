package gov.sgpr.fgv.osc.portalosc.organization.shared.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author vagnerpraia
 * 
 *         Modelo de uma organização
 * 
 */
public class OrganizationModel implements Serializable {
	private static final long serialVersionUID = -436498762969230574L;
	
	private Integer id = 0;
	private String razaoSocial = "";
	private String nomeFantasia = "";
	private Long cnpj = 0L;
	private String endereco = "";
	private String naturezaJuridica = "";
	private String cnae = "";
	private String responsavel = "";
	private String descricaoProjeto = "";
	private Integer anoFundacao = 0;
	private String site = "";
	private String email = "";
	private ArrayList<String> participacaoSocial = new ArrayList<String>();
	private ArrayList<String> fonteRecursos = new ArrayList<String>();
	private Integer totalColaboradores = 0;
	private Integer trabalhadores = 0;
	private Integer voluntarios = 0;
	private Integer portadoresDeficiencia = 0;
	private Integer recomendacoes = 0;
	private ArrayList<String> classificacao = new ArrayList<String>();
	private ArrayList<String> certificadosFederais = new ArrayList<String>();
	private ArrayList<ProjetoModel> projetos = new ArrayList<ProjetoModel>();
	private ArrayList<String> certificacao = new ArrayList<String>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		if(id != null){
			this.id = id;
		}
	}
	
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		if(razaoSocial != null){
			this.razaoSocial = razaoSocial;
		}
	}
	
	public String getNomeFantasia() {
		return nomeFantasia;
	}
	public void setNomeFantasia(String nomeFantasia) {
		if(nomeFantasia != null){
			this.nomeFantasia = nomeFantasia;
		}
	}
	
	public Long getCnpj() {
		return cnpj;
	}
	public void setCnpj(Long cnpj) {
		if(cnpj != null){
			this.cnpj = cnpj;
		}
	}
	
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		if(endereco != null){
			this.endereco = endereco;
		}
	}
	
	public String getNaturezaJuridica() {
		return naturezaJuridica;
	}
	public void setNaturezaJuridica(String naturezaJuridica) {
		if(naturezaJuridica != null){
			this.naturezaJuridica = naturezaJuridica;
		}
	}
	
	public String getCnae() {
		return cnae;
	}
	public void setCnae(String cnae) {
		if(cnae != null){
			this.cnae = cnae;
		}
	}
	
	public String getResponsavel() {
		return responsavel;
	}
	public void setResponsavel(String responsavel) {
		if(responsavel != null){
			this.responsavel = responsavel;
		}
	}
	
	public String getDescricaoProjeto() {
		return descricaoProjeto;
	}
	public void setDescricaoProjeto(String descricaoProjeto) {
		if(descricaoProjeto != null){
			this.descricaoProjeto = descricaoProjeto;
		}
	}
	
	public Integer getAnoFundacao() {
		return anoFundacao;
	}
	public void setAnoFundacao(Integer anoFundacao) {
		if(anoFundacao != null){
			this.anoFundacao = anoFundacao;
		}
	}
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		if(site != null){
			this.site = site;
		}
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		if(email != null){
			this.email = email;
		}
	}
	
	public ArrayList<String> getParticipacaoSocial() {
		return participacaoSocial;
	}
	public void setParticipacaoSocial(ArrayList<String> participacaoSocial) {
		if(participacaoSocial != null){
			this.participacaoSocial = participacaoSocial;
		}
	}
	
	public ArrayList<String> getFonteRecursos() {
		return fonteRecursos;
	}
	public void setFonteRecursos(ArrayList<String> fonteRecursos) {
		if(fonteRecursos != null){
			this.fonteRecursos = fonteRecursos;
		}
	}
	
	public Integer getTotalColaboradores() {
		return totalColaboradores;
	}
	public void setTotalColaboradores(Integer totalColaboradores) {
		if(totalColaboradores != null){
			this.totalColaboradores = totalColaboradores;
		}
	}
	
	public Integer getTrabalhadores() {
		return trabalhadores;
	}
	public void setTrabalhadores(Integer trabalhadores) {
		if(trabalhadores != null){
			this.trabalhadores = trabalhadores;
		}
	}
	
	public Integer getVoluntarios() {
		return voluntarios;
	}
	public void setVoluntarios(Integer voluntarios) {
		if(voluntarios != null){
			this.voluntarios = voluntarios;
		}
	}
	
	public Integer getPortadoresDeficiencia() {
		return portadoresDeficiencia;
	}
	public void setPortadoresDeficiencia(Integer portadoresDeficiencia) {
		if(portadoresDeficiencia != null){
			this.portadoresDeficiencia = portadoresDeficiencia;
		}
	}
	
	public Integer getRecomendacoes() {
		return recomendacoes;
	}
	public void setRecomendacoes(Integer recomendacoes) {
		if(recomendacoes != null){
			this.recomendacoes = recomendacoes;
		}
	}
	
	public ArrayList<String> getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(ArrayList<String> classificacao) {
		if(classificacao != null){
			this.classificacao = classificacao;
		}
	}
	
	public ArrayList<String> getCertificadosFederais() {
		return certificadosFederais;
	}
	public void setCertificadosFederais(ArrayList<String> certificadosFederais) {
		if(certificadosFederais != null){
			this.certificadosFederais = certificadosFederais;
		}
	}
	
	public ArrayList<ProjetoModel> getProjetos() {
		return projetos;
	}
	public void setProjetos(ArrayList<ProjetoModel> projetos) {
		if(projetos != null){
			this.projetos = projetos;
		}
	}
	
	public ArrayList<String> getCertificacao() {
		return certificacao;
	}
	public void setCertificacao(ArrayList<String> certificacao) {
		if(certificacao != null){
			this.certificacao = certificacao;
		}
	}
}