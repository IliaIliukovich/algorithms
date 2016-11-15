package balancedSearchTrees;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> points = new TreeSet<>();

    public PointSET() {
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return points.contains(p);
    }

    public void draw() {
        points.forEach(Point2D::draw);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        LinkedList<Point2D> pointsInARect = new LinkedList<>();
        points.stream().filter(rect::contains).forEach(pointsInARect::add);
        return  pointsInARect;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();
        if (points.isEmpty()) return null;
        Point2D nearest = points.first();
        double distance = nearest.distanceSquaredTo(p);
        for (Point2D point2D : points) {
            if (point2D.distanceSquaredTo(p) <  distance) {
                nearest = point2D;
                distance = point2D.distanceSquaredTo(p);
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
    }
}