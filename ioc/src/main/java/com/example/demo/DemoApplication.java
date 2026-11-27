package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        // Spring Boot 애플리케이션을 실행하고 IoC Container를 생성한다.
        var context = SpringApplication.run(DemoApplication.class, args);

        // 학습 목적으로 Container에서 Bean을 직접 조회한다.
        var barista = context.getBean(Barista.class);
        barista.makeCoffees();

        // 등록된 Bean의 개수와 이름을 확인한다.
        System.out.println("beans count = " + context.getBeanDefinitionCount());

        for (String beanName : context.getBeanDefinitionNames()) {
            System.out.println("bean " + beanName);
        }
    }
}
