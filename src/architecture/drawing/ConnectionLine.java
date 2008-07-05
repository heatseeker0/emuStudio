/*
 * ConnectionLine.java
 *
 * Created on 4.7.2008, 9:43:39
 * hold to: KISS, YAGNI
 *
 */

package architecture.drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;

/**
 *
 * @author vbmacher
 */
public class ConnectionLine {
    private Element e1;
    private Element e2;
    private ArrayList<Point> points;
    private BasicStroke thickLine;
    
    public ConnectionLine(Element e1, Element e2,
            ArrayList<Point> points) {
        this.e1 = e1;
        this.e2 = e2;
        this.points = new ArrayList<Point>();
        if (points != null)
            this.points.addAll(points);
        this.thickLine = new BasicStroke(2);
    }
    
    public void draw(Graphics2D g) {
        g.setColor(Color.black);
        int x1 = e1.getX() + e1.getWidth()/2;
        int y1 = e1.getY() + e1.getHeight()/2;
        int x2, y2;
        
        Stroke ss = g.getStroke();
        g.setStroke(thickLine);
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            x2 = (int)p.getX();
            y2 = (int)p.getY();
            g.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
        x2 = e2.getX() + e2.getWidth()/2;
        y2 = e2.getY() + e2.getHeight()/2;
        g.drawLine(x1, y1, x2, y2);
        g.setStroke(ss);
    }
    
    public static void drawSketch(Graphics2D g,Element ee1, Point ee2,
            ArrayList<Point> ppoints) {
        g.setColor(Color.black);
        int x1 = ee1.getX() + ee1.getWidth()/2;
        int y1 = ee1.getY() + ee1.getHeight()/2;
        int x2, y2;
        
        for (int i = 0; i < ppoints.size(); i++) {
            Point p = ppoints.get(i);
            x2 = (int)p.getX();
            y2 = (int)p.getY();
            g.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
        }
        if (ee2 != null) {
            x2 = (int)ee2.getX();
            y2 = (int)ee2.getY();
            g.drawLine(x1, y1, x2, y2);
        }
    }

    
    
    public void addPoint(int before, Point p) {
        points.add(before+1, p);
    }
    
    public void removePoint(int index) {
        points.remove(index);
    }
    
    public ArrayList<Point> getPoints() {
        return points;
    }
    
    public void pointMove(int index, int x, int y) {
        points.get(index).setLocation(x, y);
    }
    
    public boolean containsElement(Element e) {
        if (e1 == e || e2 == e) return true;
        return false;
    }
}
