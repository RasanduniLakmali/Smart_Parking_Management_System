package lk.ijse.userservice.service;

import lk.ijse.userservice.dto.UserDTO;

public interface UserService {

    int saveUser(UserDTO userDTO);
    UserDTO searchUser(String username);
}
