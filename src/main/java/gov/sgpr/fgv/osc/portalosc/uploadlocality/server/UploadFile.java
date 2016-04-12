package gov.sgpr.fgv.osc.portalosc.uploadlocality.server;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

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
			
			while (i.hasNext ()){
				FileItem fi = (FileItem)i.next();
				if (!fi.isFormField ()){
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
	
	private void readCSV(){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filePath + "/" + fileName));
			String lineCSV = null;
			while ((lineCSV = br.readLine()) != null) {
			    String[] cols = lineCSV.split(",");
//			    System.out.println("Coulmn 4= " + cols[4] + " , Column 5=" + cols[5]);
			}
		} catch (FileNotFoundException e) {
			
		} catch (IOException e){
			
		}
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
