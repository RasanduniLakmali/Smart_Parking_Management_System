package lk.ijse.paymentservice.repo;

import lk.ijse.paymentservice.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptDetailRepo extends JpaRepository<Receipt,Integer> {

    @Query("SELECT r FROM Receipt r ORDER BY r.receiptId DESC LIMIT 1")
    Receipt findTopByOrderByReceiptIdDesc();
}
