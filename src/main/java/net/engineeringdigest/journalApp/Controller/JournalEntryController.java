package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    private final Map<Long, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping
    public List<JournalEntry> getAll(){
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry journalEntry){
        if(journalEntries.containsKey(journalEntry.getId())){
            return false;
        }else{
            journalEntries.put(journalEntry.getId(), journalEntry);
            return true;
        }
    }

    @PutMapping("/{journalId}")
    public boolean updateEntry(@RequestBody JournalEntry journalEntry){
        if(journalEntries.containsKey(journalEntry.getId())){
            journalEntries.replace(journalEntry.getId(), journalEntry);
            return true;
        }else{
            return false;
        }
    }

    @GetMapping("/{journalId}")
    public JournalEntry getJournalById(@PathVariable Long journalId){
        return journalEntries.getOrDefault(journalId, null);
    }

    @DeleteMapping("/{journalId}")
    public boolean deleteJournalEntry(@PathVariable Long journalId){
        if(journalEntries.containsKey(journalId)){
            journalEntries.remove(journalId);
            return true;
        }else{
            return false;
        }
    }
}
