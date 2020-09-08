package spring.boot.community.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.boot.community.domain.Board;
import spring.boot.community.domain.BoardRepository;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<Board> findBoardList(Pageable pageable) {
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize()
        );

        return boardRepository.findAll(pageable);
    }

    public Board findBoardById(final Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));
    }
}
