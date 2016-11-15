package stacksAndQueues;

import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first = null;
    private Node last = null;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    public Deque() {
    }

    public boolean isEmpty() { return first == null; }

    public int size() {
        int size = 0;
        if (first != null) {
            Node tmpNode = first;
            do {
                tmpNode = tmpNode.next;
                size++;
            } while (tmpNode != null);
        }
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) throw new NullPointerException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;
        if (last == null) last = first;
        else oldFirst.prev = first;
    }

    public void addLast(Item item) {
        if (item == null) throw new NullPointerException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;
        if (first == null) first = last;
        else oldLast.next = last;
    }

    public Item removeFirst() {
        if (first == null) throw new NoSuchElementException();
        Item item = first.item;
        first = first.next;
        if (first == null) last = null;
        else first.prev = null;
        return item;
    }

    public Item removeLast() {
        if (first == null) throw new NoSuchElementException();
        Item item = last.item;
        last = last.prev;
        if (last == null) first = null;
        else last.next = null;
        return item;
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node current = first;
            @Override
            public boolean hasNext() {
                return current != null;
            }
            @Override
            public Item next() {
                if (current == null) throw new NoSuchElementException();
                Item currentItem = current.item;
                current = current.next;
                return currentItem;
            }
        };
    }

    public static void main(String[] args) {

        StdOut.println("Test size:");
        Deque<String> dequeTestSize = new Deque<>();
        StdOut.println(dequeTestSize.size());
        dequeTestSize.addLast("One");
        StdOut.println(dequeTestSize.size());
        dequeTestSize.addLast("Two");
        StdOut.println(dequeTestSize.size());
        dequeTestSize.addLast("Three");
        StdOut.println(dequeTestSize.size());

        StdOut.println("Test addLast:");
        Deque<String> dequeTestAddLast = new Deque<>();
        dequeTestAddLast.addLast("One");
        dequeTestAddLast.addLast("Two");
        dequeTestAddLast.addLast("Three");
        StdOut.println(dequeTestAddLast);

        StdOut.println("Test removeFirst:");
        Deque<String> dequeTestRemoveFirst = new Deque<>();
        dequeTestRemoveFirst.addLast("One");
        dequeTestRemoveFirst.addLast("Two");
        dequeTestRemoveFirst.addLast("Three");
        StdOut.println(dequeTestRemoveFirst);
        dequeTestRemoveFirst.removeFirst();
        StdOut.println(dequeTestRemoveFirst);
        dequeTestRemoveFirst.removeFirst();
        StdOut.println(dequeTestRemoveFirst);
        dequeTestRemoveFirst.removeFirst();
        StdOut.println(dequeTestRemoveFirst);

        StdOut.println("Test addFirst:");
        Deque<String> dequeTestAddFirst = new Deque<>();
        dequeTestAddFirst.addFirst("One");
        dequeTestAddFirst.addFirst("Two");
        dequeTestAddFirst.addFirst("Three");
        StdOut.println(dequeTestAddFirst);

        StdOut.println("Test removeLast:");
        Deque<String> dequeTestRemoveLast = new Deque<>();
        StdOut.println(dequeTestRemoveLast);
        dequeTestRemoveLast.addLast("One");
        StdOut.println(dequeTestRemoveLast);
        dequeTestRemoveLast.addLast("Two");
        StdOut.println(dequeTestRemoveLast);
        dequeTestRemoveLast.addLast("Three");
        StdOut.println(dequeTestRemoveLast);
        dequeTestRemoveLast.removeLast();
        StdOut.println(dequeTestRemoveLast);
        dequeTestRemoveLast.removeLast();
        StdOut.println(dequeTestRemoveLast);
        dequeTestRemoveLast.removeLast();
        StdOut.println(dequeTestRemoveLast);

        dequeTestRemoveLast.addLast("dfvnd");
    }

}