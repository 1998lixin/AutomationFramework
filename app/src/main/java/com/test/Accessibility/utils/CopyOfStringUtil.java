package com.test.Accessibility.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 14194 on 2018/8/29.
 */

public class CopyOfStringUtil {
    private static final String SEP1 = ",";
    private static final String SEP2 = "|";
    private static final String SEP3 = "=";
    public static String ListToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(ListToString((List<?>) list.get(i)));
                    sb.append(SEP1);
                } else if (list.get(i) instanceof Map) {
                    sb.append(MapToString((Map<?, ?>) list.get(i)));
                    sb.append(SEP1);
                } else {
                    sb.append(list.get(i));
                    sb.append(SEP1);
                }
            }
        }
        return "" + sb.toString();
    }


    /**
     51      * Map转换String
     52      *
     53      * @param map
     54      *            :需要转换的Map
     55      * @return String转换后的字符串
     56      */
    public static String MapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + SEP1 + ListToString((List<?>) value));
                sb.append(SEP2);
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString() + SEP1 + MapToString((Map<?, ?>) value));
                sb.append(SEP2);
            } else {
                sb.append(key.toString() + SEP3 + value.toString());
                sb.append(SEP2);
            }
        }
        return "M" + sb.toString();
    }
    /**
     81      * String转换Map
     82      *
     83      * @param mapText
     84      *            :需要转换的字符串
     85      * @return Map<?,?>
     86      */
      public static Map<String, Object> StringToMap(String mapText) {

                 if (mapText == null || mapText.equals("")) {
                         return null;
                     }
                 mapText = mapText.substring(1);

                 Map<String, Object> map = new HashMap<String, Object>();
                 String[] text = mapText.split("\\" + SEP2); // 转换为数组
                 for (String str : text) {
                         String[] keyText = str.split(SEP3); // 转换key与value的数组
                         if (keyText.length < 1) {
                                 continue;
                             }
                         String key = keyText[0]; // key
                         String value = keyText[1]; // value
                         if (value.charAt(0) == 'M') {
                                 Map<?, ?> map1 = StringToMap(value);
                                 map.put(key, map1);
                             } else if (value.charAt(0) == 'L') {
                                 List<?> list = StringToList(value);
                                 map.put(key, list);
                             } else {
                                 map.put(key, value);
                             }
                     }
                 return map;
             }

             /**
 117      * String转换List
 118      *
 119      * @param listText
 120      *            :需要转换的文本
 121      * @return List<?>
 122      */
             public static List<Object> StringToList(String listText) {
                 if (listText == null || listText.equals("")) {
                         return null;
                     }
                 listText = listText.substring(1);

                 List<Object> list = new ArrayList<Object>();
                 String[] text = listText.split("\\" + SEP1);
                 String listStr = "";
                 boolean flag = false;
                 for (String str : text) {
                         if (!str.equals("")) {
                                 if (str.charAt(0) == 'M') {
                                         Map<?, ?> map = StringToMap(str);
                                         list.add(map);
                                     } else if (str.charAt(0) == 'L' || flag) {
                                         flag = true;
                                         listStr += str + SEP1;
                                     } else {
                                         list.add(str);
                                     }
                             }
                         if (str.equals("")) {
                                 flag = false;
                                 List<?> lists = StringToList(listStr);
                               list.add(lists);
                             }
                   }
                 return list;
             }
}
