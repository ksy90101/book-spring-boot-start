package book.springboot.starter.configuration;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AutoConfigurationApplicationTests {
    @Value("${property.test.name}") // depth가 존재하는 값은 .으로 구분해서 값을 매핑
    private String propertyTestName;

    @Value("${propertyTest}") // 단일 값을 매핑
    private String propertyTest;

    @Value("${noKey:default value}") // 키 값이 존재하지 않은 경우 default 값을 설정하여 매핑
    private String defaultValue;

    @Value("${propertyTestArray}") // 배열형으로 매핑
    private String[] propertyTestArray;

    @Value("#{'${propertyTestList}' .split(',')}") // ,을 기준으로 리스트형으로 매핑
    private List<String> propertyTestList;

    @DisplayName("property @Value 학습테스트")
    @Test
    void valueAnnotationTest() {
        assertAll(
                () -> assertThat(this.propertyTestName).isEqualTo("property depth test"),
                () -> assertThat(this.propertyTest).isEqualTo("test"),
                () -> assertThat(this.defaultValue).isEqualTo("default value"),
                () -> assertThat(this.propertyTestArray).hasSize(3),
                () -> assertThat(this.propertyTestList).hasSize(3)
        );
    }
}
