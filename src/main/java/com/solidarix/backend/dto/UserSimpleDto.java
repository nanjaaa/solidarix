package com.solidarix.backend.dto;

import com.solidarix.backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleDto {
    private Long id;
    private String firstName;
    private String lastName;

    public static UserSimpleDto fromEntity(User user) {
        return new UserSimpleDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}
