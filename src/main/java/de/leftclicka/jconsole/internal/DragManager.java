package de.leftclicka.jconsole.internal;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DragManager implements MouseInputListener {

    private final Component frame;
    private Point lastLocation;
    private boolean dragging;

    /**
     * Could receive this via getComponent(), but I prefer this over casting/type checking on the returned component.
     * Also, this is an entirely internal so who cares.
     */
    public DragManager(Component frame) {
        this.frame = frame;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point location = e.getLocationOnScreen();
        if (e.getButton() != 0 || !isInside(location) || !dragging)
            return;
        if (lastLocation != null) {
            int dX = location.x - lastLocation.x;
            int dY = location.y - lastLocation.y;
            Point frameLocation = frame.getLocationOnScreen();
            frame.setLocation(frameLocation.x + dX, frameLocation.y + dY);
        }
        lastLocation = location;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private boolean isInside(Point point) {
        Point topLeft = frame.getLocationOnScreen();
        int width = frame.getWidth(), height = frame.getHeight();
        return
                point.x >= topLeft.x && point.x <= topLeft.x + width
                && point.y >= topLeft.y && point.y <= topLeft.y + height;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point location = e.getLocationOnScreen();
        if (e.getButton() != 0 && !isInside(location))
            return;
        dragging = true;
        lastLocation = location;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastLocation = null;
        dragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
