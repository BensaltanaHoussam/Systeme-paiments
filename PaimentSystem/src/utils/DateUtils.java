package utils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DateUtils {
    private DateUtils() {}

    public static List<LocalDate> monthlyDates(LocalDate startInclusive, LocalDate endInclusive) {
        if (startInclusive == null || endInclusive == null || endInclusive.isBefore(startInclusive)) return Collections.emptyList();
        long months = (endInclusive.getYear() - startInclusive.getYear()) * 12L
                + (endInclusive.getMonthValue() - startInclusive.getMonthValue());
        return Stream.iterate(startInclusive, d -> d.plusMonths(1))
                .limit(months + 1)
                .collect(Collectors.toList());
    }
}
