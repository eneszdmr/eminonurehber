package com.istanbul.eminonurehber.Service;


import com.istanbul.eminonurehber.DTO.CompanyDTO;
import com.istanbul.eminonurehber.DTO.UserDTO;
import com.istanbul.eminonurehber.Entity.Category;
import com.istanbul.eminonurehber.Entity.Company;
import com.istanbul.eminonurehber.Entity.Role;
import com.istanbul.eminonurehber.Entity.User;
import com.istanbul.eminonurehber.Repository.CompanyRepository;
import com.istanbul.eminonurehber.Repository.RoleRepository;
import com.istanbul.eminonurehber.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Save user and encode password
    public UserDTO saveUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Set<Role> roles = userDTO.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName) // Role ismini Role nesnesine dönüştür
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        // Kullanıcının rollerini ata
        user.setRoles(roles);


        user = userRepository.save(user);

        return userDTO;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();

        for (User user : users) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setPassword(user.getPassword());

            // Kullanıcıya ait rollerin isimlerini al
            Set<String> roleNames = user.getRoles().stream()
                    .map(Role::getName) // Role objesinin name özelliğini al
                    .collect(Collectors.toSet());

            // Roller Set olarak DTO'ya ekle
            dto.setRoles(roleNames);

            dtos.add(dto);
        }

        return dtos;
    }

//    // Find user by email
//    public UserDTO findByEmail(String email) {
//        Optional<User> user = userRepository.findByEmail(email);
//        UserDTO userDTO = new UserDTO();
//        if (user.isPresent()){
//
//            userDTO.setId(user.get().getId());
//            userDTO.setEmail(user.get().getEmail());
//            userDTO.setPassword(user.get().getPassword());
//            userDTO.setRoles(user.get().getRoles());
//        }
//
//        return userDTO;
//    }

    public UserDTO findById(Long id){
        Optional<User> user = userRepository.findById(id);
        UserDTO userDTO = new UserDTO();
        if (user.isPresent()){
            userDTO.setId(user.get().getId());
            userDTO.setEmail(user.get().getEmail());
            userDTO.setPassword(user.get().getPassword());
            Set<String> roles = user.get().getRoles().stream().map(Role::getName).collect(Collectors.toSet());
            userDTO.setRoles(roles);
        }
        return userDTO;
    }


    public User authentice(String email, String password){
        User user=userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("kullanıcı bulunamadı"));
        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("hatalı şifre");
        }
        return user;
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {

        User user = userRepository.findById(id).get();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        return dto;


    }
}
