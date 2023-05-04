package ncl.csc8019.group12.controller;

import ncl.csc8019.group12.dao.FeedbackRepository;
import ncl.csc8019.group12.dao.eneity.Feedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * FeedbackController is to record feedback and suggestions given by users.
 *
 * @author Wei tan & Pulei chen
 */
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
        //This code uses a logging tool to record the user's id, which is easy for the developer to observe in the background.
        log.info("[Feedback-Add] clientId:{}", clientId);

        //Determines if the feedback is empty, if it is empty then it means no meaningful operation.
        if (feedback.getContent() == null || feedback.getContent().isEmpty()) {
            return false;
        }

        //If not empty, create a new feedback object and store it in the feedbackRepository.
        feedback.setId(null);
        feedback.setClientId(clientId);
        Feedback save = feedbackRepository.save(feedback);

        //Determine if the feedback is stored successfully by whether the Id is empty or not.
        return save.getId() != null;
    }

}
