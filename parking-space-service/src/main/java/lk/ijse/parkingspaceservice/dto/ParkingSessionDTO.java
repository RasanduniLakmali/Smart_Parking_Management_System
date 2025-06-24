package lk.ijse.parkingspaceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSessionDTO {

    private int sessionId;

    private int parkingId;
    private int userId;

    private LocalTime entryTime;
    private LocalTime exitTime;

    private Boolean isPaid;
    private Double totalFee;

    public ParkingSessionDTO(int parkingId, int userId) {
        this.parkingId = parkingId;
        this.userId = userId;
    }
}
