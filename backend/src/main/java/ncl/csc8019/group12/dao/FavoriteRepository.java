package ncl.csc8019.group12.dao;

import ncl.csc8019.group12.dao.eneity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUid(Long uid);


}
