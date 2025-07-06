package net.harsh.journalApp.Controller;

import net.harsh.journalApp.dto.UpdateUserRequest;
import net.harsh.journalApp.dto.UserDTO;
import net.harsh.journalApp.entity.User;
import net.harsh.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUserDTOs();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody UpdateUserRequest request) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(loggedInUsername).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean updated = false;
        String newUsername = request.getNewUsername();
        if (newUsername != null && !newUsername.equals(user.getUsername())) {
            if (userService.findByUsername(newUsername).isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            user.setUsername(newUsername);
            updated = true;
        }

        if (request.getNewPassword() != null) {
            String hashed = passwordEncoder.encode(request.getNewPassword());
            user.setPassword(hashed);
            updated = true;
        }

        if (!updated) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.saveUser(user);
        return new ResponseEntity<>(new UserDTO(user.getId().toHexString(), user.getUsername()), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> optional = userService.findByUsername(username);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteById(optional.get().getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
