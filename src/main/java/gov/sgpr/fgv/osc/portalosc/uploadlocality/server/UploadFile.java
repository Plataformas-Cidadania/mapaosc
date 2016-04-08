package gov.sgpr.fgv.osc.portalosc.uploadlocality.server;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Logger;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/uploadFile")
public class UploadFile extends HttpServlet implements Servlet {
	private static final long serialVersionUID = 2934225750172052696L;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = getServletContext();
		String directory = context.getRealPath("/WEB-INF/locality");
		String filename = "";
		
		createDirectory(directory);
		
		ServletInputStream in = request.getInputStream();  
		byte[] line = new byte[128];
		int i = in.readLine(line, 0, 128);
		int boundaryLength = i - 2;
		String boundary = new String(line, 0, boundaryLength);
		while (i != -1) {
			String newLine = new String(line, 0, i);
			if (newLine.startsWith("Content-Disposition: form-data; name=\"")) {
				String s = new String(line, 0, i - 2);
				int pos = s.indexOf("filename=\"");
				if (pos != -1) {
					String filepath = s.substring(pos+10, s.length() - 1);
					pos = filepath.lastIndexOf("/");
					if (pos != -1){
						filename = filepath.substring(pos + 1);
					}else{
						filename = filepath;
					}
				}
				i = in.readLine(line, 0, 128);
				ByteArrayOutputStream buffer = new  ByteArrayOutputStream();
				newLine = new String(line, 0, i);
				while (i != -1 && !newLine.startsWith(boundary)) {
					buffer.write(line, 0, i);
					i = in.readLine(line, 0, 128);
					newLine = new String(line, 0, i);
				}
				
				try {
					RandomAccessFile f = new RandomAccessFile(directory + "/" + filename, "rw");
					f.write(buffer.toByteArray());
					f.close();
				}catch (FileNotFoundException e) {}
			}
			i = in.readLine(line, 0, 128);
        }
		
//		BufferedReader br = new BufferedReader(new FileReader(directory + "/" + filename));
//		String lineCSV = null;
//		while ((lineCSV = br.readLine()) != null) {
//		    String[] cols = lineCSV.split(",");
//		    System.out.println("Coulmn 4= " + cols[4] + " , Column 5=" + cols[5]);
//		}
		
		response.sendRedirect("/UploadLocality.html");
		
		destroy();
	}
	
	public void destroy() {
		super.destroy();
	}
	
	private Boolean validateFile(){
		Boolean result = false;
		
		return true;
	}
	
	private Boolean readFile(){
		Boolean result = false;
		
		return true;
	}
	
	private void createDirectory(String... directories) throws IOException{
		for(String d : directories){
			File dir = new File(d);
	        if(!dir.exists()){
	        	dir.mkdir();
	        }
			if(!dir.exists() || !dir.isDirectory()){
				throw new IOException("O diretório " + dir + " não é um diretório válido");
			}
		}		
	}
}
