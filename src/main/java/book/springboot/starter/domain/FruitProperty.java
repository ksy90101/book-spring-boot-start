package book.springboot.starter.domain;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Component
@ConfigurationProperties("fruit")
public class FruitProperty {
    private final List<Map<String, String>> fruits;
}
