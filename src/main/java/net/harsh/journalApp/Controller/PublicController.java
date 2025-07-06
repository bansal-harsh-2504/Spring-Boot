package net.harsh.journalApp.Controller;

import net.harsh.journalApp.dto.UserDTO;
import net.harsh.journalApp.entity.User;
import net.harsh.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") ObjectId userId) {
        System.out.println(userId);
        Optional<UserDTO> optional = userService.getUserDTO(userId);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optional.get(), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        Optional<User> optional = userService.findByUsername(user.getUsername());
        if (optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        userService.saveUser(user);
        UserDTO userDTO = new UserDTO(user.getId().toHexString(), user.getUsername());
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }
}
