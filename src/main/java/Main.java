import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        ПодборОтрезков подборОтрезков = new ПодборОтрезков(50.71f,
                Arrays.asList(11.3f, 17.05f, 24.05f, 25.66f, 30.52f, 36.005f, 1f, 5.01f, 1000.12f, 50.088f,0.001f));
        подборОтрезков.запустить();
    }
}
