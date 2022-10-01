package io.jpress.web.commons.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import io.jboot.utils.StrUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.json.JsonBody;
import io.jboot.web.validate.Regex;
import io.jpress.JPressOptions;
import io.jpress.commons.email.Email;
import io.jpress.commons.email.EmailKit;
import io.jpress.commons.email.SimpleEmailSender;
import io.jpress.web.base.ControllerBase;

import javax.validation.constraints.Pattern;

@RequestMapping("/commons/getemailcode")
public class SendEmailCodeController  extends ControllerBase {

    @Inject
    private CaptchaService captchaService;


    /**
     * 发送邮箱验证码
     * @param emailAddr
     * @param captchaVO
     */
    public void index(@Pattern(regexp = Regex.EMAIL) @JsonBody("email") String emailAddr, @JsonBody CaptchaVO captchaVO) {

        ResponseModel validResult = captchaService.verification(captchaVO);

        if (validResult != null && validResult.isSuccess()) {
            if (!StrUtil.isEmail(emailAddr)) {
                renderFailJson("The mailbox address you entered is wrong.");
                return;
            }

            SimpleEmailSender ses = new SimpleEmailSender();
            if (!ses.isEnable()) {
                renderFailJson("You can't send it without opening the mail function.");
                return;
            }

            if (!ses.isConfigOk()) {
                renderFailJson("Unconfigured is correct, SMTP or user name or password is empty.");
                return;
            }

            //获取关于邮箱的配置内容
            //邮件标题
            String subject = JPressOptions.get("reg_email_validate_title");
            //生成验证码
            String code = EmailKit.generateCode();


            Email email = Email.create();
            email.subject(subject);
            email.content(" Verification code:"+code+", For registration/login, valid in 10 points.");
            email.to(emailAddr);

            //发送邮箱验证码
            boolean sendOk = EmailKit.sendEmailCode(emailAddr, code, email);
            if (sendOk) {
                renderJson(Ret.ok().set("message", "The mailbox verification code is successfully sent, please check the mobile phone"));
            } else {
                renderJson(Ret.fail().set("message", "The email verification code fails, please contact the administrator"));
            }
        } else {
            renderFailJson("Verification error");
        }
    }
}
