# IoC (Inversion of Control)

## 일반적으로 객체를 생성하고 관리하는 방식

객체를 사용하는 클래스에서 필요한 객체를 직접 생성하면 객체의 생성과 관리에 대한 책임이 해당 클래스에 있다.

```java
public class NotificationService {
    private final EmailSender emailSender;

    public NotificationService() {
        // 필요한 객체를 클래스 내부에서 직접 생성한다.
        emailSender = new EmailSender();
    }

    public void send() {
        emailSender.send();
    }
}
```

객체가 많아지거나 생성 방법이 변경되면 객체를 사용하는 코드도 함께 수정해야 할 수 있다.

## IoC (제어의 역전)

IoC는 객체의 생성과 관리에 대한 제어권을 개발자가 작성한 코드에서 외부로 넘기는 개념이다.

Spring에서는 **IoC Container**가 객체를 생성하고 관리한다. Spring Container가 관리하는 객체를 **Bean**이라고 한다.

## Spring Bean 등록

### @Component

`@Component`를 클래스에 붙이면 Component Scan 대상이 되어 Spring Bean으로 등록된다.

```java
@Component
public class DripCoffeeMachine implements CoffeeMachine {
    @Override
    public String brew() {
        return "Brewing coffee with Drip Coffee Machine";
    }
}
```

다른 구현체도 별도의 Bean으로 등록할 수 있다.

```java
@Component("espressoMachine")
public class EspressoMachine implements CoffeeMachine {
    @Override
    public String brew() {
        return "Brewing coffee with Espresso Machine";
    }
}
```

- 기본적으로 클래스 이름의 첫 글자를 소문자로 한 이름이 Bean 이름으로 사용된다.
- `@Component("espressoMachine")`처럼 이름을 직접 지정할 수도 있다.
- 현재 예제에서는 지정한 이름과 기본 Bean 이름이 같으므로 이름을 생략해도 동일하게 등록된다.

### @Configuration과 @Bean

클래스에 `@Configuration`을 붙이고 메서드에 `@Bean`을 사용하면 메서드가 반환하는 객체를 Bean으로 등록할 수 있다.

```java
@Configuration
public class CafeConfig {
    @Bean
    public CoffeeMachine coffeeMachine() {
        String machine = System.getenv("COFFEE_MACHINE");

        if ("ESPRESSO".equalsIgnoreCase(machine)) {
            return new EspressoMachine();
        }

        return new DripCoffeeMachine();
    }
}
```

이 예제에서는 `COFFEE_MACHINE` 환경변수 값이 `ESPRESSO`이면 에스프레소 머신을 반환하고, 그 외에는 드립 커피 머신을 반환한다.

- `@Component`: 주로 클래스에 붙여 Bean으로 등록한다.
- `@Bean`: 설정 클래스의 메서드가 반환하는 객체를 Bean으로 등록한다.
- `@Bean`으로 등록한 객체의 기본 Bean 이름은 메서드 이름을 사용한다. 따라서 `coffeeMachine()` 메서드가 반환하는 객체는 `coffeeMachine`이라는 이름으로 등록된다.

## @Primary

동일한 타입의 Bean 중 하나를 단일 의존성으로 주입해야 할 때 `@Primary`가 붙은 Bean을 우선 후보로 선택한다.

기존 `DripCoffeeMachine` 클래스에 다음과 같이 추가할 수 있다.

```java
@Component
@Primary
public class DripCoffeeMachine implements CoffeeMachine {
    // 기존 코드
}
```

- `@Primary`는 동일한 타입의 Bean이 여러 개일 때 기본적으로 선택할 Bean을 지정한다.
- `List<CoffeeMachine>`처럼 여러 Bean을 모아서 주입하는 경우에는 `@Primary`가 하나만 선택하는 역할을 하지 않는다.

## Spring Container와 의존성 주입

Spring Container는 등록된 Bean을 관리하고 필요한 객체에 주입한다.

```java
@Component
public class Barista {
    @Autowired
    private List<CoffeeMachine> coffeeMachines;

    public void makeCoffees() {
        for (CoffeeMachine coffeeMachine : coffeeMachines) {
            System.out.println(coffeeMachine.brew());
        }
    }
}
```

`List<CoffeeMachine>`을 주입받으므로 Spring Container에 등록된 `CoffeeMachine` 타입의 Bean들이 리스트에 전달된다.

현재 프로젝트에는 다음과 같이 세 개의 `CoffeeMachine` Bean이 등록된다.

1. `@Component`로 등록한 `DripCoffeeMachine`
2. `@Component`로 등록한 `EspressoMachine`
3. `@Bean`으로 등록한 `coffeeMachine`

