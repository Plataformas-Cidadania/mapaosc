package gov.sgpr.fgv.osc.portalosc.uploadlocality.client.components;

import java.util.logging.Logger;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

import gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.model.AgreementLocalityModel;

public class UploadLocalityFormWidget extends Composite {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	
	public UploadLocalityFormWidget() {
		logger.info("Iniciando UploadLocalityFormWidget");
		initWidget(getHtml());
	}
	
	private HTML getHtml() {
		StringBuilder htmlBuilder = new StringBuilder();
		
		htmlBuilder.append("<div id='envio_de_arquivo' style='display: block'>");
		htmlBuilder.append("	<div>");
		htmlBuilder.append("		<label>Selecione o arquivo a ser enviado</label>");
		htmlBuilder.append("		<label for='darquivo'>Arquivo Enviado:</label>");
		htmlBuilder.append("		<input type='file' name='darquivo' id='darquivo'>");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("	<div>");
		htmlBuilder.append("		<label>Selecione o tipo de arquivo a ser enviado</label>");
		htmlBuilder.append("		<label for='dtipo_arquivo'>Tipo de Arquivo: </label>");
		htmlBuilder.append("		<select name='dtipo_arquivo' id='dtipo_arquivo'>");
		htmlBuilder.append("			<option value='csv' checked>CSV</option>");
		htmlBuilder.append("			<option value='xls'>XLS</option>");
		htmlBuilder.append("		</select>");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("</div>");
		
		htmlBuilder.append("<div id='envio_de_servico' style='display: none'>");
		htmlBuilder.append("	<div>");
		htmlBuilder.append("		<label>Indique o endereço de acesso(url) do webservice</label>");
		htmlBuilder.append("		<label for='dservico'>Endereço do Serviço:</label>");
		htmlBuilder.append("		<input type='text' name='dservico' id='dservico'>");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("	<div>");
		htmlBuilder.append("		<label>Selecione como o arquivo do webservice foi estuturado</label>");
		htmlBuilder.append("		<label for='dtipo_servico'>Arquivo Retornado: </label>");
		htmlBuilder.append("		<select name='dtipo_servico' id='dtipo_servico'>");
		htmlBuilder.append("			<option value='csv' checked>CSV</option>");
		htmlBuilder.append("			<option value='json'>JSON</option>");
		htmlBuilder.append("			<option value='xml'>XML</option>");
		htmlBuilder.append("			<option value='xls'>XLS</option>");
		htmlBuilder.append("		</select>");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("	<div>");
		htmlBuilder.append("		<label>Indique a periodicidade com a qual o Mapa deve atualizar os dados</label>");
		htmlBuilder.append("		<label for='dperiodo'> Periodicidade: </label>");
		htmlBuilder.append("		<input name='dperiodo' id='dperiodo' type='text' value=''/>");
		htmlBuilder.append("		<select name='dtipo_periodo' id='dtipo_periodo'>");
		htmlBuilder.append("			<option value='dia'>Dias</option>");
		htmlBuilder.append("			<option value='semana'>Semanas</option>");
		htmlBuilder.append("			<option value='mes'>Meses</option>");
		htmlBuilder.append("		</select>");
		htmlBuilder.append("	</div>");
		htmlBuilder.append("</div>");
		
		HTML html = new HTML(htmlBuilder.toString());
		return html;
	}
	
	public AgreementLocalityModel getUser() {
		InputElement email = InputElement.as(DOM.getElementById("cemail"));
		InputElement passwd = InputElement.as(DOM.getElementById("csenha"));
		InputElement name = InputElement.as(DOM.getElementById("cnome"));
		InputElement cpf = InputElement.as(DOM.getElementById("ccpf"));
		SelectElement state = SelectElement.as(DOM.getElementById("cest"));
		SelectElement county = SelectElement.as(DOM.getElementById("cmun"));
		InputElement organ = InputElement.as(DOM.getElementById("corgao"));
		InputElement function = InputElement.as(DOM.getElementById("ccargo"));
		InputElement phone = InputElement.as(DOM.getElementById("ctel"));
		
		AgreementLocalityModel agreement = new AgreementLocalityModel();
		
		return agreement;
	}
	
	private native void validateArquivo() /*-{
		var validate = $wnd.jQuery('#form_upload_locality').validate({
			ignore : [],
			rules : {
				darquivo : {
					required : true
				},
				dtipo_arquivo : {
					required : true
				}
			},
			messages : {
				darquivo : {
					required : 'Campo obrigatório.'
				},
				dtipo_arquivo : {
					required : 'Campo obrigatório.'
				}
			},
			
			errorPlacement: function(error, element) {
				var parent = $wnd.jQuery(element).parent();
				console.log(parent); 
				error.appendTo(parent);
			},
		});
	}-*/;
	
	private native void validateWebService() /*-{
		var validate = $wnd.jQuery('#form_upload_locality').validate({
			ignore : [],
			rules : {
				dservico : {
					required : true
				},
				dtipo_servico : {
					required : true
				},
				dtipo_periodo : {
					required : true
				}
			},
			messages : {
				dservico : {
					required : 'Campo obrigatório.'
				},
				dtipo_servico : {
					required : 'Campo obrigatório.'
				},
				dtipo_periodo : {
					required : 'Campo obrigatório.'
				}
			},
			
			errorPlacement: function(error, element) {
				var parent = $wnd.jQuery(element).parent();
				console.log(parent); 
				error.appendTo(parent);
			},
		});
	}-*/;
}
