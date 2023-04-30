package ncl.csc8019.group12.controller;

import ncl.csc8019.group12.dao.UserRepository;
import ncl.csc8019.group12.dao.eneity.User;
import ncl.csc8019.group12.enums.UserStateEnum;
import ncl.csc8019.group12.service.CacheService;
import ncl.csc8019.group12.service.EmailService;
import ncl.csc8019.group12.utils.ConfuseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import javax.transaction.Transactional;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserRepository userRepository;

    @Resource
    private CacheService cacheService;

    @Resource
    private EmailService emailService;


    @PostMapping("/login")
    public User login(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestBody User user) throws LoginException {

        log.info("[User-Login] {}; clientId:{}", user.getEmail(), clientId);

        String email = user.getEmail();
        String password = user.getPassword();

        if ((email != null && !email.isEmpty())
                && (password != null && !password.isEmpty())
        ) {
            User loginUser = userRepository.findByEmailAndPassword(email, password);
            if (UserStateEnum.ACTIVE == loginUser.getState()) {

                User returnUser = new User();
                returnUser.setToken(loginUser.getToken());
                returnUser.setNickname(loginUser.getNickname());
                returnUser.setReviews(loginUser.getReviews());
                returnUser.setFavorites(loginUser.getFavorites());
                return returnUser;
            }

        }
        throw new LoginException();
    }

    @GetMapping("/send-verify-code")
    public String sendVerifyCode(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestParam String email
    ) {
        log.info("[User-SendVerifyCode] {}; clientId:{}", email, clientId);

        if (cacheService.getVerifyCode(email) == null) {
            return emailService.sendVerifyCode(email);

        } else {
            return ConfuseUtil.md5(email);
        }
    }

    @GetMapping("/verify-code")
    public Boolean verifyCode(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestParam String email,
            @RequestParam String code
    ) {
        log.info("[User-VerifyCode] {}; clientId:{}", email, clientId);
        return code.equals(cacheService.getVerifyCode(email));
    }

    @GetMapping("/reset-password")
    public Boolean resetPassword(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestParam String email,
            @RequestParam String code,
            @RequestParam String password
    ) {
        log.info("[User-ResetPassword] {}; clientId:{}", email, clientId);

        if (code.equals(cacheService.getVerifyCode(email))) {
            return userRepository.updatePasswordWithEmail(email, password) == 1;
        } else {
            return false;
        }
    }

    @PostMapping("/sign-in")
    @Transactional(rollbackOn = Exception.class)
    public User signIn(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestParam String code,
            @RequestBody User user
    ) throws Exception {
        log.info("[User-SignIn] {}; clientId:{}", user.getEmail(), clientId);

        if (userRepository.findByEmail(user.getEmail()) == null) {
            if (code.equals(cacheService.getVerifyCode(user.getEmail()))) {
                user.setState(UserStateEnum.ACTIVE);
                user = userRepository.save(user);
                user.setToken(ConfuseUtil.encryptByRSA(user.getUid()));
                userRepository.save(user);

                User returnUser = new User();
                returnUser.setToken(user.getToken());
                returnUser.setNickname(user.getNickname());
                return returnUser;
            }
        }
        return null;
    }

    @PostMapping("/change-name")
    public Boolean changeName(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestHeader("c8019-token") String token,
            @RequestParam String name
    ) throws Exception {
        Long uid = ConfuseUtil.decryptToLongByRSA(token);

        log.info("[User-ChangeName] {} -> {}; clientId:{}", uid, name, clientId);

        return userRepository.updateNickNameWithUid(uid, name) == 1;
    }
}
