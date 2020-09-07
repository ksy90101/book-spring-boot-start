package book.spring.test.ex.service;

import java.util.List;

import org.springframework.stereotype.Service;

import book.spring.test.ex.domain.Book;
import book.spring.test.ex.domain.BookRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getBookList() {
        return this.bookRepository.findAll();
    }
}
