package net.harsh.journalApp.Controller;

import net.harsh.journalApp.entity.JournalEntryv2;
import net.harsh.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerv2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntryv2> getAll() {
        return journalEntryService.getAll();
    }

    @PostMapping
    public JournalEntryv2 createEntry(@RequestBody JournalEntryv2 journalEntry) {
        journalEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(journalEntry);
        return journalEntry;
    }

    @GetMapping("/{journalId}")
    public JournalEntryv2 getJournalById(@PathVariable ObjectId journalId) {
        return journalEntryService.findById(journalId).orElse(null);
    }

    @PutMapping("/{journalId}")
    public JournalEntryv2 updateEntry(@PathVariable ObjectId journalId, @RequestBody JournalEntryv2 newEntry) {
        JournalEntryv2 journalEntry = journalEntryService.findById(journalId).orElse(null);
        if (journalEntry != null) {
            journalEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : journalEntry.getTitle());
            journalEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : journalEntry.getContent());
        }
        journalEntryService.saveEntry(journalEntry);
        return journalEntry;
    }

    @DeleteMapping("/{journalId}")
    public boolean deleteJournalEntry(@PathVariable ObjectId journalId) {
        journalEntryService.deleteById(journalId);
        return true;
    }
}
