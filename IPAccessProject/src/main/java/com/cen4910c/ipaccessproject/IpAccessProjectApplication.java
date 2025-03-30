package com.cen4910c.ipaccessproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
	public void run(String... args) throws Exception {
		String firstName = "Bob";
		String lastName = "Person";
		String zip = "22334";
		String email = "bobEmail@email.com";
		String PhoneNumber = "5556667777";

	}

    /*
    TODO:
        now that my code can manage data from my IDE, I need to:
        1. Create local server
        2. Make a simple display for this server
        3. Make the page interact with my backend (Springboot controller)
        4. Confirm page running on server can affect and retrieve data in db
     */

}