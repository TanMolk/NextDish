package ncl.csc8019.group12.dao.entity;

import ncl.csc8019.group12.enums.UserStateEnum;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a piece of User data stored in the database of Review functions,
 * and finally, the methods of the related SQL can be easily used through the UserRepository interface.
 *
 * @author Rahcel wu & Wei tan
 */
@Entity
@Table(name = "user")
@EntityListeners(value = AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    private String email;
    private String password;
    private String nickname;
    private String token;
    private UserStateEnum state;

    @JoinColumn(name = "uid")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Review> reviews;

    @JoinColumn(name = "uid")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Favorite> favorites;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserStateEnum getState() {
        return state;
    }

    public void setState(UserStateEnum state) {
        this.state = state;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }
}
