package com.daleel.student.ms.confiuration;

import javax.management.timer.Timer;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class CachingConfig {
    public static final String STUDENTS = "students";
   
    
    @Scheduled(fixedRate = Timer.ONE_HOUR)
    @CacheEvict(
        value = {STUDENTS},
        allEntries = true)
    public void clearDB() {
      log.info("Clearing events caches");
    }

   
  }
