package net.harsh.journalApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JournalDTO {
    private String id;
    private String userId;
    private String title;
    private String content;
}
