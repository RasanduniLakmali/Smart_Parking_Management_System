package lk.ijse.vehicleservice.repo;

import lk.ijse.vehicleservice.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepo extends JpaRepository<Vehicle, Integer> {

    @Query("SELECT v from Vehicle v where v.license_plate=:licensePlate")
    Optional<Vehicle> findByLicensePlate(@Param("licensePlate") String licensePlate);
}
