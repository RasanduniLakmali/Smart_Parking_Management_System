package lk.ijse.paymentservice.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private int paymentId;
    private int parkingSessionId;
    private int driverId;
    private double amount;
    private String method;
    private LocalDateTime transactionTime;

}
