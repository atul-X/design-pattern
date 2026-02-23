package lld.lru;

import java.util.HashMap;

public class CacheService {
    LinkListService linkListService;
    int capacity;
    HashMap<Integer, LinkListService.Node> cache;

    public CacheService(int capacity){
        this.capacity = capacity;
        cache = new HashMap<>();
        linkListService=new LinkListService();
    }

    public void put(int key, int value){
        if(cache.containsKey(key)){
            LinkListService.Node node = cache.get(key);
            node.value = value;
            linkListService.removeNode(node);
            linkListService.insertAtHead(node);
            return;
        }
        LinkListService.Node node = new LinkListService.Node(key, value);
        if (cache.size()==capacity){
            cache.remove(linkListService.tail.prev.key);
            linkListService.removeNodeFromTail();
        }
        cache.put(key, node);
        linkListService.insertAtHead(node);
    }
    public int get(int key){
        LinkListService.Node node = cache.get(key);
        linkListService.removeNode(node);
        linkListService.insertAtHead(node);
        return node.value;
    }
    public void remove(int key){
        LinkListService.Node node = cache.get(key);
        linkListService.removeNode(node);
        cache.remove(key);
    }
    public void display(){
        linkListService.display();
    }

    public static void main(String[] args) {
        CacheService cacheService=new CacheService(3);
        cacheService.put(2,3);
        cacheService.display();
        cacheService.put(1,5);
        cacheService.display();
        cacheService.display();
        cacheService.put(4,2);
        cacheService.display();
        cacheService.display();
        cacheService.put(6,1);
        cacheService.display();
        cacheService.put(9,1);
        cacheService.display();
        cacheService.display();
    }
}
