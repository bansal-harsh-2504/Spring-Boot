package net.harsh.journalApp.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journal_entries")
@Data
public class JournalEntryv2 {
    @Id
    private ObjectId id;
    private String content;
    private String title;
    private LocalDateTime date;
}
