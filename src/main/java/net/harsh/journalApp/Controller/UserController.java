package net.harsh.journalApp.Controller;

import net.harsh.journalApp.dto.UpdateUserRequest;
import net.harsh.journalApp.dto.UserDTO;
import net.harsh.journalApp.entity.User;
import net.harsh.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> users = userService.getAllUserDTOs();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") ObjectId userId) {
        Optional<UserDTO> optional = userService.getUserDTO(userId);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        Optional<User> optional = userService.findByUsername(user.getUsername());
        if (optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userService.saveUser(user);
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getJournalEntries());
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UpdateUserRequest user, @PathVariable("id") ObjectId userId) {
        Optional<User> optional = userService.findById(userId);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User existingUser = optional.get();

        boolean updated = false;

        if (!existingUser.getPassword().equals(user.getCurrentPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (user.getUsername() != null && user.getUsername().isPresent() && !existingUser.getUsername().equals(user.getUsername().get())) {
            Optional<User> taken = userService.findByUsername(user.getUsername().get());
            if (taken.isPresent()) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            existingUser.setUsername(user.getUsername().get());
            updated = true;
        }
        if (user.getNewPassword() != null && !user.getNewPassword().isEmpty()) {
            existingUser.setPassword(user.getNewPassword());
            updated = true;
        }

        if (!updated) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.saveUser(existingUser);

        UserDTO responseDto = new UserDTO(existingUser.getId(), existingUser.getUsername(), existingUser.getJournalEntries());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") ObjectId userId) {
        Optional<User> optional = userService.findById(userId);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteById(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
