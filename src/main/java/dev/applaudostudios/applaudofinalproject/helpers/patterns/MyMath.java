package dev.applaudostudios.applaudofinalproject.helpers.patterns;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyMath {
    public static Double round(double number, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(number);
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}
