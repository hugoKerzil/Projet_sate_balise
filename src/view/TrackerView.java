package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.Random;
import nicellipse.component.NiShape;

public class TrackerView extends NiShape {

    private static final long serialVersionUID = 1L;

    private int vx;
    private int vy;
    private boolean nearSurface = false;
    private final Random random = new Random();

    public TrackerView(int x, int y, int vx, int vy) {
        super();
        doFilling(false);
        doOutline(false);
        setOpaque(false);
        setSize(76, 54);
        setLocation(x, y);
        // Évite un tracker totalement immobile
        this.vx = (vx == 0 && vy == 0) ? 2 : vx;
        this.vy = (vx == 0 && vy == 0) ? -1 : vy;
    }

    public void setNearSurface(boolean nearSurface) {
        this.nearSurface = nearSurface;
    }

    /**
     * Déplace le tracker dans tous les sens (X et Y),
     * tout en l'empêchant strictement de sortir de la partie bleue.
     */
    public void moveInOcean(int oceanMinX, int oceanMaxX, int oceanMinY, int oceanMaxY) {
        // Légère variation aléatoire pour un déplacement organique dans toutes les directions
        if (random.nextInt(35) == 0) {
            vx += random.nextInt(3) - 1;
            vy += random.nextInt(3) - 1;
            vx = Math.max(-3, Math.min(3, vx));
            vy = Math.max(-3, Math.min(3, vy));
            if (vx == 0 && vy == 0) {
                vx = random.nextBoolean() ? 1 : -1;
            }
        }

        int nextX = getX() + vx;
        int nextY = getY() + vy;

        int maxAllowedX = Math.max(oceanMinX, oceanMaxX - getWidth());
        int maxAllowedY = Math.max(oceanMinY, oceanMaxY - getHeight());

        // Confinement horizontal strict dans la zone bleue
        if (nextX <= oceanMinX) {
            nextX = oceanMinX;
            vx = Math.abs(vx);
        } else if (nextX >= maxAllowedX) {
            nextX = maxAllowedX;
            vx = -Math.abs(vx);
        }

        // Confinement vertical strict dans la zone bleue (ne franchit jamais la surface)
        if (nextY <= oceanMinY) {
            nextY = oceanMinY;
            vy = Math.abs(vy);
        } else if (nextY >= maxAllowedY) {
            nextY = maxAllowedY;
            vy = -Math.abs(vy);
        }

        setLocation(nextX, nextY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int bodyX = 12;
        int bodyY = 10;
        int bodyW = w - 24;
        int bodyH = h - 18;

        // Halo de transmission si la balise est en surface
        if (nearSurface) {
            g2.setColor(new Color(120, 245, 255, 70));
            g2.fillOval(bodyX - 4, bodyY - 4, bodyW + 8, bodyH + 8);
        }

        // 1. Ailerons latéraux incurvés (gauche et droite)
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

        // 2. Kiosque supérieur et quille inférieure
        g2.setColor(new Color(235, 180, 15));
        g2.fillRoundRect(w / 2 - 9, bodyY - 4, 18, 7, 3, 3);
        g2.fillRoundRect(w / 2 - 12, bodyY + bodyH - 3, 24, 6, 3, 3);
        g2.setColor(new Color(35, 35, 40));
        g2.drawRoundRect(w / 2 - 9, bodyY - 4, 18, 7, 3, 3);
        g2.drawRoundRect(w / 2 - 12, bodyY + bodyH - 3, 24, 6, 3, 3);

        // Petite antenne sur le kiosque
        g2.drawLine(w / 2, bodyY - 4, w / 2, bodyY - 8);

        // 3. Coque principale jaune-or avec dégradé 3D
        GradientPaint hullGrad = new GradientPaint(
                bodyX, bodyY, new Color(255, 232, 65),
                bodyX, bodyY + bodyH, new Color(225, 165, 10)
        );
        g2.setPaint(hullGrad);
        g2.fillOval(bodyX, bodyY, bodyW, bodyH);

        g2.setColor(new Color(30, 30, 35));
        g2.setStroke(new BasicStroke(1.6f));
        g2.drawOval(bodyX, bodyY, bodyW, bodyH);

        // 4. Trois hublots vitrés alignés au centre
        int portRadius = 9;
        int spacing = 14;
        int startX = w / 2 - spacing - portRadius / 2;
        int portY = bodyY + bodyH / 2 - portRadius / 2;

        for (int i = 0; i < 3; i++) {
            int px = startX + i * spacing;
            GradientPaint glassGrad = new GradientPaint(
                    px, portY, Color.WHITE,
                    px + portRadius, portY + portRadius, new Color(195, 232, 250)
            );
            g2.setPaint(glassGrad);
            g2.fillOval(px, portY, portRadius, portRadius);

            g2.setColor(new Color(45, 55, 70));
            g2.setStroke(new BasicStroke(1.1f));
            g2.drawOval(px, portY, portRadius, portRadius);
        }

        g2.dispose();
    }
}