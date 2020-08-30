package book.springboot.starter.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FruitPropertyTest {
    @Autowired
    private FruitProperty fruitProperty;

    @DisplayName("Property ConfigurationProperty Test")
    @Test
    void configurationPropertyTest() {
        final List<Map<String, String>> fruits = this.fruitProperty.getFruits();

        assertAll(
                () -> assertThat(fruits).hasSize(3),
                () -> assertThat(fruits.get(0).get("name")).isEqualTo("banana"),
                () -> assertThat(fruits.get(0).get("color")).isEqualTo("yellow")
        );
    }
}
