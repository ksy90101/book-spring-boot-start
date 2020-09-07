package book.spring.test.ex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import book.spring.test.ex.service.BookService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    public static void main(final String[] args) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i <= 5 / 2)// 위쪽 영역
                {
                    if (i + j <= 5 / 2 - 1)// 왼쪽 위 공백찍기
                    {
                        System.out.print(" ");
                    } else if (j - i >= 5 / 2 + 1) // 오른쪽 위 공백찍기
                    {
                        System.out.print(" ");
                    } else {
                        System.out.print("*");// *찍기
                    }
                } else if (i > 5 / 2) //아래쪽 영역
                {
                    if (i - j >= 5 / 2 + 1) //왼쪽 밑 공백
                    {
                        System.out.print(" ");
                    } else if (i + j >= 5 / 2 * 3 + 1)//오른쪽 밑 공백
                    {
                        System.out.print(" ");
                    } else {
                        System.out.print("*"); // *찍기
                    }
                }
            }
            System.out.println();//줄바꿈
        }
    }

    @GetMapping("/books")
    public String getBookList(final Model model) {
        model.addAttribute("bookList", this.bookService.getBookList());

        return "book";
    }
}
