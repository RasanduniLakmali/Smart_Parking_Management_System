package lk.ijse.parkingspaceservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    private int parkingId;
    private int userId;

    private LocalTime entryTime;
    private LocalTime exitTime;

    private Boolean isPaid;
    private Double totalFee;

    public ParkingSession(int parkingId, int userId) {
        this.parkingId = parkingId;
        this.userId = userId;
    }

}
