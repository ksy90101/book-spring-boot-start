package book.spring.test.ex.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class BookRepositoryTest {
    private static final String BOOK_TITLE = "Spring Boot Book";

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("save() : book 저장하기 테스트")
    @Test
    void saveTest() {
        final Book book = Book.builder()
                .title(BOOK_TITLE)
                .publishedAt(LocalDateTime.now())
                .build();

        testEntityManager.persist(book);

        assertThat(bookRepository.getOne(book.getId())).isEqualTo(book);
    }

    @DisplayName("BookList 저장후 검색 테스트")
    @Test
    void searchAfterSaveAllTest() {
        final Book book1 = Book.builder()
                .title(BOOK_TITLE)
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book1);
        final Book book2 = Book.builder()
                .title(BOOK_TITLE)
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book2);
        final Book book3 = Book.builder()
                .title(BOOK_TITLE)
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book3);

        final List<Book> books = bookRepository.findAll();

        assertThat(books).hasSize(3);
        assertThat(books).contains(book1, book2, book3);
    }

    @DisplayName("저장하고 삭제 테스트")
    @Test
    void deleteAfterSave() {
        final Book book1 = Book.builder()
                .title(BOOK_TITLE)
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book1);
        final Book book2 = Book.builder()
                .title(BOOK_TITLE)
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book2);
        final Book book3 = Book.builder()
                .title(BOOK_TITLE)
                .publishedAt(LocalDateTime.now())
                .build();
        testEntityManager.persist(book3);

        List<Book> books = bookRepository.findAll();

        assertThat(books).hasSize(3);

        bookRepository.deleteAll();

        books = bookRepository.findAll();

        assertThat(books).hasSize(0);
    }
}
