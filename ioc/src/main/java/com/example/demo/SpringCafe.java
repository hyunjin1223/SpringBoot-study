package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringCafe implements ApplicationRunner {
    // Spring Container가 관리하는 Barista Bean을 주입받는다.
    @Autowired
    private Barista barista;

    // 애플리케이션이 준비된 뒤 Spring Boot가 이 메서드를 호출한다.
    @Override
    public void run(ApplicationArguments args) throws Exception {
        barista.makeCoffees();

        // 옵션형 인자: --name=hong --age=20
        List<String> optionValues = args.getOptionValues("name");
        if (optionValues != null) {
            for (String optionValue : optionValues) {
                System.out.println("option arg(name) = " + optionValue);
            }
        }

        // 옵션이 아닌 일반 인자: file1.txt file2.txt
        List<String> nonOptionArgs = args.getNonOptionArgs();
        for (String nonOptionArg : nonOptionArgs) {
            System.out.println("non option arg = " + nonOptionArg);
        }
    }
}
