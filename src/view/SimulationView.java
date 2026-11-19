package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import nicellipse.component.NiSpace;

public class SimulationView extends NiSpace {

    private static final long serialVersionUID = 1L;

    // Ratio de hauteur de la zone ciel/espace (55 % en haut, 45 % océan en bas)
    private static final double SKY_RATIO = 0.55;

    private final List<SatelliteView> satellites = new ArrayList<>();
    private final List<TrackerView> trackers = new ArrayList<>();
    private final List<int[]> stars = new ArrayList<>();
    private final List<int[]> bubbles = new ArrayList<>();
    private final Timer animationTimer;

    public SimulationView(int width, int height) {
        super("Satellite & Balises", new Dimension(width, height));
        setLayout(null);
        setSize(width, height);

        initDecorElements(width, height);

        // Boucle d'animation fluide (~50 FPS)
        this.animationTimer = new Timer(20, e -> updateSimulation());
    }

    private void initDecorElements(int width, int height) {
        Random rand = new Random(42);
        int waterlineY = (int) (height * SKY_RATIO);

        // Étoiles subtiles dans la haute atmosphère
        for (int i = 0; i < 35; i++) {
            int sx = rand.nextInt(Math.max(1, width));
            int sy = rand.nextInt(Math.max(1, (int) (waterlineY * 0.65)));
            int radius = rand.nextInt(2) + 1;
            stars.add(new int[]{sx, sy, radius});
        }

        // Bulles d'ambiance dans l'océan
        for (int i = 0; i < 22; i++) {
            int bx = rand.nextInt(Math.max(1, width));
            int by = waterlineY + 20 + rand.nextInt(Math.max(1, height - waterlineY - 30));
            int radius = rand.nextInt(4) + 2;
            bubbles.add(new int[]{bx, by, radius});
        }
    }

    public int getWaterlineY() {
        return (int) (getHeight() * SKY_RATIO);
    }

    public void addSatellite(SatelliteView satellite) {
        satellites.add(satellite);
        // Maintient le satellite dans la partie supérieure
        int maxSkyY = Math.max(10, getWaterlineY() - satellite.getHeight() - 10);
        if (satellite.getY() > maxSkyY) {
            satellite.setLocation(satellite.getX(), maxSkyY);
        }
        add(satellite);
        repaint();
    }

    public void addTracker(TrackerView tracker) {
        trackers.add(tracker);
        // Force la position initiale à l'intérieur de la partie bleue
        int minY = getWaterlineY();
        int maxY = Math.max(minY, getHeight() - tracker.getHeight());
        int clampedX = Math.max(0, Math.min(tracker.getX(), Math.max(0, getWidth() - tracker.getWidth())));
        int clampedY = Math.max(minY, Math.min(tracker.getY(), maxY));
        tracker.setLocation(clampedX, clampedY);
        add(tracker);
        repaint();
    }

    public void start() {
        animationTimer.start();
    }

    public void stop() {
        animationTimer.stop();
    }

    private void updateSimulation() {
        int width = getWidth();
        int height = getHeight();
        int waterlineY = getWaterlineY();

        // 1. Satellites : déplacement uniquement de gauche à droite
        for (SatelliteView sat : satellites) {
            sat.moveLeftToRight(width);
        }

        // 2. Trackers : déplacement dans tous les sens, strictement dans la partie bleue
        for (TrackerView tracker : trackers) {
            tracker.moveInOcean(0, width, waterlineY, height);
            tracker.setNearSurface(tracker.getY() <= waterlineY + 14);
        }

        // Animation légère des bulles
        for (int[] b : bubbles) {
            b[1] -= 1;
            if (b[1] < waterlineY + 8) {
                b[1] = height - 10;
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int waterlineY = getWaterlineY();

        drawSkyAndOrbit(g2, width, waterlineY);
        drawOcean(g2, width, height, waterlineY);
        drawWaterSurface(g2, width, waterlineY);
        drawTransmissionLinks(g2, waterlineY);

        g2.dispose();
    }

    private void drawSkyAndOrbit(Graphics2D g2, int width, int waterlineY) {
        // Dégradé atmosphérique : espace profond en haut vers bleu horizon doux
        GradientPaint skyGradient = new GradientPaint(
                0, 0, new Color(13, 27, 48),
                0, waterlineY, new Color(135, 185, 224)
        );
        g2.setPaint(skyGradient);
        g2.fillRect(0, 0, width, waterlineY);

        // Étoiles discrètes en haute altitude
        for (int[] star : stars) {
            if (star[1] < waterlineY - 20) {
                g2.setColor(new Color(255, 255, 255, 130));
                g2.fillOval(star[0], star[1], star[2], star[2]);
            }
        }

        // Lignes d'orbite discrètes
        g2.setColor(new Color(255, 255, 255, 28));
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, new float[]{6f, 6f}, 0f));
        g2.drawLine(0, (int) (waterlineY * 0.25), width, (int) (waterlineY * 0.25));
        g2.drawLine(0, (int) (waterlineY * 0.55), width, (int) (waterlineY * 0.55));
    }

