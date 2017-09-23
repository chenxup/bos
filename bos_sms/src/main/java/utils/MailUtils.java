package utils;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtils {
    private static String smtp_host = "smtp.163.com";        //网易
    private static String username = "15556859588@163.com";    //邮箱
    private static String password = "592828";        //授权码（登录邮箱-->设置-->邮箱安全设置-->客户端授权密码），这里不是邮箱的密码，切记！
    private static String from = "15556859588@163.com";        //来源邮箱，使用当前账号
    public static String activeUrl = Constants.BOS_FORE_HOST + "/customer_activeMail.action";        //激活地址

    public static void sendMail(String subject, String content, String to,
                                String activecode) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", smtp_host);
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.auth", "true");
        Session session = Session.getInstance(props);
        session.setDebug(true);
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(content, "text/html;charset=utf-8");
            Transport transport = session.getTransport();
            transport.connect(smtp_host, username, password);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("邮件发送失败...");
        }
    }

}
