package com.udacity.stockhawk.data;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {

    private DecimalFormat dollarFormatUnsigned;
    private DecimalFormat dollarFormatUnsignedNoDecimals;
    private DecimalFormat dollarFormatSigned;
    private DecimalFormat percentageFormatSigned;

    private static FormatUtils formatInstance;

    public static FormatUtils getFormatUtils() {
        if(formatInstance == null) {
            formatInstance = new FormatUtils();
        }
        return formatInstance;
    }

    public DecimalFormat getDollarFormatUnsigned() {
        if(dollarFormatUnsigned == null) {
            dollarFormatUnsigned = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        }
        return dollarFormatUnsigned;
    }

    public DecimalFormat getDollarFormatUnsignedNoDecimals() {
        if(dollarFormatUnsignedNoDecimals == null) {
            dollarFormatUnsignedNoDecimals = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            dollarFormatUnsignedNoDecimals.setMinimumFractionDigits(0);
        }
        return dollarFormatUnsignedNoDecimals;
    }

    public DecimalFormat getDollarFormatSigned() {
        if(dollarFormatSigned == null) {
            dollarFormatSigned = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            dollarFormatSigned.setNegativePrefix("−$");
            dollarFormatSigned.setPositivePrefix("+$");
        }
        return dollarFormatSigned;
    }

    public DecimalFormat getPercentageFormatSigned() {
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
