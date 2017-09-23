package cn.itcast.bos.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.jms.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-jms.xml")
public class TestJmsSend {

    @Autowired
    private JmsTemplate jmsTemplate;
    //短信
    @Resource(name="queueDestination-sendSms")
    private Destination destinationSms;
    //邮件
    @Resource(name="queueDestination-sendMail")
    private Destination destinationMail;

    @Test
    public void testjms() {
        jmsTemplate.send(destinationSms, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", "15618535961");
                mapMessage.setString("activecode", "9999");
                return mapMessage;
            }
        });
    }

    @Test
    public void testmail(){
        jmsTemplate.send(destinationMail, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("subject", "速运快递激活邮件");
                mapMessage.setString("activecode", "124");
                mapMessage.setString("telephone", "234");
                mapMessage.setString("to", "1134515199@qq.com");
                return mapMessage;
            }
        });
    }


}
