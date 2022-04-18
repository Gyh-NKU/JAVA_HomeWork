import java.util.HashMap;
import java.util.Scanner;

class Node<K, V> {
    public K key;
    public V value;
    public Node<K, V> next, pre;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
        next = null;
        pre = null;
    }
}

class LRU<K, V> {

    HashMap<K, Node<K, V>> map = new HashMap<>();
    final int maxCapacity;
    int curNum;

    Node<K, V> head;
    Node<K, V> tail;

    LRU(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        curNum = 0;
        head = null;
    }

    private void moveToHead(Node<K, V> node) {
        if (node == head) {
            return;
        } else if (node == tail) {
            head.pre.next = null;
            head = head.pre;
        } else {
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }

        node.pre = head.pre;
        node.next = head;
        head.pre = node;
        head = node;
    }

    private void addToHead(Node<K, V> node) {
        if (map.isEmpty()) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.pre = node;
            head = node;
        }
    }

    private void dele() {
        map.remove(tail.key);
        var temp = tail.pre;
        tail = temp;
        temp.next = null;
        curNum--;
    }

    /**
     * get a Node without putting it to the head
     *
     * @param key
     * @return
     */
    private Node<K, V> getKV(K key) {
        return map.get(key);
    }

    /**
     * get a Value and putting the Node of it to the head
     *
     * @param key
     * @return
     */
    V get(K key) {
        var kv = getKV(key);
        if (kv != null) {
            //put the node to the head
            moveToHead(kv);
            return kv.value;
        } else {
            return null;
        }
    }

    void put(K key, V value) {
        var kv = getKV(key);

        if (kv == null) {
            kv = new Node<K, V>(key,value);
            if (map.size() == maxCapacity) {
                dele();
            }

            addToHead(kv);
            map.put(key, kv);
        } else {
            kv.value = value;
            moveToHead(kv);
        }
    }

    void print() {
        for (var i = head; i != null; i = i.next) {
            System.out.println(i.key + " " + i.value);
        }
        System.out.println();
        for (var i : map.entrySet()) {
            System.out.println(i.getKey() + " " + i.getValue().value);
        }
    }
}


class LRUCache {

    // 双向链表节点定义
    class Node {
        int key;
        String val;
        Node prev;
        Node next;
    }

    private int capacity;
    //保存链表的头节点和尾节点
    private Node first;
    private Node last;

    private HashMap<Integer, Node> map;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>(capacity);
    }

    public String get(int key) {
        Node node = map.get(key);
        if (node == null) {
            return null;
        }
        moveToHead(node);
        return node.val;
    }

    private void moveToHead(Node node) {
        if (node == first) {
            return;
        } else if (node == last) {
            last.prev.next = null;
            last = last.prev;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        node.prev = first.prev;
        node.next = first;
        first.prev = node;
        first = node;
    }

    public void put(int key, String value) {
        Node node = map.get(key);

        if (node == null) {
            node = new Node();
            node.key = key;
            node.val = value;

            if (map.size() == capacity) {
                removeLast();
            }

            addToHead(node);
            map.put(key, node);
        } else {
            node.val = value;
            moveToHead(node);
        }
    }

    private void addToHead(Node node) {
        if (map.isEmpty()) {
            first = node;
            last = node;
        } else {
            node.next = first;
            first.prev = node;
            first = node;
        }
    }

    private void removeLast() {
        map.remove(last.key);
        Node prevNode = last.prev;
        if (prevNode != null) {
            prevNode.next = null;
            last = prevNode;
        }
    }

    @Override
    public String toString() {
        return map.keySet().toString();
    }

}


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();
        LRUCache lru = new LRUCache(n);

        sc.nextLine();
        for (int i = 1; i <= m; i++) {
            String opt = sc.nextLine();
            String cmd = opt.substring(0, 3);
//            System.out.println(cmd);
            if (cmd.equals("put")) {
                int begin = 4;
                int end = begin;
                int tempK;
                String tempV;
                while (end < opt.length()) {
                    while (opt.charAt(end) != ',') {
                        end++;
                    }
                    tempK = Integer.parseInt(opt.substring(begin, end));
                    begin = end + 1;
                    end++;

                    while (opt.charAt(end) != ';' && end != opt.length() - 1) {
                        end++;
                    }
                    if (end == opt.length() - 1 && opt.charAt(end) != ';') {
                        tempV = opt.substring(begin, end + 1);
                    } else {
                        tempV = opt.substring(begin, end);
                    }
                    begin = end + 1;
                    end++;
                    lru.put(tempK, tempV);
                }
            } else {
                int begin = 4;
                int end = begin;
                int tempK;
                StringBuilder sb = new StringBuilder();
                while (end < opt.length()) {
                    while (opt.charAt(end) != ';' && end != opt.length() - 1) {
                        end++;
                    }
                    if (end == opt.length() - 1 && opt.charAt(end) != ';') {
                        tempK = Integer.parseInt(opt.substring(begin, end + 1));
                    } else {
                        tempK = Integer.parseInt(opt.substring(begin, end));
                    }
                    begin = end + 1;
                    end++;
                    sb.append(lru.get(tempK));
                    sb.append(";");
                }
                System.out.println(sb);
            }
//            lru.print();
//            System.out.println();
        }
    }
}
