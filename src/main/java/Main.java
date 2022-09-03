import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ПодборОтрезков подборОтрезков = new ПодборОтрезков(50, Arrays.asList(11, 17, 24, 25, 30, 36, 1, 5, 100));
        подборОтрезков.запустить();
    }
}
