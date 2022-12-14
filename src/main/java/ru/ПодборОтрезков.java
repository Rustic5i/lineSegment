package ru;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ПодборОтрезков {
    private final static Logger LOGGER = LoggerFactory.getLogger(ПодборОтрезков.class);

    /**
     * Требуемая длина отрезка
     */
    private final BigDecimal ТРЕБУЕМАЯ_ДЛИНА;

    /**
     * Список размеров отрезков
     */
    private final List<BigDecimal> МАССИВ_ОТРЕЗКОВ;
    private final String форматВыводаРезультата = "%s Размер %s";

    /**
     * Список полученных отрезков
     */
    private List<List<BigDecimal>> результатСписокМассивов = new ArrayList<>();


    public ПодборОтрезков(String ТРЕБУЕМАЯ_ДЛИНА, List<String> МАССИВ_ОТРЕЗВКОВ) {
        this.ТРЕБУЕМАЯ_ДЛИНА = new BigDecimal(ТРЕБУЕМАЯ_ДЛИНА.replace(",", "."));
        this.МАССИВ_ОТРЕЗКОВ = Optional.ofNullable(МАССИВ_ОТРЕЗВКОВ)
                .orElse(new ArrayList<>()).stream()
                .map(отрезок -> new BigDecimal(отрезок.replace(",", ".")))
                .collect(Collectors.toList());
    }

    public void запустить() {
        отсортироватьРазмерОтрезков();
        выполнитьРасчет();
        показатьРезультат();
    }

    private void показатьРезультат() {
        LOGGER.info("************************ Список всех возможных отрезков для размера " + ТРЕБУЕМАЯ_ДЛИНА + " *****************************************************");
        результатСписокМассивов.stream()
                .peek(value -> value.sort(Collections.reverseOrder()))
                .sorted((value1, value2) -> суммаОтрезковМассива(value1).compareTo(суммаОтрезковМассива(value2)))
                .distinct()
                .forEach(value -> {
                    показатьРезультат(value);
                });
    }

    private void показатьРезультат(List<BigDecimal> списокОтрезков) {
        BigDecimal суммаОтрезковМассива = суммаОтрезковМассива(списокОтрезков);
        if (суммаОтрезковМассива.compareTo(ТРЕБУЕМАЯ_ДЛИНА) == 0) {
            LOGGER.info(форматВыводаРезультата.formatted(списокОтрезков, суммаОтрезковМассива + " <============="));
            return;
        }
        if (списокОтрезков.size() == 1 && суммаОтрезковМассива.compareTo(ТРЕБУЕМАЯ_ДЛИНА) > 0) {
            LOGGER.info(форматВыводаРезультата.formatted(списокОтрезков, суммаОтрезковМассива + " <============= минимально большой отрезок (можно отрезать)"));
            return;
        }
        LOGGER.info(форматВыводаРезультата.formatted(списокОтрезков, суммаОтрезковМассива));
    }

    private void отсортироватьРазмерОтрезков() {
        МАССИВ_ОТРЕЗКОВ.sort(Collections.reverseOrder());
    }

    private void выполнитьРасчет() {
        List<BigDecimal> array = МАССИВ_ОТРЕЗКОВ.stream().filter(segment -> segment.compareTo(ТРЕБУЕМАЯ_ДЛИНА) <= 0).collect(Collectors.toList());
        if (array.contains(ТРЕБУЕМАЯ_ДЛИНА)){
            результатСписокМассивов.add(Arrays.asList(ТРЕБУЕМАЯ_ДЛИНА));
            array.remove(ТРЕБУЕМАЯ_ДЛИНА);
        }
        for (int i = 0; i < array.size(); i++) {
            BigDecimal кКомуПлюсуем = array.get(i);
            List<BigDecimal> массивЧтоПлюсуем = new ArrayList<>(array);
            массивЧтоПлюсуем.remove(кКомуПлюсуем);
            расчетСуммыОтрезков(кКомуПлюсуем, массивЧтоПлюсуем);
            array.remove(кКомуПлюсуем);
        }
        подобратьМинимальноБольшойОтрезок(МАССИВ_ОТРЕЗКОВ);
    }

    /**
     * Если есть отрезки больше требуемой длины.
     * Находим минимально большой отрезок и добавляем в список результатов.
     * Так как из данного отрезка возможно будет отрезать нужный размер.
     *
     * @param массив_отрезков
     */
    private void подобратьМинимальноБольшойОтрезок(List<BigDecimal> массив_отрезков) {
        массив_отрезков.stream()
                .sorted()
                .filter(отрезок -> отрезок.compareTo(ТРЕБУЕМАЯ_ДЛИНА) > 0)
                .findFirst()
                .ifPresent(минимальноБольшойОтрезок -> результатСписокМассивов.add(Arrays.asList(минимальноБольшойОтрезок)));
    }

    private void расчетСуммыОтрезков(final BigDecimal кКомуПлюсуем, final List<BigDecimal> массивЧтоПлюсуем) {
        List<BigDecimal> массивРезультатов = new ArrayList<>();
        массивРезультатов.add(кКомуПлюсуем);
        массивЧтоПлюсуем.stream().forEach(чтоПлюсуем -> {
            массивРезультатов.add(чтоПлюсуем);
            еслиБольше(массивРезультатов, чтоПлюсуем);
            еслиРавен(массивРезультатов, чтоПлюсуем);
        });
        еслиМеньше(массивРезультатов);
    }

    private void еслиМеньше(List<BigDecimal> массивРезультатов) {
        if (суммаОтрезковМассива(массивРезультатов).compareTo(ТРЕБУЕМАЯ_ДЛИНА) == -1) {
            LOGGER.warn("Не добрал " + суммаОтрезковМассива(массивРезультатов));
            результатСписокМассивов.add(new ArrayList<>(массивРезультатов));
        }
    }

    private void еслиБольше(List<BigDecimal> массивРезультатов, BigDecimal чтоПлюсуем) {
        if (суммаОтрезковМассива(массивРезультатов).compareTo(ТРЕБУЕМАЯ_ДЛИНА) > 0) {
            LOGGER.warn("Превысили размер " + суммаОтрезковМассива(массивРезультатов));
            результатСписокМассивов.add(new ArrayList<>(массивРезультатов));
            массивРезультатов.remove(чтоПлюсуем);
        }
    }

    private void еслиРавен(List<BigDecimal> массивРезультатов, BigDecimal чтоПлюсуем) {
        if (суммаОтрезковМассива(массивРезультатов).compareTo(ТРЕБУЕМАЯ_ДЛИНА) == 0) {
            LOGGER.warn("Подобрали!!" + суммаОтрезковМассива(массивРезультатов));
            результатСписокМассивов.add(new ArrayList<>(массивРезультатов));
            массивРезультатов.remove(чтоПлюсуем);
        }
    }

    private BigDecimal суммаОтрезковМассива(List<BigDecimal> массивРезультатов) {
        return массивРезультатов.stream().reduce((n, v) -> n.add(v)).orElse(new BigDecimal(0));
    }
}
