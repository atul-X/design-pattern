package lld.cache;

public interface EvictionStrategy {
    void onAccess(int key);
    void onInsert(int key);
    int evict();
    void onRemove(int key);
}