package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 설정 클래스에서 @Bean 메서드로 객체를 Bean으로 등록한다.
@Configuration
public class CafeConfig {
    @Bean
    public CoffeeMachine coffeeMachine() {
        // 환경변수에 따라 등록할 구현체를 선택한다.
        String coffeeMachine = System.getenv("COFFEE_MACHINE");
        if ("ESPRESSO".equalsIgnoreCase(coffeeMachine)) {
            return new EspressoMachine();
        }
        return new DripCoffeeMachine();
    }
}
