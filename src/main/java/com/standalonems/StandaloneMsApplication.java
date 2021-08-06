package com.standalonems;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class StandaloneMsApplication {


    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(StandaloneMsApplication.class);
        springApplication.addListeners(new ApplicationPidFileWriter());
        springApplication.run(args);
    }


}
