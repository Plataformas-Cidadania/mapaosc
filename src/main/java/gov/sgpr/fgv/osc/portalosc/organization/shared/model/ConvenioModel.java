package gov.sgpr.fgv.osc.portalosc.organization.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author vagnerpraia
 * 
 *         Modelo de um projeto
 * 
 */
public class ConvenioModel implements Serializable {
	private static final long serialVersionUID = 2546767997069093925L;
	private ArrayList<LocalizacaoModel> localizacao = new ArrayList<LocalizacaoModel>();
	private Integer nConv = -1;
	private String titulo = "";
	private String status = "";
	private Date dataInicio = null;
	private Date dataFim = null;
	private Double valorTotal = -1.0;
	private String fonteRecursos = "";
	private String publicoAlvo = "";
	private String abrangencia = "";
	private String financiadores = "";
	private String descricao = "";
	
	
	public Integer getNConv() {
		return nConv;
	}
	
	public void setNConv(Integer nConv) {
		if(nConv != null){
			this.nConv = nConv;
		}
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}
	
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public Date getDataFim() {
		return dataFim;
	}
	
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public Double getValorTotal() {
		return valorTotal;
	}
	
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public String getFonteRecursos() {
		return fonteRecursos;
	}
	
	public void setFonteRecursos(String fonteRecursos) {
		this.fonteRecursos = fonteRecursos;
	}
	
	public String getPublicoAlvo() {
		return publicoAlvo;
	}
	
	public void setPublicoAlvo(String publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}
	
	public String getAbrangencia() {
		return abrangencia;
	}
	
	public void setAbrangencia(String abrangencia) {
		this.abrangencia = abrangencia;
	}
	
	public ArrayList<LocalizacaoModel> getLocalizacao() {
		return localizacao;
	}
	
	public void setLocalizacao(ArrayList<LocalizacaoModel> localizacao) {
		if(localizacao != null){
			this.localizacao = localizacao;
		}
	}
	
	public String getFinanciadores() {
		return financiadores;
	}
	
	public void setFinanciadores(String financiadores) {
		this.financiadores = financiadores;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
