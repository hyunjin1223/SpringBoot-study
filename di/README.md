# DI (Dependency Injection)

## 일반적으로 객체를 생성하고 사용하는 방식

클래스 내부에서 필요한 객체를 직접 생성하면 사용하는 클래스가 특정 구현체에 의존하게 된다.

예를 들어 `Barista`가 `DripCoffeeMachine`을 직접 생성하면 다른 커피 머신을 사용하기 위해 `Barista` 코드도 수정해야 한다.

```java
public class Barista {
    private DripCoffeeMachine coffeeMachine;

    public Barista() {
        // Barista가 사용할 객체를 직접 생성한다.
        coffeeMachine = new DripCoffeeMachine();
    }

    public void makeCoffee() {
        System.out.println(coffeeMachine.brew());
    }
}
```

이 방식은 클래스 사이의 결합도가 높아지고, 구현체를 변경하기 어렵다는 단점이 있다.

## Dependency Injection (의존성 주입)

DI는 객체가 사용할 의존 객체를 직접 생성하지 않고 외부에서 전달받는 방식이다.

먼저 여러 커피 머신이 공통으로 사용할 기능을 인터페이스로 정의한다.

```java
public interface CoffeeMachine {
    String brew();
}
```

각 커피 머신은 `CoffeeMachine`을 구현한다.

```java
public class DripCoffeeMachine implements CoffeeMachine {
    @Override
    public String brew() {
        return "Brewing coffee with Drip Coffee Machine";
    }
}
```

```java
public class EspressoMachine implements CoffeeMachine {
    @Override
    public String brew() {
        return "Brewing coffee with Espresso Machine";
    }
}
```

## Setter Injection 구현

`Barista`는 특정 커피 머신 클래스가 아니라 공통 인터페이스에 의존한다. 사용할 객체는 Setter 메서드를 통해 외부에서 전달받는다.

```java
public class Barista {
    private CoffeeMachine coffeeMachine;

    public void setCoffeeMachine(CoffeeMachine coffeeMachine) {
        this.coffeeMachine = coffeeMachine;
    }

    public void makeCoffee() {
        System.out.println(coffeeMachine.brew());
    }
}
```

- `CoffeeMachine` 타입을 사용하므로 구현체가 바뀌어도 `Barista`의 코드를 수정할 필요가 없다.
- `setCoffeeMachine()`으로 외부에서 객체를 전달하므로 Setter Injection이라고 한다.

## Main에서 구현체 선택 및 주입

객체를 생성하고 어떤 구현체를 사용할지 결정하는 역할은 `main()` 쪽에서 담당한다.

```java
public class SpringCafe {
    public static void main(String[] args) {
        Barista barista = new Barista();

        // 사용할 구현체를 선택해 외부에서 주입한다.
        barista.setCoffeeMachine(new DripCoffeeMachine());
        barista.makeCoffee();
    }
}
```

에스프레소 머신으로 변경하려면 주입하는 객체만 바꾸면 된다.

```java
barista.setCoffeeMachine(new EspressoMachine());
```

## 정리

- **의존성(Dependency)**: 한 객체가 기능을 수행하기 위해 사용하는 다른 객체
- **의존성 주입(DI)**: 필요한 객체를 내부에서 직접 만들지 않고 외부에서 전달받는 방식
- **인터페이스 사용**: 구현체가 아닌 공통 규격에 의존하여 결합도를 낮춘다.
- **Setter Injection**: Setter 메서드를 통해 의존 객체를 전달하는 방식

현재 프로젝트는 Spring Container를 사용하지 않고 Java 코드에서 직접 객체를 주입하는 예제이다. Spring이 객체를 생성하고 관리하면서 의존성을 주입하는 방식은 `ioc` 프로젝트에서 확인한다.
