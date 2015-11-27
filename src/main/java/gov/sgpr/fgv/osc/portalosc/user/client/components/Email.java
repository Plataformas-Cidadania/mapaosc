package gov.sgpr.fgv.osc.portalosc.user.client.components;

import gov.sgpr.fgv.osc.portalosc.user.shared.model.RepresentantUser;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class Email {
	
	private String serverSMTP = null;
	private String authSMTP = null;
	private String portSMTP = null;
	private String fromAddress = null;
	private String nameAddress = null;
	
	public Email(String serverSMTP, String authSMTP, String portSMTP, String fromAddress, String nameAddress) {
		this.serverSMTP = serverSMTP;
		this.authSMTP = authSMTP;
		this.portSMTP = portSMTP;
		this.fromAddress = fromAddress;
		this.nameAddress = nameAddress;
	}
	
	public void send(String to, String subject, String content){
		
		Properties props = new Properties();
		
	  	props.put("mail.smtp.host", serverSMTP);
	    props.put("mail.smtp.auth", authSMTP);
	    props.put("mail.smtp.port", portSMTP);
		
	    Session session = Session.getDefaultInstance(props);
	    
	    /** Ativa Debug para sessão */
	    session.setDebug(true);
		  
	    try {
		  
		  MimeMessage message = new MimeMessage(session);

		  Address from = new InternetAddress(fromAddress, nameAddress);
		  Address para = new InternetAddress(to);
		  
		  message.setFrom(from);
		  message.addRecipient(RecipientType.TO, para);

		  message.setSubject(subject);
		  message.setSentDate(new java.util.Date());
		 
		  message.setContent(content, "text/html; charset=utf-8");
		 
		  Transport.send(message);
		  
		 } catch (MessagingException e) {
			 throw new RuntimeException(e);
		 } catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
		}
	}
	
	public String confirmation(String name, String token) {
	      return 
	      	 "<!DOCTYPE html>" +
	           "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"pt-br\" lang=\"pt-br\">"+
	     		 "<head>"+
	     		 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"+
	     		 "<title>E-mail Confirmacao Cadastro</title>"+
	     		 "<link href='http://fonts.googleapis.com/css?family=Roboto:400,700,400italic,700italic' rel='stylesheet' type='text/css'>"+
	     		 "</head>"+
	     		 "<body bgcolor=\"#FFFFFF\" style=\"margin: 0 auto; font-size: 16px;\">"+
	     		 "<table id=\"Table_01\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"border: 2px solid #f4f4f4; min-width:300px; width:100%; max-width:700px; margin:20px auto;\">"+
	     		 "<tbody>"+
	     		 "<tr>"+
	     		 "<td colspan=\"3\" style=\"padding:20px;\">"+
	     		 "<img src=\"https://mapaosc.ipea.gov.br/imagens/logo.png\" height=\"97\" alt=\"\"/>"+
	     		 "</td>"+
	     		 "</tr>"+
	     		 "<tr>"+
	     		 "<td height=\"27\" colspan=\"3\" bgcolor=\"#F4F4F4\" style=\"padding:10px 20px;\">"+
	     		 "<h1 style=\"padding: 0.5em;margin: 0;\"><font size=\"6\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Confirmação de Cadastro</font></h1>"+
				 "</td>"+
	     		 "</tr>"+
	     		 "<tr>"+
	     		 "<td  colspan=\"3\" bgcolor=\"#FFFFFF\" style=\"padding:20px;\">"+
	     		 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Olá, " + name + "!</font> </p>"+
	     		 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Estamos prontos para ativar sua conta. Clique no link abaixo para ativar seu cadastro.</font> </p>"+
	     		"<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\"><a href=\"https://mapaosc.ipea.gov.br/Map.html#T"+ token +"\">https://mapaosc.ipea.gov.br/Map.html#T"+ token +"</a> </font> </p>"+
	     		 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Se você não tiver criado uma conta do Mapa das OSC, desconsidere este email.</font> </p>"+
	     		 "</td>"+
	     		 "</tr>"+
	     		 "<tr>"+
	     		 "<td width=\"auto\"></td>"+
	     		 "<td valign=\"middle\" align=\"right\" style=\"padding:20px;\">"+
	     		 "<img src=\"https://mapaosc.ipea.gov.br/imagens/loading.png\" height=\"71\" width=\"71\" alt=\"\"/>"+
	     		 "</td>"+
	     		 "<td width=\"420\" bgcolor=\"#FFFFFF\" valign=\"middle\" style=\"padding: 20px 0;\">"+
	     		 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Agradecemos pelo contato,</font> </p>"+
	     		 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Equipe de Desenvolvimento do Mapa das OSCs</font> </p>"+
	     		 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\"><a href=\"https://mapaosc.ipea.gov.br\">Mapa das OSCs</a> - "+ DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) +"</font> </p>"+
	     		 "</td>"+
	     		 "</tr>"+
	     		 "</tbody>"+
	     		 "</table>"+
	     		 "</body>"+
	     		 "</html>";
	}
		
	
	public String welcome(String name) {
	    return 
	    		
	     "<!DOCTYPE html>" +
	     "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"pt-br\" lang=\"pt-br\">"+
			 "<head>"+
			 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"+
			 "<title>E-mail Boas Vindas</title>"+
			 "<link href='http://fonts.googleapis.com/css?family=Roboto:400,700,400italic,700italic' rel='stylesheet' type='text/css'>"+
			 "</head>"+
			 "<body bgcolor=\"#FFFFFF\" style=\"margin: 0 auto; font-size: 16px;\">"+
			 "<table id=\"Table_01\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"border: 2px solid #f4f4f4; min-width:300px; width:100%; max-width:700px; margin:20px auto;\">"+
			 "<tbody>"+
			 "<tr>"+
			 "<td colspan=\"3\" style=\"padding:20px;\">"+
			 "<img src=\"https://mapaosc.ipea.gov.br/imagens/logo.png\" height=\"97\" alt=\"\"/>"+
			 "</td>"+
			 "</tr>"+
			 "<tr>"+
			 "<td height=\"27\" colspan=\"3\" bgcolor=\"#F4F4F4\" style=\"padding:10px 20px;\">"+
			 "<h1 style=\"padding: 0.5em;margin: 0;\"><font size=\"6\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Obrigado por se cadastrar!</font></h1>"+
			 "</td>"+
			 "</tr>"+
			 "<tr>"+
			 "<td  colspan=\"3\" bgcolor=\"#FFFFFF\" style=\"padding:20px;\">"+
			 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Olá, " + name + "!</font> </p>"+
			 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Obrigado por se cadastrar no MAPA das Organizações da Sociedade Civil.</font> </p>"+
			 "</td>"+
			 "</tr>"+
			 "<tr>"+
			 "<td width=\"auto\"></td>"+
			 "<td valign=\"middle\" align=\"right\" style=\"padding:20px;\">"+
			 "<img src=\"https://mapaosc.ipea.gov.br/imagens/loading.png\" height=\"71\" width=\"71\" alt=\"\"/>"+
			 "</td>"+
			 "<td width=\"420\" bgcolor=\"#FFFFFF\" valign=\"middle\" style=\"padding: 20px 0;\">"+
			 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Agradecemos pelo contato,</font> </p>"+
			 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Equipe de Desenvolvimento do Mapa das OSCs</font> </p>"+
			 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\"><a href=\"https://mapaosc.ipea.gov.br\">Mapa das OSCs</a> - "+ DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) +"</font> </p>"+
			 "</td>"+
			 "</tr>"+
			 "</tbody>"+
			 "</table>"+
			 "</body>"+
			 "</html>";
	}
	
	public String changePassword(String name, String token) {
	      return 
	      	 "<!DOCTYPE html>" +
	           "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"pt-br\" lang=\"pt-br\">"+
	     		 "<head>"+
	     		 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"+
	     		 "<title>Criar Nova Senha</title>"+
	     		 "<link href='http://fonts.googleapis.com/css?family=Roboto:400,700,400italic,700italic' rel='stylesheet' type='text/css'>"+
	     		 "</head>"+
	     		 "<body bgcolor=\"#FFFFFF\" style=\"margin: 0 auto; font-size: 16px;\">"+
	     		 "<table id=\"Table_01\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"border: 2px solid #f4f4f4; min-width:300px; width:100%; max-width:700px; margin:20px auto;\">"+
	     		 "<tbody>"+
	     		 "<tr>"+
	     		 "<td colspan=\"3\" style=\"padding:20px;\">"+
	     		 "<img src=\"https://mapaosc.ipea.gov.br/imagens/logo.png\" height=\"97\" alt=\"\"/>"+
	     		 "</td>"+
	     		 "</tr>"+
	     		 "<tr>"+
	     		 "<td height=\"27\" colspan=\"3\" bgcolor=\"#F4F4F4\" style=\"padding:10px 20px;\">"+
	     		 "<h1 style=\"padding: 0.5em;margin: 0;\"><font size=\"6\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Criar Nova Senha</font></h1>"+
				 "</td>"+
	     		 "</tr>"+
	     		 "<tr>"+
	     		 "<td  colspan=\"3\" bgcolor=\"#FFFFFF\" style=\"padding:20px;\">"+
	     		 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Olá, " + name + "!</font> </p>"+
	     		 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Para cadastrar sua nova senha, clique no link* abaixo:</font> </p>"+
	     		 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\"><a href=\"https://mapaosc.ipea.gov.br/Map.html#C" + token + "\">https://mapaosc.ipea.gov.br/Map.html#C"+ token +"</a></font> </p>"+ 
	     		 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Mas se não tiver pedido para alterar sua senha, desconsidere este email e continue utilizando sua senha atual.</font> </p>"+
	     		 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"2\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">*Este link expira em 24 horas.</font> </p>"+
	     		 "</td>"+
	     		 "</tr>"+
	     		 "<tr>"+
	     		 "<td width=\"auto\"></td>"+
	     		 "<td valign=\"middle\" align=\"right\" style=\"padding:20px;\">"+
	     		 "<img src=\"https://mapaosc.ipea.gov.br/imagens/loading.png\" height=\"71\" width=\"71\" alt=\"\"/>"+
	     		 "</td>"+
	     		 "<td width=\"420\" bgcolor=\"#FFFFFF\" valign=\"middle\" style=\"padding: 20px 0;\">"+
	     		 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Agradecemos pelo contato,</font> </p>"+
	     		 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Equipe de Desenvolvimento do Mapa das OSCs</font> </p>"+
	     		 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\"><a href=\"https://mapaosc.ipea.gov.br\">Mapa das OSCs</a> - "+ DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) +"</font> </p>"+
	     		 "</td>"+
	     		 "</tr>"+
	     		 "</tbody>"+
	     		 "</table>"+
	     		 "</body>"+
	     		 "</html>";
	}
	
	public String informationOSC(RepresentantUser user, String nameOSC) {
	    return 
	    		
	     "<!DOCTYPE html>" +
	     "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"pt-br\" lang=\"pt-br\">"+
			 "<head>"+
			 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"+
			 "<title>E-mail Informativo a OSC</title>"+
			 "<link href='http://fonts.googleapis.com/css?family=Roboto:400,700,400italic,700italic' rel='stylesheet' type='text/css'>"+
			 "</head>"+
			 "<body bgcolor=\"#FFFFFF\" style=\"margin: 0 auto; font-size: 16px;\">"+
			 "<table id=\"Table_01\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"border: 2px solid #f4f4f4; min-width:300px; width:100%; max-width:700px; margin:20px auto;\">"+
			 "<tbody>"+
			 "<tr>"+
			 "<td colspan=\"3\" style=\"padding:20px;\">"+
			 "<img src=\"https://mapaosc.ipea.gov.br/imagens/logo.png\" height=\"97\" alt=\"\"/>"+
			 "</td>"+
			 "</tr>"+
			 "<tr>"+
			 "<td height=\"27\" colspan=\"3\" bgcolor=\"#F4F4F4\" style=\"padding:10px 20px;\">"+
			 "<h1 style=\"padding: 0.5em;margin: 0;\"><font size=\"6\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">E-mail Informativo a OSC</font></h1>"+
			 "</td>"+
			 "</tr>"+
			 "<tr>"+
			 "<td  colspan=\"3\" bgcolor=\"#FFFFFF\" style=\"padding:20px;\">"+
			 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Prezados, recebemos um cadastro de representante da <b>" + nameOSC + "</b> no Mapa das Organizações da Sociedade Civil.</font> </p>"+
			 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Abaixo segue os dados do cadastro e caso a organização não esteja de acordo com esse cadastro, por favor entre em contato com os responsáveis do site pelo email <b>mapaosc@ipea.gov.br</b>.</font> </p>"+
			 "<br/>" +
			 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\"><strong>Dados do Cadastro:</strong></font></p>" +
			 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Nome: " + user.getName() + "</font></p>" +
			 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">CPF: " + String.valueOf(user.getCpf()) + "</font></p>" +
			 "<p style=\"text-indent: 2.5em;text-align: justify;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">E-mail: " + user.getEmail() + "</font></p>"+
			 "</td>"+
			 "</tr>"+
			 "<tr>"+
			 "<td width=\"auto\"></td>"+
			 "<td valign=\"middle\" align=\"right\" style=\"padding:20px;\">"+
			 "<img src=\"https://mapaosc.ipea.gov.br/imagens/loading.png\" height=\"71\" width=\"71\" alt=\"\"/>"+
			 "</td>"+
			 "<td width=\"420\" bgcolor=\"#FFFFFF\" valign=\"middle\" style=\"padding: 20px 0;\">"+
			 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Agradecemos pelo contato,</font> </p>"+
			 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\">Equipe de Desenvolvimento do Mapa das OSCs</font> </p>"+
			 "<p style=\"text-align: justify; margin: 0;\"> <font size=\"4\" face=\"Roboto, arial narrow, helvetica condensed, helvetica, arial, sans-serif\"><a href=\"https://mapaosc.ipea.gov.br\">Mapa das OSCs</a> - "+ DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) +"</font> </p>"+
			 "</td>"+
			 "</tr>"+
			 "</tbody>"+
			 "</table>"+
			 "</body>"+
			 "</html>";
	}
}