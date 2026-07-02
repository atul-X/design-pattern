package lld.cache;

import java.util.HashMap;

public class LRUEvictionStrategy implements EvictionStrategy {

    private static class Node {
        int key;
        Node prev, next;
        Node(int key) { this.key = key; }
        Node() {}
    }

    private final Node head, tail;
    private final HashMap<Integer, Node> map;

    public LRUEvictionStrategy() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
        map = new HashMap<>();
    }

    private void insertAtHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    @Override
    public void onAccess(int key) {
        Node node = map.get(key);
        removeNode(node);
        insertAtHead(node);
    }

    @Override
    public void onInsert(int key) {
        Node node = new Node(key);
        map.put(key, node);
        insertAtHead(node);
    }

    @Override
    public int evict() {
        Node lru = tail.prev;
        removeNode(lru);
        map.remove(lru.key);
        return lru.key;
    }

    @Override
    public void onRemove(int key) {
        Node node = map.remove(key);
        if (node != null) removeNode(node);
    }
}