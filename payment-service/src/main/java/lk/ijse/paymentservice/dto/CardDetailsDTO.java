package lk.ijse.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDetailsDTO {


    private int cardId;
    private int driverId;
    private String mockCardNumber;
    private String mockHolderName;
    private String mockExpiry;
    private String mockCvv;


}
