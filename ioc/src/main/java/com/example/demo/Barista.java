package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Barista {
    // 등록된 CoffeeMachine Bean들을 모두 리스트로 주입받는다.
    @Autowired
    private List<CoffeeMachine> coffeeMachines;

    // @PostConstruct
    public void makeCoffees() {
        for (CoffeeMachine coffeeMachine : coffeeMachines) {
            System.out.println(coffeeMachine.brew());
        }
    }
}
