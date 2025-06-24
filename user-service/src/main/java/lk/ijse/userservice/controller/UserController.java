package lk.ijse.userservice.controller;

import jakarta.validation.Valid;


import lk.ijse.userservice.dto.AuthResponseDTO;
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
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody UserDTO userDTO) {
        userService.registerUser(userDTO);
        String token = jwtUtil.generateToken(userDTO.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponseDTO(userDTO.getEmail(), token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);

        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractUsername(token);
            return ResponseEntity.ok("Login successful for: " + email);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

//    @PostMapping("register")
//    public ResponseEntity<ResponseUtil> registerUser(@RequestBody UserDTO userDTO) {
//        System.out.println(userDTO);
//
//        try{
//            userService.registerUser(userDTO);
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ResponseUtil(200, "User registration successful!",null));
//        }catch(Exception e){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ResponseUtil(401, "Registration unsuccessful", null));
//        }
//
//    }
//
//    @PostMapping("login")
//    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
//        System.out.println(loginDTO);
//
//        String token = userService.loginUser(loginDTO);
//        return ResponseEntity.ok(token);
//
//    }
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