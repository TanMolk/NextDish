package ncl.csc8019.group12.dao;

import ncl.csc8019.group12.dao.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByPlaceId(String placeId);

}
