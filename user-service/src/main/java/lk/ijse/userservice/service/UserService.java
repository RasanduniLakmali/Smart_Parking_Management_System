package lk.ijse.userservice.service;


import lk.ijse.userservice.dto.LoginDTO;
import lk.ijse.userservice.dto.UserDTO;

import java.util.List;

public interface UserService {


    void registerUser(UserDTO userDTO);

    List<UserDTO> getAllUsers();

    UserDTO getByEmail(String email);

    boolean updateUser(UserDTO userDTO);

    LoginDTO loginUser(LoginDTO loginDTO);
}
