package net.harsh.journalApp.Controller;

import net.harsh.journalApp.entity.JournalEntryv2;
import net.harsh.journalApp.entity.User;
import net.harsh.journalApp.service.JournalEntryService;
import net.harsh.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerv2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @PostMapping("/{username}")
    public ResponseEntity<JournalEntryv2> createEntry(
            @PathVariable String username,
            @RequestBody JournalEntryv2 journalEntry) {

        Optional<User> optional = userService.findByUsername(username);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        journalEntry.setDate(LocalDateTime.now());
        journalEntry.setUserId(optional.get().getId());
        journalEntryService.saveEntry(journalEntry);

        return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<JournalEntryv2> getJournalById(@PathVariable("id") ObjectId journalId) {
        Optional<JournalEntryv2> journalEntry = journalEntryService.findById(journalId);
        return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalEntryv2> updateEntry(@PathVariable("id") ObjectId journalId, @RequestBody JournalEntryv2 newEntry) {
        Optional<JournalEntryv2> optional = journalEntryService.findById(journalId);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JournalEntryv2 journalEntry = optional.get();
        if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
            journalEntry.setTitle(newEntry.getTitle());
        }
        if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
            journalEntry.setContent(newEntry.getContent());
        }

        journalEntryService.saveEntry(journalEntry);
        return new ResponseEntity<>(journalEntry, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable("id") ObjectId journalId) {
        if (!journalEntryService.findById(journalId).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        journalEntryService.deleteById(journalId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
