package com.example.demo;

import org.springframework.stereotype.Component;

@Component("espressoMachine") // 기본 클래스명이 아닌 지정한 이름으로 Bean을 등록한다.
public class EspressoMachine implements CoffeeMachine {
    @Override
    public String brew() {
        return "Brewing coffee with Espresso Machine";
    }
}
