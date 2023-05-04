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
 * This code defines an EmailService service to send emails and generate CAPTCHAs for authentication.
 * @author Wei tan & Pulei chen
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

    /**
     * This method sends authentication mail to the user and stores it in the cache.
     * Must contain email.
     *
     * @param email   User's email address
     * @return        MD5 hash of email address
     */
    public String sendVerifyCode(String email) {
        //Generate the contents of a mail
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("[Newcastle Feed you stomach] Verify you email");
        String code = generateVerifyCode();
        message.setText("You verify code is \n\n" + code);

        //send the mail
        mailSender.send(message);

        /*
        * The method also stores the generated captcha in a cache along with the email address.
        * and returns an MD5 hash of that email address.
        * The purpose of storing the captcha in the cache is to use it in subsequent validations.
        */
        cacheService.addVerifyCode(new VerifyCode(email, code, 5 * 60 * 1000));


        return ConfuseUtil.md5(email);
    }

    /**
     * This method is to generate a random CAPTCHA.
     * @return    6-digit CAPTCHA(string)
     */
    private String generateVerifyCode() {
        /*
        * It generates a 6-digit random number and converts it to string form.
        * with any less than six digits prefixed by a 0 to ensure that the CAPTCHA is always six digits long.
        */
        int i = random.nextInt(999999);
        return ((i + 1000000) + "").substring(1, 7);
    }
}
