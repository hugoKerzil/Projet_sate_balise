package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import nicellipse.component.NiShape;

public class SatelliteView extends NiShape {

    private static final long serialVersionUID = 1L;

    private int speedX;

    public SatelliteView(int x, int y, int speedX) {
        super();
        doFilling(false);
        doOutline(false);
        setOpaque(false);
        setSize(72, 72);
        setLocation(x, y);
        // Garantit un mouvement strictement de gauche à droite (> 0)
        this.speedX = Math.max(1, Math.abs(speedX));
    }

    public void setSpeedX(int speedX) {
        this.speedX = Math.max(1, Math.abs(speedX));
    }

    /**
     * Déplace le satellite uniquement de gauche à droite (Y reste inchangé).
     */
    public void moveLeftToRight(int spaceWidth) {
        int nextX = getX() + speedX;
        if (nextX > spaceWidth) {
            nextX = -getWidth();
        }
        setLocation(nextX, getY());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        // Inclinaison à -45° fidèle au schéma d'origine
        AffineTransform old = g2.getTransform();
        g2.rotate(Math.toRadians(-45), cx, cy);

        // 1. Panneaux solaires (gauche et droite)
        GradientPaint panelGrad = new GradientPaint(
                cx - 30, cy - 5, new Color(34, 72, 118),
                cx + 30, cy + 5, new Color(70, 130, 190)
        );
        g2.setPaint(panelGrad);
        RoundRectangle2D leftWing = new RoundRectangle2D.Double(cx - 31, cy - 6, 22, 12, 3, 3);
        RoundRectangle2D rightWing = new RoundRectangle2D.Double(cx + 9, cy - 6, 22, 12, 3, 3);
        g2.fill(leftWing);
        g2.fill(rightWing);

        // Bordures et grille photovoltaïque des panneaux
        g2.setColor(new Color(200, 220, 240));
        g2.setStroke(new BasicStroke(1.2f));
        g2.draw(leftWing);
        g2.draw(rightWing);

        g2.setColor(new Color(160, 205, 245, 130));
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawLine(cx - 24, cy - 6, cx - 24, cy + 6);
        g2.drawLine(cx - 16, cy - 6, cx - 16, cy + 6);
        g2.drawLine(cx + 16, cy - 6, cx + 16, cy + 6);
        g2.drawLine(cx + 24, cy - 6, cx + 24, cy + 6);

        // Bras de liaison panneaux <-> corps
        g2.setColor(new Color(180, 190, 200));
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(cx - 9, cy, cx - 6, cy);
        g2.drawLine(cx + 6, cy, cx + 9, cy);

        // 2. Corps cylindrique central métallisé
        GradientPaint bodyGrad = new GradientPaint(
                cx - 7, cy - 11, new Color(215, 222, 230),
                cx + 7, cy + 11, new Color(105, 115, 128)
        );
        g2.setPaint(bodyGrad);
        RoundRectangle2D body = new RoundRectangle2D.Double(cx - 7, cy - 11, 14, 22, 6, 6);
        g2.fill(body);

        g2.setColor(new Color(55, 65, 78));
        g2.setStroke(new BasicStroke(1.3f));
        g2.draw(body);

        // Bague dorée centrale + voyant télémétrique
        g2.setColor(new Color(235, 185, 65));
        g2.fillRect(cx - 6, cy - 2, 12, 3);
        g2.setColor(new Color(80, 240, 255));
        g2.fillOval(cx - 2, cy - 7, 4, 4);

        // 3. Antenne / capteur orienté vers l'océan
        g2.setColor(new Color(80, 180, 255));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawArc(cx - 5, cy + 9, 10, 7, 180, 180);
        g2.drawLine(cx, cy + 11, cx, cy + 16);

        g2.setTransform(old);
        g2.dispose();
    }
}