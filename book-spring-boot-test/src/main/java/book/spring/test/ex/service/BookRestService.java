package book.spring.test.ex.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import book.spring.test.ex.domain.Book;

@Service
public class BookRestService {
    private final RestTemplate restTemplate;

    public BookRestService(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.rootUri("/api/books")
                .build();
    }

    public Book getBook() {
        return restTemplate.getForObject("/api/books", Book.class);
    }
}
