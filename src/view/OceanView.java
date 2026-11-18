package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.entity.Tracker;
import nicellipse.component.NiSpace;

public class OceanView extends NiSpace {

    private final List<TrackerView> trackerViews = new ArrayList<>();
    private final List<int[]> bubbles = new ArrayList<>();

    public OceanView() {
        super();
        setLayout(null);
        Random rand = new Random(12);
        for (int i = 0; i < 22; i++) {
            bubbles.add(new int[]{rand.nextInt(1200), 20 + rand.nextInt(300), rand.nextInt(4) + 2});
        }
    }

    public void addTracker(Tracker tracker) {
        TrackerView trackerView = new TrackerView(tracker);
        trackerViews.add(trackerView);
        add(trackerView);
    }

    public List<TrackerView> getTrackerViews() {
        return trackerViews;
    }

    public void update() {
        int w = getWidth();
        int h = getHeight();
        for (TrackerView tv : trackerViews) {
            tv.update(w, h);
        }
        for (int[] b : bubbles) {
            b[1] -= 1;
            if (b[1] < 8) b[1] = Math.max(20, h - 10);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Dégradé bleu surface -> bleu abysse
        g2.setPaint(new GradientPaint(0, 0, new Color(14, 116, 194), 0, h, new Color(4, 28, 68)));
        g2.fillRect(0, 0, w, h);

        // Rayons lumineux sous-marins
        g2.setColor(new Color(140, 220, 255, 18));
        for (int i = 0; i < 5; i++) {
            int topX = (int) (w * (0.12 + i * 0.2));
            Path2D ray = new Path2D.Double();
            ray.moveTo(topX, 0);
            ray.lineTo(topX + 45, 0);
            ray.lineTo(topX + 130, h);
            ray.lineTo(topX - 40, h);
            ray.closePath();
            g2.fill(ray);
        }

        // Repères de profondeur
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        for (int i = 1; i <= 3; i++) {
            int y = (h * i) / 4;
            g2.setColor(new Color(180, 225, 255, 35));
            g2.drawLine(0, y, w, y);
            g2.setColor(new Color(200, 235, 255, 120));
            g2.drawString("-" + (i * 100) + " m", w - 52, y - 4);
        }

        // Bulles
        g2.setColor(new Color(200, 240, 255, 70));
        for (int[] b : bubbles) {
            g2.drawOval(b[0] % Math.max(1, w), b[1] % Math.max(1, h), b[2], b[2]);
        }

        // Ligne d'onde à la surface (y = 0)
        Path2D wave = new Path2D.Double();
        wave.moveTo(0, 1);
        for (int x = 0; x <= w; x += 20) {
            wave.lineTo(x, 1 + Math.sin(x * 0.04) * 2.0);
        }
        g2.setColor(new Color(175, 238, 255, 220));
        g2.setStroke(new BasicStroke(2.2f));
        g2.draw(wave);

        g2.dispose();
    }
}