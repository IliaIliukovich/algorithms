package stacksAndQueues;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {

    public static void main(String[] args) {

        int nItems = Integer.parseInt(args[0]);

        RandomizedQueue<String> q = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q.enqueue(item);
        }

        while (nItems-- > 0)
            StdOut.println(q.dequeue());

    }

}
