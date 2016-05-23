package gov.sgpr.fgv.osc.portalosc.organization.client.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.LocalizacaoModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.OrganizationModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.ProjetoModel;
import gov.sgpr.fgv.osc.portalosc.user.client.components.SearchWidget;
import gov.sgpr.fgv.osc.portalosc.user.shared.model.SearchResult;

public class FormularioWidget extends Composite {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	private SearchWidget searchWidget = new SearchWidget();
	private Element searchTextField;
	public Integer dir = 0;
	public Integer proj = 0;
	public Integer conv = 0;
	public Integer count = 0;
	public Integer addLocalProj = 0;
	public Integer addLocalConv = 0;
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
	
	public void addResultItems(List<SearchResult> items, EventListener listener, String idElement) {
		searchWidget.setItems(items, true, idElement);
		searchWidget.addSearchListener(listener);
	}
	
	public void showOrganization(String oscInfo, String oscId, String idElement) {
		InputElement enome = DOM.getElementById(idElement).cast();
		enome.setValue(oscInfo);
		searchWidget.hideItens();
	}
	
	private HTML getHTML(OrganizationModel org, Boolean editable){
		StringBuilder htmlBuilder = new StringBuilder();
		edit = editable;
		htmlBuilder.append("<form id='entidade_edicao' name='entidade_edicao' method='post'>");
		
		htmlBuilder.append("<div class='titulo'>");
		htmlBuilder.append("	<h1><em>&nbsp;</em> ");
		htmlBuilder.append(org.getRazaoSocial() == null ? "Organização" : org.getRazaoSocial());
		htmlBuilder.append("	</h1>");
		htmlBuilder.append("</div>");
		
		htmlBuilder.append("<div class='container'>");
		htmlBuilder.append("	<div class='social'>");
//		htmlBuilder.append("		<div class='imagem'>");
//		if(org.getImagem().length() == 0){
//			htmlBuilder.append("		<img src='imagens/org_indisponivel.jpg' />");
//		}else{
//			htmlBuilder.append("		<img src='imagens/" + org.getImagem() + "' width='160' height='160' />");
//		}
//		htmlBuilder.append("		</div>");
		htmlBuilder.append("		<div class='redes'>");
		
		if(org.getGoogle().length() == 0) htmlBuilder.append("<i class='fa fa-google-plus-square fa-3x'></i>");
		else htmlBuilder.append("<a href='" + org.getGoogle() + "' target='_blank'><i class='fa fa-google-plus-square fa-3x'></i></a>");
		
		if(org.getFacebook().length() == 0) htmlBuilder.append("<i class='fa fa-facebook-square fa-3x' id='b-facebook' name='redes' title='Facebook' ></i>");
		else htmlBuilder.append("<a href='" + org.getFacebook() + "' target='_blank'><i class='fa fa-facebook-square fa-3x' id='b-facebook' name='redes' title='Facebook' ></i></a>");
		
		if(org.getLinkedin().length() == 0) htmlBuilder.append("<i class='fa fa-linkedin-square fa-3x' id='b-linkedin' name='redes' title='Linkedin' ></i>");
		else htmlBuilder.append("<a href='" + org.getLinkedin() + "' target='_blank'><i class='fa fa-linkedin-square fa-3x' id='b-linkedin' name='redes' title='Linkedin' ></i></a>");
		
		if(org.getTwitter().length() == 0) htmlBuilder.append("<i class='fa fa-twitter-square fa-3x' id='b-twitter' name='redes' title='Twitter' ></i>");
		else htmlBuilder.append("<a href='" + org.getTwitter() + "' target='_blank'><i class='fa fa-twitter-square fa-3x' id='b-twitter' name='redes' title='Twitter' ></i></a>");
		
		htmlBuilder.append("		</div>");
		htmlBuilder.append("		<a href='" + org.getComoParticipar() != "" ? org.getComoParticipar() : "#O" + org.getId().toString() + "' class='participar box'>Como Participar</a>");
		htmlBuilder.append("		<div class='recomendacoes'>");
		htmlBuilder.append("			<span class='tooltip' data-hasqtip='4' title='");
		htmlBuilder.append(org.getRecomendacoes() == null ? "0" : org.getRecomendacoes());
		htmlBuilder.append(org.getRecomendacoes() == 1 ? " recomendação' " : " recomendações' ");
		htmlBuilder.append("id='like_counter' >");
		htmlBuilder.append(org.getRecomendacoes() == null ? "0" : org.getRecomendacoes());
		htmlBuilder.append("</span>");
		htmlBuilder.append("		</div>");
		htmlBuilder.append("		<button type='button' class='recomendar' id='recomendar'>Recomendar</button>");
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
		htmlBuilder.append("						<strong>Nome fantasia:</strong>");
		if(editable)
			htmlBuilder.append("						<input type='text' name='nome_fantasia' id='nome_fantasia' placeholder='Nome pelo qual a organização é conhecida' value='" + org.getNomeFantasia() + "' " + (editable ? "" : "readonly") + "/>");
		else
			htmlBuilder.append("						<input type='text' name='nome_fantasia' id='nome_fantasia' placeholder='Informação não disponível' value='" + org.getNomeFantasia() + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>CNPJ:</strong>");
		if(org.getNumeroIdentificacao() == null || org.getNumeroIdentificacao() == -1L){
			htmlBuilder.append("					<span>Informação não disponível</span>");
		}else{
			if(org.getTipoIdentificacao() == 1){
				htmlBuilder.append("					<span>" + formatCNPJ(org.getNumeroIdentificacao()) + "</span>");
			}else if(org.getTipoIdentificacao() == 2){
				htmlBuilder.append("					<span>" + formatCEI(org.getNumeroIdentificacao()) + "</span>");
			}else{
				htmlBuilder.append("					<span>Informação não disponível</span>");
			}
		}
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Endereço:</strong>");
		if(org.getEndereco() == null || org.getEndereco() == ""){
			htmlBuilder.append("					<span id='endereco' title='Endereço'>Informação não disponível</span>");
		}else{
			htmlBuilder.append("					<a href='" + GWT.getHostPageBaseURL() + "Map.html#O" + org.getId().toString() + "' target='_blank' ><span id='endereco' title='Endereço'>" + org.getEndereco() + "</span></a>");
		}
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
//		htmlBuilder.append("					<div>");
//		htmlBuilder.append("						<strong>Outros Endereços:</strong>");
//		htmlBuilder.append("						<a href='Lista.html?grupoOSC=123'>visualizar Lista</a>");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
//		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Natureza jurídica:</strong>");
		if(org.getNaturezaJuridica() == null || org.getNaturezaJuridica() == ""){
			htmlBuilder.append("					<span>Informação não disponível</span>");
		}else{
			htmlBuilder.append("					<span>" + org.getNaturezaJuridica() + "</span>");
		}
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Atividade econômica (CNAE):</strong>");
		if(org.getCnae() == null || org.getCnae() == ""){
			htmlBuilder.append("					<span>Informação não disponível</span>");
		}else{
			htmlBuilder.append("					<span>" + org.getCnae() + "</span>");
		}
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Responsável legal:</strong>");
		if(org.getResponsavel() == null || org.getResponsavel() == ""){
			htmlBuilder.append("					<span>Informação não disponível</span>");
		}else{
			htmlBuilder.append("					<span>" + org.getResponsavel() + "</span>");
		}
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Descrição da OSC:</strong>");
		if(editable)
			htmlBuilder.append("						<textarea name='descricao_osc' id='descricao_osc' placeholder='Apresentar os objetivos da organização de maneira sucinta' " + (editable ? "" : "readonly") + ">" + org.getDescricaoProjeto() + "</textarea>");
		else
			htmlBuilder.append("						<textarea name='descricao_osc' id='descricao_osc' placeholder='Informação não disponível' " + (editable ? "" : "readonly") + ">" + org.getDescricaoProjeto() + "</textarea>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Ano de fundação:</strong>");
		htmlBuilder.append("						<input type='number' name='ano_fundacao' id='ano_fundacao' placeholder='Informação não disponível' value='" + convertNumberToString(org.getAnoFundacao()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>E-mail:</strong>");
		htmlBuilder.append("						<input type='text' name='email' id='email' placeholder='Informação não disponível' value='" + org.getEmail() + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Site:</strong>");
		htmlBuilder.append("						<input type='text' name='site' id='site' placeholder='Informação não disponível' value='" + org.getSite() + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		if(editable){
			htmlBuilder.append("				<div>");
			htmlBuilder.append("					<strong>Google+:</strong>");
			htmlBuilder.append("					<input type='text' name='google' id='google' placeholder='Informação não disponível' value='" + (org.getGoogle().contains("://") ? org.getGoogle().split("://")[1] : org.getGoogle()) + "' />");
			htmlBuilder.append("					<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
			htmlBuilder.append("				</div>");
			htmlBuilder.append("				<div>");
			htmlBuilder.append("					<strong>Facebook:</strong>");
			htmlBuilder.append("					<input type='text' name='facebook' id='facebook' placeholder='Informação não disponível' value='" + (org.getFacebook().contains("://") ? org.getFacebook().split("://")[1] : org.getFacebook()) + "' />");
			htmlBuilder.append("					<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
			htmlBuilder.append("				</div>");
			htmlBuilder.append("				<div>");
			htmlBuilder.append("					<strong>Linkedin:</strong>");
			htmlBuilder.append("					<input type='text' name='linkedin' id='linkedin' placeholder='Informação não disponível' value='" + (org.getLinkedin().contains("://") ? org.getLinkedin().split("://")[1] : org.getLinkedin()) + "' />");
			htmlBuilder.append("					<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
			htmlBuilder.append("				</div>");
			htmlBuilder.append("				<div>");
			htmlBuilder.append("					<strong>Twitter:</strong>");
			htmlBuilder.append("					<input type='text' name='twitter' id='twitter' placeholder='Informação não disponível' value='" + (org.getTwitter().contains("://") ? org.getTwitter().split("://")[1] : org.getTwitter()) + "' />");
			htmlBuilder.append("					<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
			htmlBuilder.append("				</div>");
		}
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
		htmlBuilder.append("			<h2>Quadro de dirigentes <button type='button' class='adicionar' id='addDiretor' style='float:right;margin-top:3px;margin-right: 3px;'>Adicionar</button></h2>");
		htmlBuilder.append("			<fieldset>");
		htmlBuilder.append("				<div id='diretores' class='diretores'>");
		if(org.getDiretores().size() == 0){
			htmlBuilder.append("				<div style='margin: 10px;'>");
			htmlBuilder.append("					<span id='indDir' >Informação não disponível</span>");
			htmlBuilder.append("				</div>");
		}else{
			for (DiretorModel d : org.getDiretores()){
				dir++;
				htmlBuilder.append("			<div id='incluirDir'>");
				htmlBuilder.append("				<div class='botoes'>");
				htmlBuilder.append("					<button id='removedir"+ dir +"' value='"+ d.getId() +"' type='button' class='excluir participacao'>Excluir</button>");
				htmlBuilder.append("				</div>");
				htmlBuilder.append("				<div class='div_cargo_diretor'>");
				htmlBuilder.append("					<strong>Cargo:</strong>");
				if(editable)
					htmlBuilder.append("					<input type='text' name='"+ d.getId() +"' id='cargo"+ dir +"' value='"+ d.getCargo() +"'  " + (editable ? "" : "readonly") + " placeholder='Indique cada cargo previsto no seu estatuto social' />");
				else
					htmlBuilder.append("					<input type='text' name='"+ d.getId() +"' id='cargo"+ dir +"' value='"+ d.getCargo() +"'  " + (editable ? "" : "readonly") + " placeholder='Informação não disponível' />");
				htmlBuilder.append("				</div>");
				htmlBuilder.append("				<div class='div_nome_diretor'>");
				htmlBuilder.append("					<strong>Nome:</strong>");
				if(editable)
					htmlBuilder.append("					<input type='text' name='"+ d.getId() +"' id='nome"+ dir +"' value='"+ d.getNome() +"'  " + (editable ? "" : "readonly") + " placeholder='Relacione o nome completo do dirigente' />");
				else
					htmlBuilder.append("					<input type='text' name='"+ d.getId() +"' id='nome"+ dir +"' value='"+ d.getNome() +"'  " + (editable ? "" : "readonly") + " placeholder='Informação não disponível' />");
				htmlBuilder.append("				</div>");
				htmlBuilder.append("			</div>");
			}
		}	
		htmlBuilder.append("				</div>");
		htmlBuilder.append("			</fieldset>");
		htmlBuilder.append("			<h2>Relações de Trabalho</h2>");
		htmlBuilder.append("			<fieldset>");
		htmlBuilder.append("				<div class='recursos collapsable'>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Total de colaboradores:</strong>");
		if(org.getTotalColaboradores() == null || org.getTotalColaboradores() < 0){
			htmlBuilder.append("					<span>Informação não disponível</span>");
		}else{
			htmlBuilder.append("					<span>" + convertNumberToString(org.getTotalColaboradores()) + "</span>");
		}
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS e SUAS'></span>");
		htmlBuilder.append("						<div id='recolherCol' class='collapse-click collapse-button' data-toggle='collapse' data-target='#recCol' ><div></div></div>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div id='recCol' class='collapse' >");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Trabalhadores com vínculo:</strong>");
		if(org.getTrabalhadores() == null || org.getTrabalhadores() < 0){
			htmlBuilder.append("						<span>Informação não disponível</span>");
		}else{
			htmlBuilder.append("						<span>" + convertNumberToString(org.getTrabalhadores()) + "</span>");
		}
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Trabalhadores com deficiência:</strong>");
		if(org.getPortadoresDeficiencia() == null || org.getPortadoresDeficiencia() < 0){
			htmlBuilder.append("						<span>Informação não disponível</span>");
		}else{
			htmlBuilder.append("						<span>" + convertNumberToString(org.getPortadoresDeficiencia()) + "</span>");
		}
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Voluntários:</strong>");
		htmlBuilder.append("							<input type='number' name='voluntarios' id='voluntarios' placeholder='Informação não disponível' value='" + convertNumberToString(org.getVoluntarios()) + "' " + (editable ? "" : "readonly") + "/>");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("				</div>");
		htmlBuilder.append("			</fieldset>");
		htmlBuilder.append("			<h2>Recursos</h2>");
		htmlBuilder.append("			<fieldset>");
		htmlBuilder.append("				<div class='recursos collapsable'>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Valor total dos recursos (R$):</strong>");
		if(org.getValorRecursosTotal() == null || org.getValorRecursosTotal() < 0){
			htmlBuilder.append("					<span>Informação não disponível</span>");
		}else{
			htmlBuilder.append("					<span>" + convertNumberToCurrencyString(org.getValorRecursosTotal()) + "</span>");
		}
		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte Siconv e Finep'></span>");
		htmlBuilder.append("						<div id='recolherParc' class='collapse-click collapse-button' data-toggle='collapse' data-target='#recParc' ><div></div></div>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div id='recParc' class='collapse'>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Valor das parcerias federais (R$):</strong>");
		htmlBuilder.append("							<span>" + convertNumberToCurrencyString(org.getValorParceriasFederal()) + "</span>");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Valor das parcerias estaduais (R$):</strong>");
		htmlBuilder.append("							<span>" + convertNumberToCurrencyString(org.getValorParceriasEstadual()) + "</span>");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Valor das parcerias municipais (R$):</strong>");
		htmlBuilder.append("							<span>" + convertNumberToCurrencyString(org.getValorParceriasMunicipal()) + "</span>");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Valor dos recursos privados (R$):</strong>");
		htmlBuilder.append("							<span>" + convertNumberToCurrencyString(org.getValorRecursosPrivados()) + "</span>");
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
		htmlBuilder.append("			<h2>Titulações e certificações federais</h2>");
		htmlBuilder.append("			<div>");
		htmlBuilder.append("				<ul class='dados checklist'>");
		if(org.getCertificacao().size() == 0){
			htmlBuilder.append("				<li>Organização sem certificados</li>");
		}else{
			String source = "";
			for (HashMap.Entry<String, String> entry : org.getCertificacao().entrySet()) {
				htmlBuilder.append("			<li>" + entry.getKey() + "</li>");
				source += entry.getValue() + ", ";
			}
			source = source.substring(0, source.length() - 2);
			htmlBuilder.append("				<span class='fonte_de_dados dado_oficial' title='Dado Oficial, fonte " + source + "'></span>");
		}
		htmlBuilder.append("				</ul>");
		htmlBuilder.append("			</div>");
		htmlBuilder.append("		</div>");
		htmlBuilder.append("		<div id='addFormProj' class='projetos'>");
		htmlBuilder.append("			<div class='titulo'>");
		htmlBuilder.append("				<h2>Projetos, atividades e/ou programas</h2>");
		htmlBuilder.append("				<button id='addProjetos' type='button' class='adicionar'>Adicionar</button>");
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
			htmlBuilder.append("		<div class='clearfix projeto collapsable'>");
			htmlBuilder.append("			<span id='indProj' title='projeto'>Informação não disponível</span>");
			htmlBuilder.append("		</div>");
		}else{
			for (ProjetoModel p : org.getProjetos()){
				proj++;
				htmlBuilder.append("	<div class='clearfix projeto collapsable'>");
				htmlBuilder.append("		<div class='projeto_header'>");
				htmlBuilder.append("			<div id='recolherProj' class='collapse-click collapse-button' data-toggle='collapse' data-target='#recProj"+ proj +"' ><div></div></div>");
				htmlBuilder.append("				<div class='nome'>");	
				htmlBuilder.append("					<span class='no_margin_right'>" + (org.getProjetos().indexOf(p) + 1) + " |</span>");
				if(p.getTitulo() == null || p.getTitulo() == ""){
					if(editable)
						htmlBuilder.append("				<span class='no_margin titulo_projeto'><input type='text' name='"+ p.getId() +"' id='projeto_nome_projeto"+ proj +"' placeholder='Indique o título do projeto, atividade e/ou programa' value='' " + (editable ? "" : "readonly") + "/></span>");
					else
						htmlBuilder.append("				<span class='no_margin titulo_projeto'><input type='text' name='"+ p.getId() +"' id='projeto_nome_projeto"+ proj +"' placeholder='Informação não disponível' value='' " + (editable ? "" : "readonly") + "/></span>");
				}else{
					htmlBuilder.append("				<span class='no_margin titulo_projeto'><input type='text' name='"+ p.getId() +"' id='projeto_nome_projeto"+ proj +"' placeholder='Indique o título do projeto, atividade e/ou programa' value='" + p.getTitulo() + "' " + (editable ? "" : "readonly") + "/></span>");
				}
				htmlBuilder.append("				</div>");
				htmlBuilder.append("				<span class='clear_both'></span>");
				htmlBuilder.append("			</div>");
				htmlBuilder.append("			<div id='recProj"+ proj +"' class='collapse'>");
				htmlBuilder.append("				<div class='linha clearfix'>");
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='separador left-radius'>Status</strong>");
				htmlBuilder.append("						<p id='projeto_status"+ proj +"' >");
				htmlBuilder.append("							<select " + (editable ? "" : "disabled") + ">");
				if(p.getStatus() == "Planejado"){
					htmlBuilder.append("							<option value='Planejado' selected='selected'> Planejado </option>");
					htmlBuilder.append("							<option value='Em Execução' > Em Execução </option>");
					htmlBuilder.append("							<option value='Finalizado' > Finalizado </option>");
				}else if(p.getStatus() == "Em Execução"){
					htmlBuilder.append("							<option value='Planejado' > Planejado </option>");
					htmlBuilder.append("							<option value='Em Execução' selected='selected' > Em Execução </option>");
					htmlBuilder.append("							<option value='Finalizado' > Finalizado </option>");
				}else if(p.getStatus() == "Finalizado"){
					htmlBuilder.append("							<option value='Planejado' > Planejado </option>");
					htmlBuilder.append("							<option value='Em Execução' > Em Execução </option>");
					htmlBuilder.append("							<option value='Finalizado' selected='selected' > Finalizado </option>");
				}else{
					if(editable)
						htmlBuilder.append("							<option value='' disabled selected>Indique o status</option>");
					else
						htmlBuilder.append("							<option value='' disabled selected>Informação não disponível</option>");
					htmlBuilder.append("							<option value='Planejado' > Planejado </option>");
					htmlBuilder.append("							<option value='Em Execução' > Em Execução </option>");
					htmlBuilder.append("							<option value='Finalizado' > Finalizado </option>");
				}
				htmlBuilder.append("							</select>");
				htmlBuilder.append("						</p>");
				htmlBuilder.append("					</div>");
				
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='separador'>Data Início</strong>");
				htmlBuilder.append("						<p id='data_inicio"+ proj +"' ></p>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='separador'>Data Final</strong>");
				htmlBuilder.append("						<p id='data_final"+ proj +"' ></p>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='separador'>Valor Total (R$)</strong>");
				if(editable)
					htmlBuilder.append("						<p><input type='text' name='projeto_valor_total' id='projeto_valor_total"+proj+"' placeholder='Indique o valor total' value='" + convertNumberToCurrencyString(p.getValorTotal()) + "'  " + (editable ? "" : "readonly") + " /></p>");
				else
					htmlBuilder.append("						<p><input type='text' name='projeto_valor_total' id='projeto_valor_total"+proj+"' placeholder='Informação não disponível' value='" + convertNumberToCurrencyString(p.getValorTotal()) + "'  " + (editable ? "" : "readonly") + " /></p>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='separador'>Fonte de recurso</strong>");
				htmlBuilder.append("						<p id='projeto_fonte"+ proj +"' >");
				htmlBuilder.append("							<select  " + (editable ? "" : "disabled") + ">");
				if(p.getFonteRecursos() == "Público Federal"){
					htmlBuilder.append("							<option value='Público Federal' selected='selected' > Público Federal </option>");
					htmlBuilder.append("							<option value='Público Estadual' > Público Estadual </option>");
					htmlBuilder.append("							<option value='Público Municipal' > Público Municipal </option>");
					htmlBuilder.append("							<option value='Privado' > Privado </option>");
					htmlBuilder.append("							<option value='Próprio' > Próprio </option>");
				}else if(p.getFonteRecursos() == "Público Estadual"){
					htmlBuilder.append("							<option value='Público Federal' > Público Federal </option>");
					htmlBuilder.append("							<option value='Público Estadual' selected='selected' > Público Estadual </option>");
					htmlBuilder.append("							<option value='Público Municipal' > Público Municipal </option>");
					htmlBuilder.append("							<option value='Privado' > Privado </option>");
					htmlBuilder.append("							<option value='Próprio' > Próprio </option>");
				}else if(p.getFonteRecursos() == "Público Municipal"){
					htmlBuilder.append("							<option value='Público Federal' > Público Federal </option>");
					htmlBuilder.append("							<option value='Público Estadual' > Público Estadual </option>");
					htmlBuilder.append("							<option value='Público Municipal' selected='selected' > Público Municipal </option>");
					htmlBuilder.append("							<option value='Privado' > Privado </option>");
					htmlBuilder.append("							<option value='Próprio' > Próprio </option>");
				}else if(p.getFonteRecursos() == "Privado"){
					htmlBuilder.append("							<option value='Público Federal' > Público Federal </option>");
					htmlBuilder.append("							<option value='Público Estadual' > Público Estadual </option>");
					htmlBuilder.append("							<option value='Público Municipal' > Público Municipal </option>");
					htmlBuilder.append("							<option value='Privado' selected='selected' > Privado </option>");
					htmlBuilder.append("							<option value='Próprio' > Próprio </option>");
				}else if(p.getFonteRecursos() == "Próprio"){
					htmlBuilder.append("							<option value='Público Federal' > Público Federal </option>");
					htmlBuilder.append("							<option value='Público Estadual' > Público Estadual </option>");
					htmlBuilder.append("							<option value='Público Municipal' > Público Municipal </option>");
					htmlBuilder.append("							<option value='Privado' > Privado </option>");
					htmlBuilder.append("							<option value='Próprio' selected='selected' > Próprio </option>");
				}else {
					if(editable)
						htmlBuilder.append("							<option value='' disabled selected>Indique a fonte de recurso</option>");
					else
						htmlBuilder.append("							<option value='' disabled selected>Informação não disponível</option>");
					htmlBuilder.append("							<option value='Público Federal' > Público Federal </option>");
					htmlBuilder.append("							<option value='Público Estadual' > Público Estadual </option>");
					htmlBuilder.append("							<option value='Público Municipal' > Público Municipal </option>");
					htmlBuilder.append("							<option value='Privado' > Privado </option>");
					htmlBuilder.append("							<option value='Próprio' > Próprio </option>");
				}
				htmlBuilder.append("							</select>");
				htmlBuilder.append("						</p>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='right-radius'>Link</strong>");					
				if(editable == false){
					if(p.getLink() == "link para página" || p.getLink() == "" || p.getLink() == null){
						htmlBuilder.append("				<span>Informação não disponível</span>");
					}else{
						htmlBuilder.append("				<p>");
						htmlBuilder.append("					<span id='linkproj"+proj+"' title='' class='menuTooltip'>");
						htmlBuilder.append("						<span>Você será redirecionado para a página do Projeto</span>");
						htmlBuilder.append("						<a id='link"+ proj +"' href='http://" + p.getLink() + "' target='_blank' " + (editable ? "contenteditable='true'" : "contenteditable='false'") + " contenteditable='true' >Link</a>");
						htmlBuilder.append("					</span>");
						htmlBuilder.append("				</p>");
					}
				}else{
					if(p.getLink() == null){
						htmlBuilder.append("				<p>");
						htmlBuilder.append("					<input id='link"+ proj +"' name='link"+ proj +"' placeholder='Informação não disponível' />");
						htmlBuilder.append("				</p>");
					}else{
						htmlBuilder.append("				<p>");
						htmlBuilder.append("					<input id='link"+ proj +"' name='link"+ proj +"' value='" + p.getLink() + "' />");
						htmlBuilder.append("				</p>");
					}
				}
				htmlBuilder.append("					</div>");
				htmlBuilder.append("				</div>");
				htmlBuilder.append("				<div class='clearfix linha'>");
				htmlBuilder.append("					<div class='col1_3'>");
				htmlBuilder.append("						<strong class='separador left-radius'>Público Beneficiado</strong>");
				if(p.getPublicoAlvo() == null || p.getPublicoAlvo() == ""){
					if(editable)
						htmlBuilder.append("					<textarea name='projeto_publico_alvo' id='projeto_publico_alvo"+ proj +"'  placeholder='Indique o público beneficiado' " + (editable ? "" : "readonly") + "></textarea>");
					else
						htmlBuilder.append("					<textarea name='projeto_publico_alvo' id='projeto_publico_alvo"+ proj +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + "></textarea>");
				}else{
					htmlBuilder.append("					<textarea name='projeto_publico_alvo' id='projeto_publico_alvo"+ proj +"'  placeholder='Indique o público beneficiado' " + (editable ? "" : "readonly") + ">" + p.getPublicoAlvo() + "</textarea>");
				}
				htmlBuilder.append("					</div>");
				htmlBuilder.append("<div class='col1_6'>");
				htmlBuilder.append("<strong class='separador'>Abrangência</strong>");
				htmlBuilder.append("<p id='projeto_abrang"+ proj +"' >");
				htmlBuilder.append("<select  " + (editable ? "" : "disabled") + ">");
				if(p.getAbrangencia() == "Nacional"){
					htmlBuilder.append("<option value='Nacional' selected='selected'> Nacional </option>");
					htmlBuilder.append("<option value='Regional' > Regional </option>");
					htmlBuilder.append("<option value='Estadual' > Estadual </option>");
					htmlBuilder.append("<option value='Municipal' > Municipal </option>");
				}else if(p.getAbrangencia() == "Regional"){
					htmlBuilder.append("<option value='Nacional' > Nacional </option>");
					htmlBuilder.append("<option value='Regional' selected='selected'> Regional </option>");
					htmlBuilder.append("<option value='Estadual' > Estadual </option>");
					htmlBuilder.append("<option value='Municipal' > Municipal </option>");
				}else if(p.getAbrangencia() == "Estadual"){
					htmlBuilder.append("<option value='Nacional' > Nacional </option>");
					htmlBuilder.append("<option value='Regional' > Regional </option>");
					htmlBuilder.append("<option value='Estadual' selected='selected'> Estadual </option>");
					htmlBuilder.append("<option value='Municipal' > Municipal </option>");
				}else if(p.getAbrangencia() == "Municipal"){
					htmlBuilder.append("<option value='Nacional' > Nacional </option>");
					htmlBuilder.append("<option value='Regional' > Regional </option>");
					htmlBuilder.append("<option value='Estadual' > Estadual </option>");
					htmlBuilder.append("<option value='Municipal' selected='selected'> Municipal </option>");
				}else{
					if(editable)
						htmlBuilder.append("<option value='' disabled selected>Indique Abrangência</option>");
					else
						htmlBuilder.append("<option value='' disabled selected>Informação não disponível</option>");
					htmlBuilder.append("<option value='Nacional' > Nacional </option>");
					htmlBuilder.append("<option value='Regional' > Regional </option>");
					htmlBuilder.append("<option value='Estadual' > Estadual </option>");
					htmlBuilder.append("<option value='Municipal' > Municipal </option>");
				}
				htmlBuilder.append("</select>");
				htmlBuilder.append("</p>");
				htmlBuilder.append("</div>");
				
				htmlBuilder.append("<div class='col1_2'>");
				htmlBuilder.append("<strong class='right-radius'>Localização do Projeto</strong>");
				htmlBuilder.append("<div class='localizacao_projeto'>");
				htmlBuilder.append("<ul id='locais"+proj+"' class='locais'>");
				if( p.getLocalizacao().size() >  0){
					for(LocalizacaoModel l : p.getLocalizacao()){
						if(l.getMunicipio() != null){
							addLocalProj++;
							htmlBuilder.append("<li style='margin-top: 3px;' ><a id='excluirLocal"+addLocalProj+"' name='"+ l.getIdMunicipio() +"' value='"+ l.getIdLocal() +"' class='excluir tooltip' title='Excluir'>Excluir</a> <a id='localizacaoProj"+ proj + addLocalProj +"' href='Map.html#P"+ l.getIdMunicipio() +"' target='_blank' >"+ l.getMunicipio() +"</a></li>");
						}
						if(l.getRegiao()!= null){
							addLocalProj++;
							htmlBuilder.append("<li style='margin-top: 3px;' ><a id='excluirLocal"+addLocalProj+"' name='"+ l.getIdRegiao() +"' value='"+ l.getIdLocal() +"' class='excluir tooltip' title='Excluir'>Excluir</a> <a id='localizacaoProj"+ proj + addLocalProj +"' href='Map.html#P"+ l.getIdRegiao() +"' target='_blank' >"+ l.getRegiao() +"</a></li>");
						}
						if(l.getUf()!= null){
							addLocalProj++;
							htmlBuilder.append("<li style='margin-top: 3px;' ><a id='excluirLocal"+addLocalProj+"' name='"+ l.getIdUf() +"' value='"+ l.getIdLocal() +"' class='excluir tooltip' title='Excluir'>Excluir</a> <a id='localizacaoProj"+ proj + addLocalProj +"' href='Map.html#P"+ l.getIdUf() +"' target='_blank' >"+ l.getUf() +"</a></li>");
						}
					}
				}else{
					if(!editable)
						htmlBuilder.append("<div style='margin-top: 3px;' >Informação não disponível</div>");
				}
				htmlBuilder.append("</ul>");
				
				htmlBuilder.append("<div id='buscarProj"+proj+"' >");
				htmlBuilder.append("<label for='campobusca' class='esconder' style='background: none;'>Buscar localização</label>");
				htmlBuilder.append("<input title='Campo de Busca para a Localização do Projeto' type='text' name='campobusca' id='enome"+ proj +"' placeholder='Indique a localização desejada...'>");
				htmlBuilder.append("<button type='button' name='adicionar' id='adicionar"+proj+"' class='adicionar'>Adicionar</button>");
				htmlBuilder.append("<button type='button' name='ajuda' class='ajuda' id='ajuda"+proj+"' style='padding: 5px 8px;' >?</button></div>");
				htmlBuilder.append("</div>");
				htmlBuilder.append("</div>");
				htmlBuilder.append("</div>");
				htmlBuilder.append("				<div class='clearfix linha'>");
				htmlBuilder.append("					<div class='col1_3'>");
				htmlBuilder.append("						<strong class='left-radius separador'>Financiadores do Projeto</strong>");
				if(p.getFinanciadores() == null || p.getFinanciadores() == ""){
					if(editable)
						htmlBuilder.append("					<textarea name='financiadores' id='financiadores"+ proj +"'  placeholder='Indique os financiadores' " + (editable ? "" : "readonly") + "></textarea>");
					else
						htmlBuilder.append("					<textarea name='financiadores' id='financiadores"+ proj +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + "></textarea>");
				}else{
					htmlBuilder.append("					<textarea name='financiadores' id='financiadores"+ proj +"'  placeholder='Indique os financiadores' " + (editable ? "" : "readonly") + ">" + p.getFinanciadores() + "</textarea>");
				}
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='col2_3'>");
				htmlBuilder.append("						<strong class='right-radius'>Descrição do projeto, atividade e/ou programa</strong>");
				if(p.getDescricao() == null || p.getDescricao() == ""){
					if(editable)
						htmlBuilder.append("					<textarea name='descprojeto' id='descprojeto"+ proj +"'  placeholder='Apresentar os objetivos de maneira sucinta' " + (editable ? "" : "readonly") + "></textarea>");
					else
						htmlBuilder.append("					<textarea name='descprojeto' id='descprojeto"+ proj +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + "></textarea>");
				}else{
					htmlBuilder.append("					<textarea name='descprojeto' id='descprojeto"+ proj +"'  placeholder='Apresentar os objetivos de maneira sucinta' " + (editable ? "" : "readonly") + ">" + p.getDescricao() + "</textarea>");
				}
				htmlBuilder.append("					</div>");
				htmlBuilder.append("				</div>");						
				htmlBuilder.append("			</div>");
				htmlBuilder.append("		</div>");
				htmlBuilder.append("	<hr />");
			}
			
			for (ConvenioModel c : org.getConvenios()){
				conv++;
				count = proj + conv;
				String link = "https://www.convenios.gov.br/siconv/ConsultarProposta/ResultadoDaConsultaDeConvenioSelecionarConvenio.do?sequencialConvenio="+c.getNConv()+"&Usr=guest&Pwd=guest";
				htmlBuilder.append("	<div class='clearfix projeto collapsable'>");
				htmlBuilder.append("		<div class='projeto_header'>");
				htmlBuilder.append("			<div id='recolherConv' class='collapse-click collapse-button' data-toggle='collapse' data-target='#recConv"+ conv +"' ><div></div></div>");
				htmlBuilder.append("				<div class='nome'>");
				htmlBuilder.append("					<span class='no_margin_right'>" + count  + " |</span>");
				htmlBuilder.append("					<span class='no_margin numero_convenio'> Parceria nº "+ c.getNConv() +" </span>");
				if(c.getTitulo() == null || c.getTitulo() == ""){
					htmlBuilder.append("				<span class='no_margin titulo_convenio'>Informação não disponível</span>");
				}else{
					htmlBuilder.append("				<span class='no_margin titulo_convenio'>" + c.getTitulo() + "</span>");
				}
				htmlBuilder.append("				</div>");
				htmlBuilder.append("				<span class='clear_both'></span>");
				htmlBuilder.append("			</div>");
				htmlBuilder.append("			<div id='recConv"+ conv +"' class='collapse'>");
				htmlBuilder.append("				<div class='linha clearfix'>");
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='separador left-radius'>Status</strong>");
				htmlBuilder.append("						<p id='convenio_status"+ conv +"' >");
				if(c.getStatus() == null || c.getStatus() == ""){
					htmlBuilder.append("					<span>Informação não disponível</span>");
				}else{
					htmlBuilder.append("					<span>"+ c.getStatus() +"</span>");
				}
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='separador'>Data Início</strong>");
				htmlBuilder.append("						<p><span name='projeto_data_inicio' id='convenio_data_inicio' >" + convertDateToString(c.getDataInicio()) + "</span></p>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='separador'>Data Final</strong>");
				htmlBuilder.append("						<p><span name='projeto_data_final' id='convenio_data_final' >" + convertDateToString(c.getDataFim()) + "</span></p>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='separador'>Valor Total</strong>");
				htmlBuilder.append("						<p><span name='projeto_valor_total' id='convenio_valor_total' > R$ " + convertNumberToCurrencyString(c.getValorTotal()) + "</span></p>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='separador'>Fonte de recurso</strong>");
				htmlBuilder.append("						<p id='convenio_fonte"+ conv +"' >");
				htmlBuilder.append("						<span>Público</span>");
//				if(c.getFonteRecursos() == null || c.getFonteRecursos() == ""){
//					htmlBuilder.append("					<span>Informação não disponível</span>");
//				}else{
//					htmlBuilder.append("					<span>"+ c.getFonteRecursos() +"</span>");
//				}
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='col1_6'>");
				htmlBuilder.append("						<strong class='right-radius'>Link</strong>");
				htmlBuilder.append("						<p>");
				htmlBuilder.append("							<span id='linkconv"+conv+"' title='' class='menuTooltip'>");
				htmlBuilder.append("								<span>Você será redirecionado para a página SICONV. Nesta página são apresentados estatuto, quadro de diretores, instrumento do convênio e prestação de contas</span>");
				htmlBuilder.append("								<a href="+ link +" target='_blank' contenteditable='false'>Sobre a Parceria</a>");
				htmlBuilder.append("							</span>");
				htmlBuilder.append("						</p>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("				</div>");
				htmlBuilder.append("				<div class='clearfix linha'>");
				htmlBuilder.append("					<div class='col1_3'>");
				htmlBuilder.append("						<strong class='separador left-radius'>Público Beneficiado</strong>");
				if(c.getPublicoAlvo() == null || c.getPublicoAlvo() == ""){
					if(editable)
						htmlBuilder.append("					<textarea name='"+ c.getNConv() +"' id='convenio_publico_alvo"+ conv +"'  placeholder='Indique o público beneficiado' " + (editable ? "" : "readonly") + "></textarea>");
					else
						htmlBuilder.append("					<textarea name='"+ c.getNConv() +"' id='convenio_publico_alvo"+ conv +"'  placeholder='Informação não disponível' " + (editable ? "" : "readonly") + "></textarea>");
				}else{
					htmlBuilder.append("					<textarea name='"+ c.getNConv() +"' id='convenio_publico_alvo"+ conv +"'  placeholder='Indique o público beneficiado' " + (editable ? "" : "readonly") + ">" + c.getPublicoAlvo() + "</textarea>");
				}
				htmlBuilder.append("					</div>");
				htmlBuilder.append("<div class='col1_6'>");
				htmlBuilder.append("<strong class='separador'>Abrangência</strong>");
				htmlBuilder.append("<p id='conv_abrang"+ conv +"' >");
				htmlBuilder.append("<select " + (editable ? "" : "disabled") + ">");
				if(c.getAbrangencia() == "Nacional"){
					htmlBuilder.append("<option value='Nacional' selected='selected'> Nacional </option>");
					htmlBuilder.append("<option value='Regional' > Regional </option>");
					htmlBuilder.append("<option value='Estadual' > Estadual </option>");
					htmlBuilder.append("<option value='Municipal' > Municipal </option>");
				}else if(c.getAbrangencia() == "Regional"){
					htmlBuilder.append("<option value='Nacional' > Nacional </option>");
					htmlBuilder.append("<option value='Regional' selected='selected'> Regional </option>");
					htmlBuilder.append("<option value='Estadual' > Estadual </option>");
					htmlBuilder.append("<option value='Municipal' > Municipal </option>");
				}else if(c.getAbrangencia() == "Estadual"){
					htmlBuilder.append("<option value='Nacional' > Nacional </option>");
					htmlBuilder.append("<option value='Regional' > Regional </option>");
					htmlBuilder.append("<option value='Estadual' selected='selected'> Estadual </option>");
					htmlBuilder.append("<option value='Municipal' > Municipal </option>");
				}else if(c.getAbrangencia() == "Municipal"){
					htmlBuilder.append("<option value='Nacional' > Nacional </option>");
					htmlBuilder.append("<option value='Regional' > Regional </option>");
					htmlBuilder.append("<option value='Estadual' > Estadual </option>");
					htmlBuilder.append("<option value='Municipal' selected='selected'> Municipal </option>");

				}else{
					if(editable)
						htmlBuilder.append("<option value='' disabled selected>Indique Abrangência</option>");
					else
						htmlBuilder.append("<option value='' disabled selected>Informação não disponível</option>");
					htmlBuilder.append("<option value='Nacional' > Nacional </option>");
					htmlBuilder.append("<option value='Regional' > Regional </option>");
					htmlBuilder.append("<option value='Estadual' > Estadual </option>");
					htmlBuilder.append("<option value='Municipal' > Municipal </option>");
				}
				htmlBuilder.append("</select>");
				htmlBuilder.append("</p>");
				htmlBuilder.append("</div>");
				
				htmlBuilder.append("<div class='col1_2'>");
				htmlBuilder.append("<strong class='right-radius'>Localização do Projeto</strong>");
				htmlBuilder.append("<div class='localizacao_projeto'>");
				htmlBuilder.append("<ul id='locaisConv"+conv+"' class='locais'>");
				if( c.getLocalizacao().size() >  0){
					for(LocalizacaoModel l : c.getLocalizacao()){
						if(l.getMunicipio() != null){
							addLocalConv++;
							htmlBuilder.append("<li style='margin-top: 3px;' ><a id='excluirLocalConv"+addLocalConv+"' name='"+ l.getIdMunicipio() +"' value='"+ l.getIdLocal() +"' class='excluir tooltip' title='Excluir'>Excluir</a> <a id='localizacaoConv"+ addLocalConv +"' href='Map.html#P"+ l.getIdMunicipio() +"' >"+ l.getMunicipio() +"</a></li>");
						}
						if(l.getRegiao()!= null){
							addLocalConv++;
							htmlBuilder.append("<li style='margin-top: 3px;' ><a id='excluirLocalConv"+addLocalConv+"' name='"+ l.getIdRegiao() +"' value='"+ l.getIdLocal() +"' class='excluir tooltip' title='Excluir'>Excluir</a> <a id='localizacaoConv"+ addLocalConv +"' href='Map.html#P"+ l.getIdRegiao() +"' >"+ l.getRegiao() +"</a></li>");
						}
						if(l.getUf()!= null){
							addLocalConv++;
							htmlBuilder.append("<li style='margin-top: 3px;' ><a id='excluirLocalConv"+addLocalConv+"' name='"+ l.getIdUf() +"' value='"+ l.getIdLocal() +"' class='excluir tooltip' title='Excluir'>Excluir</a> <a id='localizacaoConv"+ addLocalConv +"' href='Map.html#P"+ l.getIdUf() +"' >"+ l.getUf() +"</a></li>");
						}
					}
				}else{
					if(!editable)
						htmlBuilder.append("<div style='margin-top: 3px;' >Informação não disponível</div>");
				}
				htmlBuilder.append("</ul>");
				htmlBuilder.append("<div id='buscarConv"+conv+"'>");
				htmlBuilder.append("<label for='campobusca' class='esconder' style='background: none;'>Buscar localização</label>");
				htmlBuilder.append("<input title='Campo de Busca para a Localização do Projeto' type='text' name='campobusca' id='enome"+ count +"' placeholder='Indique a localização desejada...'>");
				htmlBuilder.append("<button type='button' name='addLocalConv' id='addLocalConv"+conv+"' class='adicionar'>Adicionar</button>");
				htmlBuilder.append("<button type='button' name='ajuda' class='ajuda' id='ajuda"+count+"' style='padding: 5px 8px;'>?</button></div>");
				htmlBuilder.append("</div>");
				htmlBuilder.append("</div>");
				htmlBuilder.append("</div>");
				htmlBuilder.append("					<div class='clearfix linha'>");
				htmlBuilder.append("						<div class='col1_3'>");
				htmlBuilder.append("							<strong class='left-radius separador'>Financiadores do Projeto</strong>");
				if(c.getFinanciadores() == null || c.getFinanciadores() == ""){
					htmlBuilder.append("						<span>Informação não disponível</span>");
				}else{
					htmlBuilder.append("						<span>"+ c.getFinanciadores() +"</span>");
				}
				htmlBuilder.append("						</div>");
				htmlBuilder.append("						<div class='col2_3'>");
				htmlBuilder.append("							<strong class='right-radius'>Descrição do projeto, atividade e/ou programa</strong>");
				if(c.getDescricao() == null || c.getDescricao() == ""){
					htmlBuilder.append("						<span>Informação não disponível</span>");
				}else{
					htmlBuilder.append("						<span>"+ c.getDescricao() +"</span>");
				}
				htmlBuilder.append("						</div>");
				htmlBuilder.append("					</div>");						
				htmlBuilder.append("				</div>");
				htmlBuilder.append("			</div>");
				htmlBuilder.append("		<hr />");
			}
		}
		htmlBuilder.append("			</div>");
		htmlBuilder.append("		</div>");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("	<div id='divbotoes' class='botoes'>");
		htmlBuilder.append("		<a href='#O"+ org.getId().toString() +"' id='cancelar' >Cancelar</a> ou <input id='btnSalvar' type='button' value='Salvar' class='salvar' />");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("</form>");
		
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
	
	public void valorTotal(EventListener listener) {
		for(int i=1; i<=proj; i++){
			Element valorTotal = DOM.getElementById("projeto_valor_total"+ i);
			Event.sinkEvents(valorTotal, Event.ONBLUR);
			Event.setEventListener(valorTotal, listener);
		}
	}
	
	private String formatCNPJ(Number number) {
		String cnpj = convertNumberToString(number);
		
		while(cnpj.length() < 14){
			cnpj = "0" + cnpj;
		}
		
		String bloco1 = cnpj.substring(0, 2);
		String bloco2 = cnpj.substring(2, 5);
		String bloco3 = cnpj.substring(5, 8);
		String bloco4 = cnpj.substring(8, 12);
		String bloco5 = cnpj.substring(12, 14);
		
		cnpj = bloco1 + "." + bloco2 + "." + bloco3 + "/" + bloco4 + "-" + bloco5;
		
		return cnpj;
	}
	
	private String formatCEI(Number number) {
		String cei = convertNumberToString(number);
		
		while(cei.length() < 12){
			cei = "0" + cei;
		}
		
		String bloco1 = cei.substring(0, 2);
		String bloco2 = cei.substring(2, 5);
		String bloco3 = cei.substring(5, 10);
		String bloco4 = cei.substring(10, 12);
		
		cei = bloco1 + "." + bloco2 + "." + bloco3 + "/" + bloco4;
		
		return cei;
	}
	
	/**
	 * @param listener
	 *            Controla o evento de click do botão salvar.
	 */
	public void addSalvarListener(EventListener listener) {
		Element div = DOM.getElementById("divbotoes");
		if(edit == false){
			div.setAttribute("style", "display: none");
			Element form = DOM.getElementById("addFormProj");
			form.setAttribute("style", "margin-bottom: 30px;");
		}else{
			div.setAttribute("style", "display: block");
			Element btnSalvar = DOM.getElementById("btnSalvar");
			Event.sinkEvents(btnSalvar, Event.ONCLICK);
			Event.setEventListener(btnSalvar, listener);
		}
	}
	
	public void addCancelListener(EventListener listener) {
		Element cancelar = DOM.getElementById("cancelar");
		Event.sinkEvents(cancelar, Event.ONCLICK);
		Event.setEventListener(cancelar, listener);
	}
	
	public void addEditar(EventListener listener) {
		Element edicao = DOM.getElementById("edicao");
		Event.sinkEvents(edicao, Event.ONCLICK);
		Event.setEventListener(edicao, listener);
	}
	
	public void addRecomendar(EventListener listener) {
		Element recomendar = DOM.getElementById("recomendar");
		Event.sinkEvents(recomendar, Event.ONCLICK);
		Event.setEventListener(recomendar, listener);
	}
	
	public void ajuda(EventListener listener) {
		for(int i=1; i<=count; i++){
			Element recomendar = DOM.getElementById("ajuda"+i);
			Event.sinkEvents(recomendar, Event.ONCLICK);
			Event.setEventListener(recomendar, listener);
		}
	}
	
	public OrganizationModel getOrg() {
		logger.info("Get Organization");
		OrganizationModel org = new OrganizationModel();
		String anoFundacaoString = InputElement.as(DOM.getElementById("ano_fundacao")).getValue();
		Integer anofundacao = -1;
		if(anoFundacaoString.length() > 0){
			try{
				anofundacao = Integer.parseInt(anoFundacaoString);
			}catch(NumberFormatException e){
				logger.severe(e.getMessage());
			}
		}
		org.setId(Integer.parseInt(History.getToken().substring(1)));
		org.setNomeFantasia(InputElement.as(DOM.getElementById("nome_fantasia")).getValue());
		org.setDescricaoProjeto(TextAreaElement.as(DOM.getElementById("descricao_osc")).getValue());
		org.setAnoFundacao(anofundacao);
		org.setEmail(InputElement.as(DOM.getElementById("email")).getValue());
		org.setSite(InputElement.as(DOM.getElementById("site")).getValue());
		
		String google = InputElement.as(DOM.getElementById("google")).getValue();
		if(google.length() > 0) org.setGoogle("https://" + (google.contains("://") ? google.split("://")[1] : google));
		
		String facebook = InputElement.as(DOM.getElementById("facebook")).getValue();
		if(facebook.length() > 0) org.setFacebook("https://" + (facebook.contains("://") ? facebook.split("://")[1] : facebook));
		
		String linkedin = InputElement.as(DOM.getElementById("linkedin")).getValue();
		if(linkedin.length() > 0) org.setLinkedin("https://" + (linkedin.contains("://") ? linkedin.split("://")[1] : linkedin));
		
		String twitter = InputElement.as(DOM.getElementById("twitter")).getValue();
		if(twitter.length() > 0) org.setTwitter("https://" + (twitter.contains("://") ? twitter.split("://")[1] : twitter));
		
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
			SelectElement selectStatus = status.getFirstChildElement().cast();
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
			SelectElement selectFonte = fonte.getFirstChildElement().cast();
			projmodel.setFonteRecursos(selectFonte.getValue());
			projmodel.setLink(InputElement.as(DOM.getElementById("link" + i)).getValue());
			projmodel.setPublicoAlvo(TextAreaElement.as(DOM.getElementById("projeto_publico_alvo" + i)).getValue());
			Element abrangencia = DOM.getElementById("projeto_abrang" + i);
			SelectElement selectAbrang = abrangencia.getFirstChild().cast();
			projmodel.setAbrangencia(selectAbrang.getValue());
			
			ArrayList<LocalizacaoModel> localizacaoList = new ArrayList<LocalizacaoModel>();
			for(int j = 1;j <= addLocalProj; j++){
				LocalizacaoModel localmodel = new LocalizacaoModel();
				Element eleLocal = DOM.getElementById("localizacaoProj" + i + j);
				if(eleLocal != null){
					Element local = eleLocal.getParentElement().getFirstChildElement();
					if(local.getAttribute("name").toString() == "addLocal"){
						String idLocal = local.getAttribute("value");
						if(idLocal.length() == 7)
							localmodel.setIdMunicipio(Integer.parseInt(idLocal));
						if(idLocal.length() == 1)
							localmodel.setIdRegiao(Integer.parseInt(idLocal));
						if(idLocal.length() == 2)
							localmodel.setIdUf(Integer.parseInt(idLocal));
						localizacaoList.add(localmodel);
					}
				}
			}
			projmodel.setLocalizacao(localizacaoList);
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
			Element abrangenciaconv = DOM.getElementById("conv_abrang" + i);
			SelectElement selectAbrangconv = abrangenciaconv.getFirstChild().cast();
			convmodel.setAbrangencia(selectAbrangconv.getValue());
			
			ArrayList<LocalizacaoModel> localizacaoList = new ArrayList<LocalizacaoModel>();
			for(int j = 1;j <= addLocalConv; j++){
				LocalizacaoModel localmodel = new LocalizacaoModel();
				Element eleLocal = DOM.getElementById("localizacaoConv" + i + j);
				if(eleLocal != null){
					Element local = eleLocal.getParentElement().getFirstChildElement();
					if(local.getAttribute("name").toString() == "addLocal"){
						String idLocal = local.getAttribute("value");
						if(idLocal.length() == 7)
							localmodel.setIdMunicipio(Integer.parseInt(idLocal));
						if(idLocal.length() == 1)
							localmodel.setIdRegiao(Integer.parseInt(idLocal));
						if(idLocal.length() == 2)
							localmodel.setIdUf(Integer.parseInt(idLocal));
						
						localizacaoList.add(localmodel);
					}
				}
			}
			convmodel.setLocalizacao(localizacaoList);
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
		} 
	}
	
	public void removeDir(EventListener listener) {
		for(int i = 1;i<=dir; i++){
			Element removeDir = DOM.getElementById("removedir"+ i);
			if(edit == false){
				removeDir.setAttribute("style", "display: none");
			}else{
				removeDir.setAttribute("style", "display: block");
				Event.sinkEvents(removeDir, Event.ONCLICK);
				Event.setEventListener(removeDir, listener);
			}
		}
	}
	
	public void removeLocalProj(EventListener listener) {
		for(int i=1; i<=addLocalProj; i++){
			Element excluirProj = DOM.getElementById("excluirLocal"+i);
			if(edit == false){
				excluirProj.setAttribute("style", "display: none");
			}else{
				excluirProj.setAttribute("style", "display: inline-block");
				Event.sinkEvents(excluirProj, Event.ONCLICK);
				Event.setEventListener(excluirProj, listener);
			}
		}
	}
	
	public void removeLocalConv(EventListener listener) {
		for(int i=1; i<=addLocalConv; i++){
			Element excluirConv = DOM.getElementById("excluirLocalConv"+i);
			if(edit == false){
				excluirConv.setAttribute("style", "display: none");
			}else{
				excluirConv.setAttribute("style", "display: inline-block");
				Event.sinkEvents(excluirConv, Event.ONCLICK);
				Event.setEventListener(excluirConv, listener);
			}
		}
	}
	
	public void addFocusListener(EventListener listener) {
		for(int i=1; i<=count; i++){
			final Element elem = DOM.getElementById("enome"+i);
			Event.sinkEvents(elem, Event.ONFOCUS);
			Event.setEventListener(elem, listener);
		}
	}
	
	public void addSearchChangeListener(EventListener listener) {
		for(int i=1; i<=count; i++){
			final Element elem = DOM.getElementById("enome"+i);
			Event.sinkEvents(elem, Event.ONKEYDOWN);
			Event.setEventListener(elem, listener);
		}
	}
	
	public void setValue(String ele, String value) {
		searchTextField = DOM.getElementById(ele);
		searchTextField.setAttribute("value", value);
	}
	
	public String getValue(String ele) {
		searchTextField = DOM.getElementById(ele);
		return searchTextField.getPropertyString("value");
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
		if(edit == false){
			addDiretor.setAttribute("style", "display: none");
		}else{
			addDiretor.setAttribute("style", "float:right;margin-top:3px;margin-right: 3px;display: block;");
			Event.sinkEvents(addDiretor, Event.ONCLICK);
			Event.setEventListener(addDiretor, listener);
		}
	}
	
	public void addProjetos(EventListener listener) {
		Element addProjetos = DOM.getElementById("addProjetos");
		if(edit == false){
			addProjetos.setAttribute("style", "display: none");
		}else{
			addProjetos.setAttribute("style", "display: block");
			Event.sinkEvents(addProjetos, Event.ONCLICK);
			Event.setEventListener(addProjetos, listener);
		}
	}
	
	public void addLocalProj(EventListener listener) {
		for(int i=1; i<=proj; i++){
			Element locaisProj = DOM.getElementById("buscarProj"+i);
			if(edit == false){
				locaisProj.setAttribute("style", "display: none");
			}else{
				locaisProj.setAttribute("style", "display: block");
				Element addLocalizacao = DOM.getElementById("adicionar"+i);
				Event.sinkEvents(addLocalizacao, Event.ONCLICK);
				Event.setEventListener(addLocalizacao, listener);
			}
		}
	}
	
	public void addLocalConv(EventListener listener) {
		for(int i=1; i<=conv; i++){
			Element locaisConv = DOM.getElementById("buscarConv"+i);
			if(edit == false){
				locaisConv.setAttribute("style", "display: none");
			}else{
				locaisConv.setAttribute("style", "display: block");
				Element addLocalizacao = DOM.getElementById("addLocalConv"+i);
				Event.sinkEvents(addLocalizacao, Event.ONCLICK);
				Event.setEventListener(addLocalizacao, listener);
			}
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
