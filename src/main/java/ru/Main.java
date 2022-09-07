package ru;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String ТРЕБУЕМАЯ_ДЛИНА = "201.374";
        List<String> СПИСОК_ОТРЕЗКОВ = Arrays.asList("11", "17,05", "24.05", "25.66", "30.52", "36.005", "1", "5.01", "1000.12", "50.088","0.001");

        ПодборОтрезков подборОтрезков = new ПодборОтрезков(ТРЕБУЕМАЯ_ДЛИНА, СПИСОК_ОТРЕЗКОВ);
        подборОтрезков.запустить();
    }
}
