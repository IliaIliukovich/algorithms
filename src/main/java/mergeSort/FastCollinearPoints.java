package mergeSort;

import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class FastCollinearPoints {

    private Point[] points;
    private Point[] tmpPoints;
    private int numberOfSegments = -1;
    private Segment[] segments;
    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw  new NullPointerException();
        for (Point point : points) {
            if (point == null) throw new NullPointerException();
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo((points[j])) == 0) throw new IllegalArgumentException();
            }
        }
        this.points = new Point[points.length];
        System.arraycopy(points, 0, this.points, 0, points.length);
        segments = new Segment[points.length * points.length];
    }

    public int numberOfSegments() {
        if (numberOfSegments == -1) {
            evaluate();
        }
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        if (numberOfSegments == -1) {
            evaluate();
        }
        LineSegment[] copySegments = new LineSegment[numberOfSegments];
        System.arraycopy(lineSegments, 0, copySegments, 0, numberOfSegments);
        for (int i = 0; i < copySegments.length; i++) {
            StdOut.println("Segments out: " + copySegments[i]);
        }
        return copySegments;
    }

    private void evaluate() {
        Point pointP;
        tmpPoints = new Point[points.length];
        System.arraycopy(points, 0, tmpPoints, 0, points.length);
        numberOfSegments = 0;
        for (int i = 0; i < points.length - 3; i++) {
            pointP = points[i];
            Arrays.sort(tmpPoints);
            Arrays.sort(tmpPoints, pointP.slopeOrder());
            findSegmensForPoint(pointP);
        }
        filterDuplicateSegments();
    }

    private void findSegmensForPoint(Point pointP) {
        boolean groupFlag = false;
        int groupCount = 0;
        double slope1;
        double slope2;
        for (int i = 1; i < tmpPoints.length - 1; i++) {
            slope1 = pointP.slopeTo(tmpPoints[i]);
            slope2 = pointP.slopeTo(tmpPoints[i + 1]);
            if (slope1 == slope2) {
                groupFlag = true;
                groupCount++;
            } else if (groupFlag && (slope1 != slope2)) {
                if (++groupCount >= 3) {
                    extractSegment(pointP, groupCount, i);
                }
                groupFlag = false;
                groupCount = 0;
            }
            if ((i == tmpPoints.length - 2) && groupFlag) {
                if (++groupCount >= 3) {
                    extractSegment(pointP, groupCount, i + 1);
                }
                groupFlag = false;
                groupCount = 0;
            }
        }
    }

    private void extractSegment(Point pointP, int groupCount, int lastElementInGroup) {
        Point firstPoint = tmpPoints[lastElementInGroup + 1 - groupCount];
        Point lastPoint = tmpPoints[lastElementInGroup];
        if (pointP.compareTo(firstPoint) < 0) {
            segments[numberOfSegments] = new Segment(pointP, lastPoint);
        } else if (pointP.compareTo(lastPoint) > 0) {
            segments[numberOfSegments] = new Segment(firstPoint, pointP);
        } else {
            segments[numberOfSegments] = new Segment(firstPoint, lastPoint);
        }
        numberOfSegments++;
    }

    private void filterDuplicateSegments() {
        Segment[] segmentsToSort = new Segment[numberOfSegments];
        System.arraycopy(segments, 0, segmentsToSort, 0, numberOfSegments);
        Arrays.sort(segmentsToSort);
        int realNumberOfSegments = 0;
        lineSegments = new LineSegment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            if ((i == numberOfSegments - 1) || !segmentsToSort[i].equals(segmentsToSort[i + 1])) {
                lineSegments[realNumberOfSegments++] = new LineSegment(segmentsToSort[i].point1, segmentsToSort[i].point2);
            }
        }
        numberOfSegments = realNumberOfSegments;
    }

    private class Segment implements Comparable<Segment> {
        private Point point1;
        private Point point2;
        Segment(Point point1, Point point2) {
            this.point1 = point1;
            this.point2 = point2;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Segment segment = (Segment) o;
            return point1.compareTo(segment.point1) == 0 && point2.compareTo(segment.point2) == 0;
        }
        @Override
        public int compareTo(Segment that) {
            if (this.point1.compareTo(that.point1) != 0) return this.point1.compareTo(that.point1);
            return this.point2.compareTo(that.point2);
        }
    }
}