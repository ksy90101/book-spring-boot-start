package book.spring.test.ex.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import book.spring.test.ex.domain.Book;
import book.spring.test.ex.service.BookRestService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BookApiController {
    private final BookRestService bookRestService;

    @GetMapping(path = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> getBook() {
        final Book book = bookRestService.getBook();

        return ResponseEntity.ok(book);
    }
}
