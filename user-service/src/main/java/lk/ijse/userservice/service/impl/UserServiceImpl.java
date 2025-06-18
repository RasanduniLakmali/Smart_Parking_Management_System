package lk.ijse.userservice.service.impl;

import lk.ijse.userservice.dto.LoginDTO;
import lk.ijse.userservice.dto.UserDTO;
import lk.ijse.userservice.entity.User;
import lk.ijse.userservice.repo.UserRepo;
import lk.ijse.userservice.service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Override
    public void registerUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        userRepo.save(user);

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
        User user = userRepo.findByEmail(email);
        return modelMapper.map(user,UserDTO.class);
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        User user = userRepo.findByEmail(userDTO.getEmail());

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
    public LoginDTO loginUser(LoginDTO loginDTO) {
          Optional<User> optionalUser = userRepo.findByUsername(loginDTO.getUsername());

          if (optionalUser.isPresent()) {
              User user = optionalUser.get();
              if (user.getPassword().equals(loginDTO.getPassword())) {
                  return loginDTO;
              }
          }
         throw new RuntimeException("Invalid credentials!");
    }
}
