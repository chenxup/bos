package cn.itcast.bos.web.action;

import cn.itcast.crm.domain.Customer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import utils.Constants;
import utils.GetRandomcode;
import utils.MailUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Action(value = "cutomer_sendSms", results = {@Result(type = "json")})
    public String sendSms() throws Exception {
//        获得手机号
        String telephone = this.model.getTelephone();
        //生成验证码（在1000-9999）
        String code = GetRandomcode.getCode(1000, 9999);
        //将验证码放入session中
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute(telephone, code);
        //设置超时时间（60秒）
        session.setMaxInactiveInterval(60);

        //发送短信
        //SendSmsResponse rs = SmsDemoUtils.sendSms(telephone, code);
        String rs = "ok";
        if ("ok".equals(rs)) {
            //成功
            System.out.println("发送成功手机号是" + telephone + "验证码为：" + code);
            //将信息返回
            Map map = new HashMap<>();
            map.put("success", code);
            this.push(map);
            return SUCCESS;
        } else {
            throw new RuntimeException("发送验证码失败");
        }

    }


    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    @Action(value = "cutomer_register", results = {@Result(name = "success", type = "redirect", location = "signup-success.html")
            , @Result(name = "input", type = "redirect", location = "signup.html")})
    public String register() throws Exception {
        //验证验证码
        String code = (String) ServletActionContext.getRequest().getSession().getAttribute(this.model.getTelephone());
        if (code == null || !code.equals(checkcode)) {
            System.out.println("验证码错误或失效");
            return INPUT;
        }

        //将密码进行MD5加密
        this.model.setPassword(DigestUtils.md5DigestAsHex(this.model.getPassword().getBytes()));

        //保存用户
        WebClient.create(Constants.CRM_MANAGEMENT_HOST + "/services/customerService").path("/customer")
                .type(MediaType.APPLICATION_JSON).post(this.model);

        //生成32位的激活码（32位随机数字）
        final String activeCode = RandomStringUtils.randomNumeric(32);
        //将激活码保存到redis中(设置24小时失效)
        redisTemplate.opsForValue().set(this.model.getTelephone(), activeCode, 24, TimeUnit.HOURS);

        //发送邮件
        new Thread(new Runnable() {
            @Override
            public void run() {
                MailUtils.sendMail("速运快递激活邮件", "尊敬的客户您好，请于24小时内，进行邮箱账户的绑定，点击下面地址完成绑定:<br/><a href='"
                        + MailUtils.activeUrl + "?telephone=" + model.getTelephone() + "&activecode=" + activeCode + "'>速运快递邮箱绑定地址</a>", model.getEmail(), "");
            }
        }).start();

        System.out.println("注册成功");
        return SUCCESS;
    }

    private String activecode;

    public void setActivecode(String activecode) {
        this.activecode = activecode;
    }

    @Action(value="customer_activeMail")
    public String actibeMail() throws Exception {
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        String code = (String) redisTemplate.opsForValue().get(this.model.getTelephone());

        //验证
        if (code == null || !code.equals(activecode)) {
            response.getWriter().write("激活码无效，请登录系统，重新绑定邮箱！");
            return NONE;
        }

        //查看激活码是否已经绑定(根据手机号查询)
        Customer customer = WebClient.create(Constants.CRM_MANAGEMENT_HOST + "/services/customer").path("/services/customerService/rstelephone" + this.model.getTelephone())
                .accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get(Customer.class);

        if (customer != null && customer.getType() != 1) {
            //将邮箱进行绑定
            WebClient.create(Constants.CRM_MANAGEMENT_HOST + "/services/customer").path("/services/customerService/updateMailType" + this.model.getTelephone())
                    .type(MediaType.APPLICATION_JSON).put(null);
            response.getWriter().write("邮箱绑定成功");
        }else {
            response.getWriter().write("邮箱已经绑定！，请勿重复操作");
        }

        return NONE;
    }
}
