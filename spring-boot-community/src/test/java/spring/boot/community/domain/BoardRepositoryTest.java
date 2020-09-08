package spring.boot.community.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import spring.boot.community.domain.enums.BoardType;

@DataJpaTest
class BoardRepositoryTest {
    private final static String BOARD_TEST_TITLE = "타이틀";

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        final User user = userRepository.save(User.builder()
                .name("rutgo")
                .password("test")
                .email("rutgo@gmail.com")
                .createdDate(LocalDateTime.now())
                .build());

        boardRepository.save(Board.builder()
                .title("타이틀")
                .subTitle("서브 타이틀")
                .content("콘텐츠")
                .boardType(BoardType.FREE)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .user(user)
                .build());
    }

    @DisplayName("findTest() 찾기 메소드 테스트")
    @Test
    void findTest() {
        final User user = userRepository.findByEmail("rutgo@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을수 없습니다."));
        final Board board = boardRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        assertThat(board.getTitle()).isEqualTo(BOARD_TEST_TITLE);
        assertThat(board.getUser()).isEqualTo(user);
    }
}