    private void drawOcean(Graphics2D g2, int width, int height, int waterlineY) {
        int oceanHeight = height - waterlineY;

        // Dégradé océanique : bleu lumineux en surface vers bleu abysse en profondeur
        GradientPaint oceanGradient = new GradientPaint(
                0, waterlineY, new Color(14, 116, 194),
                0, height, new Color(4, 28, 68)
        );
        g2.setPaint(oceanGradient);
        g2.fillRect(0, waterlineY, width, oceanHeight);

        // Rayons lumineux sous-marins
        g2.setColor(new Color(140, 220, 255, 18));
        for (int i = 0; i < 5; i++) {
            int topX = (int) (width * (0.12 + i * 0.2));
            Path2D ray = new Path2D.Double();
            ray.moveTo(topX, waterlineY);
            ray.lineTo(topX + 45, waterlineY);
            ray.lineTo(topX + 130, height);
            ray.lineTo(topX - 40, height);
            ray.closePath();
            g2.fill(ray);
        }

        // Repères de profondeur discrets à droite
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.setStroke(new BasicStroke(1f));
        int levels = 3;
        for (int i = 1; i <= levels; i++) {
            int y = waterlineY + (oceanHeight * i) / (levels + 1);
            g2.setColor(new Color(180, 225, 255, 35));
            g2.drawLine(0, y, width, y);
            g2.setColor(new Color(200, 235, 255, 120));
            g2.drawString("-" + (i * 100) + " m", width - 52, y - 4);
        }

        // Bulles
        g2.setColor(new Color(200, 240, 255, 70));
        for (int[] b : bubbles) {
            if (b[1] > waterlineY + 5 && b[1] < height - 5) {
                g2.drawOval(b[0], b[1], b[2], b[2]);
            }
        }
    }

    private void drawWaterSurface(Graphics2D g2, int width, int waterlineY) {
        // Onde de surface douce + liseré lumineux
        Path2D wave = new Path2D.Double();
        wave.moveTo(0, waterlineY);
        for (int x = 0; x <= width; x += 20) {
            double yOffset = Math.sin(x * 0.04) * 2.0;
            wave.lineTo(x, waterlineY + yOffset);
        }
        g2.setColor(new Color(175, 238, 255, 210));
        g2.setStroke(new BasicStroke(2.2f));
        g2.draw(wave);
    }

    private void drawTransmissionLinks(Graphics2D g2, int waterlineY) {
        for (TrackerView tracker : trackers) {
            if (tracker.getY() <= waterlineY + 14) {
                int tx = tracker.getX() + tracker.getWidth() / 2;
                int ty = tracker.getY() + 6;
                SatelliteView targetSatellite = null;

                for (SatelliteView sat : satellites) {
                    int satLeft = sat.getX();
                    int satRight = sat.getX() + sat.getWidth();
                    if (tx >= satLeft && tx <= satRight) {
                        targetSatellite = sat;
                        break;
                    }
                }
                if (targetSatellite != null) {
                    int sx = targetSatellite.getX() + targetSatellite.getWidth() / 2;
                    int sy = targetSatellite.getY() + targetSatellite.getHeight() / 2;

                    g2.setColor(new Color(110, 245, 255, 180));
                    g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10f, new float[]{5f, 5f}, 0f));
                    g2.drawLine(tx, ty, sx, sy);
                }
            }
        }
    }
}