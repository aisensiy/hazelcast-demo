package com.example.hazelcastdemo;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class HazelcastDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(HazelcastDemoApplication.class, args);
  }

  // init a hazelcastInstance
  @Bean
  public HazelcastInstance hazelcastInstance() {
    MapConfig mapConfig = new MapConfig("books");
    mapConfig.setTimeToLiveSeconds(15);
    Config config = new Config();
    config.addMapConfig(mapConfig);
    return Hazelcast.newHazelcastInstance(config);
  }
}

@Slf4j
@Component
@RequiredArgsConstructor
class Scheduler {
  private final BookService bookService;
  private final CacheManager cacheManager;

  @Scheduled(fixedDelay = 10000)
  public void updateCache() {
    log.info("scheduler called...");
    Book books = bookService.findBookInSlowSource("12345");
    cacheManager.getCache("books").clear();
    cacheManager.getCache("books").put("12345", books);
  }
}

@RequiredArgsConstructor
@RestController
@RequestMapping("/metrics")
class MetricsController {
  private final BusinessMetricsService businessMetricsService;

  @GetMapping
  public List<BusinessMetricsEntityList> businessMetricsEntityListList() {
    return businessMetricsService.get();
  }
}

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
class BookController {

  private final BookService bookService;

  @GetMapping("/{isbn}")
  public Book getBookNameByIsbn(@PathVariable("isbn") String isbn) {
    return bookService.getBookNameByIsbn(isbn);
  }
}

@Service
class BookService {
  @Cacheable("books")
  public Book getBookNameByIsbn(String isbn) {
    return findBookInSlowSource(isbn);
  }

  public Book findBookInSlowSource(String isbn) {
    // some long processing
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return new Book(isbn, "Sample Book Name " + LocalDateTime.now(), Map.of("a", "b"));
  }
}

@Data
@AllArgsConstructor
class Book implements Serializable {
  private String isbn;
  private String name;
  private Map<String, Object> properties;
}