package lk.ijse.parkingspaceservice.repo;

import lk.ijse.parkingspaceservice.entity.ParkingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSessionRepo extends JpaRepository<ParkingSession, Integer> {

    @Query("SELECT s from ParkingSession s where s.userId =:userId")
    ParkingSession getByUser(@Param("userId") int userId);
}
