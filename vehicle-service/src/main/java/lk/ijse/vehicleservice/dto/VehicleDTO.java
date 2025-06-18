package lk.ijse.vehicleservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VehicleDTO {

    private int vehicle_id;
    private String license_plate;
    private String type;
    private String model;
    private String color;
    private int driver_id;
    private LocalDate registration_date;


}
