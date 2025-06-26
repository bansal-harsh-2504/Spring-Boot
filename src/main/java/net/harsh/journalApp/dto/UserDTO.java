package net.harsh.journalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.harsh.journalApp.entity.JournalEntryv2;
import org.bson.types.ObjectId;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private String username;
    private List<JournalEntryv2> journalEntries;
    private String id;

    public UserDTO(ObjectId id, String username, List<JournalEntryv2> journalEntries) {
        this.id = id.toHexString();
        this.username = username;
        this.journalEntries = journalEntries;
    }
}
