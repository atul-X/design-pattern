package lld.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class LFUEvictionStrategy implements EvictionStrategy {

    private final HashMap<Integer, Integer> keyToFreq;
    private final HashMap<Integer, LinkedHashSet<Integer>> freqToKeys;
    private int minFreq;

    public LFUEvictionStrategy() {
        keyToFreq = new HashMap<>();
        freqToKeys = new HashMap<>();
        minFreq = 0;
    }

    private void incrementFreq(int key) {
        int freq = keyToFreq.get(key);
        keyToFreq.put(key, freq + 1);
        freqToKeys.get(freq).remove(key);
        if (freqToKeys.get(freq).isEmpty()) {
            freqToKeys.remove(freq);
            if (minFreq == freq) minFreq++;
        }
        freqToKeys.computeIfAbsent(freq + 1, k -> new LinkedHashSet<>()).add(key);
    }

    @Override
    public void onAccess(int key) {
        incrementFreq(key);
    }

    @Override
    public void onInsert(int key) {
        keyToFreq.put(key, 1);
        freqToKeys.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFreq = 1;
    }

    @Override
    public int evict() {
        LinkedHashSet<Integer> minFreqKeys = freqToKeys.get(minFreq);
        // LinkedHashSet preserves insertion order — first entry is the least recently used among ties
        int evictKey = minFreqKeys.iterator().next();
        minFreqKeys.remove(evictKey);
        if (minFreqKeys.isEmpty()) freqToKeys.remove(minFreq);
        keyToFreq.remove(evictKey);
        return evictKey;
    }

    @Override
    public void onRemove(int key) {
        int freq = keyToFreq.remove(key);
        LinkedHashSet<Integer> keys = freqToKeys.get(freq);
        if (keys != null) {
            keys.remove(key);
            if (keys.isEmpty()) {
                freqToKeys.remove(freq);
                if (minFreq == freq) {
                    minFreq = freqToKeys.keySet().stream().mapToInt(i -> i).min().orElse(0);
                }
            }
        }
    }
}