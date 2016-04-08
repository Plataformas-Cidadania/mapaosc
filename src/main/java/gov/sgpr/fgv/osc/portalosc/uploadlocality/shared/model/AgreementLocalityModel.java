package gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.model;

import java.sql.Date;

import gov.sgpr.fgv.osc.portalosc.user.shared.model.DefaultUser;

public class AgreementLocalityModel extends DefaultUser {
	private static final long serialVersionUID = 262736995541241034L;
	
	private Integer numeroConvenio;
	private Date dataInicio;
	private Date dataConclusao;
	private String situacaoParceria;
	private String tipoParceria;
	private Double valorTotal;
	private Double valorPago;
	private String orgaoConcedente;
	private Long cnpjProponente;
	private String razaoSocialProponente;
	private String nomeFantasiaProponente;
	private String municipioProponente;
	private String enderecoProponente;
	private Double valorContrapartidaFinanceira;
	private String objetoParceria;
	private Integer idRepresentante;
	
	public Integer getNumeroConvenio() {
		return numeroConvenio;
	}
	public void setNumeroConvenio(Integer numeroConvenio) {
		this.numeroConvenio = numeroConvenio;
	}
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataConclusao() {
		return dataConclusao;
	}
	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}
	public String getSituacaoParceria() {
		return situacaoParceria;
	}
	public void setSituacaoParceria(String situacaoParceria) {
		this.situacaoParceria = situacaoParceria;
	}
	public String getTipoParceria() {
		return tipoParceria;
	}
	public void setTipoParceria(String tipoParceria) {
		this.tipoParceria = tipoParceria;
	}
	public Double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
	public Double getValorPago() {
		return valorPago;
	}
	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}
	public String getOrgaoConcedente() {
		return orgaoConcedente;
	}
	public void setOrgaoConcedente(String orgaoConcedente) {
		this.orgaoConcedente = orgaoConcedente;
	}
	public Long getCnpjProponente() {
		return cnpjProponente;
	}
	public void setCnpjProponente(Long cnpjProponente) {
		this.cnpjProponente = cnpjProponente;
	}
	public String getRazaoSocialProponente() {
		return razaoSocialProponente;
	}
	public void setRazaoSocialProponente(String razaoSocialProponente) {
		this.razaoSocialProponente = razaoSocialProponente;
	}
	public String getNomeFantasiaProponente() {
		return nomeFantasiaProponente;
	}
	public void setNomeFantasiaProponente(String nomeFantasiaProponente) {
		this.nomeFantasiaProponente = nomeFantasiaProponente;
	}
	public String getMunicipioProponente() {
		return municipioProponente;
	}
	public void setMunicipioProponente(String municipioProponente) {
		this.municipioProponente = municipioProponente;
	}
	public String getEnderecoProponente() {
		return enderecoProponente;
	}
	public void setEnderecoProponente(String enderecoProponente) {
		this.enderecoProponente = enderecoProponente;
	}
	public Double getValorContrapartidaFinanceira() {
		return valorContrapartidaFinanceira;
	}
	public void setValorContrapartidaFinanceira(Double valorContrapartidaFinanceira) {
		this.valorContrapartidaFinanceira = valorContrapartidaFinanceira;
	}
	public String getObjetoParceria() {
		return objetoParceria;
	}
	public void setObjetoParceria(String objetoParceria) {
		this.objetoParceria = objetoParceria;
	}
	public Integer getIdRepresentante() {
		return idRepresentante;
	}
	public void setIdRepresentante(Integer idRepresentante) {
		this.idRepresentante = idRepresentante;
	}
}
