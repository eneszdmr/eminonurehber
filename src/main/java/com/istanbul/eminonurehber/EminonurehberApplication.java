package com.istanbul.eminonurehber;

import com.istanbul.eminonurehber.Entity.Role;
import com.istanbul.eminonurehber.Entity.User;
import com.istanbul.eminonurehber.Repository.RoleRepository;
import com.istanbul.eminonurehber.Repository.UserRepository;
import com.istanbul.eminonurehber.utils.SliderImageProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
@EnableConfigurationProperties(SliderImageProperties.class)
public class EminonurehberApplication {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Constructor ile gerekli bağımlılıkları inject et
    public EminonurehberApplication(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(EminonurehberApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            // Admin rolünü veritabanında olup olmadığını kontrol et
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> {
                        // Eğer admin rolü yoksa, yeni bir admin rolü oluştur
                        Role newRole = new Role();
                        newRole.setName("ADMIN");
                        return roleRepository.save(newRole); // Yeni rolü kaydet
                    });

            // Firma rolünü veritabanında olup olmadığını kontrol et
            Role firmaRole = roleRepository.findByName("FIRMA")
                    .orElseGet(() -> {
                        // Eğer firma rolü yoksa, yeni bir firma rolü oluştur
                        Role newRole = new Role();
                        newRole.setName("FIRMA");
                        return roleRepository.save(newRole); // Yeni rolü kaydet
                    });

            // Eğer admin kullanıcısı yoksa
            if (!userRepository.existsByEmail("admin@example.com")) {
                User admin = new User();
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("123")); // Şifreyi encode et
                admin.setRoles(Set.of(adminRole)); // Admin rolünü ekle
                userRepository.save(admin); // Admin kullanıcısını kaydet
                System.out.println("Admin user created: admin@example.com with password: 123");
            }
        };
    }

}
