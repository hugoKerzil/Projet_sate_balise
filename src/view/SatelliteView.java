package view;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import lombok.Getter;
import model.entity.Satellite;
import nicellipse.component.NiBasicComponent;
import nicellipse.component.NiSpace;

@Getter
public class SatelliteView extends NiSpace {

    private final Satellite satellite;

    public SatelliteView(Satellite satellite) {
        super("Satellite" + satellite.getId(), new Dimension(72, 72));
        this.satellite = satellite;
        setOpaque(false);
        setSize(72, 72);
        setLocation(satellite.getX(), satellite.getY());
    }

    public void update(int skyWidth, int skyHeight) {
        satellite.move(skyWidth, skyHeight);
        setLocation(satellite.getX(), satellite.getY());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        AffineTransform old = g2.getTransform();
        g2.rotate(Math.toRadians(-45), cx, cy);

        // Panneaux solaires
        g2.setPaint(new GradientPaint(cx - 30, cy - 5, new Color(34, 72, 118), cx + 30, cy + 5, new Color(70, 130, 190)));
        RoundRectangle2D leftWing = new RoundRectangle2D.Double(cx - 31, cy - 6, 22, 12, 3, 3);
        RoundRectangle2D rightWing = new RoundRectangle2D.Double(cx + 9, cy - 6, 22, 12, 3, 3);
        g2.fill(leftWing);
        g2.fill(rightWing);
        g2.setColor(new Color(200, 220, 240));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(leftWing);
        g2.draw(rightWing);

        // Corps métallique
        g2.setPaint(new GradientPaint(cx - 7, cy - 11, new Color(215, 222, 230), cx + 7, cy + 11, new Color(105, 115, 128)));
        RoundRectangle2D body = new RoundRectangle2D.Double(cx - 7, cy - 11, 14, 22, 6, 6);
        g2.fill(body);
        g2.setColor(new Color(55, 65, 78));
        g2.draw(body);

        // Bague dorée + antenne
        g2.setColor(new Color(235, 185, 65));
        g2.fillRect(cx - 6, cy - 2, 12, 3);
        g2.setColor(new Color(80, 180, 255));
        g2.drawArc(cx - 5, cy + 9, 10, 7, 180, 180);

        g2.setTransform(old);
        g2.dispose();
    }
}