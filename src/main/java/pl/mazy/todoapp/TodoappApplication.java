package pl.mazy.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mazy.todoapp.model.Event;
import pl.mazy.todoapp.repository.EventRepo;

import java.util.List;

@SpringBootApplication
@RestController
@EnableJpaRepositories(basePackages = "pl.mazy.todoapp.repository")
@EntityScan("pl.mazy.todoapp.model")
public class TodoappApplication {
	public static void main(String[] args) {
		SpringApplication.run(TodoappApplication.class, args);
	}

}
