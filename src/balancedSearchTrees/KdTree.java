package balancedSearchTrees;

import edu.princeton.cs.algs4.*;

import java.util.LinkedList;

public class KdTree {

    private int numberOfNodes = 0;
    private Node first = null;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private boolean isVerticalSeparation;
        private Node left;
        private Node right;
    }

    public KdTree() {
    }

    public boolean isEmpty() {
        return numberOfNodes == 0;
    }

    public int size() {
        return numberOfNodes;
    }

    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        if (first == null) {
            Node newNode = new Node();
            newNode.p = p;
            newNode.isVerticalSeparation = true;
            newNode.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            first = newNode;
        } else {
            insert(p, first);
        }
        numberOfNodes++;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return first != null && contains(p, first);
    }

    public void draw() {
        if (first != null) draw(first);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        LinkedList<Point2D> pointsInARect = new LinkedList<>();
        if (first != null) {
            findPoints(rect, first, pointsInARect);
        }
        return  pointsInARect;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();
        if (first == null) return null;
        return findNearest(first, first, p).p;
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.4, 0.4));
        tree.insert(new Point2D(0.4, 0.4));
        tree.insert(new Point2D(0.4, 0.6));
        tree.insert(new Point2D(0.6, 0.4));
        tree.insert(new Point2D(0.6, 0.4));
        tree.insert(new Point2D(0.6, 0.6));
        tree.insert(new Point2D(0.6, 0.6));
        StdDraw.setPenRadius(0.01);
        tree.draw();
        StdOut.println("Number " + tree.numberOfNodes);
        Iterable<Point2D> list = tree.range(new RectHV(0.0, 0.0, 0.5, 0.7));
        list.forEach(StdOut::println);
        StdOut.println(tree.nearest(new Point2D(0.7, 0.7)));
        StdOut.println(tree.contains(new Point2D(0.6, 0.6)));
        StdOut.println(0x7fffffff);
        ST<String,String> st = new ST<>();
    }

    private void insert(Point2D point2D, Node node) {
        if (!node.p.equals(point2D)) {
            if (isOnTheRight(point2D, node)) {
                if (node.right != null) insert(point2D, node.right);
                else node.right = makeNode(point2D, node, true);
            } else {
                if (node.left != null) insert(point2D, node.left);
                else node.left = makeNode(point2D, node, false);
            }
        } else numberOfNodes--;
    }

    private boolean isOnTheRight(Point2D point2D, Node node) {
        if (node.isVerticalSeparation) return point2D.x() >= node.p.x();
        else return point2D.y() >= node.p.y();
    }

    private Node makeNode(Point2D point2D, Node node, boolean isOnTheRight) {
        Node newNode = new Node();
        newNode.p = point2D;
        newNode.isVerticalSeparation = !node.isVerticalSeparation;
        newNode.rect = createRect(node, isOnTheRight);
        return newNode;
    }

    private RectHV createRect(Node previousNode, boolean isOnTheRight) {
        RectHV rectHV;
        if (previousNode.isVerticalSeparation) {
            if (isOnTheRight) rectHV  = new RectHV(previousNode.p.x(), previousNode.rect.ymin(),
                    previousNode.rect.xmax(), previousNode.rect.ymax());
            else rectHV  = new RectHV(previousNode.rect.xmin(), previousNode.rect.ymin(),
                    previousNode.p.x(), previousNode.rect.ymax());
        } else {
            if (isOnTheRight) rectHV  = new RectHV(previousNode.rect.xmin(), previousNode.p.y(),
                    previousNode.rect.xmax(), previousNode.rect.ymax());
            else rectHV  = new RectHV(previousNode.rect.xmin(), previousNode.rect.ymin(),
                    previousNode.rect.xmax(), previousNode.p.y());
        }
        return rectHV;
    }

    private boolean contains(Point2D p, Node node) {
        if (node.p.equals(p)) return true;
        if (isOnTheRight(p, node)) {
            if (node.right != null) return contains(p, node.right);
            else return false;
        } else {
            if (node.left != null) return contains(p, node.left);
            else return false;
        }
    }

    private void draw(Node node) {
        node.p.draw();
        if (node.left != null) draw(node.left);
        if (node.right != null) draw(node.right);
    }

    private void findPoints(RectHV rect, Node node, LinkedList<Point2D> pointsInARect) {
        if (rect.contains(node.p)) pointsInARect.add(node.p);
        if ((node.right != null) && ((node.isVerticalSeparation && rect.xmax() >= node.p.x()) ||
                (!node.isVerticalSeparation && rect.ymax() >= node.p.y())))
            findPoints(rect, node.right, pointsInARect);
        if ((node.left != null) && ((node.isVerticalSeparation && rect.xmin() <= node.p.x()) ||
                (!node.isVerticalSeparation && rect.ymin() <= node.p.y())))
            findPoints(rect, node.left, pointsInARect);
    }

    private Node findNearest(Node current, Node currentNearest, Point2D p) {
        if (p.distanceSquaredTo(current.p) < p.distanceSquaredTo(currentNearest.p)) currentNearest = current;
        if (current.right != null && current.left != null) {
            Node closest;
            Node another;
            if (current.isVerticalSeparation) {
                if (p.x() >= current.p.x()) closest = current.right;
                else closest = current.left;
            } else {
                if (p.y() >= current.p.y()) closest = current.right;
                else closest = current.left;
            }
            another = (closest.p.equals(current.left.p)) ? current.right : current.left;
            currentNearest = findNearest(closest, currentNearest, p);
            if (isUsefulToSearch(currentNearest, another, p)) currentNearest = findNearest(another, currentNearest, p);
            return currentNearest;
        }
        if (current.right != null) currentNearest = findNearest(current.right, currentNearest, p);
        if (current.left != null) currentNearest = findNearest(current.left, currentNearest, p);
        return currentNearest;
    }

    private boolean isUsefulToSearch(Node currentNearest, Node another, Point2D p) {
        return another.rect.distanceSquaredTo(p) <= currentNearest.p.distanceSquaredTo(p);
    }

}