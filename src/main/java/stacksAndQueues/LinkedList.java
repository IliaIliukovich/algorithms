package stacksAndQueues;

import java.util.Iterator;

public class LinkedList<Item> implements Iterable<Item>{

    private Node<Item> first;
    private Node<Item> last;

    private static class Node<Item> {
        Item item;
        Node<Item> next;

        private Node(Item item, Node<Item> next) {
            this.item = item;
            this.next  = next;
        }
    }

    public void add(Item item) {
        Node<Item> node = new Node<>(item, null);
        if (first == null) {
            first = node;
        }
        if (last != null) {
            last.next = node;
        }
        last = node;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            Node<Item> current = first;
            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Item next() {
                Item item = current.item;
                current = current.next;
                return item;
            }
        };
    }

    public static void main(String[] args) {

        LinkedList<String> list = new LinkedList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        list.add("four");

        list.forEach(System.out::println);


    }

}
