package com.sanron.sunweather.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {


    /**
     * 匹配正则
     *
     * @param text
     * @param regularExpression
     * @return
     */
    public static final String[] matcherText(String text, String regularExpression) {
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(text);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        if (list.size() > 0) {
            String[] result = new String[list.size()];
            list.toArray(result);
            return result;
        } else {
            return null;
        }
    }
}
