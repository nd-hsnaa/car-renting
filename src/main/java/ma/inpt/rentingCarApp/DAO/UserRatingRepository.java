package ma.inpt.rentingCarApp.DAO;

import ma.inpt.rentingCarApp.entities.UserRating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRatingRepository extends CrudRepository<UserRating, Long> {

}