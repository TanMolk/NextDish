package ncl.csc8019.group12.dao.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
* This class represents a piece of data that is stored in the database of Favorite functions,
 * and finally, the methods of the related SQL can be easily used through the FavoriteRepository interface.
 *
 * @author Wei tan & Pulei chen
 */

@Entity
@Table(name = "favorite")
@EntityListeners(value = AuditingEntityListener.class)
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String placeId;

    private Long uid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }


}
