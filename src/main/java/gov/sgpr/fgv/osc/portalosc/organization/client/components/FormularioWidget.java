package gov.sgpr.fgv.osc.portalosc.organization.client.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import org.gwtbootstrap3.extras.datepicker.client.ui.DatePicker;
import org.gwtbootstrap3.extras.datepicker.client.ui.base.constants.DatePickerLanguage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

import gov.sgpr.fgv.osc.portalosc.organization.shared.model.ConvenioModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.DiretorModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.OrganizationModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.ProjetoModel;

public class FormularioWidget extends Composite {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	public Integer dir = 0;
	public Integer proj = 0;
	public Integer conv = 0;
	public Integer count = 0;
	public Boolean edit = true;
		
	public FormularioWidget(OrganizationModel organizationModel, Boolean editable){
		initWidget(getHTML(organizationModel, editable));
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		magneticLink(conv, "linkconv");
		magneticLink(proj, "linkproj");
	}
	
	private HTML getHTML(OrganizationModel org, Boolean editable){
		StringBuilder htmlBuilder = new StringBuilder();
		edit = editable;
		htmlBuilder.append("<div class='container'>");
		htmlBuilder.append("	<div class='social'>");
		htmlBuilder.append("		<div class='imagem'>");
		htmlBuilder.append("			<img src='imagens/osc/" + (org.getGoogle() != "" ? org.getGoogle() + "' alt='" + org.getRazaoSocial() + "' width='160' height='160'" : "org_indisponivel.jpg'") + " />");
		htmlBuilder.append("		</div>");
		htmlBuilder.append("		<div class='redes'>");
		htmlBuilder.append("			<a href='" + (org.getGoogle() != "" ? org.getGoogle() : "#O" + org.getId().toString()) + "'><button type='button' id='b-google' name='redes' title='Google' class='tooltip'></button></a>");
		htmlBuilder.append("			<a href='" + (org.getFacebook() != "" ? org.getGoogle() : "#O" + org.getId().toString()) + "'><button type='button' id='b-facebook' name='redes' title='Facebook' class='tooltip'></button></a>");
		htmlBuilder.append("			<a href='" + (org.getLinkedin() != "" ? org.getGoogle() : "#O" + org.getId().toString()) + "'><button type='button' id='b-linkedin' name='redes' title='LinkedIn' class='tooltip' ></button></a>");
		htmlBuilder.append("			<a href='" + (org.getTwitter() != "" ? org.getGoogle() : "#O" + org.getId().toString()) + "'><button type='button' id='b-twitter' name='redes' title='Twitter' class='tooltip'></button></a>");
		htmlBuilder.append("		</div>");
		htmlBuilder.append("		<a href='" + (org.getComoParticipar() != "" ? org.getComoParticipar() : "#O" + org.getId().toString()) + "' class='participar box'>Como Participar</a>");
		htmlBuilder.append("		<div class='recomendacoes'>");
		htmlBuilder.append("			<span class='tooltip' title='" + org.getRecomendacoes() + " recomendações'>" + org.getRecomendacoes() + "</span>");
		htmlBuilder.append("		</div>");
//		htmlBuilder.append("		<button type='button' class='recomendar'>Recomendar</button>");
//		htmlBuilder.append("		<div class='classificacao'>");
//		htmlBuilder.append("			<h3>Classificação</h3>");
//		htmlBuilder.append("			<ul class='dados'>");
//		htmlBuilder.append("				<li class='fasfil'><a href='#'>Educação e pesquisa</a></li>");
//		htmlBuilder.append("			</ul>");
//		htmlBuilder.append("		</div>");
//		htmlBuilder.append("		<div class='tags'>");
//		htmlBuilder.append("			<img src='imagens/tags.jpg' alt='Nuvem de tags' />");
//		htmlBuilder.append("		</div>");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("	<div class='tabela'>");
		htmlBuilder.append("		<div class='dados'>");
		htmlBuilder.append("			<h2>Dados gerais</h2>");
		htmlBuilder.append("			<div class='gerais'>");
		htmlBuilder.append("				<fieldset>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Razão Social:</strong>");
		if(org.getRazaoSocial() == null || org.getRazaoSocial() == "")
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + org.getRazaoSocial() + "</span>");
//		htmlBuilder.append("						<input type='text' name='razao_social' id='razao_social' placeholder='Informação não disponível' value='" + org.getRazaoSocial() + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Nome Fantasia:</strong>");
		htmlBuilder.append("						<input type='text' name='nome_fantasia' id='nome_fantasia' placeholder='Informação não disponível' value='" + org.getNomeFantasia() + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>CNPJ:</strong>");
		if(org.getCnpj() == null || org.getCnpj() == -1L)
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + convertNumberToString(org.getCnpj()) + "</span>");
//		htmlBuilder.append("						<input type='text' name='cnpj' id='cnpj' placeholder='Informação não disponível' value='" + convertNumberToString(org.getCnpj()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Endereço:</strong>");
		if(org.getEndereco() == null || org.getEndereco() == ""){
			htmlBuilder.append("						<span id='endereco' title='Endereço'>Informação não disponível</span>");
		}else{
			htmlBuilder.append("						<a href='" + GWT.getHostPageBaseURL() + "Map.html#O" + org.getId().toString() + "'><span id='endereco' title='Endereço'>" + org.getEndereco() + "</span></a>");
		}
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
//		htmlBuilder.append("					<div>");
//		htmlBuilder.append("						<strong>Outros Endereços:</strong>");
//		htmlBuilder.append("						<a href='Lista.html?grupoOSC=123'>visualizar Lista</a>");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
//		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Natureza Jurídica:</strong>");
		if(org.getNaturezaJuridica() == null || org.getNaturezaJuridica() == "")
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + org.getNaturezaJuridica() + "</span>");
//		htmlBuilder.append("						<input type='text' name='natureza_juridica' id='natureza_juridica' placeholder='Informação não disponível' value='" + org.getNaturezaJuridica() + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>CNAE:</strong>");
		if(org.getCnae() == null || org.getCnae() == "")
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + org.getCnae() + "</span>");
//		htmlBuilder.append("						<input type='text' name='cnae' id='cnae' placeholder='Informação não disponível' value='" + org.getCnae() + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("					<span class='fonte_de_dados dado_oficial' title='Dado Oficial, fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Responsável Legal:</strong>");
		if(org.getResponsavel() == null || org.getResponsavel() == "")
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + org.getResponsavel() + "</span>");
//		htmlBuilder.append("						<input type='text' name='responsavel' id='responsavel' placeholder='Informação não disponível' value='" + org.getResponsavel() + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Descrição do Projeto:</strong>");
		htmlBuilder.append("						<textarea name='descricao_projeto' id='descricao_projeto' placeholder='Informação não disponível' " + (editable ? "" : "readonly") + ">" + org.getDescricaoProjeto() + "</textarea>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Ano de Fundação:</strong>");
		htmlBuilder.append("						<input type='number' name='ano_fundacao' id='ano_fundacao' placeholder='Informação não disponível' value='" + convertNumberToString(org.getAnoFundacao()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Site:</strong>");
		htmlBuilder.append("						<input type='text' name='site' id='site' placeholder='Informação não disponível' value='" + org.getSite() + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>E-mail:</strong>");
		htmlBuilder.append("						<input type='text' name='email' id='email' placeholder='Informação não disponível' value='" + org.getEmail() + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		
//		htmlBuilder.append("					<div>");
//		htmlBuilder.append("						<strong>Participação social:</strong>");
//		htmlBuilder.append("						<ul id='social' class='participacao'>");
//		if(org.getParticipacaoSocial().size() == 0){
//			htmlBuilder.append("<li>");
//			htmlBuilder.append("<select name='oconselhos'>");
//			htmlBuilder.append("<option value='' selected></option>");
//			htmlBuilder.append("<option value='privada'>CONJUVE</option>");
//			htmlBuilder.append("<option value='doacao'>CONAJE</option>");
//			htmlBuilder.append("<option value='publica'>CONSELHO</option>");
//			htmlBuilder.append("</select>");
//			htmlBuilder.append("<div class='botoes'><button type='button' class='excluir participacao'>Excluir</button></div>");
//			htmlBuilder.append("<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
//			htmlBuilder.append("</li>");
//			htmlBuilder.append("<li id='addsocial' class='botoes'><button type='button' class='adicionar participacao'>Adicionar</button></li>");
//		}else{
//			for (String s : org.getParticipacaoSocial()) {
//				htmlBuilder.append("							<li>");
//				htmlBuilder.append("								<span class='visualizacao_dados'>" + s + "</span>");
//				htmlBuilder.append("								<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
//				htmlBuilder.append("							</li>");
//			}
//		}
//		htmlBuilder.append("						</ul>");
//		htmlBuilder.append("					</div>");
//		htmlBuilder.append("					<div>");
//		htmlBuilder.append("						<strong>Fonte de recursos:</strong>");
//		htmlBuilder.append("						<ul id='fonte' class='recursos'>");
//		if(org.getFonteRecursos().size() == 0){
//			htmlBuilder.append("<li>");
//			htmlBuilder.append("<select name='erecursos'>");
//			htmlBuilder.append("<option value='' selected></option>");
//			htmlBuilder.append("<option value='privada'>Privada</option>");
//			htmlBuilder.append("<option value='doacao'>Doação</option>");
//			htmlBuilder.append("<option value='publica'>Pública</option>");
//			htmlBuilder.append("</select>");
//			htmlBuilder.append("<div class='botoes'><button type='button' class='excluir participacao'>Excluir</button></div>");
//			htmlBuilder.append("<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
//			htmlBuilder.append("</li>");
//			htmlBuilder.append("<li id='addRecursos' class='botoes'><button type='button' class='adicionar participacao'>Adicionar</button></li>");
//		}else{
//			for (String s : org.getFonteRecursos()) {
//				htmlBuilder.append("							<li>");
//				htmlBuilder.append("								<span class='visualizacao_dados'>" + s + "</span>"); // FALTA BANCO
//				htmlBuilder.append("								<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
//				htmlBuilder.append("							</li>");
//			}
//		}
//		htmlBuilder.append("						</ul>");
//		htmlBuilder.append("					</div>");
		htmlBuilder.append("				</fieldset>");
		htmlBuilder.append("			</div>");
		
		htmlBuilder.append(" <h2>Quadro de Diretores <button type='button' class='adicionar' id='addDiretor' style='float:right;margin-top:3px;margin-right: 3px;'>Adicionar</button></h2>");
		htmlBuilder.append(" <fieldset>");
		htmlBuilder.append(" <div id='diretores' class='diretores'>");
		if(org.getDiretores().size() == 0){
			htmlBuilder.append(" <div style='margin: 10px;'>");
			htmlBuilder.append(" <span id='indDir' >Informação não disponível</span>");
			htmlBuilder.append(" </div>");
		}else{
			for (DiretorModel d : org.getDiretores()){
				dir++;
				htmlBuilder.append(" <div id='incluirDir'>");
				htmlBuilder.append("<input type='text' name='"+ d.getId() +"' id='cargo"+ dir +"' value='"+ d.getCargo() +"'  " + (editable ? "" : "readonly") + " />");
				htmlBuilder.append("<input type='text' name='"+ d.getId() +"' id='nome"+ dir +"' value='"+ d.getNome() +"'  " + (editable ? "" : "readonly") + " />");
				htmlBuilder.append("<div class='botoes'><button id='removedir"+ dir +"' value='"+ d.getId() +"' type='button' class='excluir participacao'>Excluir</button></div>");
				htmlBuilder.append(" </div>");
			}
		}	
		htmlBuilder.append(" </div>");
		htmlBuilder.append(" </fieldset>");
		
		htmlBuilder.append("			<h2>Colaboradores</h2>");
		htmlBuilder.append("			<fieldset>");
		htmlBuilder.append("				<div class='recursos collapsable'>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Total de Colaboradores:</strong>");
		if(org.getTotalColaboradores() == null || org.getTotalColaboradores() == -1)
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + convertNumberToString(org.getTotalColaboradores()) + "</span>");
//		htmlBuilder.append("						<input type='text' name='total_colaboradores' id='total_colaboradores' placeholder='Informação não disponível' value='" + convertNumberToString(org.getTotalColaboradores()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("						<div id='recolherCol' class='collapse-click collapse-button' data-toggle='collapse' data-target='#recCol' ><div></div></div>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div id='recCol' class='collapse' >");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Trabalhadores:</strong>");
		if(org.getTrabalhadores() == null || org.getTrabalhadores() == -1)
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + convertNumberToString(org.getTrabalhadores()) + "</span>");
//		htmlBuilder.append("							<input type='text' name='trabalhadores' id='trabalhadores' placeholder='Informação não disponível' value='" + convertNumberToString(org.getTrabalhadores()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Voluntários:</strong>");
		htmlBuilder.append("							<input type='number' name='voluntarios' id='voluntarios' placeholder='Informação não disponível' value='" + convertNumberToString(org.getVoluntarios()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Colaboradores portadores de deficiência:</strong>");
		if(org.getPortadoresDeficiencia() == null || org.getPortadoresDeficiencia() == -1)
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + convertNumberToString(org.getPortadoresDeficiencia()) + "</span>");
//		htmlBuilder.append("							<input type='text' name='colaboradores_portadores_deficiencia' id='colaboradores_portadores_deficiencia' placeholder='Informação não disponível' value='" + convertNumberToString(org.getPortadoresDeficiencia()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("				</div>");
		htmlBuilder.append("			</fieldset>");
		htmlBuilder.append("			<h2>Recursos públicos</h2>");
		htmlBuilder.append("			<fieldset>");
		htmlBuilder.append("				<div class='recursos collapsable'>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Valor total das parcerias (R$):</strong>");
		if(org.getValorParceriasTotal() == null || org.getValorParceriasTotal() == -1.0)
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + convertNumberToCurrencyString(org.getValorParceriasTotal()) + "</span>");
//		htmlBuilder.append("						<input type='text' name='valor_total_parcerias' id='valor_total_parcerias' placeholder='Informação não disponível' value='" + convertNumberToCurrencyString(org.getValorParceriasTotal()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte SINAFI'></span>");
		htmlBuilder.append("						<div id='recolherParc' class='collapse-click collapse-button' data-toggle='collapse' data-target='#recParc' ><div></div></div>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div id='recParc' class='collapse'>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Valor das parcerias federais (R$):</strong>");
		if(org.getValorParceriasFederal() == null || org.getValorParceriasFederal() == -1.0)
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + convertNumberToCurrencyString(org.getValorParceriasFederal()) + "</span>");
//		htmlBuilder.append("							<input type='text' name='valor_parcerias_federais' id='valor_parcerias_federais' placeholder='Informação não disponível' value='" + convertNumberToCurrencyString(org.getValorParceriasFederal()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Valor das parcerias estaduais (R$):</strong>");
		if(org.getValorParceriasEstadual() == null || org.getValorParceriasEstadual() == -1.0)
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + convertNumberToCurrencyString(org.getValorParceriasEstadual()) + "</span>");
//		htmlBuilder.append("							<input type='text' name='valor_parcerias_estaduais' id='valor_parcerias_estaduais' placeholder='Informação não disponível' value='" + convertNumberToCurrencyString(org.getValorParceriasEstadual()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Valor das parcerias municipais (R$):</strong>");
		if(org.getValorParceriasMunicipal() == null || org.getValorParceriasMunicipal() == -1.0)
			htmlBuilder.append("						<span>Informação não disponível</span>");
		else
			htmlBuilder.append("						<span>" + convertNumberToCurrencyString(org.getValorParceriasMunicipal()) + "</span>");
