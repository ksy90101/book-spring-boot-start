package book.spring.test.ex.controller;

import static org.hamcrest.Matchers.contains;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import book.spring.test.ex.domain.Book;
import book.spring.test.ex.service.BookService;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @DisplayName("getBookList(): 정상 동작 테스트")
    @Test
    void getBookListTest() throws Exception {
        final Book book = new Book("Spring Boot Book", LocalDateTime.now());
        given(this.bookService.getBookList()).willReturn(Collections.singletonList(book));

        this.mockMvc.perform(get("/books"))
                .andExpect(status().isOk()) // status Code == 200(OK)
                .andExpect(view().name("book")) // viewName == book
                .andExpect(model().attributeExists("bookList")) // 모델 프로퍼티 중에 bookList가 있는지 확인
                .andExpect(model().attribute("bookList", contains(book))); // bookList에 위의 book 객체가 있는지 확인
    }
}
