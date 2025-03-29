package m1.info.reza.staff;

import m1.info.reza.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantStaffRepository extends JpaRepository<RestaurantStaff, Long> {
    List<RestaurantStaff> findByUser(User user);
}
