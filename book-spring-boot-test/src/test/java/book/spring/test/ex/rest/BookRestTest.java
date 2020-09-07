package book.spring.test.ex.rest;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;

import book.spring.test.ex.domain.Book;
import book.spring.test.ex.service.BookRestService;

@RestClientTest(BookRestService.class)
public class BookRestTest {

    @Autowired
    private BookRestService bookRestService;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @DisplayName("rest 테스트")
    @Test
    void restTest() {
        mockRestServiceServer.expect(requestTo("/api/books"))
                .andRespond(withSuccess(new ClassPathResource("/book.json", getClass()), MediaType.APPLICATION_JSON));

        final Book book = bookRestService.getBook();

        assertThat(book.getTitle()).isEqualTo("Spring Boot Book");
    }

    @DisplayName("rest error 테스트")
    @Test
    void restErrorTest() {
        mockRestServiceServer.expect(requestTo("/api/books"))
                .andRespond(withServerError());

        assertThatThrownBy(() -> bookRestService.getBook())
                .isInstanceOf(HttpServerErrorException.class);
    }
}
