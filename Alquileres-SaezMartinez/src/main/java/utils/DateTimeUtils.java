package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtils {

    // Define el formato estándar para las fechas
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Convierte un LocalDateTime a un String usando el formato definido.
     *
     * @param dateTime El LocalDateTime a convertir.
     * @return El String formateado o null si dateTime es null.
     */
    public static String toString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(FORMATTER);
    }

    /**
     * Convierte un String a un LocalDateTime usando el formato definido.
     *
     * @param dateTimeString El String que representa la fecha.
     * @return El LocalDateTime resultante o null si dateTimeString es null o vacío.
     * @throws DateTimeParseException si el String no tiene el formato correcto.
     */
    public static LocalDateTime toLocalDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }

    /**
     * Verifica si un String es válido como LocalDateTime en el formato definido.
     *
     * @param dateTimeString El String a verificar.
     * @return true si el String es válido, false en caso contrario.
     */
    public static boolean isValid(String dateTimeString) {
        try {
            toLocalDateTime(dateTimeString);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

