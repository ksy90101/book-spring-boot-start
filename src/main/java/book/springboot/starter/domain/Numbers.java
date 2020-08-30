package book.springboot.starter.domain;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Component
@ConfigurationProperties("number")
public class Numbers {
    private final List<Number> numbers;
}