세 번째 Bean은 `COFFEE_MACHINE` 환경변수에 따라 실제 구현 객체가 달라진다. 첫 번째 Bean과 같은 구현 클래스의 객체가 반환되더라도 별도로 등록된 Bean이므로 리스트에는 각각 포함된다.

또한 `@Primary`는 단일 Bean을 주입할 때 적용되므로 리스트에 들어가는 Bean의 개수에는 영향을 주지 않는다.

현재 예제에서는 코드의 간결함을 위해 `@Autowired`를 이용한 필드 주입을 사용한다.

실무에서는 의존성을 명확하게 표현하고 객체의 불변성을 유지할 수 있는 생성자 주입을 주로 사용하는 것을 권장한다.

## ApplicationContext

`ApplicationContext`는 Spring IoC Container의 주요 인터페이스다. Bean을 조회하거나 Container에 등록된 정보를 확인할 수 있다.

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        var context = SpringApplication.run(DemoApplication.class, args);

        // Container에서 Barista Bean을 조회한다.
        var barista = context.getBean(Barista.class);
        barista.makeCoffees();

        // 등록된 Bean의 개수와 이름을 확인한다.
        System.out.println(context.getBeanDefinitionCount());

        for (String beanName : context.getBeanDefinitionNames()) {
            System.out.println(beanName);
        }
    }
}
```

`context.getBean()`은 Container를 이해하기 위한 조회 방법이다. 일반적인 애플리케이션 코드에서는 필요한 Bean을 생성자 주입 등으로 전달받아 사용하는 편이 좋다.

## ApplicationRunner와 ApplicationArguments

`ApplicationRunner`를 구현하면 Spring Boot 애플리케이션의 초기화가 완료된 후 `run()` 메서드가 실행된다.

```java
@Component
public class SpringCafe implements ApplicationRunner {
    @Autowired
    private Barista barista;

    @Override
    public void run(ApplicationArguments args) {
        barista.makeCoffees();

        // --name=hong처럼 전달한 옵션형 인자를 조회한다.
        System.out.println(args.getOptionValues("name"));

        // file1.txt처럼 옵션이 아닌 인자를 조회한다.
        System.out.println(args.getNonOptionArgs());
    }
}
```

현재 프로젝트에서는 `@Autowired`를 사용한 필드 주입으로 `Barista` Bean을 전달받는다.

실행 인자 예시는 다음과 같다.

```text
--name=hong --age=20 file1.txt file2.txt
```

- `getOptionValues("name")`: `--name=hong`과 같은 옵션형 인자에서 값을 가져온다.
- `getNonOptionArgs()`: 옵션이 아닌 일반 인자를 가져온다.

## 실행 흐름

1. `SpringApplication.run()`이 Spring Boot 애플리케이션을 시작한다.
2. `@SpringBootApplication`을 기준으로 설정을 읽고 Bean을 등록한다.
3. Spring Container가 필요한 의존성을 주입한다.
4. 애플리케이션 초기화가 완료되면 `ApplicationRunner`를 구현한 `SpringCafe`의 `run()`이 실행된다.
5. `SpringApplication.run()`이 완료된 후 `ApplicationContext`가 `main()`에 반환된다.
6. `DemoApplication.main()`에서 `Barista` Bean을 조회하고 `makeCoffees()`를 다시 호출한다.

현재 예제에서는 `SpringCafe.run()`과 `DemoApplication.main()` 양쪽에서 `barista.makeCoffees()`를 호출하므로 커피 머신 출력이 두 번 나타난다.

## 정리

- **IoC**: 객체의 생성과 관리에 대한 제어권을 외부에 맡기는 개념
- **IoC Container**: Spring Bean을 생성하고 관리하는 컨테이너
- **Bean**: Spring Container가 관리하는 객체
- **@Component**: 클래스를 Bean으로 등록
- **@Bean**: 메서드가 반환하는 객체를 Bean으로 등록
- **@Primary**: 동일한 타입의 단일 의존성 주입에서 우선적으로 선택할 Bean 지정
- **ApplicationContext**: Bean 조회와 Container 기능을 제공하는 인터페이스
- **ApplicationRunner**: Spring Boot 애플리케이션 초기화가 완료된 후 실행할 작업을 정의하는 인터페이스
- **ApplicationArguments**: 애플리케이션 실행 인자를 옵션형 인자와 일반 인자로 구분해 조회하는 인터페이스
- **DI와 IoC의 관계**: IoC는 제어권을 넘기는 개념이고, DI는 그 과정에서 의존 객체를 전달하는 방법이다.