package richard.test.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by richard on 4/12/18.
 */
public class UnmodifiedMap {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>(10);
        map.put("1", "2");
        map.put("2", "3");
        map.put("3", "4");
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            it.next().getKey().equals("2");
            it.remove();
        }
        Map<String, String> unmodifiedMap = Collections.unmodifiableMap(map);
        unmodifiedMap.put("5", "6");
    }
}
