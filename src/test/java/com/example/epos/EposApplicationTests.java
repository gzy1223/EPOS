package com.example.epos;

import com.example.epos.common.BaseContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.epos.common.BaseContext.searchSubString;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EposApplicationTests {

    @Test
    void contextLoads() {
        String str = "gzy7189@gmail.com";
        String subStr = "gzy7";
    }

}
