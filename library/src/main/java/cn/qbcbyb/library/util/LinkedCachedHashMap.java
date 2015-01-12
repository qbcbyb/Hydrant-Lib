package cn.qbcbyb.library.util;

import java.util.Collection;
import java.util.LinkedHashMap;

public class LinkedCachedHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1L;
    private int mCapacity;

    public interface OnRemove<T> {
        public void onRemove(T value);
    }

    public OnRemove<V> onRemove;

    public LinkedCachedHashMap(int initialCapacity, float loadFactor, boolean accessOrder, OnRemove<V> onRemove) {
        super(initialCapacity, loadFactor, accessOrder);
        this.mCapacity = initialCapacity;
        this.onRemove = onRemove;
    }

    public void ensureCapacity(final int aCapacity) {
        if (aCapacity > mCapacity) {
            mCapacity = aCapacity;
        }
    }

    @Override
    public Collection<V> values() {
        synchronized (this) {
            return super.values();
        }
    }

    @Override
    public void clear() {
        synchronized (this) {
            super.clear();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        synchronized (this) {
            return super.containsValue(value);
        }
    }

    @Override
    public V get(Object key) {
        synchronized (this) {
            return super.get(key);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        synchronized (this) {
            return super.containsKey(key);
        }
    }

    @Override
    public V put(K key, V value) {
        synchronized (this) {
            return super.put(key, value);
        }
    }

    @Override
    public V remove(Object key) {
        synchronized (this) {
            return super.remove(key);
        }
    }

    @Override
    protected boolean removeEldestEntry(Entry<K, V> eldest) {
        synchronized (this) {
            if (size() > mCapacity) {
                V remove = remove(eldest.getKey());
                if (this.onRemove != null) {
                    onRemove.onRemove(remove);
                }
            }
            return false;
        }
    }
}
