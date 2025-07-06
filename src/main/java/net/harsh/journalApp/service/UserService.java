package net.harsh.journalApp.service;

import net.harsh.journalApp.dto.UserDTO;
import net.harsh.journalApp.entity.User;
import net.harsh.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUserDTOs() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId().toHexString(), user.getUsername()))
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> getUserDTO(ObjectId userId) {
        return userRepository.findById(userId).map(user -> new UserDTO(user.getId().toHexString(), user.getUsername()));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteById(ObjectId userId) {
        userRepository.deleteById(userId);
    }
}
