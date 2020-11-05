package ru.romzhel.eshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.romzhel.eshop.services.MailService;

@SpringBootApplication
public class EshopApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(EshopApplication.class, args);
	}


	@Autowired
	private MailService mailService;

	@Override
	public void run(String... args) throws Exception {
//		mailService.sendMail("romzhel@mail.ru", "тест", "сообщение");
	}
}
