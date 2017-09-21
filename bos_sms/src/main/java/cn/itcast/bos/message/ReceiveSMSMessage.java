package cn.itcast.bos.message;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import utils.SmsDemoUtils;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ReceiveSMSMessage implements MessageListener {
    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        try {
            String telephone = mapMessage.getString("telephone");
            String activecode = mapMessage.getString("activecode");
            SendSmsResponse sendSmsResponse = SmsDemoUtils.sendSms(telephone, activecode);
            String code = sendSmsResponse.getCode();
            System.out.println("短信响应码");
            System.out.println(code);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
