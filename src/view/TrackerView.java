package view;

import java.awt.*;
import java.awt.geom.Path2D;
import lombok.Getter;
import model.entity.Tracker;
import nicellipse.component.NiBasicComponent;
import nicellipse.component.NiSpace;

@Getter
public class TrackerView extends NiSpace {
    public Tracker getTracker() {
        return tracker;
    }

    private final Tracker tracker;

    public TrackerView(Tracker tracker) {
        super("Tracker " + tracker.getId(), new Dimension(76, 54));
        this.tracker = tracker;
        setOpaque(false);
        setSize(76, 54);
        setLocation(tracker.getX(), tracker.getY());
    }

    public void update(int oceanWidth, int oceanHeight) {
        int maxX = Math.max(0, oceanWidth - getWidth());
        int maxY = Math.max(0, oceanHeight - getHeight());
        tracker.move(maxX, maxY);
        setLocation(tracker.getX(), tracker.getY());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int bodyX = 12, bodyY = 10, bodyW = w - 24, bodyH = h - 18;

        // Halo lumineux si proche de la surface
        if (tracker.getY() <= 14) {
            g2.setColor(new Color(120, 245, 255, 70));
            g2.fillOval(bodyX - 4, bodyY - 4, bodyW + 8, bodyH + 8);
        }

        // Ailerons latéraux
        g2.setColor(new Color(245, 195, 20));
        Path2D leftFin = new Path2D.Double();
        leftFin.moveTo(bodyX + 4, bodyY + 8);
        leftFin.quadTo(2, bodyY + 4, 4, bodyY + 12);
        leftFin.lineTo(bodyX + 2, bodyY + bodyH / 2.0);
        leftFin.lineTo(4, bodyY + bodyH - 12);
        leftFin.quadTo(2, bodyY + bodyH - 4, bodyX + 4, bodyY + bodyH - 8);
        leftFin.closePath();
        g2.fill(leftFin);

        Path2D rightFin = new Path2D.Double();
        rightFin.moveTo(bodyX + bodyW - 4, bodyY + 8);
        rightFin.quadTo(w - 2, bodyY + 4, w - 4, bodyY + 12);
        rightFin.lineTo(bodyX + bodyW - 2, bodyY + bodyH / 2.0);
        rightFin.lineTo(w - 4, bodyY + bodyH - 12);
        rightFin.quadTo(w - 2, bodyY + bodyH - 4, bodyX + bodyW - 4, bodyY + bodyH - 8);
        rightFin.closePath();
        g2.fill(rightFin);

        g2.setColor(new Color(35, 35, 40));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(leftFin);
        g2.draw(rightFin);

        // Kiosque & quille
        g2.setColor(new Color(235, 180, 15));
        g2.fillRoundRect(w / 2 - 9, bodyY - 4, 18, 7, 3, 3);
        g2.fillRoundRect(w / 2 - 12, bodyY + bodyH - 3, 24, 6, 3, 3);
        g2.setColor(new Color(35, 35, 40));
        g2.drawRoundRect(w / 2 - 9, bodyY - 4, 18, 7, 3, 3);
        g2.drawRoundRect(w / 2 - 12, bodyY + bodyH - 3, 24, 6, 3, 3);

        // Coque principale
        g2.setPaint(new GradientPaint(bodyX, bodyY, new Color(255, 232, 65), bodyX, bodyY + bodyH, new Color(225, 165, 10)));
        g2.fillOval(bodyX, bodyY, bodyW, bodyH);
        g2.setColor(new Color(30, 30, 35));
        g2.setStroke(new BasicStroke(1.6f));
        g2.drawOval(bodyX, bodyY, bodyW, bodyH);

        // Trois hublots
        int portRadius = 9, spacing = 14;
        int startX = w / 2 - spacing - portRadius / 2;
        int portY = bodyY + bodyH / 2 - portRadius / 2;
        for (int i = 0; i < 3; i++) {
            int px = startX + i * spacing;
            g2.setPaint(new GradientPaint(px, portY, Color.WHITE, px + portRadius, portY + portRadius, new Color(195, 232, 250)));
            g2.fillOval(px, portY, portRadius, portRadius);
            g2.setColor(new Color(45, 55, 70));
            g2.setStroke(new BasicStroke(1.1f));
            g2.drawOval(px, portY, portRadius, portRadius);
        }

        g2.dispose();
    }
}