//		htmlBuilder.append("							<input type='text' name='valor_parcerias_municipais' id='valor_parcerias_municipais' placeholder='Informação não disponível' value='" + convertNumberToCurrencyString(org.getValorParceriasMunicipal()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("				</div>");
		htmlBuilder.append("			</fieldset>");
		htmlBuilder.append("		</div>");
		htmlBuilder.append("		<div class='docs'>");
//		htmlBuilder.append("			<h2>Últimas atualizações <a href='atualizacoes.html' class='box'>Ver todas</a></h2>");
//		htmlBuilder.append("			<div>");
//		htmlBuilder.append("				<ul>");
//		htmlBuilder.append("					<li>Recebeu <a href='#'>10 novas recomendações</a> <em class='data'>6 set 2013</em></li>");
//		htmlBuilder.append("					<li>Adicionou o projeto <a href='#'>Lorem Ipsum dolor sit amet</a> <em class='data'>5 set 2013</em></li>");
//		htmlBuilder.append("					<li>Recebeu <a href='#'>50 novas recomendações</a><em class='data'>20 jul 2013</em></li>");
//		htmlBuilder.append("				</ul>");
//		htmlBuilder.append("			</div>");
//		htmlBuilder.append("			<h2>Documentos</h2>");
//		htmlBuilder.append("			<div class='documentos'>");
//		htmlBuilder.append("				<ul class='dados'>");
//		htmlBuilder.append("					<li class='incluidos'>(100% incluídos)</li>");
//		htmlBuilder.append("					<li><a href='#'>Estatuto</a> (2,4 MB)</li>");
//		htmlBuilder.append("					<li><a href='#'>Quadro de dirigentes</a> (210 KB)</li>");
//		htmlBuilder.append("					<li><a href='#'>Convênios</a> (1,3 MB)</li>");
//		htmlBuilder.append("					<li><a href='#'>Prestação de contas</a> (940 KB)</li>");
//		htmlBuilder.append("					<li class='fonte'><em>Fonte:</em> SICONV</li>");
//		htmlBuilder.append("				</ul>");
//		htmlBuilder.append("			</div>");
		htmlBuilder.append("			<h2>Certificados federais</h2>");
		htmlBuilder.append("			<div>");
		htmlBuilder.append("				<ul class='dados checklist'>");
		if(org.getCertificacao().size() == 0){
			htmlBuilder.append("					<li>Organização sem certificados</li>");
		}else{
			for (String s : org.getCertificacao()) {
				htmlBuilder.append("					<li>" + s + "</li>");
//				htmlBuilder.append("					<li class='fonte'><em>Fonte:</em> SICONV</li>");
			}
		}
		htmlBuilder.append("				</ul>");
		htmlBuilder.append("			</div>");
		htmlBuilder.append("		</div>");
		
		htmlBuilder.append("		<div id='addFormProj' class='projetos'>");
		htmlBuilder.append("			<div class='titulo'>");
		htmlBuilder.append("				<h2>Projetos</h2>");
		htmlBuilder.append("					<button id='addProjetos' type='button' class='adicionar'>Adicionar</button>");
