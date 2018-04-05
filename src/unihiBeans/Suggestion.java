package unihiBeans;

import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@ManagedBean
@RequestScoped
public class Suggestion {

	protected String data;
	protected String from;
	protected String email;
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String send() {
		final String username = "stemcellnet.sysbiolab@gmail.com";
		final String password = "@2Replekia!";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		if(this.data!=null && this.data.length()>0)
		{
			try {
 
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("stemcellnet.sysbiolab@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("stemcellnet.sysbiolab@gmail.com,josepedr@gmail.com"));
				message.setSubject("Suggestion for stemcellnet");
				message.setText("From:"+this.from+"\nEmail:"+this.email+"\n"+this.data);
 
				Transport.send(message);
 
//				System.out.println("Done");
 
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		}
		
		return "stemCellNetThankYou";
	}
	
}
