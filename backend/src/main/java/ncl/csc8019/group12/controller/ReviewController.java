package ncl.csc8019.group12.controller;

import ncl.csc8019.group12.dao.ReviewRepository;
import ncl.csc8019.group12.dao.UserRepository;
import ncl.csc8019.group12.dao.entity.Review;
import ncl.csc8019.group12.dao.entity.User;
import ncl.csc8019.group12.utils.ConfuseUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Review Controller is to record and update reviews related to a location.
 * This controller contains methods to retrieve all reviews with a placeId (getReviewsByPlaceId()).
 *                                  to post a review(addReview()).
 *                                  to delete a review(deleteReview()).
 * @author Rachel wu & Wei tan
 */
@CrossOrigin
@RestController
@RequestMapping("/review")
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    @Resource
    private ReviewRepository reviewRepository;

    @Resource
    private UserRepository userRepository;

    /**
     * Retrieve reviews with placeId
     * @param clientId
     * @param placeId
     * @return
     */

    /*
    * Log placeId and clientId.
    * Create a list of reviews to store all the reviews related to the placeId from reviewRepository.
    * Create a map to store a stream of uids and corresponding nicknames.
    * If the user's nickname is null, return an empty string instead.
    *
    * Create a jsonArray to store the reviews with a forEach loop:
    * Retrieved the corresponding nickname with the uid.
    * Create a jsonObject to store the review.
    * Set the id and placeId to null.
    * Put the nickname in the review jsonObject if it exists.
    * Else, put anonymous instead.
    * Put the jsonObject in the array.
    * Repeat the loop.
    *
    * Turn the jsonArray to string.
    * Return the value.
     */
    @GetMapping
    public String getReviewsByPlaceId(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestParam String placeId
    ) {
        log.info("[Review-Get] placeId:{}; clientId:{}", placeId, clientId);

        List<Review> reviews = reviewRepository.findAllByPlaceId(placeId);

        Map<Long, String> uidNickNameMap = userRepository
                .findUsersByUidIn(reviews.stream().map(Review::getUid).collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(
                        User::getUid,
                        (u) -> u.getNickname() == null ? "" : u.getNickname()));

        JSONArray jsonArray = new JSONArray();
        reviews.forEach(review -> {
            String nickname = uidNickNameMap.get(review.getUid());
            review.setUid(null);
            review.setPlaceId(null);

            JSONObject jsonObject = new JSONObject(review);
            jsonObject.put("author", nickname.isEmpty() ? "anonymous" : nickname);
            jsonArray.put(jsonObject);
        });

        return jsonArray.toString();
    }

    /**
     * Post a review
     *
     * @param clientId
     * @param token
     * @param review
     * @return
     */

    /*
    * Log the clientId.
    * Return false if the review content is null or empty.
    * Set the id to null temporarily to enable unique id generation later.
    * Set a unique token/ id for the review.
    * Return false if the setting fails.
    * Else, save the review in the reviewRepository.
    * Return true if a non-null review can be retrieved with the id.
     */

    @PostMapping
    public Boolean addReview(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestHeader("c8019-token") String token,
            @RequestBody Review review
    ) {
        log.info("[Review-Add] clientId:{}", clientId);


        if (review.getContent() == null || review.getContent().isEmpty()) {
            return false;
        }

        review.setId(null);

        try {
            review.setUid(ConfuseUtil.decryptToLongByRSA(token));
        } catch (Exception e) {
            log.info("[Review-Add] decryptToLong fail: {}", token);
            return false;
        }

        reviewRepository.save(review);
        return review.getId() != null;
    }

    /**
     * Delete Review
     *
     * @param clientId
     * @param token
     * @param id
     * @return
     */

    /*
    * Log the id and clientId.
    * Identify the uid with the token.
    * Return false if the identification has failed and log the issue.
    * Else, create a review object with the same id and uid.
    * If a review object can be found with the id,
    * delete this review object and return true.
    * Else, return false.
    *
     */

    @DeleteMapping("/{id}")
    public Boolean deleteReview(
            @RequestHeader("c8019-client-id") String clientId,
            @RequestHeader("c8019-token") String token,
            @PathVariable Long id
    ) {
        log.info("[Review-Delete] id:{}; clientId:{}", id, clientId);

        Long uid;
        try {
            uid = ConfuseUtil.decryptToLongByRSA(token);
        } catch (Exception e) {
            log.info("[Review-Delete] decryptToLong fail: {}", token);
            return false;
        }
        Review review = new Review();
        review.setId(id);
        review.setUid(uid);

        if (reviewRepository.existsById(id)) {
            reviewRepository.delete(review);
            return true;
        }

        return false;
    }
}
