import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ПодборОтрезков {
    private final static Logger LOGGER = LoggerFactory.getLogger(ПодборОтрезков.class);

    /**
     * Требуемая длина отрезка
     */
    private final Integer ТРЕБУЕМАЯ_ДЛИНА;

    /**
     * Список размеров отрезков
     */
    private final List<Integer> МАССИВ_ОТРЕЗВКОВ;
    private final String форматВыводаРезультата = "%s Размер %s";

    /**
     * Список полученных отрезков
     */
    private List<List<Integer>> результатСписокМассивов = new ArrayList<>();

    public ПодборОтрезков(Integer ТРЕБУЕМАЯ_ДЛИНА, List<Integer> МАССИВ_ОТРЕЗВКОВ) {
        this.ТРЕБУЕМАЯ_ДЛИНА = ТРЕБУЕМАЯ_ДЛИНА;
        this.МАССИВ_ОТРЕЗВКОВ = МАССИВ_ОТРЕЗВКОВ;
    }

    public void запустить() {
        отсортироватьРазмерОтрезков();
        выполнитьРасчет();
        показатьРезультат();
    }

    private void показатьРезультат() {
        LOGGER.info("*****************************************************************************");
        результатСписокМассивов.stream()
                .peek(value -> value.sort(Collections.reverseOrder()))
                .sorted((value1, value2) -> суммаОтрезковМассива(value1).compareTo(суммаОтрезковМассива(value2)))
                .distinct()
                .forEach(value -> {
                    LOGGER.info(форматВыводаРезультата.formatted(value, суммаОтрезковМассива(value)));
                });
    }

    private void отсортироватьРазмерОтрезков() {
        МАССИВ_ОТРЕЗВКОВ.sort(Collections.reverseOrder());
    }

    private void выполнитьРасчет() {
        List<Integer> array = МАССИВ_ОТРЕЗВКОВ.stream().filter(segment -> segment <= ТРЕБУЕМАЯ_ДЛИНА).collect(Collectors.toList());
        for (int i = 0; i < array.size(); i++) {
            Integer кКомуПлюсуем = array.get(i);
            List<Integer> массивЧтоПлюсуем = array.stream().dropWhile(v -> v == кКомуПлюсуем).collect(Collectors.toList());
            расчетСуммыОтрезков(кКомуПлюсуем, массивЧтоПлюсуем);
        }
    }

    private void расчетСуммыОтрезков(final Integer кКомуПлюсуем, final List<Integer> массивЧтоПлюсуем) {
        List<Integer> массивРезультатов = new ArrayList<>();
        массивРезультатов.add(кКомуПлюсуем);
        массивЧтоПлюсуем.stream().forEach(чтоПлюсуем -> {
            массивРезультатов.add(чтоПлюсуем);
            еслиБольше(массивРезультатов, чтоПлюсуем);
            еслиРавен(массивРезультатов, чтоПлюсуем);
        });
        еслиМеньше(массивРезультатов);
    }

    private void еслиМеньше(List<Integer> массивРезультатов) {
        LOGGER.warn("Не добрал " + суммаОтрезковМассива(массивРезультатов));
        результатСписокМассивов.add(new ArrayList<>(массивРезультатов));
    }

    private void еслиБольше(List<Integer> массивРезультатов, Integer чтоПлюсуем) {
        if (суммаОтрезковМассива(массивРезультатов) > ТРЕБУЕМАЯ_ДЛИНА) {
            LOGGER.warn("Превысили размер " + суммаОтрезковМассива(массивРезультатов));
            результатСписокМассивов.add(new ArrayList<>(массивРезультатов));
            массивРезультатов.remove(чтоПлюсуем);
        }
    }

    private void еслиРавен(List<Integer> массивРезультатов, Integer чтоПлюсуем) {
        if (суммаОтрезковМассива(массивРезультатов) == ТРЕБУЕМАЯ_ДЛИНА) {
            LOGGER.warn("Подобрали!!" + суммаОтрезковМассива(массивРезультатов));
            результатСписокМассивов.add(new ArrayList<>(массивРезультатов));
            массивРезультатов.remove(чтоПлюсуем);
        }
    }

    private Integer суммаОтрезковМассива(List<Integer> массивРезультатов) {
        return массивРезультатов.stream().reduce((n, v) -> n + v).orElse(0);
    }
}
