package ncl.csc8019.group12.dao.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * This class represents a piece of feedback data stored in the database of Feedback functions,
 * and finally, the methods of the related SQL can be easily used through the FeedbackRepository interface.
 *
 * @author Pulei chen
 */
@Entity
@Table(name = "feedback")
@EntityListeners(value = AuditingEntityListener.class)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
