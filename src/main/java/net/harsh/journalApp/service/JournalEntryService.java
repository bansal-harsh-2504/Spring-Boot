package net.harsh.journalApp.service;

import net.harsh.journalApp.dto.JournalDTO;
import net.harsh.journalApp.entity.JournalEntryv2;
import net.harsh.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public void saveEntry(JournalEntryv2 journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public Optional<JournalEntryv2> findById(ObjectId journalId) {
        return journalEntryRepository.findById(journalId);
    }

    public void deleteById(ObjectId journalId) {
        journalEntryRepository.deleteById(journalId);
    }

    public List<JournalDTO> getJournalsByUserId(ObjectId userId) {
        return journalEntryRepository.findByUserId(userId).stream().map(entry -> new JournalDTO(entry.getId().toHexString(), entry.getUserId().toHexString(), entry.getTitle(), entry.getContent())).collect(Collectors.toList());
    }
}
