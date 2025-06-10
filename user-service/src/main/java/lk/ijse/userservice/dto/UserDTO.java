package lk.ijse.userservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDTO {

    private int user_id;
    private String username;
    private String email;
    private String password;
    private String full_name;
    private String phone_number;
    private String address;
    private LocalDate registration_date;
    private String role;

}
