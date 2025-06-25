package lk.ijse.userservice.controller;


import lk.ijse.userservice.dto.LoginDTO;
import lk.ijse.userservice.dto.UserDTO;
import lk.ijse.userservice.service.UserService;

import lk.ijse.userservice.util.JwtUtil;
import lk.ijse.userservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user-service/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        String token = jwtUtil.generateToken(userDTO.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseUtil> login(@RequestBody LoginDTO loginDTO) {
        boolean isLogged = userService.loginUser(loginDTO);
        if (isLogged) {
//            String token = jwtUtil.generateToken(loginDTO.getEmail());
//            return ResponseEntity.ok(Map.of("token", token));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseUtil(201,"User login successfull!",null));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseUtil(401,"Invalid credentials",null));
        }
    }


    @GetMapping("getAll")
    public List<UserDTO> getAllUsers() {
        List<UserDTO> userDTOS = userService.getAllUsers();
        return userDTOS;
    }

    @GetMapping("getByEmail/{email}")
    public UserDTO getUserByEmail(@PathVariable String email) {
        UserDTO userDTO = userService.getByEmail(email);
        return userDTO;
    }


    @PutMapping("update")
    public ResponseUtil updateUser(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        boolean isUpdated = userService.updateUser(userDTO);

        if (isUpdated) {
            return new ResponseUtil(201, "User updated successfully!", null);
        }else {
            return new ResponseUtil(200, "User not updated!", null);
        }
    }

    @GetMapping("getById/{userId}")
    public UserDTO getUserById(@PathVariable int userId) {
        UserDTO userDTO = userService.getUserById(userId);
        if (userDTO != null) {
            return userDTO;
        }
        return null;
    }
}