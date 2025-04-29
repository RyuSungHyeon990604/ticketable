package com.example.modulegame;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
	classes = {
		RedisAutoConfiguration.class,
		RedisReactiveAutoConfiguration.class
	}
)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class ModuleGameApplicationTests {

	@Test
	void contextLoads() {
	}

}
