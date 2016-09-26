package com.fuzho.nimingban.imageloader;

/**
 * Created by fuzho on 2016/9/20.
 *
 */
public interface Cache<K,T> {
    public void set(K key, T object);
    public T get(K key);
}
