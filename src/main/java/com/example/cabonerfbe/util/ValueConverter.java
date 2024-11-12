package com.example.cabonerfbe.util;

import com.example.cabonerfbe.enums.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ValueConverter {
    public static BigDecimal bigDecimalConverter(double value) {
        return BigDecimal.valueOf(value).setScale(Constants.BIG_DECIMAL_DEFAULT_SCALE, RoundingMode.HALF_UP);
    }
}
