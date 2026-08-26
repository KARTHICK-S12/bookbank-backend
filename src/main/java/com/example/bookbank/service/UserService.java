package com.example.bookbank.service;

import com.example.bookbank.repository.UserRepository;
import com.example.bookbank.entity.User;
import org.springframework.stereotype.Service;
import com.example.bookbank.security.JwtService;

import java.util.List;
import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,JwtService jwtService){
    this.userRepository=userRepository;
        this.jwtService = jwtService;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser( User user){
        String hashedPassword = BCrypt.hashpw(
                user.getPassword(),
                BCrypt.gensalt()
        );

        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public String validateUser(String email,String password){


        Optional<User> userOptional = userRepository.findByEmail(email);

        if(userOptional.isEmpty() ){
            return "user not found please register by continue";
        }

        User user=userOptional.get();

        if(BCrypt.checkpw(password,user.getPassword())){
            String token = jwtService.generateToken(
                    user.getId(),
                    user.getRole().name()
            );

            return token;
        }

        return "Invalid Password";
    }

    public User updateUser(Long id, User userDetails){
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPhone(userDetails.getPhone());
        existingUser.setRole(userDetails.getRole());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}