import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class LRUNode<K, V> {
    K key;
    V value;
    LRUNode<K, V> next;
    LRUNode<K, V> prev;

    public LRUNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.next = null;
        this.prev = null;
    }
}

class LRU<K, V> {
    final int maxCapacity;
    int curNum;

    LRUNode<K, V> head;
    LRUNode<K, V> tail;

    LRU(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        curNum = 0;
        head = null;
    }

    private void dele() {
        var temp = tail.prev;
        tail = temp;
        temp.next = null;
        curNum--;
    }

    void put(K key, V value) {
        var kv = getKV(key);

        if (kv == null) {
            if (maxCapacity == 1) {
                curNum = 1;
                head = new LRUNode<K, V>(key, value);
                tail = head;
                return;
            }

            if (head == null) {
                head = new LRUNode<K, V>(key, value);
                tail = head;
                curNum++;
            } else {
                if (curNum == maxCapacity) {
                    dele();
                }
                var temp = new LRUNode<K, V>(key, value);
                head.prev = temp;
                temp.next = head;
                head = temp;
                curNum++;

            }
        } else {
            kv.value = value;
            get(kv.key);
        }

    }

    private LRUNode<K, V> getKV(K key) {
        if (maxCapacity == 1) {
            if (head.key == key) {
                return head;
            } else {
                return null;
            }
        }
        var i = head;
        while (i != null) {
            if (i.key == key) {
                return i;
            }
            i = i.next;
        }
        return null;
    }

    V get(K key) {
        var kv = getKV(key);
        if (kv != null) {
            if (kv != head) {
                if (kv == tail) {
                    tail = kv.prev;
                }
                kv.prev.next = kv.next;
                head.prev = kv;
                kv.next = head;
                head = kv;
            }

            return kv.value;
        } else {
            return null;
        }
    }

    void print() {
        var i = head;
        while (i != null) {
            System.out.println(i.key + " " + i.value);
            i = i.next;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt();
        LRU<Integer, String> lru = new LRU<>(n);

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
                    System.out.print(lru.get(tempK) + ";");
                }
                System.out.println();
            }
//            lru.print();
//            System.out.println();
        }
    }
}
