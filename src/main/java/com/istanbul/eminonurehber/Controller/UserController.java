package com.istanbul.eminonurehber.Controller;

import com.istanbul.eminonurehber.Config.JwtUtil;
import com.istanbul.eminonurehber.DTO.CompanyDTO;
import com.istanbul.eminonurehber.DTO.UserDTO;
import com.istanbul.eminonurehber.Entity.User;
import com.istanbul.eminonurehber.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getOneUSer(@PathVariable Long id){
        return userService.findById(id);
    }

    @PostMapping("/register")
    public UserDTO register(@RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);  // Registers a user and returns a DTO
    }

//    @PostMapping("/login")
//    public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) {
//        return ResponseEntity.ok(userService.authentice(email,password));
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        User user = userService.authentice(email, password);
        String token = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token, user.getRoles().toString()));
    }

    // Token'ı doğrulamak için endpoint (Opsiyonel)
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
        return ResponseEntity.ok("Token geçerli: " + email);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        System.out.println("logged out");
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out");
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateCompany(@PathVariable Long id, @RequestBody UserDTO userDto) {
        UserDTO user=userService.updateUser(id,userDto);
        return ResponseEntity.ok(user);
    }



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class AuthResponse {
        private String token;
        private String roles;

    }
}
