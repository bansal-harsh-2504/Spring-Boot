package net.harsh.journalApp.Controller;

import net.harsh.journalApp.dto.JournalDTO;
import net.harsh.journalApp.entity.JournalEntryv2;
import net.harsh.journalApp.entity.User;
import net.harsh.journalApp.service.JournalEntryService;
import net.harsh.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journals")
public class JournalEntryControllerv2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalDTO>> getAllJournalsByUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> optional = userService.findByUsername(username);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<JournalDTO> journalEntries = journalEntryService.getJournalsByUserId(optional.get().getId());
        return new ResponseEntity<>(journalEntries, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalDTO> getJournalById(@PathVariable("id") ObjectId journalId) {
        Optional<JournalEntryv2> optional = journalEntryService.findById(journalId);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        JournalEntryv2 journalEntry = optional.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByUsername(authentication.getName()).orElse(null);
        if (currentUser == null || !journalEntry.getUserId().equals(currentUser.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        JournalDTO journalDTO = new JournalDTO(journalEntry.getId().toHexString(), journalEntry.getUserId().toHexString(), journalEntry.getTitle(), journalEntry.getContent());
        return new ResponseEntity<>(journalDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<JournalDTO> createEntry(@RequestBody JournalEntryv2 journalEntry) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> optional = userService.findByUsername(username);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        journalEntry.setDate(LocalDateTime.now());
        journalEntry.setUserId(optional.get().getId());
        journalEntryService.saveEntry(journalEntry);
        JournalDTO journalDTO = new JournalDTO(journalEntry.getId().toHexString(), journalEntry.getUserId().toHexString(), journalEntry.getTitle(), journalEntry.getContent());
        return new ResponseEntity<>(journalDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalDTO> updateEntry(@PathVariable("id") ObjectId journalId, @RequestBody JournalEntryv2 newEntry) {
        Optional<JournalEntryv2> optional = journalEntryService.findById(journalId);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JournalEntryv2 journalEntry = optional.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByUsername(authentication.getName()).orElse(null);
        if (currentUser == null || !journalEntry.getUserId().equals(currentUser.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
            journalEntry.setTitle(newEntry.getTitle());
        }
        journalEntry.setContent(newEntry.getContent());

        journalEntryService.saveEntry(journalEntry);
        JournalDTO journalDTO = new JournalDTO(journalEntry.getId().toHexString(), journalEntry.getUserId().toHexString(), journalEntry.getTitle(), journalEntry.getContent());
        return new ResponseEntity<>(journalDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJournalEntry(@PathVariable("id") ObjectId journalId) {
        Optional<JournalEntryv2> optional = journalEntryService.findById(journalId);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        JournalEntryv2 journalEntry = optional.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByUsername(authentication.getName()).orElse(null);
        if (currentUser == null || !journalEntry.getUserId().equals(currentUser.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        journalEntryService.deleteById(journalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
