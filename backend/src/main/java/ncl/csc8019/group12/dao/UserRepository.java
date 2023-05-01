package ncl.csc8019.group12.dao;

import ncl.csc8019.group12.dao.eneity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    List<User> findUsersByUidIn(List<Long> uidList);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = ?2 WHERE u.email = ?1")
    int updatePasswordWithEmail(String email, String password);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.nickname = ?2 WHERE u.uid = ?1")
    int updateNickNameWithUid(Long uid, String nickname);

    boolean existsByEmail(String email);
}
