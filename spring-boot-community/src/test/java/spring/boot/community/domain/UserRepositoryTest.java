package spring.boot.community.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .name("rutgo")
                .password("test")
                .email("rutgo@gmail.com")
                .createdDate(LocalDateTime.now())
                .build());
    }

    @DisplayName("findByEmail() 메소드 테스트")
    @Test
    void findByEmailTest() {
        final User expected = userRepository.findByEmail("rutgo@gmail.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을수 없습니다."));

        assertThat(expected).isEqualTo(user);
    }
}
