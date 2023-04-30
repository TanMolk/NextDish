package ncl.csc8019.group12.service;

import ncl.csc8019.group12.delay.VerifyCode;
import ncl.csc8019.group12.utils.ConfuseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author wei tan
 */

@Service
public class EmailService {

    private final Random random = new Random();

    @Value("${spring.mail.from}")
    private String from;
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private CacheService cacheService;

    public String sendVerifyCode(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("[Newcastle Feed you stomach] Verify you email");
        String code = generateVerifyCode();
        message.setText("You verify code is \n\n" + code);

        //send mail
        mailSender.send(message);

        cacheService.addVerifyCode(new VerifyCode(email, code, 5 * 60 * 1000));


        return ConfuseUtil.md5(email);
    }

    private String generateVerifyCode() {
        int i = random.nextInt(999999);
        return ((i + 1000000) + "").substring(1, 7);
    }
}
