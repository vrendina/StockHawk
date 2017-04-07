package com.udacity.stockhawk.data;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {

    private static DecimalFormat dollarFormatUnsigned;
    private static DecimalFormat dollarFormatSigned;

    private static DecimalFormat percentageFormatSigned;

    public static DecimalFormat getDollarFormatUnsigned() {
        if(dollarFormatUnsigned == null) {
            dollarFormatUnsigned = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        }
        return dollarFormatUnsigned;
    }

    public static DecimalFormat getDollarFormatSigned() {
        if(dollarFormatSigned == null) {
            dollarFormatSigned = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            dollarFormatSigned.setNegativePrefix("−$");
            dollarFormatSigned.setPositivePrefix("+$");
        }
        return dollarFormatSigned;
    }

    public static DecimalFormat getPercentageFormatSigned() {
        if(percentageFormatSigned == null) {
            percentageFormatSigned = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
            percentageFormatSigned.setMaximumFractionDigits(2);
            percentageFormatSigned.setMinimumFractionDigits(2);
            percentageFormatSigned.setPositivePrefix("+");
            percentageFormatSigned.setNegativePrefix("−");
        }
        return percentageFormatSigned;
    }
}
