package spring.boot.community;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import spring.boot.community.domain.Board;
import spring.boot.community.domain.BoardRepository;
import spring.boot.community.domain.User;
import spring.boot.community.domain.UserRepository;
import spring.boot.community.domain.enums.BoardType;

@SpringBootApplication
public class CommunityApplication {
    public static void main(final String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(final UserRepository userRepository, final BoardRepository boardRepository) {
        return (args -> {
            final User user = userRepository.save(User.builder()
                    .name("rutgo")
                    .password("test")
                    .email("rutgo@gmail.com")
                    .createdDate(LocalDateTime.now())
                    .build());

            IntStream.rangeClosed(1, 200).forEach(index ->
                    boardRepository.save(Board.builder()
                            .title("타이틀")
                            .subTitle("서브 타이틀")
                            .content("콘텐츠")
                            .boardType(BoardType.FREE)
                            .createdDate(LocalDateTime.now())
                            .updatedDate(LocalDateTime.now())
                            .user(user)
                            .build()));
        });
    }
}
