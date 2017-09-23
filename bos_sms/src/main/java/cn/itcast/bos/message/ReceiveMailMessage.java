package cn.itcast.bos.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import utils.MailUtils;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component("receiveMailMessage")
public class ReceiveMailMessage implements MessageListener{
    //发送邮件
    @Autowired
    private SimpleMailMessage simpleMailMessage;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        try {
            String subject = mapMessage.getString("subject");
            String activecode = mapMessage.getString("activecode");
            String telephone = mapMessage.getString("telephone");
            String to = mapMessage.getString("to");
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText("<html>尊敬的客户您好，请于24小时内，进行邮箱账户的绑定，点击下面地址完成绑定:<br/><a href='"
                    + MailUtils.activeUrl + "?telephone=" + telephone + "&activecode=" + activecode + "'>速运快递邮箱绑定地址</a></html>");
            simpleMailMessage.setTo(to);
            javaMailSender.send(simpleMailMessage);
            System.out.println("邮件发送成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
