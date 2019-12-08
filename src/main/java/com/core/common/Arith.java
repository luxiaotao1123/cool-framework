package com.core.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 四则运算
 * Created by vincent on 2019-04-15
 */
public class Arith {

    /**
     * 加法
     * @param dot 保留小数点
     * @param numbers 数字类型
     */
    public static double add(int dot, Number... numbers) {
        return getAirth(dot, ArithType.ADD.getValue(), numbers);
    }

    /**
     * 减法
     * @param dot 保留小数点
     * @param numbers 第一个是被减数
     */
    public static double subtract(int dot, Number... numbers) {
        return getAirth(dot, ArithType.SUBTRACT.getValue(), numbers);
    }

    /**
     * 乘法
     * @param dot 保留小数点
     * @param numbers 乘法的因子
     */
    public static double multiplys(int dot, Number... numbers) {
        return getAirth(dot, ArithType.MULTIPLY.getValue(), numbers);
    }

    /**
     * 除法
     * @param dot 保留小数点
     * @param numbers 除数，第一个是除数
     */
    public static double divides(int dot, Number... numbers) {
        return getAirth(dot, ArithType.DIVIDE.getValue(), numbers);
    }

    /**
     * 获取百分比
     * @param numbers 除数，第一个是除数
     * @return 百分数 * 100
     */
    public static int percentage(Number... numbers){
        return (int) (divides(2,numbers) * 100);
    }

    /**
     * 余数
     * @param numbers
     * @return
     */
    public static double remainder(Number... numbers){
        return new BigDecimal(numbers[0].toString()).remainder(new BigDecimal(String.valueOf(numbers[1]))).doubleValue();
    }

    /**
     * 综合的运算
     * @param dot       保留精度
     * @param arithType 运算的类型
     * @param numbers   需要运算的数字
     */
    private static double getAirth(int dot, int arithType, Number... numbers) {
        if (numbers == null || numbers.length == 0) {
            return 0.0;
        }
        BigDecimal bigDecimal = new BigDecimal(numbers[0].toString());
        for (int i = 1; i < numbers.length; i++) {
            switch (arithType) {
                case 0:
                    bigDecimal = bigDecimal.add(new BigDecimal(String.valueOf(numbers[i])));

                    break;
                case 1:
                    bigDecimal = bigDecimal.divide(new BigDecimal(String.valueOf(numbers[i])), dot, RoundingMode.HALF_UP);
                    break;
                case 2:
                    bigDecimal = bigDecimal.subtract(new BigDecimal(String.valueOf(numbers[i])));
                    break;
                case 3:
                    bigDecimal = bigDecimal.multiply(new BigDecimal(String.valueOf(numbers[i])));
                    break;
                default:
                    return 0.00;
            }
        }
        return bigDecimal.setScale(dot, RoundingMode.HALF_UP).doubleValue();
    }


    /**
     * 运算的类型
     */
    private enum ArithType {
        ADD("add", 0),
        DIVIDE("divide", 1),
        SUBTRACT("subtract", 2),
        MULTIPLY("multiply", 3);
        @SuppressWarnings("unused")
		private String key;
        private int value;
        ArithType(String key, int value) {
            this.key = key;
            this.value = value;
        }
        public int getValue() {
            return value;
        }
    }

}
