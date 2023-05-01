package ncl.csc8019.group12.controller;

import ncl.csc8019.group12.dao.FeedbackRepository;
import ncl.csc8019.group12.dao.eneity.Feedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private static final Logger log = LoggerFactory.getLogger(FeedbackController.class);

    @Resource
    private FeedbackRepository feedbackRepository;

    @PostMapping
    public Boolean addFeedback(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestBody Feedback feedback
    ) {

        log.info("[Feedback-Add] clientId:{}", clientId);

        if (feedback.getContent() == null || feedback.getContent().isEmpty()) {
            return false;
        }

        feedback.setId(null);
        feedback.setClientId(clientId);
        Feedback save = feedbackRepository.save(feedback);
        return save.getId() != null;
    }

}
