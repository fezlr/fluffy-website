package fezlr.fluffy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FluffyApplication {
	public static void main(String[] args) {
		SpringApplication.run(FluffyApplication.class, args);
    }
}
