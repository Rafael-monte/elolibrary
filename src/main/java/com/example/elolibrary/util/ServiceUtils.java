package com.example.elolibrary.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class ServiceUtils {

    public static String createExceptionMessage(final MessageTemplate template, Object... params) {
        return String.format(template.getValue(), (Object) params);
    }

    public static Boolean isFalsyString(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static String dateToDDMMYYYY(LocalDate date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }


}
