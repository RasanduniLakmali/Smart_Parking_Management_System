package lk.ijse.parkingspaceservice.repo;

import jakarta.transaction.Transactional;
import lk.ijse.parkingspaceservice.entity.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingRepo extends JpaRepository<Parking, Integer> {

    @Query("SELECT p from Parking p where p.space_code=:spaceCode")
    Optional<Parking> findByCode(@Param("spaceCode") String spaceCode);


    @Query("SELECT p from Parking  p where p.is_available=:isAvailable")
    List<Parking> findAvailableSpaces(@Param("isAvailable") boolean isAvailable);

    @Query("SELECT p from Parking p where p.city=:city")
    List<Parking> filterSpacesByCity(@Param("city") String city);

    @Modifying
    @Transactional
    @Query("UPDATE Parking p SET p.is_available= false where p.space_code=:spaceCode")
    void setOccupied(@Param("spaceCode") String spaceCode);


    @Modifying
    @Transactional
    @Query("UPDATE Parking p SET p.is_available= true where p.space_code=:spaceCode")
    void setAvailable(@Param("spaceCode") String spaceCode);


    @Query("SELECT  p from Parking p where p.space_code=:spaceCode")
    Parking findBySpaceCode(@Param("spaceCode") String spaceCode);
}
