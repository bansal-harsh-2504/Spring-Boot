package net.harsh.journalApp.dto;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import java.util.Optional;

@Data
public class UpdateUserRequest {
    private String currentPassword;
    private String newUsername;
    private String newPassword;
}
