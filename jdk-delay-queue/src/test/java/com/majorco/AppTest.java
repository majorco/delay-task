package com.majorco;

import static org.junit.Assert.assertTrue;

import com.majorco.delay.TestTask;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {

  /**
   * Rigorous Test :-)
   */
  @Test
  public void shouldAnswerWithTrue() {
    assertTrue(true);
  }

  public static void main(String[] args) throws IOException {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    final LocalDateTime parse = LocalDateTime.parse("2022-11-14 16:45:11", dateTimeFormatter);
    TestTask testTask = new TestTask("5656", parse, "666", "我是任务");
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.postForEntity("http://127.0.0.1:8080/task", testTask, Void.class);
  }

  @Test
  public void testRedis() {

  }
}
