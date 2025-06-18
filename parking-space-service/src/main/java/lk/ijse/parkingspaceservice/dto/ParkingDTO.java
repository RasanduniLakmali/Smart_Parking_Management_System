package lk.ijse.parkingspaceservice.dto;

import lombok.Data;

@Data
public class ParkingDTO {

    private int parking_id;
    private String space_code;
    private String location;
    private String city;
    private String zone;
    private String description;
    private Boolean is_available;
    private int owner_id;


}
