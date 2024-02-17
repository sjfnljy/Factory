package com.sjf.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 产生指定前缀编码工具类
 * @Author: SJF
 * @Date: 2024/1/7 22:02
 */
public final class PrefixGenerateUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);

    /**
     * 自动生成编号
     * @param prefix  前缀，往往是一串字符串
     * @return String 生成的编号
     */
    public static String generateCode(String prefix) {
        Date currentDate = new Date();
        String formattedDate = DATE_FORMAT.format(currentDate);
        int currentSequence = SEQUENCE.getAndIncrement();
        return prefix + formattedDate + String.format("%05d", currentSequence);
    }
}
