package com.challenge.tuiter.integracion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import java.time.Duration;

@Tag("integration")
@SpringBootTest(classes = TestApplication.class,
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(RedisTestConfig.class)
@EnableAutoConfiguration(
  exclude = {org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class, org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class})
@ActiveProfiles("test")
abstract class BaseIntegrationTest {

  @Container
  protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

  @Container
  protected static final ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(
    "confluentinc/cp-kafka:7.5.0").withReuse(true);

  @Container
  protected static final GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
    .withExposedPorts(6379).withStartupTimeout(Duration.ofSeconds(30));
  protected WebTestClient webTestClient;
  @LocalServerPort
  private int port;

  @DynamicPropertySource
  static void registrarPropiedades(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);

    registry.add("spring.redis.host", redis::getHost);
    registry.add("spring.redis.port", () -> redis.getMappedPort(6379));

    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @BeforeEach
  void initClient() {
    this.webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
  }
}
