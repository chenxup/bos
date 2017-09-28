package cn.itcast.bos.message;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import org.springframework.stereotype.Component;
import utils.SmsDemoUtils;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class ReceiveSMSMessageForCourier implements MessageListener {
    @Override
    public void onMessage(Message message) {
        MapMessage mapMessage = (MapMessage) message;
        try {
            String seccode = mapMessage.getString("seccode");
            String telephone = mapMessage.getString("telephone");
            String sendAddress = mapMessage.getString("sendAddress");
            String sendName = mapMessage.getString("sendName");
            String sendMobile = mapMessage.getString("sendMobile");
            String sendmodbileMsg = mapMessage.getString("sendmodbileMsg");

            SendSmsResponse sendSmsResponse = SmsDemoUtils.sendSms(telephone, seccode);

            String code = sendSmsResponse.getCode();
            System.out.println("短信响应码");
            System.out.println(code);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
