package gov.sgpr.fgv.osc.portalosc.uploadlocality.server;

import gov.sgpr.fgv.osc.portalosc.uploadlocality.shared.model.AgreementLocalityModel;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 7997607729952822465L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private boolean isMultipart;
	private File file;
	
	private String fileType;
	private String filePath;
	private String tempPath;
	private String fileName;
	private String contentType;
	
	private String msg = "";
	
	private ArrayList<AgreementLocalityModel> convenios = new ArrayList<AgreementLocalityModel>();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		fileType = request.getParameter("dtipo_arquivo");
		
		isMultipart = ServletFileUpload.isMultipartContent(request);
		if(!isMultipart){
			try {
				response.sendRedirect("/UploadLocality.html");
			} catch (IOException e) {
				
			}
		}
		
		filePath = getServletContext().getRealPath("/WEB-INF/locality");
		tempPath = filePath + "/temp";
		
		createDirectory(filePath, tempPath);
		
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(tempPath));
		
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try{
			List<?> fileItems = upload.parseRequest(request);
			Iterator<?> i = fileItems.iterator();
			
			while(i.hasNext()){
				FileItem fi = (FileItem) i.next();
				if (!fi.isFormField()){
					fileName = fi.getName();
					contentType = fi.getContentType();
					if(fileName.lastIndexOf("/") >= 0){
						file = new File(filePath + "/" +fileName.substring( fileName.lastIndexOf("/"))) ;
					}else{
						file = new File(filePath + "/" + fileName.substring(fileName.lastIndexOf("/") + 1)) ;
					}
					fi.write(file);
				}
			}
			response.sendRedirect("/UploadLocality.html");
		}catch(Exception ex) {
			System.out.println(ex);
		}
		
		if(fileType == "XLS"){
			
		}else if(fileType == "CSV"){
			readCSV();
		}else if(fileType == "XML"){
			
		}else if(fileType == "JSON"){
			
		}
	}
	
	private Boolean readCSV(){
		Boolean result = true;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath + "/" + fileName));
			String lineCSV = null;
			while ((lineCSV = br.readLine()) != null) {
			    String[] cols = lineCSV.split(",");
			    
			    if(cols.length < 12){
			    	msg = "Há colunas faltando.";
			    }else{
			    	AgreementLocalityModel conv = new AgreementLocalityModel();
			    	
			    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			    	
			    	try{
			    		conv.setNumeroConvenio(Integer.parseInt(cols[0].trim()));
			    	}catch(NumberFormatException e){
			    		msg = "A coluna de \"Número do Convênio\" não foi validada.";
			    	}
			    	
			    	try{
			    		conv.setDataInicio(df.parse(cols[1].trim()));
			    	}catch(ParseException e){
			    		msg = "A coluna de \"Data de início\" não foi validada.";
			    	}
				    
			    	try{
			    		conv.setDataPublicacao(df.parse(cols[2].trim()));
			    	}catch(ParseException e){
			    		msg = "A coluna de \"Data de publicação\" não foi validada.";
			    	}
			    	
			    	try{
			    		conv.setDataConclusao(df.parse(cols[3].trim()));
			    	}catch(ParseException e){
			    		msg = "A coluna de \"Data de Conclusão\" não foi validada.";
			    	}
			    			    	
				    conv.setSituacaoParceria(cols[4].trim());
				    
				    try{
			    		conv.setValorTotal(Double.parseDouble(cols[5].trim()));
			    	}catch(NumberFormatException e){
			    		msg = "A coluna de \"Valor Global\" não foi validada.";
			    	}
				    
				    try{
			    		conv.setValorPago(Double.parseDouble(cols[6].trim()));
			    	}catch(NumberFormatException e){
			    		msg = "A coluna de \"Valor Pago\" não foi validada.";
			    	}
				    
				    conv.setOrgaoConcedente(cols[7].trim());
				    
				    try{
			    		conv.setCnpjProponente(Long.parseLong(cols[8].trim()));
			    	}catch(NumberFormatException e){
			    		msg = "A coluna de \"CNPJ do Proponente\" não foi validada.";
			    	}
				    
				    conv.setRazaoSocialProponente(cols[9].trim());
				    conv.setMunicipioProponente(cols[10].trim());
				    conv.setObjetoParceria(cols[11].trim());
				    
				    if(cols.length == 13){
				    	conv.setNomeFantasiaProponente(cols[12].trim());
				    }else if(cols.length == 14){
				    	conv.setEnderecoProponente(cols[13].trim());
				    }else if(cols.length == 15){
				    	try{
				    		conv.setValorContrapartidaFinanceira(Double.parseDouble(cols[14].trim()));
				    	}catch(NumberFormatException e){
				    		msg = "A coluna de \"Valor da Contrapartida financeira\" não foi validada.";
				    	}
				    }
			    }
			    
			    if(msg != ""){
			    	result = false;
			    	break;
			    }
			}
			br.close();
		}catch (Exception e){
			msg = "Ocorreu um erro no momento da leitura do arquivo.";
			result = false;
		}
		return result;
	}
	
	private void createDirectory(String... directories) {
		try{
			for(String d : directories){
				File dir = new File(d);
		        if(!dir.exists()){
		        	dir.mkdir();
		        }
				if(!dir.exists() || !dir.isDirectory()){
					throw new IOException("O diretório " + dir + " não é um diretório válido");
				}
			}
		} catch (IOException e){
			
		}
	}
}
