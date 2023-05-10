package ncl.csc8019.group12.controller;

import ncl.csc8019.group12.dao.UserRepository;
import ncl.csc8019.group12.dao.eneity.User;
import ncl.csc8019.group12.enums.UserStateEnum;
import ncl.csc8019.group12.service.CacheService;
import ncl.csc8019.group12.service.EmailService;
import ncl.csc8019.group12.utils.ConfuseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import java.util.Optional;

/**
 * User Controller is to handle account registration, login and updates.
 * This controller contains methods to register a user(register()).
 *                                  to login a user(login()).
 *                                  to verify a user(sendVerifyCode()).
 *                                  to authenticate a verification code (verifyCode()).
 *                                  to reset password (resetPassword()).
 *                                  to register an account (signIn()).
 *                                  to change a nickname (changeName()).
 *                                  to check if a user email exists (exist()).
 *                                  to retrieve data with token (getDataByToken()).
 *
 * @author  Wei tan & Rachel wu
 */
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




    /**
     * Login - Authenticating a user based on clientId and user credentials
     *
     * @param clientId
     * @param user
     * @return
     * @throws LoginException
     */

    /*
    * Log the user's email and clientId.
    * Ensure Email and password input are not null or empty.
    * Find the user with the given email and password.
    * If the state of the user is active (verified),
    * create a user object with the same token, nickname, review and favourites.
    * Return the user.
     */

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

    /**
     * Send verification code
     *
     * @param clientId
     * @param email
     * @return
     */

    /*
    * Log the user's email and clientId.
    * If cache does not find verification code that has been sent to the email,
    * send verification code to the email and store in cache.
    * Return the verification code.
    * Else, return the md5 of the email.
     */

    @GetMapping("/send-verify-code")
    public String sendVerifyCode(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestParam String email
    ) {
        log.info("[User-SendVerifyCode] {}; clientId:{}", email, clientId);

        if (cacheService.getVerifyCode(email, false) == null) {
            return emailService.sendVerifyCode(email);

        } else {
            return ConfuseUtil.md5(email);
        }
    }

    /**
     * Verification of code
     *
     * @param clientId
     * @param email
     * @param code
     * @return
     */

    /*
    * Log the email and clientId.
    * Return true if the verification code input matches with the code in cache.
     */

    @GetMapping("/verify-code")
    public Boolean verifyCode(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestParam String email,
            @RequestParam String code
    ) {
        log.info("[User-VerifyCode] {}; clientId:{}", email, clientId);
        return code.equals(cacheService.getVerifyCode(email, true));
    }

    /**
     * Password Reset
     *
     * @param clientId
     * @param email
     * @param code
     * @param password
     * @return
     */

    /*
    * Log the email and clientId.
    * If the verification code input matches with the code in cache,
    * update the password and return true.
    * Else, return false.
     */

    @GetMapping("/reset-password")
    public Boolean resetPassword(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestParam String email,
            @RequestParam String code,
            @RequestParam String password
    ) {
        log.info("[User-ResetPassword] {}; clientId:{}", email, clientId);

        if (code.equals(cacheService.getVerifyCode(email, true))) {
            return userRepository.updatePasswordWithEmail(email, password) == 1;
        } else {
            return false;
        }
    }

    /**
     * Sign up for an account
     *
     * @param clientId
     * @param code
     * @param user
     * @return
     * @throws Exception
     */

    /*
    * Log the email and clientId.
    * If the email has not been registered before and the code input matches the code in cache,
    * set the user state as active,
    * save the user in userRepository,
    * set a token,
    * save the user again to update the token value.
    * Create and return a new user object with the same token and nickname.
     */

    @PostMapping("/sign-in")
    @Transactional(rollbackFor = Exception.class)
    public User signIn(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestParam String code,
            @RequestBody User user
    ) throws Exception {
        log.info("[User-SignIn] {}; clientId:{}", user.getEmail(), clientId);

        if (userRepository.findByEmail(user.getEmail()) == null) {
            if (code.equals(cacheService.getVerifyCode(user.getEmail(), true))) {
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

    /**
     * Nickname change
     * @param clientId
     * @param token
     * @param name
     * @return
     * @throws Exception
     */

    /*
    * Identify the uid with the token input.
    * Log the uid, name and clientId.
    * Return 1 if the nickname has been changed successfully.
    *
     */

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

    /**
     * Check if the user exists with the email input
     *
     * @param clientId
     * @param email
     * @return
     */

    /*
    * Log the email and clientId.
    * Return true if the user exists.
     */

    @GetMapping("/exist")
    public Boolean exist(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestParam String email
    ) {
        log.info("[User-exist] {}; clientId:{}", email, clientId);

        return userRepository.existsByEmail(email);
    }

    /**
     * Retrieve User's data with the token while preventing sensitive data to be exposed.
     * @param clientId
     * @param token
     * @return
     * @throws Exception
     */

    /*
    * Identify the uid with the token input.
    * Log the uid, name and clientId.
    * If a user is found with the uid,
    * set all sensitive data fields to null,
    * return the user object.
    * Else, return Login Exception.
     */
    @GetMapping
    public User getDataByToken(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestHeader("c8019-token") String token
    ) throws Exception {

        Long uid = ConfuseUtil.decryptToLongByRSA(token);
        log.info("[User-getDataByToken] {}; clientId:{}", uid, clientId);

        Optional<User> userOptional = userRepository.findById(uid);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setToken(null);
            user.setUid(null);
            user.setEmail(null);
            user.setPassword(null);
            user.setState(null);
            user.getFavorites().forEach(f -> f.setUid(null));
            user.getReviews().forEach(r -> {
                r.setUid(null);
                r.setContent(null);
            });

            return user;


        } else {
            throw new LoginException();
        }
    }
}
