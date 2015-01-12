package cn.qbcbyb.library.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapUtil {
    /**
     * 对Map排序
     *
     * @param map unsorted map
     * @return sorted map
     */
    public static final Map<String, String> sort(Map<String, String> map) {
        Map<String, String> sorted = new LinkedHashMap<String, String>();
        for (String key : getSortedKeys(map)) {
            sorted.put(key, map.get(key));
        }
        return sorted;
    }

    private static List<String> getSortedKeys(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);
        return keys;
    }

    /**
     * Form-urlDecodes and appends all keys from the source {@link java.util.Map} to the target {@link java.util.Map}
     *
     * @param source Map from where the keys get copied and decoded
     * @param target Map where the decoded keys are copied to
     */
    public static void decodeAndAppendEntries(Map<String, String> source, Map<String, String> target) {
        for (String key : source.keySet()) {
            target.put(URLUtil.percentEncode(key), URLUtil.percentEncode(source.get(key)));
        }
    }

}
