package book.spring.test.ex.json;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import book.spring.test.ex.domain.Book;

@JsonTest
public class BookJsonTest {

    @Autowired
    private JacksonTester<Book> json;

    @DisplayName("json 테스트")
    @Test
    void jsonTest() throws IOException {
        final Book book = Book.builder()
                .title("Spring Boot Book")
                .build();

        final String content = "{\"title\": \"Spring Boot Book\"}";

        assertThat(json.parseObject(content).getTitle()).isEqualTo(book.getTitle());
        assertThat(json.write(book)).isEqualToJson("/book.json");
        assertThat(json.write(book)).hasJsonPathStringValue("title");
        assertThat(json.write(book)).extractingJsonPathStringValue("title").isEqualTo("Spring Boot Book");
    }
}
