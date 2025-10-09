package com.capgroup.hotelmicroservices.msreserva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MsReservaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsReservaApplication.class, args);
    }

}
