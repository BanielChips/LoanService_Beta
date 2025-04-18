package com.cen4910c.ipaccessproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class IpAccessProjectApplication implements CommandLineRunner {

    private final DataHandling dataHandling;

    public IpAccessProjectApplication(DataHandling dataHandling) {
        this.dataHandling = dataHandling;
    }

    public static void main(String[] args) {
        SpringApplication.run(IpAccessProjectApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String firstName = "Test";
        String lastName = "User";
        String zip = "12345";
        String email = "test@email.com";
        String password = new BCryptPasswordEncoder().encode("test123");  //  fixed line
        String phone = "5555555555";

        if (dataHandling.getUserByEmail(email) == null) {
            dataHandling.addUser(firstName, lastName, zip, email, password, phone);
            System.out.println("Test user created");
        } else {
            System.out.println("Test user already exists");
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
