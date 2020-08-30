package book.springboot.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class StarterApplication {

    public static void main(final String[] args) {
        SpringApplication.run(StarterApplication.class, args);
    }

    @GetMapping
    public String helloWorld() {
        return "HelloWorld";
    }
}
