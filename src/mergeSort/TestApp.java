package mergeSort;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class TestApp {

    public static void main(String[] args) {

//        StdDraw.setScale(-3, 3);
//        StdDraw.setPenRadius(0.01);

        Point[] points1 = new Point[4];
        points1[0] = new Point(1, 1);
        points1[1] = new Point(2, 1);
        points1[2] = new Point(1, 2);
        points1[3] = new Point(2, 2);
//        points1[0].draw();
//        points1[1].draw();
//        points1[2].draw();
//        points1[3].draw();

        Point[] points2 = new Point[4];
        points2[0] = new Point(-1, -1);
        points2[1] = new Point(0, 0);
        points2[2] = new Point(1, 1);
        points2[3] = new Point(2, 2);
//        points2[0].draw();
//        points2[1].draw();
//        points2[2].draw();
//        points2[3].draw();

        Point[] goodPoints = new Point[6];
        goodPoints[0] = new Point(1, 1);
        goodPoints[1] = new Point(2, 1);
        goodPoints[2] = new Point(1, 2);
        goodPoints[3] = new Point(2, 2);
        goodPoints[4] = new Point(-1, -1);
        goodPoints[5] = new Point(0, 0);

//        new BruteCollinearPoints(points1).segments();
//        new BruteCollinearPoints(points2).segments();

        StdOut.println("Test 6 goodPoints:");
        new FastCollinearPoints(goodPoints).segments();

        Point[] horisontalPoints = new Point[6];
        horisontalPoints[0] = new Point(1, 1);
        horisontalPoints[1] = new Point(2, 1);
        horisontalPoints[2] = new Point(3, 1);
        horisontalPoints[3] = new Point(4, 1);
        horisontalPoints[4] = new Point(-1, -1);
        horisontalPoints[5] = new Point(0, 0);

        StdOut.println("Test 6 horisontalPoints:");
        new FastCollinearPoints(horisontalPoints).segments();

        Point[] verticalPoints = new Point[6];
        verticalPoints[0] = new Point(1, 1);
        verticalPoints[1] = new Point(1, 2);
        verticalPoints[2] = new Point(1, 3);
        verticalPoints[3] = new Point(1, 4);
        verticalPoints[4] = new Point(-1, -1);
        verticalPoints[5] = new Point(0, 0);

        StdOut.println("Test 6 verticalPoints:");
        FastCollinearPoints collenear = new FastCollinearPoints(verticalPoints);
        LineSegment[] segments = collenear.segments();
        verticalPoints[0] = new Point(1, 1);
        verticalPoints[1] = new Point(1, 1);
        verticalPoints[2] = new Point(1, 1);
        verticalPoints[3] = new Point(1, 1);
        verticalPoints[4] = new Point(1, 1);
        verticalPoints[5] = new Point(1, 1);
        segments[0] = null;
        StdOut.println(collenear.segments().length);

        StdOut.println("Test slopeTo");
        StdOut.println(new Point(309, 417).slopeTo(new Point(309,309)));

        // read the n points from a file
        StdOut.println("Test 8");
        In in = new In("B:/java/input8.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }


        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println(collinear.segments().length);
        StdOut.println(collinear.numberOfSegments());


    }

}
