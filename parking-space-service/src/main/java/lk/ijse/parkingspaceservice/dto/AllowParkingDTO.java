package lk.ijse.parkingspaceservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AllowParkingDTO {

    private int id;
    private String spaceCode;
    private String licensePlate;
    private int userId;
    private LocalDate allowedDate;
    private LocalTime entryTime;
    private LocalTime exitTime;

}
