package stacksAndQueues;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] itemArray;
    private int numberOfItems;
    private int realNumberOfItems;

    public RandomizedQueue() {
        this.itemArray = (Item[]) new Object[0];
        this.numberOfItems = 0;
        this.realNumberOfItems = 0;
    }

    public void enqueue(Item item) {
        if (item == null) throw new NullPointerException();
        if (numberOfItems == 0) resize(1);
        else if (numberOfItems == itemArray.length) resize(2 * itemArray.length);
        itemArray[numberOfItems++] = item;
        realNumberOfItems++;
    }

    public Item sample() {
        if (realNumberOfItems > 0) {
            RandomOutput randomOutput = new RandomOutput(itemArray, numberOfItems).get();
            return randomOutput.randomItem;
        } else {
            throw new NoSuchElementException();
        }
    }

    public Item dequeue() {
        if (realNumberOfItems > 0) {
            RandomOutput randomOutput = new RandomOutput(itemArray, numberOfItems).get();
            itemArray[randomOutput.randomIndex] = null;
            if (--realNumberOfItems == itemArray.length / 4) resize(itemArray.length / 2);
            return randomOutput.randomItem;
        } else {
            throw new NoSuchElementException();
        }
    }

    public boolean isEmpty() {
        return realNumberOfItems == 0;
    }

    public int size() {
        return realNumberOfItems;
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Item[] copy = getRandomCopy(itemArray, numberOfItems, realNumberOfItems);
            private int copyNumberOfItems = copy.length;
            @Override
            public boolean hasNext() {
                return copyNumberOfItems > 0;
            }
            @Override
            public Item next() {
                if (copyNumberOfItems == 0) throw new NoSuchElementException();
                return copy[--copyNumberOfItems];
            }
        };
    }

    private Item[] getRandomCopy(Item[] itemArray, int numberOfItems, int realNumberOfItems) {
        Item[] copy = (Item[]) new Object[realNumberOfItems];
        Item[] tmpCopy = getCopy(itemArray, numberOfItems, realNumberOfItems);
        for (int i = 0; i < realNumberOfItems; i++) {
            RandomOutput randomOutput = new RandomOutput(tmpCopy, tmpCopy.length).get();
            tmpCopy[randomOutput.randomIndex] = null;
            copy[i] = randomOutput.randomItem;
            if (tmpCopy.length - i - 1 == tmpCopy.length / 2) {
                tmpCopy = getCopy(tmpCopy, tmpCopy.length, tmpCopy.length - i - 1);
            }
        }
        return copy;
    }

    public static void main(String[] args) {

        RandomizedQueue<String> queue = new RandomizedQueue<>();
        StdOut.println(queue);
        int temp = 0;
        for (int i = 0; i < 10; i++) {
            queue.enqueue("a= " + temp++);
            StdOut.println(queue);
        }
        StdOut.println();
        queue.forEach(StdOut::println);
        StdOut.println();
        queue.forEach(StdOut::println);
        StdOut.println();

        for (int i = 0; i < 10; i++) {
            queue.dequeue();
            StdOut.println(queue);
        }

        RandomizedQueue<String> queue2 = new RandomizedQueue<>();
        queue2.enqueue("one");
        queue2.forEach(StdOut::println);
        queue2.dequeue();
        queue2.forEach(StdOut::println);

        RandomizedQueue<String> queue3 = new RandomizedQueue<>();
        StdOut.println("Test 3");
        queue3.enqueue("one");
        queue3.forEach(StdOut::println);
        queue3.dequeue();
        queue3.forEach(StdOut::println);
        queue3.enqueue("two");
        queue3.forEach(StdOut::println);
    }

    private void resize(int newCapacity) {
        itemArray = getCopy(itemArray, numberOfItems, newCapacity);
        numberOfItems = realNumberOfItems;
    }

    private Item[] getCopy(Item[] itemArray, int numberOfItems, int newCapacity) {
        Item[] copy =  (Item[]) new Object[newCapacity];
        int j = 0;
        for (int i = 0; i < numberOfItems; i++) {
            if (itemArray[i] != null) {
                copy[j++] = itemArray[i];
            }
        }
        return copy;
    }

    private class RandomOutput {

        private Item[] itemArray;
        private int numberOfItems;
        private Item randomItem;
        private int randomIndex;


        public RandomOutput(Item[] itemArray, int numberOfItems) {
            this.itemArray = itemArray;
            this.numberOfItems = numberOfItems;
        }

        RandomOutput get() {
            do {
                randomIndex = StdRandom.uniform(numberOfItems);
            } while (itemArray[randomIndex] == null);
            randomItem = itemArray[randomIndex];
            return this;
        }
    }

}