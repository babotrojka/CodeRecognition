package hr.fer.zemris.ocitavanje.koda.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class serving as both directional map
 */
public class BiMap {
    /**
     * Inner map
     */
    private Map<Integer, String> map = new HashMap<>();

    /**
     * Singleton instance
     */
    private static BiMap biMap = new BiMap();

    /**
     * Get instance
     * @return instance
     */
    public static BiMap getInstance() {
        return biMap;
    }

    /**
     * Put to map
     * @param i
     * @param s
     */
    public void put(Integer i, String s) {
        map.put(i, s);
    }

    /**
     * Get from map
     * @param key
     * @return
     */
    public String get(Integer key) {
        return map.get(key);
    }

    /**
     * Get inversed
     * @param key
     * @return
     */
    public Integer getInversed(String key) {
        for(Map.Entry<Integer, String> me : map.entrySet())
            if(me.getValue().equals(key))
                return me.getKey();

        return null;
    }
}
