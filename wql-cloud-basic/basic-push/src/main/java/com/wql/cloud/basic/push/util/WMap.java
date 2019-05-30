package com.wql.cloud.basic.push.util;


import java.util.LinkedHashMap;

/**
 * 自定义Map
 */
public class WMap<K, V> extends LinkedHashMap<K, V> {

    public static <K, V> WMap<K, V> init(K k, V v) {
        return new WMap<K, V>().add(k, v);
    }

    public static <K> WMap<K,Object> initO(K k, Object v) {
        return new WMap<K, Object>().add(k, v);
    }

    public WMap<K, V> add(K k, V v) {
        put(k, v);
        return this;
    }

}

