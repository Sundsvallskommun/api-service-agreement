package se.sundsvall.agreement;

import org.springframework.cloud.openfeign.EnableFeignClients;
import se.sundsvall.dept44.ServiceApplication;

import static org.springframework.boot.SpringApplication.run;

@EnableFeignClients
@ServiceApplication
public class Application {
	public static void main(String... args) {
		run(Application.class, args);
	}
}
