package spring.boot.community.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import spring.boot.community.service.BoardService;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping({"", "/"})
    public String board(@RequestParam(defaultValue = "0") final Long id, final Model model) {
        model.addAttribute("board", boardService.findBoardById(id));

        return "/board/form";
    }

    @GetMapping("/list")
    public String list(@PageableDefault final Pageable pageable, final Model model) {
        model.addAttribute("boards", boardService.findBoardList(pageable));

        return "board/list";
    }
}