//		htmlBuilder.append("				<div class='filtro'>");
//		htmlBuilder.append("					<span>Fontes:</span>");
//		htmlBuilder.append("					<span>");
//		htmlBuilder.append("						<label for='PUBLICA'>Pública</label>");
//		htmlBuilder.append("						<input type='checkbox' name='PUBLICA' " + (editable ? "" : "readonly") + "/>");
//		htmlBuilder.append("					</span>");
//		htmlBuilder.append("					<span>");
//		htmlBuilder.append("						<label for='PRIVADO'>Privada</label>");
//		htmlBuilder.append("						<input type='checkbox' name='PRIVADO' " + (editable ? "" : "readonly") + "/>");
//		htmlBuilder.append("					</span>");
//		htmlBuilder.append("					<span>");
//		htmlBuilder.append("						<label for='PROPRIA'>Própria</label>");
//		htmlBuilder.append("						<input type='checkbox' name='PROPRIA' " + (editable ? "" : "readonly") + "/>");
//		htmlBuilder.append("					</span>");
//		htmlBuilder.append("				</div>");
		htmlBuilder.append("			</div>");
		
		if(org.getProjetos().size() == 0 && org.getConvenios().size() == 0){
			htmlBuilder.append("			<div class='clearfix projeto collapsable'>");
			htmlBuilder.append("				<span id='indProj' title='projeto'>Informação não disponível</span>");
			htmlBuilder.append("			</div>");
		}else{
			for (ProjetoModel p : org.getProjetos()){
				proj++;
				htmlBuilder.append("			<div class='clearfix projeto collapsable'>");
				htmlBuilder.append("				<div class='projeto_header'>");
				htmlBuilder.append("					<div id='recolherProj' class='collapse-click collapse-button' data-toggle='collapse' data-target='#recProj"+ proj +"' ><div></div></div>");
				htmlBuilder.append("						<div class='nome'>");	
				htmlBuilder.append("							<span class='no_margin_right'>" + (org.getProjetos().indexOf(p) + 1) + " |</span>");
				if(p.getTitulo() == null)
					htmlBuilder.append("							<span class='no_margin titulo_projeto'><input type='text' name='"+ p.getId() +"' id='projeto_nome_projeto"+ proj +"' placeholder='Informação não disponível' value='' " + (editable ? "" : "readonly") + "/></span>");
				else
					htmlBuilder.append("							<span class='no_margin titulo_projeto'><input type='text' name='"+ p.getId() +"' id='projeto_nome_projeto"+ proj +"' placeholder='Informação não disponível' value='" + p.getTitulo() + "' " + (editable ? "" : "readonly") + "/></span>");
				htmlBuilder.append("						</div>");
				htmlBuilder.append("						<span class='clear_both'></span>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div id='recProj"+ proj +"' class='collapse'>");
				htmlBuilder.append("						<div class='linha clearfix'>");
				
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador left-radius'>Status</strong>");
				htmlBuilder.append("<p id='projeto_status"+ proj +"' >");
				htmlBuilder.append("<select " + (editable ? "" : "disabled") + ">");
				if(p.getStatus() == "Planejado"){
//					htmlBuilder.append("<option value='' ></option>");
					htmlBuilder.append("<option value='Planejado' selected='selected'> Planejado </option>");
					htmlBuilder.append("<option value='Em Execução' > Em Execução </option>");
					htmlBuilder.append("<option value='Finalizado' > Finalizado </option>");
				}else if(p.getStatus() == "Em Execução"){
//					htmlBuilder.append("<option value='' ></option>");
					htmlBuilder.append("<option value='Planejado' > Planejado </option>");
					htmlBuilder.append("<option value='Em Execução' selected='selected' > Em Execução </option>");
					htmlBuilder.append("<option value='Finalizado' > Finalizado </option>");
				}else if(p.getStatus() == "Finalizado"){
//					htmlBuilder.append("<option value='' ></option>");
					htmlBuilder.append("<option value='Planejado' > Planejado </option>");
					htmlBuilder.append("<option value='Em Execução' > Em Execução </option>");
					htmlBuilder.append("<option value='Finalizado' selected='selected' > Finalizado </option>");
				}else{
					htmlBuilder.append("<option value='' disabled selected>Informação não disponível</option>");
					htmlBuilder.append("<option value='Planejado' > Planejado </option>");
					htmlBuilder.append("<option value='Em Execução' > Em Execução </option>");
					htmlBuilder.append("<option value='Finalizado' > Finalizado </option>");
				}
				htmlBuilder.append("</select>");
				htmlBuilder.append("</p>");
				htmlBuilder.append("</div>");
				
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador'>Data Início</strong>");
				htmlBuilder.append("								<p id='data_inicio"+ proj +"' ></p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador'>Data Final</strong>");
				htmlBuilder.append("								<p id='data_final"+ proj +"' ></p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador'>Valor Total (R$)</strong>");
				htmlBuilder.append("								<p><input type='text' name='projeto_valor_total' id='projeto_valor_total"+proj+"' placeholder='Informação não disponível' value='" + convertNumberToCurrencyString(p.getValorTotal()) + "'  " + (editable ? "" : "readonly") + " /></p>");
				htmlBuilder.append("							</div>");
				
				htmlBuilder.append("<div class='col1_6'>");
				htmlBuilder.append("<strong class='separador'>Fonte de recurso</strong>");
				htmlBuilder.append("<p id='projeto_fonte"+ proj +"' ><select  " + (editable ? "" : "disabled") + ">");
				if(p.getFonteRecursos() == "Público"){
//					htmlBuilder.append("<option value='' >Informação não disponível</option>");
					htmlBuilder.append("<option value='Público' selected='selected' > Público </option>");
					htmlBuilder.append("<option value='Privado' > Privado </option>");
				}else if(p.getFonteRecursos() == "Privado"){
//					htmlBuilder.append("<option value='' ></option>");
					htmlBuilder.append("<option value='Público' > Público </option>");
					htmlBuilder.append("<option value='Privado' selected='selected' > Privado </option>");
				}else{
					htmlBuilder.append("<option value='' disabled selected>Informação não disponível</option>");
					htmlBuilder.append("<option value='Público' > Público </option>");
					htmlBuilder.append("<option value='Privado' > Privado </option>");
				}
				htmlBuilder.append("</select>");
				htmlBuilder.append("</p>");
				htmlBuilder.append("</div>");
				
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='right-radius'>Link</strong>");					
				if(editable == false){
					if(p.getLink() == "link para página" || p.getLink() == "" || p.getLink() == null)
						htmlBuilder.append("<span>Informação não disponível</span>");
					else{
						htmlBuilder.append("<p>");
						htmlBuilder.append("<span id='linkproj"+proj+"' title='' class='menuTooltip'><span>Você será redirecionado para a página do Projeto</span>");
						htmlBuilder.append("<a id='link"+ proj +"' href='http://" + p.getLink() + "' target='_blank' " + (editable ? "contenteditable='true'" : "contenteditable='false'") + " contenteditable='true' >link para página</a>");
						htmlBuilder.append("</span>");
						htmlBuilder.append("</p>");
					}
				}else{
					if(p.getLink() == null){
						htmlBuilder.append("<p><input id='link"+ proj +"' name='link"+ proj +"' placeholder='Digite a página do projeto' /></p>");
					}else{
						htmlBuilder.append("<p><input id='link"+ proj +"' name='link"+ proj +"' value='" + p.getLink() + "' /></p>");
					}
				}
				htmlBuilder.append("							</div>");
				htmlBuilder.append("						</div>");
				htmlBuilder.append("						<div class='clearfix linha'>");
				htmlBuilder.append("							<div class='col1_3'>");
				htmlBuilder.append("								<strong class='separador left-radius'>Público Alvo do Projeto</strong>");
				if(p.getPublicoAlvo() == null)
					htmlBuilder.append("								<textarea name='projeto_publico_alvo' id='projeto_publico_alvo"+ proj +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + "></textarea>");
				else
					htmlBuilder.append("								<textarea name='projeto_publico_alvo' id='projeto_publico_alvo"+ proj +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + ">" + p.getPublicoAlvo() + "</textarea>");
				htmlBuilder.append("							</div>");
//				htmlBuilder.append("							<div class='col1_6'>");
//				htmlBuilder.append("								<strong class='separador'>Abrangência</strong>");
//				htmlBuilder.append("								<p>");
//				htmlBuilder.append("									<select " + (editable ? "" : "disabled='disabled'") + ">");
//				htmlBuilder.append("										<option value='nacional' " + (p.getAbrangencia() == "nacional" ? "checked='checked'" : "") + "> Nacional </option>");
//				htmlBuilder.append("										<option value='regional' " + (p.getAbrangencia() == "regional" ? "checked='checked'" : "") + "> Regional </option>");
//				htmlBuilder.append("										<option value='estadual' " + (p.getAbrangencia() == "estadual" ? "checked='checked'" : "") + "> Estadual </option>");
//				htmlBuilder.append("										<option value='municipal' " + (p.getAbrangencia() == "municipal" ? "checked='checked'" : "") + "> Municipal </option>");
//				htmlBuilder.append("									</select>");
//				htmlBuilder.append("								</p>");
//				htmlBuilder.append("							</div>");
//				htmlBuilder.append("							<div class='col1_2'>");
//				htmlBuilder.append("								<strong class='right-radius'>Localização do Projeto</strong>");
//				htmlBuilder.append("								<div class='localizacao_projet'>");
//				htmlBuilder.append("									<ul class='locais'>");
//				htmlBuilder.append("										<li><a href='mapa.html#5'>Nordeste</a></li>");
//				htmlBuilder.append("									</ul>");
//				htmlBuilder.append("								</div>");
//				htmlBuilder.append("							</div>");
				htmlBuilder.append("						</div>");
				htmlBuilder.append("						<div class='clearfix linha'>");
				htmlBuilder.append("							<div class='col1_3'>");
				htmlBuilder.append("								<strong class='left-radius separador'>Financiadores do Projeto</strong>");
				if(p.getFinanciadores() == null)
					htmlBuilder.append("								<textarea name='financiadores' id='financiadores"+ proj +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + "></textarea>");
				else
					htmlBuilder.append("								<textarea name='financiadores' id='financiadores"+ proj +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + ">" + p.getFinanciadores() + "</textarea>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col2_3'>");
				htmlBuilder.append("								<strong class='right-radius'>Descrição do Projeto</strong>");
				if(p.getDescricao() == null)
					htmlBuilder.append("								<textarea name='descprojeto' id='descprojeto"+ proj +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + "></textarea>");
				else
					htmlBuilder.append("								<textarea name='descprojeto' id='descprojeto"+ proj +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + ">" + p.getDescricao() + "</textarea>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("						</div>");						
				htmlBuilder.append("					</div>");
				htmlBuilder.append("				</div>");
				htmlBuilder.append("			<hr />");
			}
			
			for (ConvenioModel c : org.getConvenios()){
				conv++;
				count = proj + conv;
				String link = "https://www.convenios.gov.br/siconv/ConsultarProposta/ResultadoDaConsultaDeConvenioSelecionarConvenio.do?sequencialConvenio="+c.getNConv()+"&Usr=guest&Pwd=guest";
				htmlBuilder.append("			<div class='clearfix projeto collapsable'>");
				htmlBuilder.append("				<div class='projeto_header'>");
				htmlBuilder.append("					<div id='recolherConv' class='collapse-click collapse-button' data-toggle='collapse' data-target='#recConv"+ conv +"' ><div></div></div>");
				htmlBuilder.append("						<div class='nome'>");
				
				htmlBuilder.append("							<span class='no_margin_right'>" + count  + " |</span>");
				htmlBuilder.append("<span class='no_margin numero_convenio'> Parceria nº "+ c.getNConv() +" </span>");
				if(c.getTitulo() == null || c.getTitulo() == "")
					htmlBuilder.append("<span class='no_margin titulo_convenio'>Informação não disponível</span>");
				else
					htmlBuilder.append("<span class='no_margin titulo_convenio'>" + c.getTitulo() + "</span>");
				htmlBuilder.append("						</div>");
				htmlBuilder.append("						<span class='clear_both'></span>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div id='recConv"+ conv +"' class='collapse'>");
				htmlBuilder.append("						<div class='linha clearfix'>");
				
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador left-radius'>Status</strong>");
				htmlBuilder.append("								<p id='convenio_status"+ conv +"' >");
				if(c.getStatus() == null || c.getStatus() == "")
					htmlBuilder.append("<span>Informação não disponível</span>");
				else
					htmlBuilder.append("<span>"+ c.getStatus() +"</span>");
				htmlBuilder.append("</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador'>Data Início</strong>");
				htmlBuilder.append("								<p><span name='projeto_data_inicio' id='convenio_data_inicio' >" + convertDateToString(c.getDataInicio()) + "</span></p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador'>Data Final</strong>");
				htmlBuilder.append("								<p><span name='projeto_data_final' id='convenio_data_final' >" + convertDateToString(c.getDataFim()) + "</span></p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador'>Valor Total</strong>");
				htmlBuilder.append("								<p><span name='projeto_valor_total' id='convenio_valor_total' > R$ " + convertNumberToCurrencyString(c.getValorTotal()) + "</span></p>");
				htmlBuilder.append("							</div>");
				
				htmlBuilder.append("<div class='col1_6'>");
				htmlBuilder.append("<strong class='separador'>Fonte de recurso</strong>");
				htmlBuilder.append("<p id='convenio_fonte"+ conv +"' >");
				htmlBuilder.append("<span>Público</span>");
//				if(c.getFonteRecursos() == null || c.getFonteRecursos() == "")
//					htmlBuilder.append("<span>Informação não disponível</span>");
//				else
//					htmlBuilder.append("<span>"+ c.getFonteRecursos() +"</span>");
				htmlBuilder.append("</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='right-radius'>Link</strong>");
				htmlBuilder.append("<p>");
				htmlBuilder.append("<span id='linkconv"+conv+"' title='' class='menuTooltip'><span>Você será redirecionado para a página SICONV. Nesta página são apresentados estatuto, quadro de diretores, instrumento do convênio e prestação de contas</span>");
				htmlBuilder.append("<a href="+ link +" target='_blank' contenteditable='false'>Sobre a Parceria</a>");
				htmlBuilder.append("</span>");
				htmlBuilder.append("</p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("						</div>");
				htmlBuilder.append("						<div class='clearfix linha'>");
				htmlBuilder.append("							<div class='col1_3'>");
				htmlBuilder.append("								<strong class='separador left-radius'>Público Alvo do Projeto</strong>");
				
				if(c.getPublicoAlvo() == null)
					htmlBuilder.append("								<textarea name='"+ c.getNConv() +"' id='convenio_publico_alvo"+ conv +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + "></textarea>");
				else
					htmlBuilder.append("								<textarea name='"+ c.getNConv() +"' id='convenio_publico_alvo"+ conv +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + ">" + c.getPublicoAlvo() + "</textarea>");
				
				htmlBuilder.append("							</div>");
//				htmlBuilder.append("							<div class='col1_6'>");
//				htmlBuilder.append("								<strong class='separador'>Abrangência</strong>");
//				htmlBuilder.append("								<p>");
//				htmlBuilder.append("									<select " + (editable ? "" : "disabled='disabled'") + ">");
//				htmlBuilder.append("										<option value='nacional' checked='checked'> Nacional </option>");
//				htmlBuilder.append("										<option value='regional' checked='checked'> Regional </option>");
//				htmlBuilder.append("										<option value='estadual' checked='checked'> Estadual </option>");
//				htmlBuilder.append("										<option value='municipal' checked='checked'> Municipal </option>");
//				htmlBuilder.append("									</select>");
//				htmlBuilder.append("								</p>");
//				htmlBuilder.append("							</div>");
//				htmlBuilder.append("<div class='col1_2'>");
//				htmlBuilder.append("<strong class='right-radius'>Localização do Projeto</strong>");
//				htmlBuilder.append("<div class='localizacao_projeto'>");
//				htmlBuilder.append("<ul class='locais'>");
//				htmlBuilder.append("<li><a href='mapa.html#5'>Nordeste</a></li>");
//				htmlBuilder.append("<li><a href='mapa.html#4'>Norte</a></li>");
//				htmlBuilder.append("</ul>");
//				htmlBuilder.append("</div>");
//				htmlBuilder.append("</div>");
				htmlBuilder.append("						</div>");
				htmlBuilder.append("						<div class='clearfix linha'>");
				htmlBuilder.append("							<div class='col1_3'>");
				htmlBuilder.append("								<strong class='left-radius separador'>Financiadores do Projeto</strong>");
				if(c.getFinanciadores() == null || c.getFinanciadores() == "")
					htmlBuilder.append("<span>Informação não disponível</span>");
				else
					htmlBuilder.append("<span>"+ c.getFinanciadores() +"</span>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col2_3'>");
				htmlBuilder.append("								<strong class='right-radius'>Descrição do Projeto</strong>");
				if(c.getDescricao() == null || c.getDescricao() == "")
					htmlBuilder.append("<span>Informação não disponível</span>");
				else
					htmlBuilder.append("<span>"+ c.getDescricao() +"</span>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("						</div>");						
				htmlBuilder.append("					</div>");
				htmlBuilder.append("				</div>");
				htmlBuilder.append("			<hr />");
			}
		}
		htmlBuilder.append("		</div>");
		
		htmlBuilder.append("	</div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("<div id='divbotoes' class='botoes'>");
		htmlBuilder.append("<a href='" + GWT.getHostPageBaseURL() + "Map.html#O" + org.getId().toString() + "' >Cancelar</a> ou <input id='btnSalvar' type='submit' value='Salvar' class='salvar' />");
		htmlBuilder.append("</div>");
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}
	
	private String convertNumberToString(Number number){
		String result = "";
		if(number.longValue() >= 0){
			result = String.valueOf(number);
		}
		return result;
	}
	
	public Number convertStringToNumber(String string){
		Number result = null;
		if(string != null){
			result = Integer.parseInt(string);
		}
		return result;
	}
	
	public String convertNumberToCurrencyString(Number number){
		String result = "";
		if(number.longValue() >= 0){
			result = NumberFormat.getFormat("###,###,##0.00").format(number);
//			NumberFormat fmt = NumberFormat.getDecimalFormat();
//			result = fmt.format(number);
		}
		return result;
	}
	
	private String convertDateToString(Date date){
		String result = "";
		if(date != null){
			result = DateTimeFormat.getFormat("dd/MM/yyyy").format(date);
		}
		return result;
	}
	
	private Date getDate(String data) {
		Date date = DateTimeFormat.getFormat("dd/MM/yyyy").parse(data);
	    return date;
	}
	
	/**
	 * @param listener
	 *            Controla o evento de click do botão salvar.
	 */
	public void addSalvarListener(EventListener listener) {
		Element div = DOM.getElementById("divbotoes");
		if(edit == false)
			div.setAttribute("style", "display: none");
		else{
			div.setAttribute("style", "display: block");
			Element btnSalvar = DOM.getElementById("btnSalvar");
			Event.sinkEvents(btnSalvar, Event.ONCLICK);
			Event.setEventListener(btnSalvar, listener);
		}
	}
	
	public void addEditar(EventListener listener) {
		Element edicao = DOM.getElementById("edicao");
		Event.sinkEvents(edicao, Event.ONCLICK);
		Event.setEventListener(edicao, listener);
	}
	
	public OrganizationModel getOrg() {
		logger.info("Get Organization");
		OrganizationModel org = new OrganizationModel();
		Integer anofundacao = Integer.parseInt(InputElement.as(DOM.getElementById("ano_fundacao")).getValue());
		org.setId(Integer.parseInt(History.getToken().substring(1)));
		org.setNomeFantasia(InputElement.as(DOM.getElementById("nome_fantasia")).getValue());
		org.setDescricaoProjeto(TextAreaElement.as(DOM.getElementById("descricao_projeto")).getValue());
		org.setAnoFundacao(anofundacao);
		org.setSite(InputElement.as(DOM.getElementById("site")).getValue());
		org.setEmail(InputElement.as(DOM.getElementById("email")).getValue());
		
		ArrayList<DiretorModel> diretorList = new ArrayList<DiretorModel>();
		for(int i = 1;i <= dir; i++){
			DiretorModel dirmodel = new DiretorModel();
			Element ele = DOM.getElementById("cargo" + i);
			if(ele != null){
				String cargodir = InputElement.as(DOM.getElementById("cargo" + i)).getValue();
				String namedir = InputElement.as(DOM.getElementById("nome" + i)).getValue();
				if(cargodir != "" && namedir != ""){
					String name = ele.getAttribute("name");
					if(name == "addDir"){
						dirmodel.setId(dirmodel.getId());
						dirmodel.setCargo(cargodir);
						dirmodel.setNome(namedir);
					}else{
						Integer id = Integer.parseInt(name);
						dirmodel.setId(id);
						dirmodel.setCargo(InputElement.as(DOM.getElementById("cargo"+ i)).getValue());
						dirmodel.setNome(InputElement.as(DOM.getElementById("nome"+ i)).getValue());
					}
					diretorList.add(dirmodel);
				}
			}
		}
		org.setDiretores(diretorList);
		
		ArrayList<ProjetoModel> projetoList = new ArrayList<ProjetoModel>();
		for(int i = 1;i <= proj; i++){
			ProjetoModel projmodel = new ProjetoModel();
			Element ele = DOM.getElementById("projeto_nome_projeto" + i);
			String name = ele.getAttribute("name");
			if(name == "addProj"){
				projmodel.setId(projmodel.getId());
			}else{
				projmodel.setId(Integer.parseInt(name));	
			}
			projmodel.setTitulo(InputElement.as(DOM.getElementById("projeto_nome_projeto" + i)).getValue());
			Element status = DOM.getElementById("projeto_status" + i);
			SelectElement selectStatus = status.getFirstChild().cast();
			projmodel.setStatus(selectStatus.getValue());
			String dtInicio = InputElement.as(DOM.getElementById("projeto_data_inicio" + i)).getValue();
			if(dtInicio.length() > 0) projmodel.setDataInicio(getDate(dtInicio));
			String dtFinal = InputElement.as(DOM.getElementById("projeto_data_final" + i)).getValue();
			if(dtFinal.length() > 0) projmodel.setDataFim(getDate(dtFinal));
			Element valor = DOM.getElementById("projeto_valor_total" + i);
			String valorTotalvalue = InputElement.as(valor).getValue();
			if(valorTotalvalue != null && valorTotalvalue != ""){
				String[] valorSplit = valorTotalvalue.split(",",0);
				String valorReplace = valorSplit[0].replaceAll("[.,]", "");
				Double valorTotal = Double.parseDouble(valorReplace);
				projmodel.setValorTotal(valorTotal);
			}else{
				projmodel.setValorTotal(-1.0);
			}
			Element fonte = DOM.getElementById("projeto_fonte" + i);
			SelectElement selectFonte = fonte.getFirstChild().cast();
			projmodel.setFonteRecursos(selectFonte.getValue());
			projmodel.setLink(InputElement.as(DOM.getElementById("link" + i)).getValue());
			projmodel.setPublicoAlvo(TextAreaElement.as(DOM.getElementById("projeto_publico_alvo" + i)).getValue());
			projmodel.setFinanciadores(TextAreaElement.as(DOM.getElementById("financiadores" + i)).getValue());
			projmodel.setDescricao(TextAreaElement.as(DOM.getElementById("descprojeto" + i)).getValue());
			
			projetoList.add(projmodel);
		}
		org.setProjetos(projetoList);
		ArrayList<ConvenioModel> convList = new ArrayList<ConvenioModel>();
		for(int i = 1;i <= conv; i++){
			ConvenioModel convmodel = new ConvenioModel();
			Element ele = DOM.getElementById("convenio_publico_alvo" + i);
			String name = ele.getAttribute("name");
			Integer id = Integer.parseInt(name);
			convmodel.setNConv(id);
			convmodel.setPublicoAlvo(TextAreaElement.as(ele).getValue());
			convList.add(convmodel);
		}
		org.setConvenios(convList);
		Integer voluntarios = Integer.parseInt(InputElement.as(DOM.getElementById("voluntarios")).getValue());
		org.setVoluntarios(voluntarios);
		
		return org;
	}
	
	public void addDate(OrganizationModel org,String ele, String idElement, String getDate, Boolean enabled){
		String data = "";
		Integer i = null;
		for (int j = 0; j < org.getProjetos().size(); j++ ) {
			if(getDate == "Inicio"){
				data = convertDateToString(org.getProjetos().get(j).getDataInicio());
			}else if(getDate == "Final"){
				data = convertDateToString(org.getProjetos().get(j).getDataFim());
			}
			i = j + 1;
			Element element = DOM.getElementById(ele + i);
			PopupPanel popup = new PopupPanel();
			DatePicker picker = new DatePicker();
			picker.setEnabled(enabled);
			picker.setSize("120px", "");
			picker.setLanguage(DatePickerLanguage.PT_BR);
			picker.setFormat("dd/mm/yyyy");
			picker.setId(idElement + i);
			if(data.length() > 0){
				picker.setValue(getDate(data));
			}
			popup.add(picker);
			popup.show();
			element.appendChild(picker.getElement());
//			Element date = DOM.getElementById(idElement + i);
//			date.setAttribute("required", "required");
		} 
	}
	
	public void removeDir(EventListener listener) {
		for(int i = 1;i<=dir; i++){
			Element removeDir = DOM.getElementById("removedir"+ i);
			if(edit == false)
				removeDir.setAttribute("style", "display: none");
			else{
				removeDir.setAttribute("style", "display: block");
				Event.sinkEvents(removeDir, Event.ONCLICK);
				Event.setEventListener(removeDir, listener);
			}
		}
	}
	
//	public void addSocial(EventListener listener) {
//		Element addSocial = DOM.getElementById("addsocial");
//		Event.sinkEvents(addSocial, Event.ONCLICK);
//		Event.setEventListener(addSocial, listener);
//	}
	
//	public void addFonteRec(EventListener listener) {
//		Element addFonteRec = DOM.getElementById("addRecursos");
//		Event.sinkEvents(addFonteRec, Event.ONCLICK);
//		Event.setEventListener(addFonteRec, listener);
//	}

	public void addDiretor(EventListener listener) {
		Element addDiretor = DOM.getElementById("addDiretor");
		if(edit == false)
			addDiretor.setAttribute("style", "display: none");
		else{
			addDiretor.setAttribute("style", "float:right;margin-top:3px;margin-right: 3px;display: block;");
			Event.sinkEvents(addDiretor, Event.ONCLICK);
			Event.setEventListener(addDiretor, listener);
		}
	}
	
	public void addProjetos(EventListener listener) {
		Element addProjetos = DOM.getElementById("addProjetos");
		if(edit == false)
			addProjetos.setAttribute("style", "display: none");
		else{
			addProjetos.setAttribute("style", "display: block");
			Event.sinkEvents(addProjetos, Event.ONCLICK);
			Event.setEventListener(addProjetos, listener);
		}
	}
	
	private void magneticLink(Integer count, String element) {
		for(int i = 1;i <= count; i++){
			final Element spanPopup = DOM.getElementById(element+i);
			if(spanPopup != null){
				final Element span = (Element) spanPopup.getFirstChildElement();
				if (span != null) {
					Event.sinkEvents(spanPopup, Event.ONMOUSEOVER);
					Event.sinkEvents(spanPopup, Event.ONMOUSEMOVE);
					Event.setEventListener(spanPopup, new EventListener() {
						
						public void onBrowserEvent(Event event) {
							final int left = event.getClientX() + 5;
							final int bottom = event.getClientY() - span.getClientHeight() - 5;
							span.getStyle().setLeft(left, Unit.PX);
							span.getStyle().setTop(bottom, Unit.PX);
						}
					});
				}
			}
		}
		
	}

}