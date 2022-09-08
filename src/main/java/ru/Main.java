package ru;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String ТРЕБУЕМАЯ_ДЛИНА = "100";
        List<String> СПИСОК_ОТРЕЗКОВ = Arrays.asList("20", "20", "10","25","25","50","100", "15", "15", "30", "1","1","19","14","18","101");

        ПодборОтрезков подборОтрезков = new ПодборОтрезков(ТРЕБУЕМАЯ_ДЛИНА, СПИСОК_ОТРЕЗКОВ);
        подборОтрезков.запустить();
    }
}
