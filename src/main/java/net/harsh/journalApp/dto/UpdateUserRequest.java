package net.harsh.journalApp.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class UpdateUserRequest {
    private Optional<String> username;
    private String currentPassword;
    private String newPassword;
}
