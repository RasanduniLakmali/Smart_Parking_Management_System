package lk.ijse.parkingspaceservice.repo;

import lk.ijse.parkingspaceservice.entity.AllowParking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowParkingRepo extends JpaRepository<AllowParking, Integer> {


}
