package mergeSort;

import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class BruteCollinearPoints {

    private Point[] points;
    private int numberOfSegments = -1;
    private LineSegment[] segment;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw  new NullPointerException();
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) throw  new NullPointerException();
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo((points[j])) == 0) throw new IllegalArgumentException();
            }
        }
        this.points = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = points[i];
        }
    }

    public int numberOfSegments() {
        if (numberOfSegments == -1) {
            evaluate();
        }
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        if (segment == null) {
            evaluate();
        }
        return makeSegmentCopy();
    }

    private void evaluate() {

        Point[] tmpPoints = new Point[4];
        Point[] segmentPoints;
        double slope12;
        double slope13;
        double slope14;
        LineSegment[] tmpSegment = new LineSegment[points.length]; // TODO
        int tmpNumberOfSegments = 0;

        for (int pointIndex1 = 0; pointIndex1 < points.length; pointIndex1++) {
            tmpPoints[0] = points[pointIndex1];
            for (int pointIndex2 = pointIndex1 + 1; pointIndex2 < points.length; pointIndex2++) {
                tmpPoints[1] = points[pointIndex2];
                for (int pointIndex3 = pointIndex2 + 1; pointIndex3 < points.length; pointIndex3++) {
                    tmpPoints[2] = points[pointIndex3];
                    for (int pointIndex4 = pointIndex3 + 1; pointIndex4 < points.length; pointIndex4++) {
                        tmpPoints[3] = points[pointIndex4];
                        slope12 = tmpPoints[0].slopeTo(tmpPoints[1]);
                        slope13 = tmpPoints[0].slopeTo(tmpPoints[2]);
                        slope14 = tmpPoints[0].slopeTo(tmpPoints[3]);
                        if (slope12 == slope13 && slope12 == slope14) {
                            StdOut.println("Yes! " + tmpPoints[0] + " " + tmpPoints[1] + " " +
                                    tmpPoints[2] + " " + tmpPoints[3] + " slope = " + slope12);
                            segmentPoints = findSegmentPoints(tmpPoints);
                            tmpSegment[tmpNumberOfSegments++] = new LineSegment(segmentPoints[0], segmentPoints[1]);
                        }
                    }
                }
            }
        }

        LineSegment[] newSegment = new LineSegment[tmpNumberOfSegments];
        for (int i = 0; i < tmpNumberOfSegments; i++) {
            newSegment[i] = tmpSegment[i];
            StdOut.println(newSegment[i]);
        }
        segment = newSegment;
        numberOfSegments = tmpNumberOfSegments;

    }

    private Point[] findSegmentPoints(Point[] findPoints) {
        Point[] pointsToSort = {findPoints[0], findPoints[1], findPoints[2], findPoints[3]};
        Arrays.sort(pointsToSort);
        Point[] segmentPoints = {pointsToSort[0], pointsToSort[3]};
        return segmentPoints;
    }

    private LineSegment[] makeSegmentCopy() {
        LineSegment[] lineSegment = new LineSegment[segment.length];
        for (int i = 0; i < segment.length; i++) {
            lineSegment[i] = segment[i];
        }
        return lineSegment;
    }

}