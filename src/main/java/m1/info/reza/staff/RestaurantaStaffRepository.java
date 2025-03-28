package m1.info.reza.staff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantaStaffRepository extends JpaRepository<RestaurantStaff, Long> {
}
