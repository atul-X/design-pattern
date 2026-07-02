package lld.cache;

import java.util.HashMap;

public class CacheService {
    private final int capacity;
    private final HashMap<Integer, Integer> store;
    private final EvictionStrategy strategy;

    public CacheService(int capacity, EvictionStrategy strategy) {
        this.capacity = capacity;
        this.strategy = strategy;
        this.store = new HashMap<>();
    }

    public void put(int key, int value) {
        if (store.containsKey(key)) {
            store.put(key, value);
            strategy.onAccess(key);
            return;
        }
        if (store.size() == capacity) {
            int evictedKey = strategy.evict();
            store.remove(evictedKey);
        }
        store.put(key, value);
        strategy.onInsert(key);
    }

    public int get(int key) {
        if (!store.containsKey(key)) return -1;
        strategy.onAccess(key);
        return store.get(key);
    }

    public void remove(int key) {
        if (store.containsKey(key)) {
            store.remove(key);
            strategy.onRemove(key);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== LRU Cache ===");
        CacheService lru = new CacheService(3, new LRUEvictionStrategy());
        lru.put(1, 10);
        lru.put(2, 20);
        lru.put(3, 30);
        System.out.println("get(1): " + lru.get(1));  // 10, moves 1 to MRU
        lru.put(4, 40);                                 // evicts 2 (LRU)
        System.out.println("get(2): " + lru.get(2));  // -1 evicted
        System.out.println("get(3): " + lru.get(3));  // 30
        System.out.println("get(4): " + lru.get(4));  // 40

        System.out.println("\n=== LFU Cache ===");
        CacheService lfu = new CacheService(3, new LFUEvictionStrategy());
        lfu.put(1, 10);
        lfu.put(2, 20);
        lfu.put(3, 30);
        lfu.get(1);  // freq(1)=2
        lfu.get(1);  // freq(1)=3
        lfu.get(2);  // freq(2)=2
        lfu.put(4, 40);  // evicts 3 (freq=1, least frequent)
        System.out.println("get(3): " + lfu.get(3));  // -1 evicted
        System.out.println("get(1): " + lfu.get(1));  // 10
        System.out.println("get(2): " + lfu.get(2));  // 20
        System.out.println("get(4): " + lfu.get(4));  // 40
    }
}