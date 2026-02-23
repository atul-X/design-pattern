package lld.lru;

public class LinkListService {
    static class Node{
        int key;
        int value;
        Node prev;
        Node next;

        public Node() {
        }

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
    Node head;
    Node tail;

    public LinkListService() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    void insertAtHead(Node node){
        node.next = head.next;
        node.prev = node;
        head.next.prev = node;
        head.next = node;
    }

    void removeNode(Node node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    void removeNodeFromTail(){
        Node tailNode=tail.prev;
        removeNode(tailNode);
    }
    void display(){
        Node dummy=head;
        System.out.println("*****Displaying started*****");
        while (dummy!=null){
            System.out.println(dummy.key+" "+dummy.value);
            dummy=dummy.next;
        }
        System.out.println("*****Displaying ended*****");
    }

}
