package lk.ijse.userservice.service.impl;

import lk.ijse.userservice.dto.LoginDTO;
import lk.ijse.userservice.dto.UserDTO;
import lk.ijse.userservice.entity.User;
import lk.ijse.userservice.repo.UserRepo;
import lk.ijse.userservice.service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already has account");
        } else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setFull_name(userDTO.getFull_name());
            user.setPhone_number(userDTO.getPhone_number());
            user.setAddress(userDTO.getAddress());
            user.setRegistration_date(userDTO.getRegistration_date());
            user.setRole(userDTO.getRole());
            userRepository.save(user);

        }

    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user,UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getByEmail(String email) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        User user = optionalUser.get();
        return modelMapper.map(user,UserDTO.class);
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        Optional<User> optionalUser = userRepo.findByEmail(userDTO.getEmail());
        User user = optionalUser.get();

        System.out.println("optionalUser: " + user);

        if (user != null) {

            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setFull_name(userDTO.getFull_name());
            user.setPhone_number(userDTO.getPhone_number());
            user.setAddress(userDTO.getAddress());
            user.setRegistration_date(userDTO.getRegistration_date());
            user.setRole(userDTO.getRole());
            userRepo.save(user);
            return true;
        }
        return false;
    }



    @Override
    public UserDTO getUserById(int userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return modelMapper.map(user,UserDTO.class);
        }
        throw new RuntimeException("Invalid user Id!");
    }

//    @Override
//    public boolean loginUser(LoginDTO loginDTO) {
//        Optional<User> optionalUser = userRepository.findByEmail(loginDTO.getEmail());
//
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            // Check password using BCrypt
//            return passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
//        }
//
//        return false;
//    }


    @Override
    public boolean loginUser(LoginDTO loginDTO) {
        Optional<User> optionalUser = userRepo.findByEmail(loginDTO.getEmail());
        User user = optionalUser.get();
        if (user != null && user.getPassword().equals(loginDTO.getPassword())) {
            return true;
        }
        return false;
    }

}
