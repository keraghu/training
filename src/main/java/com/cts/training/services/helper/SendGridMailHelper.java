package com.cts.training.services.helper;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

public class SendGridMailHelper {
	
	public void sendMail(String mailTo,String subject,String message){
		
		 SendGrid sendgrid = new SendGrid("Sudarsanan", "sendgrid5");
		 SendGrid.Email email = new SendGrid.Email();
		 System.out.println("mail to is "+mailTo);
		 email.addTo(mailTo);
		    email.setFrom("CTSPaaSOpenShiftTeam");
		    email.setSubject(subject);
		    email.setText(message);
		    /*HttpHost proxy = new HttpHost("proxy.cognizant.com", 6050);
		    Credentials credentials = new UsernamePasswordCredentials("382015","");
		    AuthScope authScope = new AuthScope("proxy.cognizant.com", 6050);
		    CredentialsProvider credsProvider = new BasicCredentialsProvider();
		    credsProvider.setCredentials(authScope, credentials);
		    CloseableHttpClient http = HttpClientBuilder.create().setProxy(proxy).setDefaultCredentialsProvider(credsProvider).setUserAgent("sendgrid/" + sendgrid.getVersion() + ";java").build();
		    sendgrid.setClient(http);*/
		    try {
		        SendGrid.Response response = sendgrid.send(email);
		        System.out.println(response.getMessage());
		      }
		      catch (SendGridException e) {
		        System.err.println(e);
		      }
		
	}
	
	/*public static void  main(String[] arg){
		SendGridMailHelper h=new SendGridMailHelper();
		h.sendMail("Nagarajan.Mohan@cognizant.com", "Hello World", "My first email with SendGrid Java!");
	}*/

}
