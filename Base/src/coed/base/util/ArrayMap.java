// based on http://www.java2s.com/Code/Java/Collections-Data-Structure/CustomArrayMapimplementation.htm
package coed.base.util;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ArrayMap<K, V> extends AbstractMap<K, V> implements Cloneable, Serializable {

  static class Entry<K, V> implements Map.Entry<K, V> {
    protected K key;
    protected V value;

    public Entry(K key, V value) {
      this.key = key;
      this.value = value;
    }

    public K getKey() {
      return key;
    }

    public V getValue() {
      return value;
    }

    public V setValue(V newValue) {
      V oldValue = value;
      value = newValue;
      return oldValue;
    }

    public boolean equals(Object o) {
      if (!(o instanceof Map.Entry)) {
        return false;
      }
      Map.Entry e = (Map.Entry) o;
      return (key == null ? e.getKey() == null : key.equals(e.getKey()))
          && (value == null ? e.getValue() == null : value.equals(e
              .getValue()));
    }

    public int hashCode() {
      int keyHash = (key == null ? 0 : key.hashCode());
      int valueHash = (value == null ? 0 : value.hashCode());
      return keyHash ^ valueHash;
    }

    public String toString() {
      return key + "=" + value;
    }
  }

  private Set<Map.Entry<K,V>> entries = null;

  private ArrayList<Map.Entry<K,V>> list;

  public ArrayMap() {
    list = new ArrayList<Map.Entry<K,V>>();
  }

  public ArrayMap(Map map) {
    list = new ArrayList<Map.Entry<K,V>>();
    putAll(map);
  }

  public ArrayMap(int initialCapacity) {
    list = new ArrayList<Map.Entry<K,V>>(initialCapacity);
  }

  public Set<Map.Entry<K,V>> entrySet() {
    if (entries == null) {
      entries = new AbstractSet<Map.Entry<K,V>>() {
        public void clear() {
          list.clear();
        }

        public Iterator<Map.Entry<K,V>> iterator() {
          return list.iterator();
        }

        public int size() {
          return list.size();
        }
      };
    }
    return entries;
  }

  public V put(K key, V value) {
    int size = list.size();
    Entry<K,V> entry = null;
    int i;
    if (key == null) {
      for (i = 0; i < size; i++) {
        entry = (Entry<K,V>) (list.get(i));
        if (entry.getKey() == null) {
          break;
        }
      }
    } else {
      for (i = 0; i < size; i++) {
        entry = (Entry<K,V>) (list.get(i));
        if (key.equals(entry.getKey())) {
          break;
        }
      }
    }
    V oldValue = null;
    if (i < size) {
      oldValue = entry.getValue();
      entry.setValue(value);
    } else {
      list.add(new Entry<K,V>(key, value));
    }
    return oldValue;
  }

  @Override
  public Object clone() {
    return new ArrayMap<K,V>(this);
  }
}