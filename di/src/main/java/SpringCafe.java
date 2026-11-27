public class SpringCafe {
    public static void main(String[] args) {
        Barista barista = new Barista();

        // 사용할 구현체를 선택해 외부에서 주입한다.
        barista.setCoffeeMachine(new DripCoffeeMachine());
        barista.makeCoffee();
    }
}
