package com.istanbul.eminonurehber.DTO;

import com.istanbul.eminonurehber.Entity.Role;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private String password;
    private Set<String> roles; // Roller bir Set olarak alınacak
}