package gov.sgpr.fgv.osc.portalosc.organization.client.components;

import java.util.Date;
import java.util.logging.Logger;

import org.omg.CORBA.DATA_CONVERSION;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

import gov.sgpr.fgv.osc.portalosc.organization.client.controller.OrganizationController;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.ConfigurationModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.OrganizationModel;
import gov.sgpr.fgv.osc.portalosc.organization.shared.model.ProjetoModel;

public class FormularioWidget extends Composite {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	
	public FormularioWidget(OrganizationModel organizationModel){
		initWidget(getHTML(organizationModel));
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
	}
	
	private HTML getHTML(OrganizationModel org){
		StringBuilder htmlBuilder = new StringBuilder();
		
		htmlBuilder.append("<div class='container'>");
		htmlBuilder.append("	<div class='social'>");
		htmlBuilder.append("		<div class='imagem'>");
		htmlBuilder.append("			<img src='imagens/org_imagem.jpg' alt='" + org.getRazaoSocial() + "' width='160' height='160' />"); // FALTA BANCO
		htmlBuilder.append("		</div>");
		htmlBuilder.append("		<div class='redes'>");
//		htmlBuilder.append("			<button type='button' id='b-google' name='redes' value='false' title='Google' class='tooltip'></button>"); // FALTA BANCO
//		htmlBuilder.append("			<button type='button' id='b-facebook' name='redes' value='false' title='Facebook' class='tooltip'></button>"); // FALTA BANCO
//		htmlBuilder.append("			<button type='button' id='b-linkedin' name='redes' value='false' title='LinkedIn' class='tooltip'></button>"); // FALTA BANCO
//		htmlBuilder.append("			<button type='button' id='b-twitter' name='redes' value='false' title='Twitter' class='tooltip'></button>"); // FALTA BANCO
		htmlBuilder.append("		</div>");
		htmlBuilder.append("		<a href='como_participar.html' class='participar box'>Como Participar</a>");
		htmlBuilder.append("		<div class='recomendacoes'>");
		htmlBuilder.append("			<span class='tooltip' title='" + org.getRecomendacoes() + " recomendações'>" + org.getRecomendacoes() + "</span>");
		htmlBuilder.append("		</div>");
//		htmlBuilder.append("		<button type='button' class='recomendar'>Recomendar</button>");
//		htmlBuilder.append("		<div class='classificacao'>");
//		htmlBuilder.append("			<h3>Classificação</h3>");
//		htmlBuilder.append("			<ul class='dados'>");
//		htmlBuilder.append("				<li class='fasfil'><a href='#'>Educação e pesquisa</a></li>"); // FALTA BANCO
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
		htmlBuilder.append("						<input type='text' name='razao_social' id='razao_social' placeholder='Informação não disponível' value='" + org.getRazaoSocial() + "' />");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Nome Fantasia:</strong>");
		htmlBuilder.append("						<input type='text' name='nome_fantasia' id='nome_fantasia' placeholder='Informação não disponível' value='" + org.getNomeFantasia() + "' />");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>CNPJ:</strong>");
		htmlBuilder.append("						<input type='text' name='cnpj' id='cnpj' placeholder='Informação não disponível' value='" + org.getCnpj().toString() + "' />");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Endereço:</strong>");
		if(org.getEndereco() == ""){
			htmlBuilder.append("						<span id='endereco' title='Endereço'>Informação não disponível</span>");
		}else{
			htmlBuilder.append("						<a href='" + GWT.getHostPageBaseURL() + "Map.html#O" + org.getId().toString() + "'><span id='endereco' title='Endereço'>" + org.getEndereco() + "</span></a>");
		}
//		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
//		htmlBuilder.append("					<div>");
//		htmlBuilder.append("						<strong>Outros Endereços:</strong>");
//		htmlBuilder.append("						<a href='Lista.html?grupoOSC=123'>visualizar Lista</a>");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
//		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Natureza Jurídica:</strong>");
		htmlBuilder.append("						<input type='text' name='natureza_juridica' id='natureza_juridica' placeholder='Informação não disponível' value='" + org.getNaturezaJuridica() + "' />");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>CNAE:</strong>");
		htmlBuilder.append("						<input type='text' name='cnae' id='cnae' placeholder='Informação não disponível' value='" + org.getCnae() + "' />");
//		htmlBuilder.append("					<span class='fonte_de_dados dado_oficial' title='Dado Oficial, fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Responsável Legal:</strong>");
		htmlBuilder.append("						<input type='text' name='responsavel' id='responsavel' placeholder='Informação não disponível' value='" + org.getResponsavel() + "' />");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, fonte RAIS'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Descrição do Projeto:</strong>");
		htmlBuilder.append("						<textarea name='descricao_projeto' id='descricao_projeto' placeholder='Informação não disponível'>" + org.getDescricaoProjeto() + "</textarea>");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Ano de Fundação:</strong>");
		htmlBuilder.append("						<input type='text' name='ano_fundacao' id='ano_fundacao' placeholder='Informação não disponível' value='" + convertNumberToString(org.getAnoFundacao()) + "' />");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Site:</strong>");
		htmlBuilder.append("						<input type='text' name='site' id='site' placeholder='Informação não disponível' value='" + org.getSite() + "' />");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>E-mail:</strong>");
		htmlBuilder.append("						<input type='text' name='email' id='email' placeholder='Informação não disponível' value='" + org.getEmail() + "' />");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
		htmlBuilder.append("					</div>");
		
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Participação social:</strong>");
		htmlBuilder.append("						<ul class='participacao'>");
		if(org.getParticipacaoSocial().size() == 0){
			htmlBuilder.append("							<li>");
			htmlBuilder.append("								<span class='visualizacao_dados'>Não consta como participante de conselhos</span>");
//			htmlBuilder.append("								<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
			htmlBuilder.append("							</li>");
		}else{
			for (String s : org.getParticipacaoSocial()) {
				htmlBuilder.append("							<li>");
				htmlBuilder.append("								<span class='visualizacao_dados'>" + s + "</span>");
//				htmlBuilder.append("								<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
				htmlBuilder.append("							</li>");
			}
		}
		htmlBuilder.append("						</ul>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Fonte de recursos:</strong>");
		htmlBuilder.append("						<ul class='recursos'>");
		if(org.getFonteRecursos().size() == 0){
			htmlBuilder.append("							<li>");
			htmlBuilder.append("								<span class='visualizacao_dados'>Informação não disponível</span>");
//			htmlBuilder.append("								<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
			htmlBuilder.append("							</li>");
		}else{
			for (String s : org.getFonteRecursos()) {
				htmlBuilder.append("							<li>");
				htmlBuilder.append("								<span class='visualizacao_dados'>" + s + "</span>"); // FALTA BANCO
//				htmlBuilder.append("								<span class='fonte_de_dados dado_organizacao' title='Dado preenchido pela Organização'></span>");
				htmlBuilder.append("							</li>");
			}
		}
		htmlBuilder.append("						</ul>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("				</fieldset>");
		htmlBuilder.append("			</div>");
		htmlBuilder.append("			<h2>Colaboradores</h2>");
		htmlBuilder.append("			<fieldset>");
		htmlBuilder.append("				<div class='recursos collapsable'>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Total de Colaboradores:</strong>");
		htmlBuilder.append("						<input type='text' name='total_colaboradores' id='total_colaboradores' placeholder='Informação não disponível' value='" + convertNumberToString(org.getTotalColaboradores()) + "' />");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte RAIS'></span>");
		htmlBuilder.append("						<div class='collapse-click collapse-button'><div></div></div>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div class='collapse-hidden'>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Trabalhadores:</strong>");
		htmlBuilder.append("							<input type='text' name='trabalhadores' id='trabalhadores' placeholder='Informação não disponível' value='" + convertNumberToString(org.getTrabalhadores()) + "' />");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Voluntários:</strong>");
		htmlBuilder.append("							<input type='text' name='voluntarios' id='voluntarios' placeholder='Informação não disponível' value='" + convertNumberToString(org.getVoluntarios()) + "' />");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Colaboradores portadores de deficiência:</strong>");
		htmlBuilder.append("							<input type='text' name='colaboradores_portadores_deficiencia' id='colaboradores_portadores_deficiencia' placeholder='Informação não disponível' value='" + convertNumberToString(org.getPortadoresDeficiencia()) + "' />");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("				</div>");
		htmlBuilder.append("			</fieldset>");
		htmlBuilder.append("			<h2>Recursos públicos</h2>");
		htmlBuilder.append("			<fieldset>");
		htmlBuilder.append("				<div class='recursos collapsable'>");
		htmlBuilder.append("					<div>");
		htmlBuilder.append("						<strong>Valor total das parcerias:</strong>");
		htmlBuilder.append("						<input type='text' name='valor_total_parcerias' id='valor_total_parcerias' placeholder='Informação não disponível' />");
//		htmlBuilder.append("						<span class='fonte_de_dados dado_oficial' title='Dado Oficial, Fonte SINAFI'></span>");
		htmlBuilder.append("						<div class='collapse-click collapse-button'><div></div></div>");
		htmlBuilder.append("					</div>");
		htmlBuilder.append("					<div class='collapse-hidden'>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Valor das parcerias federais:</strong>");
		htmlBuilder.append("							<input type='text' name='valor_parcerias_federais' id='valor_parcerias_federais' placeholder='Informação não disponível' />");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Valor das parcerias estaduais:</strong>");
		htmlBuilder.append("							<input type='text' name='valor_parcerias_estaduais' id='valor_parcerias_estaduais' placeholder='Informação não disponível' />");
		htmlBuilder.append("						</div>");
		htmlBuilder.append("						<div>");
		htmlBuilder.append("							<strong>Valor das parcerias municipais:</strong>");
		htmlBuilder.append("							<input type='text' name='valor_parcerias_municipais' id='valor_parcerias_municipais' placeholder='Informação não disponível' />");
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
		for (String s : org.getCertificacao()) {
			htmlBuilder.append("					<li>" + s + "</li>");
//			htmlBuilder.append("					<li class='fonte'><em>Fonte:</em> SICONV</li>");
		}
		htmlBuilder.append("				</ul>");
		htmlBuilder.append("			</div>");
		htmlBuilder.append("		</div>");
		
		htmlBuilder.append("		<div class='projetos'>");
		htmlBuilder.append("			<div class='titulo'>");
		htmlBuilder.append("				<h2>Projetos</h2>");
//		htmlBuilder.append("				<div class='filtro'>");
//		htmlBuilder.append("					<span>Fontes:</span>");
//		htmlBuilder.append("					<span>");
//		htmlBuilder.append("						<label for='PUBLICA'>Pública</label>");
//		htmlBuilder.append("						<input type='checkbox' name='PUBLICA' />");
//		htmlBuilder.append("					</span>");
//		htmlBuilder.append("					<span>");
//		htmlBuilder.append("						<label for='PRIVADO'>Privada</label>");
//		htmlBuilder.append("						<input type='checkbox' name='PRIVADO' />");
//		htmlBuilder.append("					</span>");
//		htmlBuilder.append("					<span>");
//		htmlBuilder.append("						<label for='PROPRIA'>Própria</label>");
//		htmlBuilder.append("						<input type='checkbox' name='PROPRIA' />");
//		htmlBuilder.append("					</span>");
//		htmlBuilder.append("				</div>");
		htmlBuilder.append("			</div>");
		if(org.getProjetos().size() == 0){
			htmlBuilder.append("			<div class='clearfix projeto collapsable'>");
			htmlBuilder.append("				<span id='projeto' title='projeto'>Informação não disponível</span>");
			htmlBuilder.append("			</div>");
		}else{
			for(int i = 1; i <= org.getProjetos().size(); i++){
				ProjetoModel p = org.getProjetos().get(i);
				htmlBuilder.append("			<div class='clearfix projeto collapsable'>");
				htmlBuilder.append("				<div class='projeto_header'>");
				htmlBuilder.append("					<div class='collapse-click collapse-button'><div></div></div>");
				htmlBuilder.append("						<div class='nome'>");
				htmlBuilder.append("							<span class='no_margin_right'>" + i + " |</span>");
				htmlBuilder.append("							<input type='text' name='projeto_nome_projeto' id='projeto_nome_projeto' placeholder='Informação não disponível' value='" + p.getTitulo() + "' />");
				htmlBuilder.append("						</div>");
				htmlBuilder.append("						<span class='clear_both'></span>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='projeto_body collapse-hidden'>");
				htmlBuilder.append("						<div class='linha clearfix'>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador left-radius'>Status</strong>");
				htmlBuilder.append("								<p>");
				htmlBuilder.append("									<select id='projeto_status'>");
				if(p.getStatus() == "planejado"){
					htmlBuilder.append("										<option value='planejado' checked='checked'> Planejado </option>");
					htmlBuilder.append("										<option value='execucao' > Em Execução </option>");
					htmlBuilder.append("										<option value='finalizado' > Finalizado </option>");
				}else if(p.getStatus() == "execucao"){
					htmlBuilder.append("										<option value='planejado' > Planejado </option>");
					htmlBuilder.append("										<option value='execucao' checked='checked'> Em Execução </option>");
					htmlBuilder.append("										<option value='finalizado' > Finalizado </option>");
				}else if(p.getStatus() == "finalizado"){
					htmlBuilder.append("										<option value='planejado' > Planejado </option>");
					htmlBuilder.append("										<option value='execucao' > Em Execução </option>");
					htmlBuilder.append("										<option value='finalizado' checked='checked'> Finalizado </option>");
				}
				htmlBuilder.append("									</select>");
				htmlBuilder.append("								</p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador'>Data Início</strong>");
				htmlBuilder.append("								<p><input type='text' name='projeto_data_inicio' id='projeto_data_inicio' placeholder='Informação não disponível' value='" + convertDateToString(p.getDataInicio()) + "' /></p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador'>Data Final</strong>");
				htmlBuilder.append("								<p><input type='text' name='projeto_data_final' id='projeto_data_final' placeholder='Informação não disponível' value='" + convertDateToString(p.getDataFim()) + "' /></p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador'>Valor Total</strong>");
				htmlBuilder.append("								<p><input type='text' name='projeto_valor_total' id='projeto_valor_total' placeholder='Informação não disponível' value='" + convertNumberToString(p.getValorTotal()) + "' /></p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador'>Principal Fonte de Recurso</strong>");
				htmlBuilder.append("								<p><input type='text' name='projeto_fonte_recursos' id='projeto_fonte_recursos' placeholder='Informação não disponível' value='" + p.getFonteRecursos() + "' /></p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='right-radius'>Link</strong>");
				htmlBuilder.append("								<p><a href='vamosla' contenteditable='false'>link para página</a></p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("						</div>");
				htmlBuilder.append("						<div class='clearfix linha'>");
				htmlBuilder.append("							<div class='col1_3'>");
				htmlBuilder.append("								<strong class='separador left-radius'>Público Alvo do Projeto</strong>");
				htmlBuilder.append("								<textarea name='projeto_publico_alvo' id='projeto_publico_alvo' disabled='disabled'>Pessoas de baixa renda...</textarea>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col1_6'>");
				htmlBuilder.append("								<strong class='separador'>Abrangência</strong>");
				htmlBuilder.append("								<p>");
				htmlBuilder.append("									<select disabled='disabled'>");
				htmlBuilder.append("										<option value='1' > Nacional </option>");
				htmlBuilder.append("										<option value='2' > Regional </option>");
				htmlBuilder.append("										<option value='3' > Estadual </option>");
				htmlBuilder.append("										<option value='4' > Municipal </option>");
				htmlBuilder.append("									</select>");
				htmlBuilder.append("								</p>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("							<div class='col1_2'>");
				htmlBuilder.append("							<strong class='right-radius'>Localização do Projeto</strong>");
				htmlBuilder.append("							<div class='localizacao_projet'>");
				htmlBuilder.append("								<ul class='locais'>");
				htmlBuilder.append("									<li><a href='mapa.html#5'>Nordeste</a></li>");
				htmlBuilder.append("								</ul>");
				htmlBuilder.append("							</div>");
				htmlBuilder.append("						</div>");
				htmlBuilder.append("					</div>");
				htmlBuilder.append("					<div class='clearfix linha'>");
				htmlBuilder.append("						<div class='col1_3'>");
				htmlBuilder.append("							<strong class='left-radius separador'>Financiadores do Projeto</strong>");
				htmlBuilder.append("							<textarea name='financiadores' id='financiadores1' disabled='disabled'>Empresa AXDAS Associados, XXR, Lorem IPSUM</textarea>");
				htmlBuilder.append("						</div>");
				htmlBuilder.append("						<div class='col2_3'>");
				htmlBuilder.append("							<strong class='right-radius'>Descrição do Projeto</strong>");
				htmlBuilder.append("							<textarea name='descprojeto' id='descprojeto1' disabled='disabled'>Lorem ipsum dolor sit, consectetur adipiscing elit. Nulla eu malesuada nulla.auctor ullamcorper quam at porta.</textarea>");
				htmlBuilder.append("						</div>");
				htmlBuilder.append("					</div>");						
				htmlBuilder.append("				</div>");
				htmlBuilder.append("			<hr />");
			}
		}
		htmlBuilder.append("		</div>");
		
		htmlBuilder.append("	</div>");
		htmlBuilder.append("</div>");
		
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}
	
	private String convertNumberToString(Number number){
		String result = String.valueOf(number);
		if(number.longValue() == 0L){
			result = "";
		}
		return result;
	}
	
	private String convertDateToString(Date date){
		String result = date.toString();
		if(result.length() == 0){
			result = "";
		}
		return result;
	}
	
	/**
	 * @param listener
	 *            Controla o evento de busca de OSC.
	 */
	public void addFocusListener(EventListener listener) {
		final Element elem = DOM.getElementById("enome");
		Event.sinkEvents(elem, Event.ONFOCUS);
		Event.setEventListener(elem, listener);
	}
	
	/**
	 * @param listener
	 *            Controla o evento de busca de OSC.
	 */
	public void addSearchChangeListener(EventListener listener) {
		final Element elem = DOM.getElementById("enome");
		Event.sinkEvents(elem, Event.ONKEYDOWN);
		Event.setEventListener(elem, listener);
	}
	
//	/**
//	 * @param listener
//	 *            Controla o evento de click do botão "Localizar".
//	 */
//	public void addSearchClickListener(EventListener listener) {
//		final Element elem = DOM.getElementById("localizarOSC");
//		Event.sinkEvents(elem, Event.ONCLICK);
//		Event.setEventListener(elem, listener);
//	}
	
	/**
	 * @param listener
	 *            Controla o evento de click do botão "Localizar".
	 */
	public void addCancelOSCClickListener(EventListener listener) {
		final Element elem = DOM.getElementById("cancelarOSC");
		Event.sinkEvents(elem, Event.ONCLICK);
		Event.setEventListener(elem, listener);
	}
	
	/**
	 * @param listener
	 *            Controla o evento de click do botão salvar.
	 */
	public void addSalvarListener(EventListener listener) {
		Element btnSalvar = DOM.getElementById("btnSalvar");
		Event.sinkEvents(btnSalvar, Event.ONCLICK);
		Event.setEventListener(btnSalvar, listener);
		validate();
	}
	
	/**
	 * @param listener
	 *            Controla o evento de click do botão cancelar.
	 */
	public void addCancelarListener(EventListener listener) {
		Element btnCancelar = DOM.getElementById("btnCancelar");
		Event.sinkEvents(btnCancelar, Event.ONCLICK);
		Event.setEventListener(btnCancelar, listener);
	}
	
	public ConfigurationModel getUser() {
		ConfigurationModel user = new ConfigurationModel();
		String oscCode = InputElement.as(DOM.getElementById("oscCode")).getValue();
		if(oscCode == "0"){
			user.setTipoUsuario(2);
		}
		else{
			user.setTipoUsuario(Integer.valueOf(InputElement.as(DOM.getElementById("ctipo")).getValue()));
		}
		user.setId(Integer.valueOf(InputElement.as(DOM.getElementById("cid")).getValue()));
		user.setNome(InputElement.as(DOM.getElementById("cnome")).getValue());
		user.setCPF(Long.valueOf(InputElement.as(DOM.getElementById("ccpf")).getValue().replaceAll("\\D", "")));
		user.setEmail(InputElement.as(DOM.getElementById("cemail")).getValue());
		user.setSenha(OrganizationController.encrypt(InputElement.as(DOM.getElementById("csenha")).getValue()));
		if(DOM.getElementById("inscrever").getPropertyBoolean("checked")){
			user.setListaEmail(true);
		}
		else{
			user.setListaEmail(false);
		}
		user.setIdOsc(Integer.valueOf(oscCode));
		return user;
	}
	
	public void setUser(ConfigurationModel user) {
		DOM.getElementById("ctipo").setAttribute("value", String.valueOf(user.getTipoUsuario()));
		DOM.getElementById("cid").setAttribute("value", String.valueOf(user.getId()));
		DOM.getElementById("cnome").setAttribute("value", user.getNome());
		DOM.getElementById("ccpf").setAttribute("value", String.valueOf(user.getCPF()));
		DOM.getElementById("cemail").setAttribute("value", user.getEmail());
		DOM.getElementById("csenha").setAttribute("value", OrganizationController.decrypt(user.getSenha()));
		DOM.getElementById("ccsenha").setAttribute("value", OrganizationController.decrypt(user.getSenha()));
		if(user.getListaEmail()){
			DOM.getElementById("inscrever").setAttribute("checked", "checked");
		}
		DOM.getElementById("eid").setAttribute("value", String.valueOf(user.getIdOsc()));
		if(user.getIdOsc() != 0){
			DOM.getElementById("entity_name").getStyle().setDisplay(Display.BLOCK);
			DOM.getElementById("botoes").getStyle().setDisplay(Display.BLOCK);
		}
		DOM.getElementById("oscCode").setAttribute("value", String.valueOf(user.getIdOsc()));
	}
	
	public void showOrganization(String oscInfo, String oscId) {
		DOM.getElementById("resultado_busca").getStyle().setDisplay(Display.NONE);
		DOM.getElementById("entity_name").getStyle().setDisplay(Display.BLOCK);
		DOM.getElementById("botoes").getStyle().setDisplay(Display.BLOCK);
		DOM.getElementById("oscName").setInnerText(oscInfo);
		DOM.getElementById("oscCode").setAttribute("value", oscId);
		DOM.getElementById("ctipo").setAttribute("value", "4");
	}
	
	public void clearOSC(){
		DOM.getElementById("entity_name").getStyle().setDisplay(Display.NONE);
		DOM.getElementById("botoes").getStyle().setDisplay(Display.NONE);
		DOM.getElementById("oscName").setInnerText("");
		DOM.getElementById("oscCode").setAttribute("value", "0");
		DOM.getElementById("ctipo").setAttribute("value", "2");
	}
	
	public Boolean getEmail(){
		Boolean result = false;
		if(InputElement.as(DOM.getElementById("oscCode")).getValue() != InputElement.as(DOM.getElementById("eid")).getValue() && 
				InputElement.as(DOM.getElementById("ctipo")).getValue() == "4"){
			result = true;
		}
		return result;
	}
	
	private native void validate() /*-{
		$wnd.jQuery.validator.addMethod("notEqual", function(value, element,
				param) {
			return value != param;
		}, "Por favor, informe um email não cadastrado.");
		
		$wnd.jQuery.validator.addMethod("notEqualCPF", function(value, element,
				param) {
			value = value.replace('.', '');
			value = value.replace('.', '');
			value = value.replace('-', '');
			value = parseInt(value);
			return value != param;
		}, "Por favor, informe um cpf não cadastrado.");
	
		$wnd.jQuery.validator
				.addMethod(
						"verificaCPF",
						function(value, element) {
							value = value.replace('.', '');
							value = value.replace('.', '');
							cpf = value.replace('-', '');
							while (cpf.length < 11)
								cpf = "0" + cpf;
							var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
							var a = [];
							var b = new Number;
							var c = 11;
							for (i = 0; i < 11; i++) {
								a[i] = cpf.charAt(i);
								if (i < 9)
									b += (a[i] * --c);
							}
							if ((x = b % 11) < 2) {
								a[9] = 0
							} else {
								a[9] = 11 - x
							}
							b = 0;
							c = 11;
							for (y = 0; y < 10; y++)
								b += (a[y] * c--);
							if ((x = b % 11) < 2) {
								a[10] = 0;
							} else {
								a[10] = 11 - x;
							}
							if ((cpf.charAt(9) != a[9])
									|| (cpf.charAt(10) != a[10])
									|| cpf.match(expReg))
								return false;
							return true;
						}, "Informe um CPF válido."); // Mensagem padrão
	
		var validate = $wnd.jQuery('#form_cadastro_config').validate({
			ignore : [],
			rules : {
				cemail : {
					required : true,
					email : true
				},
				cnome : {
					required : true,
					minlength : 5
				},
				ccpf : {
					verificaCPF : true,
					required : true
				},
				csenha : {
					required : true,
					minlength : 6
				},
				ccsenha : {
					required : true,
					equalTo : '#csenha'
				},
				cinscrever : {
					required : false
				}
			},
			messages : {
				csenha : {
					required : 'Campo obrigatório.',
					minlength : 'Senha muito curta.'
				},
				cemail : {
					required : 'Campo obrigatório.',
					email : 'Por favor, informe um email válido.'
				},
				ccsenha : {
					required : 'Campo obrigatório.',
					equalTo : 'As duas senhas devem ser iguais.'
				},
				cnome : {
					required : 'Campo obrigatório.',
					minlength : 'O nome deve conter ao menos {0} caracteres.'
				},
				ccpf : {
					verificaCPF : 'CPF inválido.',
					required : 'Informe seu CPF.'
				}
			},
			
			errorPlacement: function(error, element) {
				var parent = $wnd.jQuery(element).parent();
				console.log(parent); 
				error.appendTo(parent);
			},
		});
	}-*/;
	
	/**
	 * Verifica se o formulário é valido.
	 * 
	 * @return true se válido
	 */
	public native boolean isValid() /*-{
		return $wnd.jQuery('#form_cadastro_config').valid();
	}-*/;
	
	/**
	 * Adiciona regra para email inválido.
	 * 
	 * @param invalidEmail
	 */
	public native void addInvalidEmail(String invalidEmail) /*-{
	$wnd.jQuery('#cemail').rules('add', {
		notEqual : invalidEmail,
		messages : {
			notEqual : "Este email já esta cadastrado no Mapa"
		}
	});
}-*/;
	
	/**
	 * Adiciona regra para cpf já cadastrado no mapa.
	 * 
	 * @param email
	 */
	public native void addInvalidCpf(String invalidCpf) /*-{
		$wnd.jQuery('#ccpf').rules('add', {
			notEqualCPF : invalidCpf,
			messages : {
				notEqualCPF : "Este CPF já esta cadastrado no Mapa"
			}
		});
	}-*/;
	
	/**
	 * Adiciona regra para email inválido.
	 * 
	 * @param passwd
	 *            senha inválida
	 */
	
	public native void addInvalidPassword(String invalidPasswd) /*-{
		$wnd.jQuery('#csenha').rules('remove');
		$wnd.jQuery('#ccsenha').rules('remove');
		$wnd.jQuery('#csenha').rules('add', {
			notEqual : invalidPasswd,
			messages : {
				notEqual : "Senha inválida"
			}
		});
	}-*/;
}