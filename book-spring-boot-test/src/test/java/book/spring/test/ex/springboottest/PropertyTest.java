package book.spring.test.ex.springboottest;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import book.spring.test.ex.TestApplication;

@SpringBootTest(properties = {"property.value=propertyValue",
        "value=test"}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestApplication.class})
public class PropertyTest {
    @Value("${property.value}")
    private String propertyValue;

    @Value("${value}")
    private String value;

    @DisplayName("@SpringBootTest properties 학습 테스트")
    @Test
    void propertyTest() {
        assertThat(this.propertyValue).isEqualTo("propertyValue");
        assertThat(this.value).isEqualTo("test");
    }
}
