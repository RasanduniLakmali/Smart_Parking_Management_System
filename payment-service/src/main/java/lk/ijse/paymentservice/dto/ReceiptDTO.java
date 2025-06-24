package lk.ijse.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDTO {

    private int receiptId;
    private String receiptNumber;
    private String driverId;
    private double amount;
    private LocalDateTime issuedTime;


}
