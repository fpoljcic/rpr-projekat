package ba.unsa.etf.rpr.projekat;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.Properties;

public class Email {
    private String userName;
    private String password;
    private ArrayList<String> recipients;
    private String subject;
    private String body;

    public Email() {
        recipients = new ArrayList<>();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRecipients(ArrayList<String> recipients) {
        this.recipients = recipients;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addRecipient(String recipient) {
        recipients.add(recipient);
    }

    public Email(String userName, String password, ArrayList<String> recipients, String subject, String body) {
        this.userName = userName;
        this.password = password;
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
    }

    private void checkEmail() throws WrongEmailException {
        if (userName == null || password == null || recipients.isEmpty() || subject == null || body == null)
            throw new WrongEmailException("Pogrešni parametri za email");
    }

    public void send() throws WrongEmailException, MessagingException {
        checkEmail();
        send(userName, password, recipients, subject, body);
    }

    public void send(String from, String pass, ArrayList<String> to, String subject, String body) throws WrongEmailException, MessagingException {
        checkEmail();
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(from));
        InternetAddress[] toAddress = new InternetAddress[to.size()];

        for( int i = 0; i < to.size(); i++ )
            toAddress[i] = new InternetAddress(to.get(i));

        for (InternetAddress address : toAddress)
            message.addRecipient(Message.RecipientType.TO, address);

        message.setSubject(subject);
        message.setText(body);
        Transport transport = session.getTransport("smtp");
        transport.connect(host, from, pass);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}

