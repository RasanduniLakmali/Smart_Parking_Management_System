package lk.ijse.paymentservice.repo;

import lk.ijse.paymentservice.entity.CardDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDetailsRepo extends JpaRepository<CardDetails, Integer> {
}
