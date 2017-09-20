package cn.itcast.bos.test;

import org.junit.Test;
import utils.MailUtils;

public class TestMail {

    @Test
    public void testSendMail(){
        //设置主题
        String subject = "测试邮件";

        //激活码
        String activecode = "anvcd";

        //发送者
        String to = "1134515199@qq.com";
        MailUtils.sendMail(subject, "", to, activecode);

    }

}
